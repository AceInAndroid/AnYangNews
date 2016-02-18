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
		
		//��ת�����ţ�����
		RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(1000);
		animation.setFillAfter(true);
		ScaleAnimation aScaleAnimation = new ScaleAnimation(0, 1, 0, 1,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		aScaleAnimation.setDuration(1000);
		aScaleAnimation.setFillAfter(true);
		
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setFillAfter(true);
		
		
		//��������
		
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
				//�ж��Ƿ���Ҫ����������������
				Boolean isGuideShow = PrefUtils.getBoolean("is_guide_show", false, getApplicationContext());
				if(isGuideShow){
					//����������������ҳ��
					startActivity(new Intent(getApplicationContext(),
							MainActivity.class));
					
				}else {
					//����������������
					startActivity(new Intent(getApplicationContext(),
							GuideActivity.class));
				}
				
				
				finish();
			}
		});
	}
}
