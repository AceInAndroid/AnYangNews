package com.zb.zhihuianyang;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zb.zhihuianyang.model.FindFragment;
import com.zb.zhihuianyang.model.MeFragment;
import com.zb.zhihuianyang.model.MenuListPager;
import com.zb.zhihuianyang.model.NewsFragment;
import com.zb.zhihuianyang.model.VideosFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/* 
 * ace in 20160207
 */

public class MainActivity extends SlidingFragmentActivity implements OnClickListener {
	private LinearLayout mNews;
	private LinearLayout mVideos;
	private LinearLayout mFind;
	private LinearLayout mMe;

	private Fragment mTab01;
	private Fragment mTab02;
	private Fragment mTab03;
	private Fragment mTab04;

	private ImageButton topMenu;
	
	public ImageButton dpgridView;

	// 给每个fragment设置tag
	private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
	private static final String TAG_NEWS = "NEWS";
	private static final String TAG_VIDEO = "VIDEO";
	private static final String TAG_FIND = "FIND";
	private static final String TAG_ME = "ME";
	

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 初始化

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initEvents();
		setFrag(0);

	}

	/**
	 * 初始化事件
	 */
	private void initEvents() {
		mNews.setOnClickListener(this);
		mMe.setOnClickListener(this);
		mFind.setOnClickListener(this);
		mVideos.setOnClickListener(this);
		topMenu.setOnClickListener(this);
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		// 初始化控件
		mNews = (LinearLayout) findViewById(R.id.news);
		mVideos = (LinearLayout) findViewById(R.id.video);
		mFind = (LinearLayout) findViewById(R.id.find);
		mMe = (LinearLayout) findViewById(R.id.me);
		topMenu = (ImageButton) findViewById(R.id.top_menu);
		dpgridView = (ImageButton) findViewById(R.id.btn_display);

		// 初始化侧滑菜单
		// 添加侧边栏

		setBehindContentView(R.layout.left_menu);
		SlidingMenu slidingMenu = getSlidingMenu();
		// 全屏触摸
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 屏幕预留250像素
		slidingMenu.setBehindOffset(250);
		initfragment();

	}
	
	private void initfragment() {
		// TODO Auto-generated method stub
		// 获得事务
		FragmentManager fm = getSupportFragmentManager();
		// 开启事务
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.fl_left_menu, new MenuListPager(), TAG_LEFT_MENU);
		transaction.commit();
	}

	// 设置点击事件
	@Override
	public void onClick(View v) {
		mNews.setBackgroundColor(Color.parseColor("#E6E6E6"));
		resetBackground();
		switch (v.getId()) {
		case R.id.news:
			setFrag(0);

			mNews.setBackgroundColor(Color.parseColor("#E6E6E6"));
			break;
		case R.id.video:
			setFrag(1);
			mVideos.setBackgroundColor(Color.parseColor("#E6E6E6"));
			break;
		case R.id.find:
			setFrag(2);
			mFind.setBackgroundColor(Color.parseColor("#E6E6E6"));
			break;

		case R.id.me:
			setFrag(3);
			mMe.setBackgroundColor(Color.parseColor("#E6E6E6"));
			break;

		case R.id.top_menu:
			getLeftMenuFragment().toggle();

			break;

		default:
			break;
		}
	}

	private void resetBackground() {
		// 设置背景色
		mNews.setBackgroundColor(Color.parseColor("#F7F7F7"));
		mFind.setBackgroundColor(Color.parseColor("#F7F7F7"));
		mMe.setBackgroundColor(Color.parseColor("#F7F7F7"));
		mVideos.setBackgroundColor(Color.parseColor("#F7F7F7"));
	}

	// 设置要显示的Fragment
	private void setFrag(int i) {
		// TODO Auto-generated method stub
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		setFragmenthide(transaction);
		switch (i) {
		case 0:
			if (mTab01 == null) {
				mTab01 = new NewsFragment();

				transaction.add(R.id.id_content, mTab01, TAG_NEWS);
			} else {
				transaction.show(mTab01);
			}

			break;
		case 1:
			if (mTab02 == null) {
				mTab02 = new VideosFragment(dpgridView);
				transaction.add(R.id.id_content, mTab02, TAG_VIDEO);
			} else {
				transaction.show(mTab02);
			}

			break;
		case 2:
			if (mTab03 == null) {
				mTab03 = new FindFragment();
				transaction.add(R.id.id_content, mTab03, TAG_FIND);
			} else {
				transaction.show(mTab03);
			}

			break;
		case 3:
			if (mTab04 == null) {
				mTab04 = new MeFragment();
				transaction.add(R.id.id_content, mTab04, TAG_ME);
			} else {
				transaction.show(mTab04);
			}

			break;

		default:
			break;
		}
		transaction.commit();
	}

	private void setFragmenthide(FragmentTransaction transaction) {
		// 隐藏所有Fragment

		if (mTab01 != null) {
			transaction.hide(mTab01);
		}
		if (mTab02 != null) {
			transaction.hide(mTab02);
		}
		if (mTab03 != null) {
			transaction.hide(mTab03);
		}
		if (mTab04 != null) {
			transaction.hide(mTab04);
		}

	}

	public MenuListPager getLeftMenuFragment() {
		FragmentManager fm = getSupportFragmentManager();
		MenuListPager fragment = (MenuListPager) fm.findFragmentByTag(TAG_LEFT_MENU);
		return fragment;
	}

	public NewsFragment getNewsFragment() {
		FragmentManager fm = getSupportFragmentManager();
		NewsFragment fragment = (NewsFragment) fm.findFragmentByTag(TAG_NEWS);
		return fragment;
	}

}
