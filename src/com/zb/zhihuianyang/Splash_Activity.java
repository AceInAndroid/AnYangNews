package com.zb.zhihuianyang;

import com.zb.zhihuianyang.utils.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class Splash_Activity extends Activity {

	private RelativeLayout rlRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_layout);
		rlRoot = (RelativeLayout) findViewById(R.id.rlRoot);
		
		//旋转，缩放，渐变
		RotateAnimation animation = new RotateAnimation(0, 360, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(1000);
		animation.setFillAfter(true);
		ScaleAnimation aScaleAnimation = new ScaleAnimation(0, 1, 0, 1,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		aScaleAnimation.setDuration(1000);
		aScaleAnimation.setFillAfter(true);
		
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setFillAfter(true);
		
		
		//动画集合
		
		AnimationSet aSet = new AnimationSet(true);
		aSet.addAnimation(animation);
		aSet.addAnimation(aScaleAnimation);
		aSet.addAnimation(alphaAnimation);
		rlRoot.startAnimation(aSet);
		
		aSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				//判断是否需要跳到新手引导界面
				Boolean isGuideShow = PrefUtils.getBoolean("is_guide_show", false, getApplicationContext());
				if(isGuideShow){
					//动画结束后跳到主页面
					startActivity(new Intent(getApplicationContext(),
							MainActivity.class));
					
				}else {
					//跳到新手引导界面
					startActivity(new Intent(getApplicationContext(),
							GuideActivity.class));
				}
				
				
				finish();
			}
		});
	}
}
