#**AnYang���ſͻ���**
###���:xUtils , slidingMenu,ViewPagerIndicator
###����Ŀ��������֪ʶ�㣺
####1`��` ��������ҳ�Ļ���Բ��ʵ��,��Ҫͨ��ȫ�ֵ���ͼ��
    ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
    		@Override
			public void onGlobalLayout() {
				
				// //�Ƴ�����
				// ivRedPoint.getViewTreeObserver()
				// .removeOnGlobalLayoutListener(this);
				//�õ����Ƶ�С�ҵ���
				mPointWidth = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
			}
		});
        
        
--------------------------------------------------------
###2`��`����: 
##### RotateAnimation,ScaleAnimation ,AlphaAnimation.
#####��������:AnimationSet
#####����״̬����:setAnimationListener
#####XML���Զ��� rotate
-----------------------------------------------------
###3`��`selector��ʹ��,selector��״̬����������
-----------------------------
###4`��`���:
####�� XUtils:
#####-ViewUtilsģ�� : ע��ʵ��UI����Դ�����¼���
#####-HttpUtilsģ�� : Get�����������ȡJSON��Ϣ
#####-BitmapUtilsģ��

####��GSON

#####-GSON����JSON����

####��SlidingMenu: ���ò���� 

####��ViewPagerIndicator��������ҳǩ����

###�ѵ�:

####��������¼��ĳ�ͻ listviewͷ���ֵ�ͷ������չ����ViewPager�ؼ�ͨ�� dispatchTouchEvent ����
####�ж���ָ�������� �͵�ǰlistview��Ŀ��,getParent().requestDisallowInterceptTouchEvent������
####true����false�����Ƹ��ؼ��Ƿ������¼�
----------------------
###5 ����ʵ������ˢ���ϻ����ظ�����߼�,�Զ����� MyRefreshListView �ӿڻص���ˢ������
######����ͷ���� 1, ��ȡͷ���ָ߶�, 2.���ø�paddingTop,���־ͻ�����
    mHeaderView.measure(0, 0);// �ֶ���������
    	mHeaderViewHeight = mHeaderView.getMeasuredHeight();// ����֮��ĸ߶�
		// ����ͷ����
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

######�Ų���:
    mFooterView.measure(0, 0);
    	mFooterViewHeight = mFooterView.getMeasuredHeight();
######onTouchEvent:

    public boolean onTouchEvent(MotionEvent ev) {
    	switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {// ����û���סͷ���������»���, �ᵼ��listview�޷��õ�ACTION_DOWN,
								// ��ʱҪ���»�ȡstartY
				startY = (int) ev.getY();
			}

			// �����ǰ����ˢ��, ʲô��������
			if (mCurrentState == STATE_REFRESHING) {
				break;
			}

			int endY = (int) ev.getY();
			int dy = endY - startY;

			if (dy > 0 && getFirstVisiblePosition() == 0) {// ���»���&��ǰ��ʾ���ǵ�һ��item,����������ˢ��
				int paddingTop = dy - mHeaderViewHeight;// ���㵱ǰ��paddingtopֵ

				// ����padding�л�״̬
				if (paddingTop >= 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
					// �л����ɿ�ˢ��
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshState();
				} else if (paddingTop < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
					// �л�������ˢ��
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshState();
				}

				mHeaderView.setPadding(0, paddingTop, 0, 0);// ��������ͷ����padding
				return true;
			}

			break;
		case MotionEvent.ACTION_UP:
			startY = -1;// ��ʼ�������

			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				// �����ǰ���ɿ�ˢ��, ��Ҫ�л�Ϊ����ˢ��
				mCurrentState = STATE_REFRESHING;
				// ��ʾͷ����
				mHeaderView.setPadding(0, 0, 0, 0);

				refreshState();

				// ����ˢ�»ص�
				if (mListener != null) {
					mListener.onRefresh();
				}

			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				// ����ͷ����
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			}

			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

######�½��ӿڻص�:

    // �ص�����ˢ�½ӿڵķ���
	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	public interface OnRefreshListener {
		// ����ˢ�µĻص�
		public void onRefresh();

		// ���ظ���Ļص�
		public void loadMore();
	}
    
###6 ΢��΢������Ҫƽ̨��һ������ʵ��

###7 webView����
