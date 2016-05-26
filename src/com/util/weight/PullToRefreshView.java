package com.util.weight;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hw.chineseLearn.R;

/**
 * 下拉刷新
 * 
 * @author
 */
public class PullToRefreshView extends LinearLayout {
	private static final String TAG = "PullToRefreshView";
	// refresh states
	private static final int PULL_TO_REFRESH = 2; // 上拉刷新（正在上拉刷新，还没有放手）
	private static final int RELEASE_TO_REFRESH = 3; // 下拉刷新（正在下拉，还没有放手）
	private static final int REFRESHING = 4; // 正在刷新（这个刷新可以是下拉刷新也可以是上拉刷新）
	// pull state
	private static final int PULL_UP_STATE = 0;
	private static final int PULL_DOWN_STATE = 1;
	/**
	 * last y
	 */
	private int mLastMotionY;
	/**
	 * lock
	 */
	private boolean mLock;
	/**
	 * header view
	 */
	private View mHeaderView;
	/**
	 * footer view
	 */
	private View mFooterView;
	/**
	 * list or grid
	 */
	private AdapterView<?> mAdapterView;
	/**
	 * scrollview
	 */
	private ScrollView mScrollView;
	/**
	 * header view height
	 */
	private int mHeaderViewHeight;
	/**
	 * footer view height
	 */
	private int mFooterViewHeight;
	/**
	 * header view image
	 */
	private ImageView mHeaderImageView;
	/**
	 * footer view image
	 */
	private ImageView mFooterImageView;
	/**
	 * header tip text
	 */
	private TextView mHeaderTextView;
	/**
	 * footer tip text
	 */
	private TextView mFooterTextView;
	/**
	 * header refresh time
	 */
	private TextView mHeaderUpdateTextView;
	/**
	 * footer refresh time
	 */
	private TextView mFooterUpdateTextView;
	/**
	 * header progress bar
	 */
	private ProgressBar mHeaderProgressBar;
	/**
	 * footer progress bar
	 */
	private ProgressBar mFooterProgressBar;
	/**
	 * layout inflater
	 */
	private LayoutInflater mInflater;
	/**
	 * header view current state
	 */
	private int mHeaderState;
	/**
	 * footer view current state
	 */
	private int mFooterState;
	/**
	 * pull state,pull up or pull down;PULL_UP_STATE or PULL_DOWN_STATE
	 */
	private int mPullState;
	/**
	 * 变为向下的箭头改变箭头方向
	 */
	private RotateAnimation mDownAnimation;
	/**
	 * 变为逆向的箭头旋转
	 */
	private RotateAnimation mReverseFlipAnimation;
	/**
	 * footer refresh listener
	 */
	private OnFooterRefreshListener mOnFooterRefreshListener;
	/**
	 * footer refresh listener
	 */
	private OnHeaderRefreshListener mOnHeaderRefreshListener;

	private OnRefreshTouchListener mOnTouchListener;
	private boolean IsTouch = true;

	private boolean canLoad = true;// 支持上拉操作的标志位

	/**
	 * last update time
	 */
	// private String mLastUpdateTime;

	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullToRefreshView(Context context) {
		super(context);
		init();
	}

	/**
	 * init
	 * 
	 * @description
	 * @param context
	 *            hylin 2012-7-26上午10:08:33
	 */
	private void init() {
		// Load all of the animations we need in code rather than through XML
		mDownAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mDownAnimation.setInterpolator(new LinearInterpolator());
		mDownAnimation.setDuration(250);
		mDownAnimation.setFillAfter(true);

		mReverseFlipAnimation = new RotateAnimation(-180, 00,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(250);
		mReverseFlipAnimation.setFillAfter(true);

		mInflater = LayoutInflater.from(getContext());
		// header view 在此添加,保证是第一次添加到linearlayout的最上端
		 addHeaderView();
	}

	private void addHeaderView() {
		// header view
		mHeaderView = mInflater.inflate(R.layout.refresh_header, this, false);

		mHeaderImageView = (ImageView) mHeaderView
				.findViewById(R.id.pull_to_refresh_image);
		mHeaderTextView = (TextView) mHeaderView
				.findViewById(R.id.pull_to_refresh_text);
		mHeaderUpdateTextView = (TextView) mHeaderView
				.findViewById(R.id.pull_to_refresh_updated_header);
		mHeaderProgressBar = (ProgressBar) mHeaderView
				.findViewById(R.id.pull_to_refresh_progress);
		// header layout
		measureView(mHeaderView);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				mHeaderViewHeight);
		// 设置topMargin的高度为负的header View高度,即将其隐藏在
		params.topMargin = -(mHeaderViewHeight);
		// mHeaderView.setLayoutParams(params1);
		addView(mHeaderView, params);

	}

