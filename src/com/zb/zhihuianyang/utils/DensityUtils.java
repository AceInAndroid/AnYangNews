package com.zb.zhihuianyang.utils;

import android.content.Context;

public class DensityUtils {
	public static float pxToDp(float px ,Context context){
		float density = context.getResources().getDisplayMetrics().density;
		float dp = px / density;
		return dp;
	}
	
	public static int dpToPx(float dp , Context context){
		float density = context.getResources().getDisplayMetrics().density;
		int px = (int) (dp * density * 0.5f);
		return px;
	}

}
