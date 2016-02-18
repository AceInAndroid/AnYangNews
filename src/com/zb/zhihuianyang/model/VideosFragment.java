package com.zb.zhihuianyang.model;

import java.util.ArrayList;

import com.google.gson.Gson;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zb.zhihuianyang.MainActivity;
import com.zb.zhihuianyang.R;
import com.zb.zhihuianyang.base.BaseFragment;
import com.zb.zhihuianyang.contens.Contants;
import com.zb.zhihuianyang.domain.PhotoBeans;
import com.zb.zhihuianyang.domain.PhotoBeans.PhotoNewsDatas;
import com.zb.zhihuianyang.utils.CacheUtils;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VideosFragment extends BaseFragment implements  android.view.View.OnClickListener {

	@ViewInject(R.id.Photo_lv_list)
	private ListView lvList;
	@ViewInject(R.id.Photo_gv_list)
	private GridView gvList;
	
	private ImageButton mGridView;
	// 图片列表数据
	private ArrayList<PhotoNewsDatas> mPhotoList;

	private boolean isList = true;// 当前界面状态

	public VideosFragment(ImageButton dpgridView) {
		// TODO Auto-generated constructor stub
		mGridView = dpgridView;
	}
	
	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.tab02, null);
		ViewUtils.inject(this, view);
		mGridView.setVisibility(View.VISIBLE);
		mGridView.setOnClickListener(this);
		return view;
	}

	@Override
	public void initData() {
		String cache = CacheUtils.getCache(Contants.PHOTOS_URL, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			processResult(cache);
		}

		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, Contants.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				processResult(responseInfo.result);

				CacheUtils.setCache(Contants.PHOTOS_URL, responseInfo.result, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void processResult(String result) {
		Gson gson = new Gson();
		PhotoBeans photoBean = gson.fromJson(result, PhotoBeans.class);
		mPhotoList = photoBean.data.news;

		lvList.setAdapter(new PhotoAdapter());
		gvList.setAdapter(new PhotoAdapter());
	}

	class PhotoAdapter extends BaseAdapter {

		private BitmapUtils mBitmapUtils;

		public PhotoAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.news_image_loading);
		}

		@Override
		public int getCount() {
			return mPhotoList.size();
		}

		@Override
		public PhotoNewsDatas getItem(int position) {
			return mPhotoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_photo, null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView.findViewById(R.id.photo_tv_title);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.photo_iv_icon);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			PhotoNewsDatas item = getItem(position);

			holder.tvTitle.setText(item.title);
			mBitmapUtils.display(holder.ivIcon, item.listimage);

			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tvTitle;
		public ImageView ivIcon;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_display:
			// 如果当前是列表, 要切换成grid, 如果是grid,就切换成列表
			if (isList) {
				isList = false;

				lvList.setVisibility(View.GONE);
				gvList.setVisibility(View.VISIBLE);

				mGridView.setImageResource(R.drawable.icon_pic_list_type);
			} else {
				isList = true;

				lvList.setVisibility(View.VISIBLE);
				gvList.setVisibility(View.GONE);

				mGridView.setImageResource(R.drawable.icon_pic_grid_type);
			}

			break;

		default:
			break;
		}
	}


}
