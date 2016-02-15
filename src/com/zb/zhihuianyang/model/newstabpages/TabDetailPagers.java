package com.zb.zhihuianyang.model.newstabpages;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.google.gson.Gson;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;
import com.zb.zhihuianyang.R;
import com.zb.zhihuianyang.base.BaseDetailPager;
import com.zb.zhihuianyang.contens.Contants;
import com.zb.zhihuianyang.domain.NewsDatas;
import com.zb.zhihuianyang.domain.NewsDatas.NewsTabs.News;
import com.zb.zhihuianyang.domain.NewsDatas.NewsTabs.TopNews;
import com.zb.zhihuianyang.domain.NewsMenuData.NewsTabDatas;
import com.zb.zhihuianyang.utils.CacheUtils;
import com.zb.zhihuianyang.view.TopNewsViewPager;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TabDetailPagers extends BaseDetailPager {

	//新闻列表数据
	private NewsTabDatas mTabDatas;
	//头条新闻ViewPager适配器
	private TopNewsAdapter mTopNewsAdapter;
	//头条新闻列表数据
	private ArrayList<TopNews> mTopNewsList;
	//新闻列表数据
	private ArrayList<News> newsListViewData;
	//页签新闻请求地址
	private String mUrl;


	@ViewInject(R.id.tab_detailpager_viewpager)
	private TopNewsViewPager mViewPager;
	
	@ViewInject(R.id.tab_detailpage_listview)
	private ListView mListView;
	
	@ViewInject(R.id.topnews_tvtitle)
	TextView topNewsTitle;
	
	@ViewInject(R.id.topnews_circle_indicator)
	CirclePageIndicator mCirclePageIndicator;
	


	public TabDetailPagers(Activity activity,NewsTabDatas mTabs) {
		super(activity);
		// TODO Auto-generated constructor stub
		mTabDatas = mTabs;
		mUrl = Contants.SERVER_URL + mTabs.url;
		
	}

	@Override
	public View initView() {
	View view = View.inflate(mActivity, R.layout.tabdetailpagers_main, null);
	ViewUtils.inject(this, view);
	View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);
	ViewUtils.inject(this, headerView);
	//给listview添加头布局
	mListView.addHeaderView(headerView);
		return view;

	}
	@Override
	public void initData() {
		super.initData();
		String cache = CacheUtils.getCache(mUrl, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			processResult(cache);
		}
	
		getDataFromSevice();
		
	}
	/**
	 * 从服务器获取数据
	 */
	private void getDataFromSevice() {
		HttpUtils httpUtils = new HttpUtils();

		httpUtils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// 请求成功调用
				String result = responseInfo.result;// 获得json字符串

				processResult(result);
				CacheUtils.setCache(mUrl, result, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// 请求失败调用
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();

			}
		});
	}

	protected void processResult(String result) {
	
		Gson gson = new Gson();
		NewsDatas mNewsDatas = gson.fromJson(result, NewsDatas.class);
		//获取头条新闻数据
		mTopNewsList = mNewsDatas.data.topnews;
		

		
		if(mTopNewsList != null){
			mTopNewsAdapter = new TopNewsAdapter();
			
			mViewPager.setAdapter(mTopNewsAdapter);
			//给页面指示器设置Viewpager
			mCirclePageIndicator.setViewPager(mViewPager);
			mCirclePageIndicator.setSnap(true);
			//给mCirclePageIndicator 设置监听
			mCirclePageIndicator.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int position) {
					// TODO Auto-generated method stub
					String title = mTopNewsList.get(position).title; 
					topNewsTitle.setText(title);
				}
				
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPageScrollStateChanged(int state) {
					// TODO Auto-generated method stub
					
				}
			});
				
			mCirclePageIndicator.onPageSelected(0);// 将小圆点位置归零, 解决它会在页面销毁时仍记录上次位置的bug
			topNewsTitle.setText(mTopNewsList.get(0).title);// 初始化第一页标题
			
		}else {
			Toast.makeText(mActivity, "服务器错误", Toast.LENGTH_LONG).show();
		}
		
		
		newsListViewData = mNewsDatas.data.news;
		System.out.println("新闻列表数据:" + newsListViewData);
		if (newsListViewData != null) {
			mListView.setAdapter(new MyListViewAdapter());
		}
		
	}
	
	/**
	 * 头条新闻Viewpager的适配器
	 * @author Ace
	 *
	 */
	class TopNewsAdapter extends PagerAdapter{
		private BitmapUtils bitmapUtils;

		public TopNewsAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.news_image_loading);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTopNewsList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(mActivity);
			imageView.setScaleType(ScaleType.FIT_XY);
			bitmapUtils.display(imageView, mTopNewsList.get(position).topimage);
			
			
			container.addView(imageView);
			
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		
			container.removeView((View) object);
		}	
	}
	
	/**
	 * 新闻列表适配器
	 * @author Ace
	 *
	 */
	
	class MyListViewAdapter extends BaseAdapter{
		private ViewHolder mViewHolder;
		private BitmapUtils bitmapUtils;

		public MyListViewAdapter() {
			
			bitmapUtils = new BitmapUtils(mActivity);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return newsListViewData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return newsListViewData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			mViewHolder = new ViewHolder();
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.tab_detailpager_listview_adapter_item, null);
				mViewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.tablvitem_icon);
				mViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tablvitem_tv_title);
				mViewHolder.tvDate = (TextView) convertView.findViewById(R.id.tablvitem_tv_pubtime);
				convertView.setTag(mViewHolder);
			}else{
				mViewHolder = (ViewHolder) convertView.getTag();

			}
			bitmapUtils.display(mViewHolder.ivIcon, newsListViewData.get(position).listimage);
			mViewHolder.tvTitle.setText(newsListViewData.get(position).title);
			mViewHolder.tvDate.setText(newsListViewData.get(position).pubdate);
			return convertView;
		}
		
	}
	
	class ViewHolder{
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView ivIcon;
	}
	

}