	private void addFooterView() {
		// footer view
		mFooterView = mInflater.inflate(R.layout.refresh_footer, this, false);
		mFooterImageView = (ImageView) mFooterView
				.findViewById(R.id.pull_to_load_image);
		mFooterTextView = (TextView) mFooterView
				.findViewById(R.id.pull_to_load_text);
		mFooterUpdateTextView = (TextView) mFooterView
				.findViewById(R.id.pull_to_refresh_updated_footer);
		mFooterProgressBar = (ProgressBar) mFooterView
				.findViewById(R.id.pull_to_load_progress);
		// footer layout
		measureView(mFooterView);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				mFooterViewHeight);
		// int top = getHeight();
		// params.topMargin
		// =getHeight();//在这里getHeight()==0,但在onInterceptTouchEvent()方法里getHeight()已经有�?�?不再�?;
		// getHeight()�?��时�?会赋�?稍�?再研究一�?
		// 由于是线性布�?��以直接添�?只要AdapterView的高度是MATCH_PARENT,那么footer
		// view就会被添加到�?��,并隐�?
		addView(mFooterView, params);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// footer view 在此添加保证添加到linearlayout中的�?��
		addFooterView();
		initContentAdapterView();
	}

	/**
	 * init AdapterView like ListView,GridView and so on;or init ScrollView
	 * 
	 * @description hylin 2012-7-30下午8:48:12
	 */
	private void initContentAdapterView() {
		int count = getChildCount();
		if (count < 3) {
			throw new IllegalArgumentException(
					"this layout must contain 3 child views,and AdapterView or ScrollView must in the second position!");
		}
		View view = null;
		for (int i = 0; i < count - 1; ++i) {
			view = getChildAt(i);
			if (view instanceof AdapterView<?>) {
				mAdapterView = (AdapterView<?>) view;
			}
			if (view instanceof ScrollView) {
				// finish later
				mScrollView = (ScrollView) view;
			}
		}
		if (mAdapterView == null && mScrollView == null) {
			throw new IllegalArgumentException(
					"must contain a AdapterView or ScrollView in this layout!");
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int y = (int) e.getRawY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 首先拦截down事件,记录y坐标
			mLastMotionY = y;
			if (mOnTouchListener != null) {
				mOnTouchListener.onTouchListener(this);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			System.out.println("ACTION_MOVE");
			// deltaY > 0 是向下运�?< 0是向上运�?
			int deltaY = y - mLastMotionY;
			if (isRefreshViewScroll(deltaY)) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return false;
	}

	/*
	 * 如果在onInterceptTouchEvent()方法中没有拦�?即onInterceptTouchEvent()方法�?return
	 * false)则由PullToRefreshView 的子View来处�?否则由下面的方法来处�?即由PullToRefreshView自己来处�?
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mLock) {
			return true;
		}
		int y = (int) event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// onInterceptTouchEvent已经记录
			// mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaY = y - mLastMotionY;
			if (mPullState == PULL_DOWN_STATE) {
				// PullToRefreshView执行下拉
				Log.i(TAG, " pull down!parent view move!");
				// headerPrepareToRefresh(deltaY);a
				// setHeaderPadding(-mHeaderViewHeight);
			} else if (mPullState == PULL_UP_STATE && canLoad) {
				// PullToRefreshView执行上拉
				Log.i(TAG, "pull up!parent view move!");
				footerPrepareToRefresh(deltaY);
			}
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			int topMargin = getHeaderTopMargin();
			if (mPullState == PULL_DOWN_STATE) {
				if (topMargin >= 0) {
					// �?��刷新
					// headerRefreshing();a
				} else {
					// 还没有执行刷新，重新隐藏
					setHeaderTopMargin(-mHeaderViewHeight);
				}
			} else if (mPullState == PULL_UP_STATE) {
				if (Math.abs(topMargin) >= mHeaderViewHeight
						+ mFooterViewHeight) {
					// �?��执行footer 刷新
					footerRefreshing();
				} else {
					// 还没有执行刷新，重新隐藏
					setHeaderTopMargin(-mHeaderViewHeight);
				}
			}

			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 是否应该到了父View,即PullToRefreshView滑动
	 * 
	 * @param deltaY
	 *            , deltaY > 0 是向下运�?< 0是向上运�?
	 * @return
	 */
	private boolean isRefreshViewScroll(int deltaY) {
		if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
			return false;
		}
		// 对于ListView和GridView
		if (mAdapterView != null) {
			// 子view(ListView or GridView)滑动到最顶端
			if (deltaY > 5) {

				View child = mAdapterView.getChildAt(0);
				if (child == null) {
					// 如果mAdapterView中没有数�?不拦�?
					return false;
				}
				if (mAdapterView.getFirstVisiblePosition() == 0
						&& child.getTop() == 0) {
					mPullState = PULL_DOWN_STATE;
					return true;
				}
				int top = child.getTop();
				int padding = mAdapterView.getPaddingTop();
				if (mAdapterView.getFirstVisiblePosition() == 0
						&& Math.abs(top - padding) <= 8
						&& mPullState != RELEASE_TO_REFRESH) {// 这里之前�?可以判断,但现在不�?还没找到原因
					mPullState = PULL_DOWN_STATE;
					return true;
				}

			} else if (deltaY < -5) {
				View lastChild = mAdapterView.getChildAt(mAdapterView
						.getChildCount() - 1);
				if (lastChild == null || !canLoad) {
					// 如果mAdapterView中没有数�?不拦�?
					return false;
				}
				// �?���?��子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
				// 等于父View的高度说明mAdapterView已经滑动到最�?并且还不能是人家下拉刷新的步�?
				if (lastChild.getBottom() <= getHeight()
						&& mAdapterView.getLastVisiblePosition() == mAdapterView
								.getCount() - 1
						&& mPullState != PULL_TO_REFRESH) {
					mPullState = PULL_UP_STATE;
					return true;
				}
			}
		}
		// 对于ScrollView
		if (mScrollView != null) {
			// 子scroll view滑动到最顶端
			View child = mScrollView.getChildAt(0);
			if (deltaY > 5 && mScrollView.getScrollY() == 0) {
				mPullState = PULL_DOWN_STATE;
				return true;
			} else if (deltaY < -5
					&& child.getMeasuredHeight() <= getHeight()
							+ mScrollView.getScrollY()) {
				mPullState = PULL_UP_STATE;
				return true;
			}
		}
		return false;
	}

	/**
	 * header 准备刷新,手指移动过程,还没有释�?
	 * 
	 * @param deltaY
	 *            ,手指滑动的距�?
	 */
	private int recoverDown = 0; // 0 表示未恢复，1表示已恢�?

	private void headerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		// 当header view的topMargin>=0时，说明已经完全显示出来�?修改header view 的提示状�?
		if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH
				&& deltaY > 0) {
			mHeaderTextView.setText(R.string.pull_to_refresh_release_label);
			mHeaderUpdateTextView.setVisibility(View.VISIBLE);
			mHeaderState = RELEASE_TO_REFRESH;
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mDownAnimation);
			recoverDown = 0;
		}
		if (newTopMargin <= 0 && deltaY < 0 && recoverDown == 0
				&& mHeaderState == RELEASE_TO_REFRESH) {
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mReverseFlipAnimation);
			mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
			recoverDown = 1;
		} else if (newTopMargin >= 0 && deltaY > 0 && recoverDown == 1) {
			mHeaderTextView.setText(R.string.pull_to_refresh_release_label);
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mDownAnimation);
			recoverDown = 0;
		}
	}

	/**
	 * footer 准备刷新,手指移动过程,还没有释�?移动footer view高度同样和移动header view
	 * 高度是一样，都是通过修改header view的topmargin的�?来达�?
	 * 
	 * @param deltaY
	 *            ,手指滑动的距�?
	 */
	private int recoverUp = 0;// 0 表示未恢复，1表示已恢�?

	private void footerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		// 如果header view topMargin 的绝对�?大于或等于header + footer 的高�?
		// 说明footer view 完全显示出来了，修改footer view 的提示状�?
		if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight)
				&& mFooterState != RELEASE_TO_REFRESH) {
			mFooterTextView
					.setText(R.string.pull_to_refresh_footer_release_label);
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mDownAnimation);
			mFooterState = RELEASE_TO_REFRESH;
			recoverUp = 0;
		}
		if (Math.abs(newTopMargin) <= (mHeaderViewHeight + mFooterViewHeight)
				&& deltaY > 0 && recoverUp == 0
				&& mFooterState == RELEASE_TO_REFRESH) {
			mFooterTextView.setText(R.string.pull_to_refresh_footer_pull_label);
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mReverseFlipAnimation);
			recoverUp = 1;
		} else if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight)
				&& deltaY < 0 && recoverUp == 1) {
			mFooterTextView
					.setText(R.string.pull_to_refresh_footer_release_label);
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mDownAnimation);
			recoverUp = 0;
		}
	}

	/**
	 * 修改Header view top margin的�?
	 * 
	 * @description
	 * @param deltaY
	 * @return hylin 2012-7-31下午1:14:31
	 */
	private int changingHeaderViewTopMargin(int deltaY) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		float newTopMargin = params.topMargin + deltaY * 0.5f;

		// 如果下拉，拉的很多的话，下面的上拉就会显示，这里是避免上拉的出现�?
		if (mPullState == PULL_DOWN_STATE && newTopMargin <= -mHeaderViewHeight) {
			return params.topMargin;
		}
		// 同理，相�?
		else if (mPullState == PULL_UP_STATE
				&& newTopMargin >= -mHeaderViewHeight) {
			return params.topMargin;
		}
		params.topMargin = (int) newTopMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
		return params.topMargin;
	}

	/**
	 * header refreshing
	 * 
	 * @description hylin 2012-7-31上午9:10:12
	 */
	private void headerRefreshing() {
		mHeaderState = REFRESHING;
		setHeaderTopMargin(0);
		mHeaderImageView.setVisibility(View.GONE);
		mHeaderImageView.clearAnimation();
		mHeaderImageView.setImageDrawable(null);
		mHeaderProgressBar.setVisibility(View.VISIBLE);
		mHeaderTextView.setText(R.string.pull_to_refresh_refreshing_label);
		if (mOnHeaderRefreshListener != null) {
			mOnHeaderRefreshListener.onHeaderRefresh(this);
		}
	}

	/**
	 * footer refreshing
	 * 
	 * @description hylin 2012-7-31上午9:09:59
	 */
	private void footerRefreshing() {
		mFooterState = REFRESHING;
		int top = mHeaderViewHeight + mFooterViewHeight;
		setHeaderTopMargin(-top);
		mFooterImageView.setVisibility(View.GONE);
		mFooterImageView.clearAnimation();
		mFooterImageView.setImageDrawable(null);
		mFooterProgressBar.setVisibility(View.VISIBLE);
		mFooterTextView
				.setText(R.string.pull_to_refresh_footer_refreshing_label);
		if (mOnFooterRefreshListener != null) {
			mOnFooterRefreshListener.onFooterRefresh(this);
		}
	}

	/**
	 * 设置header view 的topMargin的�?
	 * 
	 * @description
	 * @param topMargin
	 *            ，为0时，说明header view 刚好完全显示出来�?�?mHeaderViewHeight时，说明完全隐藏�?
	 *            hylin 2012-7-31上午11:24:06
	 */
	private void setHeaderTopMargin(int topMargin) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		params.topMargin = topMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
	}

	/**
	 * header view 完成更新后恢复初始状�?
	 * 
	 * @description hylin 2012-7-31上午11:54:23
	 */
	public void onHeaderRefreshComplete() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
		setHeaderTopMargin(-mHeaderViewHeight);
		mHeaderImageView.setVisibility(View.VISIBLE);
		mHeaderImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
		mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
		mHeaderProgressBar.setVisibility(View.GONE);
		mHeaderUpdateTextView.setText("Update on：" + dateFormat.format(now));
		mHeaderState = PULL_TO_REFRESH;
	}

	/**
	 * Resets the list to a normal state after a refresh.
	 * 
	 * @param lastUpdated
	 *            Last updated at.
	 */
	public void onHeaderRefreshComplete(CharSequence lastUpdated) {
		setLastUpdated(lastUpdated);
		onHeaderRefreshComplete();
	}

	/**
	 * footer view 完成更新后恢复初始状�?
	 */
	public void onFooterRefreshComplete() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
		setHeaderTopMargin(-mHeaderViewHeight);
		mFooterImageView.setVisibility(View.VISIBLE);
		mFooterImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow_up);
		mFooterTextView.setText(R.string.pull_to_refresh_footer_pull_label);
		mFooterProgressBar.setVisibility(View.GONE);
		// mFooterUpdateTextView.setText("Update on：" + dateFormat.format(now));
		mFooterState = PULL_TO_REFRESH;
	}

	/**
	 * Set a text to represent when the list was last updated.
	 * 
	 * @param lastUpdated
	 *            Last updated at.
	 */
	public void setLastUpdated(CharSequence lastUpdated) {
		if (lastUpdated != null) {
			mHeaderUpdateTextView.setVisibility(View.VISIBLE);
			mHeaderUpdateTextView.setText(lastUpdated);
		} else {
			mHeaderUpdateTextView.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取当前header view 的topMargin
	 * 
	 * @description
	 * @return hylin 2012-7-31上午11:22:50
	 */
	private int getHeaderTopMargin() {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		return params.topMargin;
	}

	/**
	 * 设置当前是否支持上拉操作
	 */
	public void setCanLoad(boolean canLoad) {
		this.canLoad = canLoad;
	}

	/**
	 * set headerRefreshListener
	 * 
	 * @description
	 * @param headerRefreshListener
	 *            hylin 2012-7-31上午11:43:58
	 */
	public void setOnHeaderRefreshListener(
			OnHeaderRefreshListener headerRefreshListener) {
		mOnHeaderRefreshListener = headerRefreshListener;
	}

	public void setOnFooterRefreshListener(
			OnFooterRefreshListener footerRefreshListener) {
		mOnFooterRefreshListener = footerRefreshListener;
	}

	public void setOnRefreshTouchListener(
			OnRefreshTouchListener onRefreshTouchListener) {
		mOnTouchListener = onRefreshTouchListener;
	}

	/**
	 * Interface definition for a callback to be invoked when list/grid footer
	 * view should be refreshed.
	 */
	public interface OnFooterRefreshListener {
		public void onFooterRefresh(PullToRefreshView view);
	}

	/**
	 * Interface definition for a callback to be invoked when list/grid header
	 * view should be refreshed.
	 */
	public interface OnHeaderRefreshListener {
		public void onHeaderRefresh(PullToRefreshView view);
	}

	public interface OnRefreshTouchListener {
		public void onTouchListener(PullToRefreshView view);
	}
}