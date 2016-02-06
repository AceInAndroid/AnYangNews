package com.zb.zhihuianyang;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.zb.zhihuianyang.utils.PrefUtils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class GuideActivity extends Activity implements android.view.View.OnClickListener{

	private ViewPager vPager;
	private int[] imagesID = new int[] {R.drawable.guide_1,R.drawable.guide_4,R.drawable.guide_3};
	private ArrayList<ImageView> imageList;
	private LinearLayout llContainer;
	private ImageView ivRedPoint;
	private int mPointWidth;
	private Button startbtn;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		llContainer = (LinearLayout) findViewById(R.id.linearLayout1);
		startbtn = (Button) findViewById(R.id.button1);
		
		vPager = (ViewPager) findViewById(R.id.vp_paper);
		
		//获得小红点对象
		ivRedPoint = (ImageView)findViewById(R.id.iv_red_point);
		 
		
		//设置List集合把图片装进集合里
			imageList= new ArrayList<ImageView>();
			for(int i = 0 ; i< imagesID.length;i++){
			ImageView view = new ImageView(this);
			view.setBackgroundResource(imagesID[i]);
			imageList.add(view);
			
			//初始化圆点
			
			ImageView pointView = new ImageView(this);
			pointView.setImageResource(R.drawable.shape_cicrle_default);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
					
			if(i > 0){
				params.leftMargin = 10;
			}
			pointView.setLayoutParams(params);
			llContainer.addView(pointView);
		}
			
			
			startbtn.setOnClickListener(this);
		
		//给viewpage设置适配器
			
		
		
		vPager.setAdapter(new Myadapter());
				
		//获得视图树
				
		ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
			
			

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
//				//移除监听
//				ivRedPoint.getViewTreeObserver()
//				.removeOnGlobalLayoutListener(this);

				mPointWidth = llContainer.getChildAt(1).getLeft() -
						llContainer.getChildAt(0).getLeft();
			}
		});
		
		//给viewpage设置监听器
		
		 vPager.setOnPageChangeListener(new OnPageChangeListener() {
		 
			 
			 //第三个页面才设置按钮显示
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				if (position == imagesID.length -1){
					startbtn.setVisibility(View.VISIBLE);
				}else{
					startbtn.setVisibility(View.GONE);
				}
				
			}
			//页面滑动中变化
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				 
				System.out.println("当前位置:" + position + "偏移百分比："  + positionOffset);
				//计算当前小红点的左边距
				int leftMargin = (int)(mPointWidth* positionOffset +position*mPointWidth);
				//拿到小红点的布局参数
				ivRedPoint.getLayoutParams();
				RelativeLayout.LayoutParams params =(LayoutParams) ivRedPoint.getLayoutParams();
				params.leftMargin = leftMargin;
				ivRedPoint.setLayoutParams(params);
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
	}
	
	class Myadapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imagesID.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		};
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			
			ImageView view = imageList.get(position);
			container.addView(view);
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			
			container.removeView((View) object);
			
		}

}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			//记录引导状态 bu再展示
			PrefUtils.putBoolean("is_guide_show", true, this);
			startActivity(new Intent(this,MainActivity.class));
			finish();
			break;

		default:
			break;
		}
	}

}




