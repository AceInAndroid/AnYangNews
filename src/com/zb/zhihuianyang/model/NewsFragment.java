package com.zb.zhihuianyang.model;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import com.zb.zhihuianyang.MainActivity;
import com.zb.zhihuianyang.R;
import com.zb.zhihuianyang.base.BaseFragment;
import com.zb.zhihuianyang.contens.Contants;
import com.zb.zhihuianyang.domain.NewsMenuData;
import com.zb.zhihuianyang.domain.NewsMenuData.NewsTabDatas;
import com.zb.zhihuianyang.model.newstabpages.TabDetailPagers;
import com.zb.zhihuianyang.utils.CacheUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class NewsFragment extends BaseFragment implements OnPageChangeListener {

	private NewsMenuData newsMenuData;

	private ViewPager mViewPager;

	private PageIndicator mPageIndicator;

	private ArrayList<NewsTabDatas> mTabList;// 页签网络数据集合
	private ArrayList<TabDetailPagers> mTabPagers;// 页签页面集合

	private View view;

	@Override
	public View initView() {
		view = View.inflate(mActivity, R.layout.tab01, null);
		mViewPager = (ViewPager) view.findViewById(R.id.news_viewpager);
		mPageIndicator = (PageIndicator) view.findViewById(R.id.indicator);
		
		return view;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		String cache = CacheUtils.getCache(Contants.CATEGORIES_URL, mActivity);
		if (!TextUtils.isEmpty(cache)) {// 用工具类TextUtils判断cache是否为空
			processResult(cache);
		} else {
			getDataFromSevice();
		}

	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromSevice() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, Contants.CATEGORIES_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// 请求成功调用
				String result = responseInfo.result;// 获得json字符串

				processResult(result);
				CacheUtils.setCache(Contants.CATEGORIES_URL, result, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// 请求失败调用
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();

			}
		});
	}

	/**
	 * 解析JSON字符串
	 * 
	 * @param result
	 */

	protected void processResult(String result) {
		// 解析JSON

		Gson gson = new Gson();
		newsMenuData = gson.fromJson(result, NewsMenuData.class);

		// 拿到侧边栏对象
		MainActivity myMainActivity = (MainActivity) mActivity;
		MenuListPager menuListPager = myMainActivity.getLeftMenuFragment();
		// 给侧边栏ListView设计数据
		menuListPager.setData(newsMenuData.data);

		mTabList = newsMenuData.data.get(0).children;

		// Toast.makeText(myMainActivity, mTabList.toString(),
		// Toast.LENGTH_LONG).show();
		if (mTabList != null) {
			initViewPagerData();
		} else {
			Toast.makeText(myMainActivity, "mTabList是空哒请检查服务器", Toast.LENGTH_LONG).show();
		}

	}

	private void initViewPagerData() {

		// 初始化12个页签
		mTabPagers = new ArrayList<TabDetailPagers>();
		for (NewsTabDatas tabData : mTabList) {
			// 创建一个页签对象
			TabDetailPagers pager = new TabDetailPagers(mActivity, tabData);
			mTabPagers.add(pager);
		}
		mViewPager.setAdapter(new NewsMenuAdapter());

		// 此方法在viewpager设置完数据之后再调用
		mPageIndicator.setViewPager(mViewPager);// 将页面指示器和ViewPager关联起来
		mPageIndicator.setOnPageChangeListener(this);// 当viewpager和指针绑定时,需要将页面切换监听设置给指针
	}

	class NewsMenuAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTabList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			TabDetailPagers pager = mTabPagers.get(position);
			container.addView(pager.mRootView);
			pager.initData();
			return pager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);

		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return mTabList.get(position).title;
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		System.out.println("position:" + position);
		if (position == 0) {// 在第一个页签,允许侧边栏出现
			// 开启侧边栏
			setSlidingMenuEnable(true);
		} else {// 其他页签,禁用侧边栏, 保证viewpager可以正常向右滑动
			// 关闭侧边栏
			setSlidingMenuEnable(false);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}

	/**
	 * 设置侧边栏可用不可用
	 * 
	 * @param enable
	 */
	private void setSlidingMenuEnable(boolean enable) {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();

		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			// 禁用掉侧边栏滑动效果
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

}
