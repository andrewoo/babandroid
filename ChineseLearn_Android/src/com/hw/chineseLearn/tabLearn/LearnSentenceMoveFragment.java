package com.hw.chineseLearn.tabLearn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.bean.LGModelWord;
import com.hw.chineseLearn.dao.bean.LGModelWord.SubLGModel;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.util.thread.ThreadWithDialogTask;
import com.util.tool.HttpHelper;
import com.util.tool.MediaPlayUtil;
import com.util.tool.UiUtil;
import com.util.tool.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 拼句子
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LearnSentenceMoveFragment extends BaseFragment implements
		OnTouchListener {
	private View contentView;// 主view

	private ThreadWithDialogTask task;
	public static LearnSentenceMoveFragment fragment;
	Context context;

	private RelativeLayout rlRoot;
	private int linLineHeight;
	private LinearLayout lin_play_and_text;
	private LinearLayout lin_line;
	private TextView txt_name;

	private String TAG = "LearnSentenceMoveFragment";
	int screenWidth, screenHeight;

	ArrayList<TextView> topViewList = new ArrayList<TextView>();
	ArrayList<TextView> bottomViewList = new ArrayList<TextView>();
	ArrayList<Integer> orignViewX = new ArrayList<Integer>();
	ArrayList<Integer> orignViewY = new ArrayList<Integer>();
	private static final String ASSETS_SOUNDS_PATH = "sounds/";

	// 初始化bottomView的初始位置
	private int x = 0;
	private int y = 0;
	int rows = 1;

	// 初始化bottomViewGrey的初始位置
	private int xb = 0;
	private int yb = 0;

	boolean outOfTopArea = false;
	boolean dragState = false;
	int currentDragTag = -1;

	// 手指按下时记录的左边值
	float downX, downY;
	float upX, upY;
	String filePath = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragment = this;
		context = getActivity();
		
		viewLeftRightPadding=UiUtil.dip2px(context, 8);
		viewTopBottomPadding=UiUtil.dip2px(context, 10);
		
		initData();
		play();
		x = 0;
		y = UiUtil.dip2px(context, 50 * 3 + 1 * 2 + 50);
		screenWidth = CustomApplication.app.displayMetrics.widthPixels - (UiUtil.dip2px(context, 40));
		screenHeight = CustomApplication.app.displayMetrics.heightPixels;

		initView();
		initBottomMoveViews();
		initBottomGreyViews();
	}

	private void initView() {
		contentView = LayoutInflater.from(context).inflate(
				R.layout.fragment_lesson_sentence_move, null);
		FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(screenWidth*2, screenHeight);
		contentView.setLayoutParams(params);
		task = new ThreadWithDialogTask();
		rlRoot = (RelativeLayout) contentView.findViewById(R.id.rl_root);
		lin_play_and_text = (LinearLayout) contentView.findViewById(R.id.lin_play_and_text);
		// lin_play_and_text.setVisibility(View.GONE);
		lin_line = (LinearLayout) contentView.findViewById(R.id.lin_line);
		
		btn_play_normal = (Button) contentView.findViewById(R.id.btn_play_normal);

		btn_play_normal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


					File file = new File(filePath);
					if (!file.exists()) {
						// 下载并播放
						//下载完成后在这里播放 不是工具类里
						HttpHelper.setOnCompleteDownloadListener(new HttpHelper.OnCompleteDownloadListener() {
							@Override
							public void onCompleteDownloadListener(String filePath) {//类中有了 参数貌似无用了
								play();
							}
						});
						HttpHelper.downLoadLessonVoices(voicePath, true);
					} else {
						play();
					}
			}
		});

		btn_play_slow = (Button)contentView.findViewById(R.id.btn_play_slow);
		btn_play_slow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				play();
			}
		});
		txt_name = (TextView) contentView.findViewById(R.id.txt_name);
		int textVieWidth=Utility.getScreenWidth(context)-UiUtil.px2dip(context, 90);
		LinearLayout.LayoutParams paramsTextView=new LinearLayout.LayoutParams(textVieWidth,LayoutParams.WRAP_CONTENT);//设置textview宽度 防止超出屏幕
		txt_name.setLayoutParams(paramsTextView);
		
		txt_name.setText(title);
		
	}

	AnimationDrawable rocketAnimation;

	private void play() {
		final MediaPlayUtil instance = MediaPlayUtil.getInstance();

		instance.setReset();//reset触发载入完成的监听
		instance.setOnPrepareCompleteListener(new MediaPlayUtil.OnPrepareCompleteListener() {
			@Override
			public void doAnimation() {
				btn_play_normal.setBackgroundResource(R.drawable.animation_sound);
				rocketAnimation = (AnimationDrawable) btn_play_normal.getBackground();
				rocketAnimation.setOneShot(false);
				rocketAnimation.stop();
				rocketAnimation.start();
			}
		});

		instance.setPlayOnCompleteListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mediaPlayer) {
				rocketAnimation.setOneShot(true);
			}
		});

		instance.play(filePath);
	}

	String slowVoicePath="";
	private void initData() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			if (bundle.containsKey("modelWord")) {
				LGModelWord modelWord = (LGModelWord) bundle.getSerializable("modelWord");
				title = modelWord.getTitle();// 得到title
				subLGModelList = modelWord.getSubLGModelList();
				answerList = modelWord.getAnswerList();
				voicePath = modelWord.getVoicePath();
				slowVoicePath = modelWord.getSlowVoicePath();

				filePath = DatabaseHelperMy.LESSON_SOUND_PATH + "/" + voicePath;
				Log.d("filePath", "filePath:" + filePath);
				File file = new File(filePath);
				if (!file.exists()) {
					// 下载并播放
					HttpHelper.downLoadLessonVoices(voicePath, true);
				} else {
					play();
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return contentView;
	}

	private void initBottomGreyViews() {
		xb = 0;
		yb = UiUtil.dip2px(context, 152 + 50);
		for (int i = 0; i < bottomViewList.size(); i++) {
			TextView textView = new TextView(context);
			TextView textViewB = bottomViewList.get(i);
			LinearLayout.LayoutParams ly = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			textView.setLayoutParams(ly);
			textView.setPadding(viewLeftRightPadding, (int)(viewTopBottomPadding*0.2),
					viewLeftRightPadding, (int)(viewTopBottomPadding*0.2));
			textView.setText("" + textViewB.getText());

			textView.setTextColor(context.getResources().getColor(
					R.color.white));
			textView.setTextSize(UiUtil.sp2px(context,6));
			textView.setTag(i);
			textView.setGravity(Gravity.CENTER);
			textView.setBackground(context.getResources().getDrawable(
					R.drawable.bg_grey));
			rlRoot.addView(textView);
			textView.setEnabled(false);
			textViewB.bringToFront();

			int viewTextWidth = (int) Layout.getDesiredWidth(textView.getText()
					.toString(), textView.getPaint());

			int x1 = 0, y1 = yb;

			if (xb + viewTextWidth
					+ UiUtil.dip2px(context, viewLeftRightPadding) > screenWidth) {
				rows++;
				x1 = 0;
				y1 += UiUtil.dip2px(context, 30 + 5);

			} else {
				x1 = xb;
				y1 = yb;
			}
			moveViewWithFingerUp(textView, x1, y1);
			xb = x1;
			xb += (viewTextWidth + UiUtil.dip2px(context, viewLeftRightPadding+10));
			yb = y1;

		}
	}

	int viewLeftRightPadding =30; 
	int viewTopBottomPadding = 20;

	private String[] textSplits;

	private String title;

	private List<SubLGModel> subLGModelList;

	private LGModelWord modelWord;

	private List<String> answerList;

	private String voicePath;

	private Button btn_play_normal,btn_play_slow;

	private void initBottomMoveViews() {

		for (int i = 0; i < subLGModelList.size(); i++) {

			final TextView textView = new TextView(context);
			LinearLayout.LayoutParams ly = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			textView.setLayoutParams(ly);
			textView.setPadding(viewLeftRightPadding, (int)(viewTopBottomPadding*0.2),
					viewLeftRightPadding, (int)(viewTopBottomPadding*0.2));
			String word = subLGModelList.get(i).getOption();
			textView.setText("" + word);
			textView.setBackground(context.getResources().getDrawable(
					R.drawable.bg_white_to_blue));
			textView.setTextColor(context.getResources().getColor(
					R.color.deep_grey));
			textView.setTextSize(UiUtil.sp2px(context,6));
			textView.setTag(i);
			textView.setGravity(Gravity.CENTER);
			rlRoot.addView(textView);
			bottomViewList.add(textView);
			rlRoot.invalidate();

			textView.setOnTouchListener(this);

			int viewTextWidth = (int) Layout.getDesiredWidth(textView.getText().toString(), textView.getPaint());

			int x1 = 0, y1 = y;

			if (x + viewTextWidth
					+ UiUtil.dip2px(context, viewLeftRightPadding) > screenWidth) {
				rows++;
				x1 = 0;
				y1 += UiUtil.dip2px(context, 30 + 5);

			} else {
				x1 = x;
				y1 = y;
			}
			// Log.d(TAG, "x:" + x);
			// Log.d(TAG, "y:" + y);
			// Log.d(TAG, "rows:" + rows);
			// Log.d(TAG, "viewTextWidth:" + viewTextWidth);

			orignViewX.add(x1);
			orignViewY.add(y1);
			moveViewWithFingerUp(textView, x1, y1);
			x = x1;
			x += (viewTextWidth + UiUtil.dip2px(context, viewLeftRightPadding+10));
			y = y1;

		}
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		currentDragTag = (Integer) v.getTag();
		TextView textView = (TextView) v;
		textView.bringToFront();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			textView.setBackground(context.getResources().getDrawable(
					R.drawable.bg_blue));
			textView.setTextColor(context.getResources()
					.getColor(R.color.white));

			int[] locations = new int[2];
			lin_line.getLocationInWindow(locations);
			// lin_lin在屏幕中的y
			linLineHeight = locations[1];
			Log.d(TAG, "linLineHeight:" + linLineHeight);
			downX = event.getRawX();
			downY = event.getRawY();

			break;
		case MotionEvent.ACTION_MOVE:

			upX = event.getRawX();
			upY = event.getRawY();

			if (Math.abs(downX - upX) < 1 || Math.abs(downY - upY) < 1) {
				dragState = false;
				return false;
			}

			if (Math.abs(downX - upX) > 10 || Math.abs(downY - upY) > 10) { // 真正的onTouch事件
				dragState = true;
				moveViewWithFinger(v, event.getRawX(), event.getRawY());// 动态设置view的位置，拖动效果
				checkTouchArea(currentDragTag, v, event);
			}

			break;
		case MotionEvent.ACTION_UP:
			dragState = false;
			upX = event.getRawX();
			upY = event.getRawY();
			textView.setBackground(context.getResources().getDrawable(
					R.drawable.bg_white1));
			textView.setTextColor(context.getResources().getColor(
					R.color.deep_grey));

			if (Math.abs(downX - upX) <= 1 || Math.abs(downY - upY) <= 1) {

				// Toast.makeText(context, "当作click事件来处理", 500).show();

				if (isTopContainaView(currentDragTag)) {
					for (int i = 0; i < topViewList.size(); i++) {
						int listTag = (Integer) topViewList.get(i).getTag();
						if (listTag == currentDragTag) {
							topViewList.remove(i);
							bottomViewList.add(textView);
							moveToOrignPosition(textView);
							break;
						}
					}
				} else {

					for (int j = 0; j < bottomViewList.size(); j++) {
						int bottomlistTag = (Integer) bottomViewList.get(j)
								.getTag();
						if (bottomlistTag != currentDragTag) {
							continue;
						}
						bottomViewList.remove(j);
						topViewList.add(textView);
						rlRoot.invalidate();
						break;
					}
				}
				currentDragTag = -1;
				refeshTopView();
				return false;// 距离较小，当作click事件来处理
			}

			if ((Math.abs(downX - upX) > 1 && Math.abs(downX - upX) < 10)
					|| (Math.abs(downY - upY) > 1 && Math.abs(downY - upY) < 10)) {
				moveToOrignPosition(textView);//
			}

			if (Math.abs(downX - upX) >= 10 || Math.abs(downY - upY) >= 10) { // 真正的onTouch事件
				// Toast.makeText(context, "真正的onTouch事件", 500).show();
				// 手指移开时 执行刷新操作
				if (outOfTopArea) {
					moveToOrignPosition(textView);//
				} else {

					int index = findPositionByMoveView(event.getRawX(),
							event.getRawY());

					if (index == -1) {
						// 移动的位置没有找到控件,添加到topview中
						for (int i = 0; i < bottomViewList.size(); i++) {
							int listTag = (Integer) bottomViewList.get(i)
									.getTag();
							if (listTag != currentDragTag) {
								continue;
							}
							bottomViewList.remove(i);
							topViewList.add(textView);
							break;
						}

					}
				}
			}

			currentDragTag = -1;
			refeshTopView();
			break;
		}
		return true;
	}

	/**
	 * @param thisTag
	 * @param v
	 * @param event
	 */
	private void checkTouchArea(int thisTag, View v, MotionEvent event) {
		int ylin = linLineHeight + UiUtil.dip2px(context, 50 * 3 + 1 * 2);//

		// Log.d(TAG, "event.getRawY():" + event.getRawY());
		// Log.d(TAG, "dip2px(context, 152):" + dip2px(context, 152));
		if (event.getRawY() > (ylin)) {// 拖出指定区域
			outOfTopArea = true;

			for (int i = 0; i < topViewList.size(); i++) {
				int listTag = (Integer) topViewList.get(i).getTag();
				if (listTag != thisTag) {
					continue;
				}
				topViewList.remove(i);
				bottomViewList.add((TextView) v);
				// Log.d(TAG, "listTag:" + listTag);
				break;
			}

		} else {// 拖到或者未拖出指定区域
			outOfTopArea = false;

			int index = findPositionByMoveView(event.getRawX(), event.getRawY());

			if (index != -1) {// 移动的位置找到了控件

				if (isTopContainaView(thisTag)) {// 拖动的控件是否在topViewList中
					String aa = "拖动的是上面的view,根据index交换view的位置";
					Log.d(TAG, aa);

					for (int i = 0; i < topViewList.size(); i++) {
						int listTag = (Integer) topViewList.get(i).getTag();
						if (listTag != thisTag) {
							continue;
						}
						currentDragTag = listTag;
						TextView textView = topViewList.get(i);
						if (index < i) {
							topViewList.remove(i);
							topViewList.add(index, textView);
						} else if (index > i) {
							topViewList.remove(i);
							topViewList.add(index, textView);
							// topViewList.set(index, textView);
							// topViewList.set(index - 1, (TextView) v);

						}
						break;
					}

				} else {
					String a = "从下面拖入的view,直接插入到拖动的位置";
					Log.d(TAG, a);
					for (int i = 0; i < bottomViewList.size(); i++) {
						int listTag = (Integer) bottomViewList.get(i).getTag();
						if (listTag != thisTag) {
							continue;
						}
						currentDragTag = listTag;
						bottomViewList.remove(i);
						topViewList.add(index, (TextView) v);
						break;
					}
				}
				refeshTopView();
			}

		}
	}

	/**
	 * 拖动的控件是否在topViewList中
	 * 
	 * @param thisTag
	 * @return
	 */
	private boolean isTopContainaView(int thisTag) {

		boolean isContainsView = false;
		for (int i = 0; i < topViewList.size(); i++) {
			int listTag = (Integer) topViewList.get(i).getTag();
			if (listTag != thisTag) {
				continue;
			}
			isContainsView = true;
		}
		return isContainsView;
	}

	/**
	 * @param rawX
	 * @param rawY
	 * @return
	 */
	private int findPositionByMoveView(float rawX, float rawY) {
		int position = -1;

		for (int i = 0; i < topViewList.size(); i++) {
			TextView textView = topViewList.get(i);
			int listTag = (Integer) textView.getTag();
			int leftX = textView.getLeft();
			int viewWidth = textView.getWidth();

			// Log.e(TAG, "rawX:" + (int) rawX);
			// Log.d(TAG, "leftX:" + leftX);
			// Log.d(TAG, "viewWidth:" + viewWidth);

			int topY = textView.getTop();
			int viewHeight = textView.getHeight();

			// Log.e(TAG, "rawY:" + (int) rawY);
			// Log.d(TAG, "linLineHeight:" + linLineHeight);
			// Log.d(TAG, "topY:" + topY);
			// Log.d(TAG, "topY+linLineHeight:" + (topY + linLineHeight));
			// Log.d(TAG, "viewHeight:" + viewHeight);

			// 判断手指touch的x,y坐标点上有没有控件，如果有，返回这个控件在集合中的index
			if ((int) rawX > leftX
					&& (int) rawX < (leftX + viewWidth)
					&& (int) rawY > (topY + linLineHeight)
					&& (int) rawY < (topY + linLineHeight + viewHeight + UiUtil
							.dip2px(context, 20))) {

				if (currentDragTag != listTag) {
					position = i;
				}

			}

		}
		// Log.d(TAG, "position:" + position);
		return position;
	}

	/**
	 * view回到原来的位置
	 * 
	 * @param textView
	 */
	private void moveToOrignPosition(TextView textView) {

		int tag = (Integer) textView.getTag();
		int x = 0, y = 0;
		x = orignViewX.get(tag);
		y = orignViewY.get(tag);
		moveViewWithFingerUp(textView, x, y);
	}

	/**
	 * 刷新topView
	 */
	private void refeshTopView() {
		int x = 0;
		int y = UiUtil.dip2px(context, 13);// topView y坐标的初始位置
		Log.d(TAG, "topViewList.size()：" + topViewList.size());
		for (int i = 0; i < topViewList.size(); i++) {
			TextView textView = topViewList.get(i);
			int tag = (Integer) textView.getTag();
			// textView字符串的宽度
			int viewTextWidth = (int) Layout.getDesiredWidth(textView.getText()
					.toString(), textView.getPaint());

			int x1 = 0, y1 = y;

			if (x + viewTextWidth
					+ UiUtil.dip2px(context, viewLeftRightPadding) > screenWidth) {
				rows++;
				x1 = 0;
				y1 += UiUtil.dip2px(context, 50);// y坐标增量为50dp

			} else {
				x1 = x;
				y1 = y;
			}
			// Log.d(TAG, "x:" + x);
			// Log.d(TAG, "y:" + y);
			// Log.d(TAG, "rows:" + rows);
			// Log.d(TAG, "viewTextWidth:" + viewTextWidth);

			if (currentDragTag != tag) {
				moveViewWithFingerUp(textView, x1, y1);
			} else {
				currentDragTag = -1;
			}
			x = x1;
			x += (viewTextWidth + UiUtil.dip2px(context, viewLeftRightPadding+10));
			y = y1;
		}
		getTopViewStrings();
	}

	/**
	 * 获取topView中的字符串
	 * 
	 * @return
	 */
	private String getTopViewStrings() {
		String answerString = "";
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < topViewList.size(); i++) {

			TextView textView = topViewList.get(i);
			textView.setBackground(context.getResources().getDrawable(
					R.drawable.bg_blue));
			textView.setTextColor(context.getResources()
					.getColor(R.color.white));
			String textViewText = textView.getText().toString();
			buffer = buffer.append(textViewText + " ");
		}
		answerString = buffer.toString();
		buffer = null;
		Log.d(TAG, answerString);
		if (!"".equals(answerString)) {
			if (getActivity() instanceof LessonExerciseActivity) {
				LessonExerciseActivity lessonExerciseActivity = (LessonExerciseActivity) getActivity();
				lessonExerciseActivity.isCheckBtnActived(true);
			}
			if (getActivity() instanceof LessonReviewExerciseActivity) {
				LessonReviewExerciseActivity lessonReviewExerciseActivity = (LessonReviewExerciseActivity) getActivity();
				lessonReviewExerciseActivity.isCheckBtnActived(true);
			}
			if (getActivity() instanceof LessonTestOutTestActivity) {
				LessonTestOutTestActivity lessonTestOutTestActivity = (LessonTestOutTestActivity) getActivity();
				lessonTestOutTestActivity.isCheckBtnActived(true);
			}

		} else {
			if (getActivity() instanceof LessonExerciseActivity) {
				LessonExerciseActivity lessonExerciseActivity = (LessonExerciseActivity) getActivity();
				lessonExerciseActivity.isCheckBtnActived(false);
			}
			if (getActivity() instanceof LessonReviewExerciseActivity) {
				LessonReviewExerciseActivity lessonReviewExerciseActivity = (LessonReviewExerciseActivity) getActivity();
				lessonReviewExerciseActivity.isCheckBtnActived(false);
			}
			if (getActivity() instanceof LessonTestOutTestActivity) {
				LessonTestOutTestActivity lessonTestOutTestActivity = (LessonTestOutTestActivity) getActivity();
				lessonTestOutTestActivity.isCheckBtnActived(false);
			}
		}
		return answerString;
	}

	/**
	 * 设置View的布局属性，使得view随着手指移动 注意：view所在的布局必须使用RelativeLayout 而且不得设置居中等样式
	 * 
	 * @param view
	 * @param rawX
	 * @param rawY
	 */
	private void moveViewWithFinger(View view, float rawX, float rawY) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
				.getLayoutParams();
		params.leftMargin = (int) rawX - view.getWidth() / 2;
		params.topMargin = (int) rawY - linLineHeight - view.getHeight() / 2;
		view.setLayoutParams(params);
	}

	/**
	 * 手指移开时，确定view 的位置
	 * 
	 * @param view
	 * @param rawX
	 * @param rawY
	 */
	private void moveViewWithFingerUp(View view, float rawX, float rawY) {

		// if (!dragState) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
				.getLayoutParams();
		params.leftMargin = (int) rawX;
		params.topMargin = (int) rawY;
		view.setLayoutParams(params);
		view.invalidate();
		// }

	}

	/** 获取移动动画 */
	public Animation getMoveAnimation(float toXValue, float toYValue) {
		TranslateAnimation mTranslateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF,
				toXValue, Animation.RELATIVE_TO_SELF, 0.0F,
				Animation.RELATIVE_TO_SELF, toYValue);// 当前位置移动到指定位置
		mTranslateAnimation.setFillAfter(true);// 设置一个动画效果执行完毕后，View对象保留在终止的位置。
		mTranslateAnimation.setDuration(300L);
		return mTranslateAnimation;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MediaPlayUtil.getInstance().release();
		MediaPlayUtil.getInstance().stop();

	}

	/**
	 * 判断当前题是否正确
	 */
	@Override
	public boolean isRight() {

		String topViewStrings = getTopViewStrings();
		String stringFilter = UiUtil.StringFilter(topViewStrings);

		for (int i = 0; i < answerList.size(); i++) {
			if (answerList.get(i).equals(stringFilter)) {
				return true;
			}
		}
		return false;
	}
}
