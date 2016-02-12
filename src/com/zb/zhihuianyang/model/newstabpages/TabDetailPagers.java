package com.zb.zhihuianyang.model.newstabpages;

import com.zb.zhihuianyang.base.BaseDetailPager;
import com.zb.zhihuianyang.domain.NewsMenuData.NewsTabDatas;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class TabDetailPagers extends BaseDetailPager {

	private TextView mTextView;
	private NewsTabDatas mTabDatas;

	public TabDetailPagers(Activity activity,NewsTabDatas mTabs) {
		super(activity);
		// TODO Auto-generated constructor stub
		mTabDatas = mTabs;
		
	}

	@Override
	public View initView() {
	
		mTextView = new TextView(mActivity);
		mTextView.setTextColor(Color.RED);
		mTextView.setTextSize(22);
		mTextView.setGravity(Gravity.CENTER);
		return mTextView;

	}
	@Override
	public void initData() {
		
		super.initData();
		mTextView.setText(mTabDatas.title);
		
	}

}
