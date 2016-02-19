package com.zb.zhihuianyang.view;

import android.content.Context;
import android.drm.DrmStore.Action;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 头条新闻ViewPager
 * 
 * @author Ace
 * @date 2016-02-15
 */

public class TopNewsViewPager extends ViewPager {
	int startX;
	int startY;

	public TopNewsViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 分情况决定父控件是否需要拦截事件
	 * 
	 * 1. 上下划动需要拦截 2. 向右划&第一个页面,需要拦截 3. 向左划&最后一个页面, 需要拦截
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			System.out.println("开始位置:" +startX + "," +startY );
			// 请求父控件及祖宗控件不要拦截事件
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
		
			int endX = (int) ev.getX();
			int endY = (int) ev.getY();

			int dx = endX - startX;
			int dy = endY - startY;
			System.out.println( "结束位置: " + endX + "," +endY );
			System.out.println( "间距:"  + dx  );

			if (Math.abs(dx) > Math.abs(dy)) {// 左右划
				if (dx > 0) {// 向右滑动
					if (this.getCurrentItem() == 0) {
						// 第一个页面
						// 请求父控件及祖宗控件拦截事件
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
	
					// 向左滑动
					if (getCurrentItem() == this.getAdapter().getCount() - 1) {
						// 最后一个item
						// 请求父控件及祖宗控件拦截事件
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}

			} else {
				// 上下滑动
				// 请求父控件及祖宗控件拦截事件
				getParent().requestDisallowInterceptTouchEvent(false);
			}

			break;

		default:
			break;
		}

		return super.dispatchTouchEvent(ev);
	}
}
