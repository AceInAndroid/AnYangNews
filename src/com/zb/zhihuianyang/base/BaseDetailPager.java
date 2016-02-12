package com.zb.zhihuianyang.base;

import android.app.Activity;
import android.view.View;

/**
 * 侧边栏菜单详情页基类
 * 
 * @author Ace
 * @date 2016-2-12
 */
public abstract class BaseDetailPager {

	public Activity mActivity;
	// 菜单详情页根布局
	public View mRootView;

	public BaseDetailPager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
	}

	public abstract View initView();

	public void initData() {

	}
}
