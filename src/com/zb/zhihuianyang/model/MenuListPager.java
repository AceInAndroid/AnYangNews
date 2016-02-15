package com.zb.zhihuianyang.model;



import java.util.ArrayList;

import com.zb.zhihuianyang.MainActivity ;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zb.zhihuianyang.R;

import com.zb.zhihuianyang.base.BaseFragment;
import com.zb.zhihuianyang.domain.NewsMenuData.NewsDatas;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuListPager extends BaseFragment {
	@ViewInject(R.id.menu_listview)
	private ListView menuListView;
	private ArrayList<NewsDatas> mMenuList;
	private int mCurrrentPos;

	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		ViewUtils.inject(this, view);
		
		menuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				mCurrrentPos = position;
			}
		});
		return view;
	}
	/**
	 * 侧边栏展开或者收起的方法
	 */
	public void toggle() {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();// 开关(如果状态为开,它就关;如果状态为关,它就开)
	}
	 
	public void setData(ArrayList<NewsDatas> data){

		mMenuList = data;

		menuListView.setAdapter(new MenuAdapter());
	}
	
	class MenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mMenuList.size();
		}

		@Override
		public NewsDatas getItem(int position) {
			return mMenuList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = View.inflate(mActivity, R.layout.list_item_left_menu,
					null);
			TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);
 
			NewsDatas data = getItem(position);
			tvMenu.setText(data.title);

			if (mCurrrentPos == position) {
				// 如果当前要绘制的item刚好是被选中的, 需要设置为红色
				tvMenu.setEnabled(true);
			} else {
				// 其他item都是白色
				tvMenu.setEnabled(false);
			}
			
			return view;
		}

	}
}
