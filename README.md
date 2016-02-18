#**AnYang新闻客户端**
###框架:xUtils , slidingMenu,ViewPagerIndicator
###本项目包含以下知识点：
####1`、` 新手引导页的滑动圆点实现,需要通过全局的视图树
    ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
    		@Override
			public void onGlobalLayout() {
				
				// //移除监听
				// ivRedPoint.getViewTreeObserver()
				// .removeOnGlobalLayoutListener(this);
				//拿到绘制的小灰点间距
				mPointWidth = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
			}
		});
        
        
--------------------------------------------------------
###2`、`动画: 
##### RotateAnimation,ScaleAnimation ,AlphaAnimation.
#####动画集合:AnimationSet
#####动画状态监听:setAnimationListener
#####XML属性动画 rotate
-----------------------------------------------------
###3`、`selector的使用,selector的状态和属性设置
-----------------------------
###4`、`框架:
####① XUtils:
#####-ViewUtils模块 : 注解实现UI、资源、和事件绑定
#####-HttpUtils模块 : Get请求服务器获取JSON信息
#####-BitmapUtils模块

####②GSON

#####-GSON解析JSON数据

####③SlidingMenu: 设置侧边栏 

####④ViewPagerIndicator设置新闻页签导航

###难点:

####解决滑动事件的冲突 listview头布局的头条新闻展播的ViewPager控件通过 dispatchTouchEvent 方法
####判断手指滑动方向 和当前listview条目来,getParent().requestDisallowInterceptTouchEvent方法的
####true或者false来控制父控件是否拦截事件
----------------------
###5 代码实现下拉刷新上滑加载更多的逻辑,自定义了 MyRefreshListView 接口回调来刷新数据
######隐藏头布局 1, 获取头布局高度, 2.设置负paddingTop,布局就会向上
    mHeaderView.measure(0, 0);// 手动测量布局
    	mHeaderViewHeight = mHeaderView.getMeasuredHeight();// 测量之后的高度
		// 隐藏头布局
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

######脚布局:
    mFooterView.measure(0, 0);
    	mFooterViewHeight = mFooterView.getMeasuredHeight();
######onTouchEvent:

    public boolean onTouchEvent(MotionEvent ev) {
    	switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {// 如果用户按住头条新闻向下滑动, 会导致listview无法拿到ACTION_DOWN,
								// 此时要重新获取startY
				startY = (int) ev.getY();
			}

			// 如果当前正在刷新, 什么都不做了
			if (mCurrentState == STATE_REFRESHING) {
				break;
			}

			int endY = (int) ev.getY();
			int dy = endY - startY;

			if (dy > 0 && getFirstVisiblePosition() == 0) {// 向下滑动&当前显示的是第一个item,才允许下拉刷新
				int paddingTop = dy - mHeaderViewHeight;// 计算当前的paddingtop值

				// 根据padding切换状态
				if (paddingTop >= 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
					// 切换到松开刷新
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshState();
				} else if (paddingTop < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
					// 切换到下拉刷新
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshState();
				}

				mHeaderView.setPadding(0, paddingTop, 0, 0);// 重新设置头布局padding
				return true;
			}

			break;
		case MotionEvent.ACTION_UP:
			startY = -1;// 起始坐标归零

			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				// 如果当前是松开刷新, 就要切换为正在刷新
				mCurrentState = STATE_REFRESHING;
				// 显示头布局
				mHeaderView.setPadding(0, 0, 0, 0);

				refreshState();

				// 下拉刷新回调
				if (mListener != null) {
					mListener.onRefresh();
				}

			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				// 隐藏头布局
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			}

			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

######新建接口回调:

    // 回调下拉刷新接口的方法
	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	public interface OnRefreshListener {
		// 下拉刷新的回调
		public void onRefresh();

		// 加载更多的回调
		public void loadMore();
	}
    
###6 微信微博等主要平台的一键分享实现

###7 webView设置
