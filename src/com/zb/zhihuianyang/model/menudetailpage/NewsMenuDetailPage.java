package com.zb.zhihuianyang.model.menudetailpage;

import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;
import com.zb.zhihuianyang.R;
import com.zb.zhihuianyang.base.BaseDetailPager;
import com.zb.zhihuianyang.domain.NewsMenuData.NewsTabDatas;
import com.zb.zhihuianyang.model.newstabpages.TabDetailPagers;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * 菜单详情页-新闻
 * 
 * ViewPagerIndicator使用流程: 1. 引入Library库 2. 布局文件中配置TabPageIndicator 3.
 * 将指针和Viewpager关联起来 4. 重写getPageTitle方法,返回每个页面的标题(PagerAdapter) 5.
 * 设置activity主题样式 6. 修改源码中的样式(修改图片, 文字颜色)
 * 
 * @author Ace
 * 
 * @date 2016-2-12
 */

public class NewsMenuDetailPage extends BaseDetailPager {

	@ViewInject(R.id.vp_news_detail)
	private ViewPager mViewPager;
	@ViewInject(R.id.indicator)
	private TabPageIndicator mIndicator;

	private ArrayList<NewsTabDatas> mTabList;// 页签网络数据集合
	private ArrayList<TabDetailPagers> mTabPagers;// 页签页面集合

	public NewsMenuDetailPage(Activity activity, ArrayList<NewsTabDatas> children) {
		super(activity);
		mTabList = children;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, com.zb.zhihuianyang.R.layout.menu_news_detailpager, null);
		ViewUtils.inject(this, view);
		return view;

	}

	@Override
	public void initData() {
		// 初始化12个页签
		mTabPagers = new ArrayList<TabDetailPagers>();
		for (NewsTabDatas tabData : mTabList) {
			// 创建一个页签对象
			TabDetailPagers pager = new TabDetailPagers(mActivity, tabData);
			mTabPagers.add(pager);
		}

		mViewPager.setAdapter(new NewsMenuAdapter());
		// mViewPager.setOnPageChangeListener(this);

		// 此方法在viewpager设置完数据之后再调用
		mIndicator.setViewPager(mViewPager);// 将页面指示器和ViewPager关联起来

	}

	class NewsMenuAdapter extends PagerAdapter {
		// 返回页面指示器的标题
		@Override
		public CharSequence getPageTitle(int position) {
			return mTabList.get(position).title;
		}

		@Override
		public int getCount() {
			return mTabPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPagers pager = mTabPagers.get(position);
			container.addView(pager.mRootView);
			pager.initData();// 初始化数据
			return pager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
