package com.zb.zhihuianyang;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class NewsContentActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.ll_controller)
	private LinearLayout llController;
	@ViewInject(R.id.top_btn_back)
	private ImageButton btnBack;
	@ViewInject(R.id.top_menu)
	private ImageButton btnMenu;
	@ViewInject(R.id.btn_textsize)
	private ImageButton btnTextSize;
	@ViewInject(R.id.btn_share)
	private ImageButton btnShare;
	@ViewInject(R.id.wv_webview)
	private WebView mWebView;
	@ViewInject(R.id.pb_loading)
	private ProgressBar pbLoading;
	@ViewInject(R.id.top_tv_title)
	private TextView toptitle;

	private String mUrl = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_content_main);
		ViewUtils.inject(this);

		btnBack.setVisibility(View.VISIBLE);
		btnMenu.setVisibility(View.GONE);
		llController.setVisibility(View.VISIBLE);
		toptitle.setVisibility(View.GONE);

		btnBack.setOnClickListener(this);
		btnTextSize.setOnClickListener(this);
		btnShare.setOnClickListener(this);
		mUrl = getIntent().getStringExtra("url");
		// 加载网页链接
		mWebView.loadUrl(mUrl);
		WebSettings settings = mWebView.getSettings();
		settings.setBuiltInZoomControls(true);// 显示放大缩小按钮
		settings.setUseWideViewPort(true);// 只是双击缩放
		settings.setJavaScriptEnabled(true);// 打开js功能

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("网页跳转:" + url);
				view.loadUrl(url);// 强制在当前页面加载网页, 不用跳浏览器
				return true;
			}

			// 网页开始加载回调
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {

				super.onPageStarted(view, url, favicon);
				System.out.println("网页开始加载");
				pbLoading.setVisibility(View.VISIBLE);
			}

			// 网页加载完成
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("网页加载结束");
				pbLoading.setVisibility(View.GONE);
			}

		});

		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// 获取加载进度
				super.onProgressChanged(view, newProgress);
				System.out.println("newProgress:" + newProgress);

			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				// 获得网页的标题
				super.onReceivedTitle(view, title);
				System.out.println("title:" + title);
			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_btn_back:
			finish();
			break;
		case R.id.btn_textsize:
			showChooseDialog();
			break;
		case R.id.btn_share:
			showShare();
			break;
		default:
			break;
		}

	}

	// 点击确定前, 用户选择的字体大小的位置
	private int mChooseItem;
	// 当前的字体位置
	private int mSelectItem = 2;

	/**
	 * 选择字体大小的弹窗
	 */
	private void showChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("字体大小设置");
		String[] mItems = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体" };

		builder.setSingleChoiceItems(mItems, mSelectItem, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mChooseItem = which;

			}
		});

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				WebSettings settings = mWebView.getSettings();

				switch (mChooseItem) {
				case 0:
					settings.setTextSize(TextSize.LARGEST);
					break;
				case 1:
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					settings.setTextSize(TextSize.SMALLEST);
					break;
				default:
					break;
				}

				mSelectItem = mChooseItem;
			}

		});

		builder.setNegativeButton("取消", null);
		builder.show();
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 修改主题
		oks.setTheme(OnekeyShareTheme.SKYBLUE);

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://www.cnblogs.com/AceIsSunshineRain/");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("Ace是个2b");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://www.cnblogs.com/AceIsSunshineRain/");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本我就试一试哈哈哈哈哈哈红红火火恍恍惚惚");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://www.cnblogs.com/AceIsSunshineRain/");

		// 启动分享GUI
		oks.show(this);
	}

}
