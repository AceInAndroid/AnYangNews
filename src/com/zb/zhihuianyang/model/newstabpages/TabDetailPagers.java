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
import com.zb.zhihuianyang.MainActivity;
import com.zb.zhihuianyang.NewsContentActivity;
import com.zb.zhihuianyang.R;
import com.zb.zhihuianyang.base.BaseDetailPager;
import com.zb.zhihuianyang.contens.Contants;
import com.zb.zhihuianyang.domain.NewsDatas;
import com.zb.zhihuianyang.domain.NewsDatas.NewsTabs.News;
import com.zb.zhihuianyang.domain.NewsDatas.NewsTabs.TopNews;
import com.zb.zhihuianyang.domain.NewsMenuData.NewsTabDatas;
import com.zb.zhihuianyang.utils.CacheUtils;
import com.zb.zhihuianyang.utils.PrefUtils;
import com.zb.zhihuianyang.view.MyRefreshListView;
import com.zb.zhihuianyang.view.MyRefreshListView.OnRefreshListener;
import com.zb.zhihuianyang.view.TopNewsViewPager;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TabDetailPagers extends BaseDetailPager {

	// 新闻列表数据
	private NewsTabDatas mTabDatas;
	// 头条新闻ViewPager适配器
	private TopNewsAdapter mTopNewsAdapter;
	// 头条新闻列表数据
	private ArrayList<TopNews> mTopNewsList;
	// 新闻列表数据
	private ArrayList<News> newsListViewData;
	// 页签新闻请求地址
	private String mUrl;
	// 下一页的链接
	private String mMoreUrl;
	//图片轮播handler
	private Handler mHandler;

	@ViewInject(R.id.tab_detailpager_viewpager)
	private TopNewsViewPager mViewPager;

	@ViewInject(R.id.tab_detailpage_listview)
	private MyRefreshListView mListView;

	@ViewInject(R.id.topnews_tvtitle)
	TextView topNewsTitle;

	@ViewInject(R.id.topnews_circle_indicator)
	CirclePageIndicator mCirclePageIndicator;
	private MyListViewAdapter newsListViewDataAdapter;
	
	

	public TabDetailPagers(Activity activity, NewsTabDatas mTabs) {
		super(activity);
		// TODO Auto-generated constructor stub
		mTabDatas = mTabs;
		mUrl = Contants.SERVER_URL + mTabs.url;
		MainActivity mainActivity = new MainActivity();
		Intent intent = new Intent();
		intent.putExtra("url", mUrl);
		
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.tabdetailpagers_main, null);
		ViewUtils.inject(this, view);
		View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);
		ViewUtils.inject(this, headerView);
		// 给listview添加头布局
		mListView.addHeaderView(headerView);
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				// 从网络加载数据
				getDataFromSevice();
			}

			// 加载更多
			@Override
			public void loadMore() {
				// 加载更多数据
				if (mMoreUrl != null) {
//					System.out.println("加载下一页数据...");
					getMoreDataFromServer();
				} else {
					mListView.onRefreshComplete(true);// 收起加载更多布局
					Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
				}
			}
		});
		// 给listview设置点击监听
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
//				System.out.println("pos:" + position);

				News news = newsListViewData.get(position);

				// 当前点击的item的标题颜色置灰
				TextView tvTitle = (TextView) view.findViewById(R.id.tablvitem_tv_title);
				tvTitle.setTextColor(Color.GRAY);

				// 将已读状态持久化到本地
				// key: read_ids; value: 1324,1325,1326
				String readIds = PrefUtils.getString("read_ids", "", mActivity);
				if (!readIds.contains(news.id)) {// 以前没有添加过,才添加进来
					readIds = readIds + news.id + ",";// 1324,1325,
					PrefUtils.putString("read_ids", readIds, mActivity);
				}
				
				// 跳到详情页
				Intent intent = new Intent(mActivity, NewsContentActivity.class);
				intent.putExtra("url", news.url);
				mActivity.startActivity(intent);
			}

		});
		
       
        

		return view;

	}
	
	
	    

	@Override
	public void initData() {
		super.initData();
		String cache = CacheUtils.getCache(mUrl, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			processResult(cache, false);
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

				processResult(result, false);
				CacheUtils.setCache(mUrl, result, mActivity);
				mListView.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// 请求失败调用
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
				mListView.onRefreshComplete(false);
			}
		});
	}

	/**
	 * 加载更多数据
	 */
	protected void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processResult(result, true);
				// 收起加载更多布局
				mListView.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				// 收起加载更多布局
				mListView.onRefreshComplete(false);
			}
		});
	}

	protected void processResult(String result, Boolean isMore) {

		Gson gson = new Gson();
		NewsDatas mNewsDatas = gson.fromJson(result, NewsDatas.class);
		if (!TextUtils.isEmpty(mNewsDatas.data.more)) {
			// 初始化地址
			mMoreUrl = Contants.SERVER_URL + mNewsDatas.data.more;
		} else {
			// 没有下一页了
			mMoreUrl = null;
		}

		if (!isMore) {
			// 获取头条新闻数据
			mTopNewsList = mNewsDatas.data.topnews;
			if (mTopNewsList != null) {
				mTopNewsAdapter = new TopNewsAdapter();
				mViewPager.setAdapter(mTopNewsAdapter);
				// 给页面指示器设置Viewpager
				mCirclePageIndicator.setViewPager(mViewPager);// 将指示器和viewpager绑定
				mCirclePageIndicator.setSnap(true);
				// 给mCirclePageIndicator 设置监听
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

				mCirclePageIndicator.onPageSelected(0);// 将小圆点位置归零,
														// 解决它会在页面销毁时仍记录上次位置的bug
				topNewsTitle.setText(mTopNewsList.get(0).title);// 初始化第一页标题
			}

			// 初始化新闻列表
			newsListViewData = mNewsDatas.data.news;
			// System.out.println("新闻列表数据:" + newsListViewData);
			if (newsListViewData != null) {
				newsListViewDataAdapter = new MyListViewAdapter();
				mListView.setAdapter(newsListViewDataAdapter);
			}
			//图片轮播效果
			if (mHandler == null) {
				mHandler = new Handler() {
					public void handleMessage(android.os.Message msg) {
						int currentItem = mViewPager.getCurrentItem();

						if (currentItem < mTopNewsList.size() - 1) {
							currentItem++;
						} else {
							currentItem = 0;
						}

						mViewPager.setCurrentItem(currentItem);

						mHandler.sendEmptyMessageDelayed(0, 2000);
					};
				};

				// 发送延时消息延时2秒切换广告条
				mHandler.sendEmptyMessageDelayed(0, 2000);
				
			
				mViewPager.setOnTouchListener(new OnTouchListener(){

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							System.out.println("ACTION_DOWN");
							// 删除所有消息
							mHandler.removeCallbacksAndMessages(null);
							break;
						case MotionEvent.ACTION_CANCEL:// 事件取消(当按下后,然后移动下拉刷新,导致抬起后无法响应ACTION_UP,
														// 但此时会响应ACTION_CANCEL,也需要继续播放轮播条)
						case MotionEvent.ACTION_UP:
							// 延时2秒切换广告条
							mHandler.sendEmptyMessageDelayed(0, 2000);
							break;
						default:
							break;
						}
						return false;
					}
					
				});
			
			}
				
		} else {
			// 加载更多
			ArrayList<News> moreData = mNewsDatas.data.news;
			newsListViewData.addAll(moreData);// 追加数据
			newsListViewDataAdapter.notifyDataSetChanged();// 刷新listview
		}

	}

	/**
	 * 头条新闻Viewpager的适配器
	 * 
	 * @author Ace
	 *
	 */
	class TopNewsAdapter extends PagerAdapter {
		private BitmapUtils bitmapUtils;

		public TopNewsAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.news_image_loading);// 设置默认加载图片
		}

		@Override
		public int getCount() {

			return mTopNewsList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {

			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(mActivity);
			imageView.setScaleType(ScaleType.FIT_XY);// 设置图片填充效果, 表示填充父窗体
			// 获取图片链接, 使用链接下载图片, 将图片设置给ImageView, 考虑内存溢出问题, 图片本地缓存
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
	 * 
	 * @author Ace
	 *
	 */

	class MyListViewAdapter extends BaseAdapter {

		private BitmapUtils bitmapUtils;

		public MyListViewAdapter() {
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils.configDefaultLoadingImage(R.drawable.news_image_loading);
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
			ViewHolder mViewHolder;

			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.tab_detailpager_listview_adapter_item, null);
				mViewHolder = new ViewHolder();
				mViewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.tablvitem_icon);
				mViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tablvitem_tv_title);
				mViewHolder.tvDate = (TextView) convertView.findViewById(R.id.tablvitem_tv_pubtime);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();

			}

			News news = (News) getItem(position);
			System.out.println("当前条目的新闻是" + news.toString());
			mViewHolder.tvTitle.setText(news.title);
			mViewHolder.tvDate.setText(news.pubdate);
			bitmapUtils.display(mViewHolder.ivIcon, news.listimage);

			// 标记已读和未读
			String readIds = PrefUtils.getString("read_ids", "", mActivity);
			if (readIds.contains(news.id)) {
				// 已读
				mViewHolder.tvTitle.setTextColor(Color.GRAY);
			} else {
				// 未读
				mViewHolder.tvTitle.setTextColor(Color.BLACK);
			}
			return convertView;
		}
	}

	class ViewHolder {
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView ivIcon;
	}

}
