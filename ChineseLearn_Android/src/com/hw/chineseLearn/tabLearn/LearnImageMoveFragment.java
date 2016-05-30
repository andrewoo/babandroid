package com.hw.chineseLearn.tabLearn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
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
import com.util.tool.BitmapLoader;
import com.util.tool.HttpHelper;
import com.util.tool.ImageZoom;
import com.util.tool.MediaPlayUtil;
import com.util.tool.MediaPlayerHelper;
import com.util.tool.UiUtil;
import com.util.tool.UtilMedthod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习-拼汉字
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LearnImageMoveFragment extends BaseFragment implements
		OnClickListener, OnTouchListener {
	private static final String ASSETS_LGCHARACTERPART_PATH = "data/character_part/";
	private String TAG = "LearnImageMoveFragment";
	private View contentView;// 主view
	private ThreadWithDialogTask task;
	public static LearnImageMoveFragment fragment;
	Context context;
	private RelativeLayout rel_root;
	private LinearLayout rel_top;
	private TextView tv_word;
	private Button btn_hint;
	private int relTopHeight;
	private int screenWidth, screenHeight;
	int mizigeX;
	int mizigeY;
	int mizigeWidth;
	int mizigeHeight;
	private List<String> picList = new ArrayList<String>();
	private List<String> randomList = new ArrayList<String>();
	// private List<String> partPicNameList = new ArrayList<String>();
	private static final String ASSETS_SOUNDS_PATH = "sounds/";
	private int colorBlue = 0;
	private int colorGrey = 0;
	private int viewMagin;
	private int itemViewWidth;

	ArrayList<Point> pointList = new ArrayList<Point>();

	ArrayList<ImageView> bgViewList = new ArrayList<ImageView>();
	ArrayList<ImageView> moveViewList = new ArrayList<ImageView>();
	ArrayList<ImageView> answerViewList = new ArrayList<ImageView>();

	ArrayList<Integer> orignViewX = new ArrayList<Integer>();
	ArrayList<Integer> orignViewY = new ArrayList<Integer>();
	ImageView mizigeView;
	Drawable bgWhite, bgHint;
	boolean isSetHint = false;
	/**
	 * 所有选项和view的对应map
	 */
	HashMap<ImageView, SubLGModel> subLGModelMap = new HashMap<ImageView, SubLGModel>();
	HashMap<ImageView, SubLGModel> choosedSubLGModelMap = new HashMap<ImageView, SubLGModel>();
	String filePath = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragment = this;
		context = getActivity(); 
		initData();// 得到传过来的参数
		initView();
	}

	private void play() {
		MediaPlayUtil instance = MediaPlayUtil.getInstance();
		//播放完成后 取消帧动画
		instance.setPlayOnCompleteListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mediaPlayer) {
				if(rocketAnimation!=null){
					rocketAnimation.stop();
				}
			}
		});

		instance.play(filePath);
	}

	public void playClickSound() {
		new MediaPlayerHelper("sounds/main_click.mp3").play();
	}

	/**
	 * 
	 */
	private void initView() {

		screenWidth = CustomApplication.app.displayMetrics.widthPixels
				- (UiUtil.dip2px(context, 40));
		screenHeight = CustomApplication.app.displayMetrics.heightPixels;
		viewMagin = UiUtil.dip2px(context, 10);
		itemViewWidth = (screenWidth - viewMagin * 2) / 3;
		colorBlue = context.getResources().getColor(R.color.chinese_skill_blue);
		colorGrey = context.getResources().getColor(R.color.min_grey);
		bgWhite = context.getResources().getDrawable(R.drawable.bg_white1);
		bgHint = UtilMedthod.setBackgroundRounded(context, itemViewWidth,
				itemViewWidth, 10, colorBlue);

		contentView = LayoutInflater.from(context).inflate(
				R.layout.fragment_lesson_image_move, null);
		task = new ThreadWithDialogTask();
		tv_word = (TextView) contentView.findViewById(R.id.tv_word);
		//test
		ll_root = (RelativeLayout)getActivity().findViewById(R.id.ll_root);
//		FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(screenWidth*2, screenHeight);
//		ll_root.setLayoutParams(params);
		tv_word.setText(title);// 拿到title
		rel_root = (RelativeLayout) contentView.findViewById(R.id.rel_root);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(screenWidth*2, screenHeight);
		rel_root.setLayoutParams(params);
		
		rel_root.bringToFront();
		rel_top = (LinearLayout) contentView.findViewById(R.id.rel_top);
		//设置TextTitle的高度
		RelativeLayout.LayoutParams params1=new RelativeLayout.LayoutParams(screenWidth,LayoutParams.WRAP_CONTENT);
		rel_top.setLayoutParams(params1);
		

		iv_dv_view = (ImageView) contentView.findViewById(R.id.iv_dv_view);
		iv_dv_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				File file = new File(filePath);
				if (!file.exists()) {
					// 下载并播放
					HttpHelper.setOnCompleteDownloadListener(new HttpHelper.OnCompleteDownloadListener() {
						@Override
						public void onCompleteDownloadListener(String filePath) {//类中有了 参数貌似无用了
							play();
						}
					});
					HttpHelper.downLoadLessonVoices(voicePath, true);
					doDrawableAnimation();
				} else {
					doDrawableAnimation();
					play();
				}

			}
		});

		btn_hint = (Button) contentView.findViewById(R.id.btn_hint);
		btn_hint.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isSetHint) {
					isSetHint = false;
				} else {
					isSetHint = true;
				}
				setHintBgs(isSetHint);
			}
		});
		initBgViews();
		initMoveViews();
	}

	AnimationDrawable rocketAnimation;
	private void doDrawableAnimation() {

		iv_dv_view.setBackgroundResource(R.drawable.animation_sound);
		rocketAnimation = (AnimationDrawable) iv_dv_view.getBackground();
		rocketAnimation.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		MediaPlayUtil.getInstance().release();
	}

	private void initData() {

		Bundle bundle = getArguments();
		if (bundle != null) {
			if (bundle.containsKey("modelWord")) {
				LGModelWord modelWord = (LGModelWord) bundle
						.getSerializable("modelWord");
				voicePath = modelWord.getVoicePath();
				title = modelWord.getTitle();
				subLGModelList = modelWord.getSubLGModelList();
				answerList = modelWord.getAnswerList();

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

	/**
	 * @param isSetHint
	 */
	private void setHintBgs(boolean isSetHint) {

		if (isSetHint) {
			for (int i = 0; i < answerList.size(); i++) {
				String idStr = answerList.get(i);
				int id = -1;
				try {
					id = Integer.parseInt(idStr);
				} catch (Exception e) {
					// TODO: handle exception
				}

				for (int j = 0; j < bgViewList.size(); j++) {
					ImageView imageView = bgViewList.get(j);
					if (imageView == null) {
						continue;
					}
					int tag = (Integer) imageView.getTag();
					if (id == tag) {
						imageView.setImageDrawable(bgHint);
						break;
					}
				}
			}
			for (int i = 0; i < answerViewList.size(); i++) {
				ImageView iv = answerViewList.get(i);
				if (iv != null) {
					iv.setVisibility(View.VISIBLE);
				}
			}

		} else {// 不设置hint
			for (int i = 0; i < bgViewList.size(); i++) {
				ImageView imageView = bgViewList.get(i);
				if (imageView == null) {
					continue;
				}
				imageView.setImageDrawable(bgWhite);
			}

			for (int i = 0; i < answerViewList.size(); i++) {
				ImageView iv = answerViewList.get(i);
				if (iv != null) {
					iv.setVisibility(View.GONE);
				}
			}

		}

	}

	/**
	 * 
	 */
	private void initBgViews() {
		bgViewList.clear();
		int x = 0;
		int y = UiUtil.dip2px(context, 65);// topView y坐标的初始位置

		for (int i = 0; i < subLGModelList.size(); i++) {
			SubLGModel model = subLGModelList.get(i);
			ImageView imageView = new ImageView(context);
			RelativeLayout.LayoutParams ly = new RelativeLayout.LayoutParams(
					itemViewWidth, itemViewWidth);
			imageView.setLayoutParams(ly);
			imageView.setImageDrawable(bgWhite);
			imageView.setFocusable(false);
			moveViewWithFingerUp(imageView, x, y);
			if (model != null) {
				int wordId = model.getWordId();
				imageView.setTag(wordId);
			} else {
				Log.e(TAG, "initBgViews,subLGModel == null");
			}
			bgViewList.add(imageView);
			rel_root.addView(imageView);//test
//			ll_root.addView(imageView);//背景View暂时不加

			int imageWidth = imageView.getWidth();
			int height = imageView.getHeight();
			Log.d(TAG, "imageWidth:" + imageWidth);
			Log.d(TAG, "height:" + height);

			int x1 = 0, y1 = y;

			if (i == 4) {
				x1 = 0;
				y1 += (itemViewWidth + viewMagin);
			} else {

				if (x + itemViewWidth > screenWidth) {
					x1 = 0;
					y1 += (itemViewWidth + viewMagin);

				} else {
					x1 = x;
					y1 = y;
				}
			}
			// Log.d(TAG, "x:" + x);
			// Log.d(TAG, "y:" + y);
			// Log.d(TAG, "itemViewWidth:" + itemViewWidth);

			Point point = new Point();
			point.x = x1;
			point.y = y1;
			pointList.add(point);

			moveViewWithFingerUp(imageView, x1, y1);
			x = x1;
			x += (itemViewWidth + viewMagin);
			y = y1;
		}

		mizigeView = new ImageView(context);
		mizigeWidth = screenWidth - itemViewWidth - viewMagin;
		mizigeHeight = mizigeWidth;
		RelativeLayout.LayoutParams ly = new RelativeLayout.LayoutParams(
				mizigeWidth, mizigeHeight);

		mizigeView.setLayoutParams(ly);
		mizigeView.setBackground(context.getResources().getDrawable(
				R.drawable.mizige));
		mizigeView.setFocusable(false);
		rel_root.addView(mizigeView);
		int xx = pointList.get(3).x;
		Log.d(TAG, "pointList.get(3).x:" + xx);
		mizigeX = pointList.get(3).x + itemViewWidth + viewMagin;
		mizigeY = pointList.get(3).y;

		moveViewWithFingerUp(mizigeView, mizigeX, mizigeY);
		findAndSetAnswerViews();
	}

	/**
	 * 找到正确答案的view，并添加到正确答案view集合中
	 */
	private void findAndSetAnswerViews() {
		for (int i = 0; i < answerList.size(); i++) {
			String idStr = answerList.get(i);
			int id = -1;
			try {
				id = Integer.parseInt(idStr);
			} catch (Exception e) {
				// TODO: handle exception
			}

			for (int j = 0; j < subLGModelList.size(); j++) {

				SubLGModel model = subLGModelList.get(j);

				if (model == null) {
					continue;
				}
				int wordId = model.getWordId();
				if (id == wordId) {
					ImageView iv = new ImageView(context);// 复制view
					String imageName = model.getImageName();
					Bitmap bitmap = BitmapLoader.getImageFromAssetsFile(ASSETS_LGCHARACTERPART_PATH+ imageName);
					iv.setImageBitmap(UtilMedthod.translateImageColor(bitmap,
							colorGrey));
					RelativeLayout.LayoutParams ly = new RelativeLayout.LayoutParams(
							mizigeWidth, mizigeHeight);
					iv.setLayoutParams(ly);
					iv.setFocusable(false);
					iv.setVisibility(View.GONE);
					rel_root.addView(iv);
					moveViewWithFingerUp(iv, mizigeX, mizigeY);
					answerViewList.add(iv);
					break;
				}
			}
		}
	}

	/**
	 * 
	 */
	private void initMoveViews() {
		moveViewList.clear();
		scale = (float) mizigeWidth / (float) 420;// 缩放比
		scaleView = (float) itemViewWidth / (float) 420;
		int x = 0;
		int y = UiUtil.dip2px(context, 65);// topView y坐标的初始位置

		for (int i = 0; i < subLGModelList.size(); i++) {

			ImageView imageView = new ImageView(context);
			RelativeLayout.LayoutParams ly = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			imageView.setLayoutParams(ly);
			imageView.setTag(i);
			imageView.setFocusable(false);
			imageView.setOnTouchListener(this);
			SubLGModel model = subLGModelList.get(i);
			Bitmap scaleBitmap=null;
			if (model != null) {
				String imageName = model.getImageName();
				String strRect = model.getStrRect();// x.y.宽.高.
				int wordId = model.getWordId();
				Bitmap bitmap = BitmapLoader.getImageFromAssetsFile(ASSETS_LGCHARACTERPART_PATH+ imageName);
				Bitmap cropBitmap = UtilMedthod.getCropBitmap(bitmap);//截取图片为只有像素的矩形
				//计算切割后的图片 放到ImageView上的大小
//				float scaleWidth=(float)itemViewWidth/bitmap.getWidth();
//				float scaleHeight=(float)itemViewWidth/bitmap.getHeight();
				
				scaleBitmap = ImageZoom.scale(cropBitmap, scaleView, scaleView);
				imageView.setImageBitmap(scaleBitmap);
				// imageView.setTag(wordId);

			} else {
				Log.e(TAG, "initMoveViews,subLGModel == null");
			}
			subLGModelMap.put(imageView, model);// 建立对应关系
			moveViewList.add(imageView);
//			ll_root.addView(imageView);
			rel_root.addView(imageView);

			int x1 = 0, y1 = y;

			if (i == 4) {
				x1 = 0;
				y1 += (itemViewWidth + viewMagin);
			} else {

				if (x + itemViewWidth > screenWidth) {
					x1 = 0;
					y1 += (itemViewWidth + viewMagin);

				} else {
					x1 = x;
					y1 = y;
				}
			}
			// Log.d(TAG, "x:" + x);
			// Log.d(TAG, "y:" + y);
			// Log.d(TAG, "itemViewWidth:" + itemViewWidth);
			if(scaleBitmap!=null){
				orignViewX.add(x1+(itemViewWidth/2-scaleBitmap.getWidth()/2));
				orignViewY.add(y1+(itemViewWidth/2-scaleBitmap.getHeight()/2));
				moveViewWithFingerUp(imageView, x1+(itemViewWidth/2-scaleBitmap.getWidth()/2), y1+(itemViewWidth/2-scaleBitmap.getHeight()/2));
			}
			x = x1;
			x += (itemViewWidth + viewMagin);
			y = y1;

		}
		
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
		params.topMargin = (int) rawY - relTopHeight - view.getHeight() / 2;
		
//		params.width = mizigeWidth;
//		params.height = mizigeHeight;
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

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
				.getLayoutParams();
		params.leftMargin = (int) rawX;
		params.topMargin = (int) rawY;
		
		view.setLayoutParams(params);
		view.invalidate();
	}

	/**
	 * view回到原来的位置
	 * 
	 */
	private void moveToOrignPosition(ImageView imageView) {
		
		@SuppressWarnings("unused")
		Bitmap bitmap = ((BitmapDrawable) (imageView.getDrawable())).getBitmap();//拿到view上的bitmap 获得原始宽度
		int tag = (Integer) imageView.getTag();
		int x = 0, y = 0;
		x = orignViewX.get(tag);
		y = orignViewY.get(tag);

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
		params.leftMargin = x;
		params.topMargin = y;
		int width = imageView.getWidth();
		int height = imageView.getHeight();
//		params.width = (int) (itemViewWidth*scale);
//		params.height = (int) (itemViewWidth*scale);//scaleView
		params.width = bitmap.getWidth();
		params.height = bitmap.getHeight();//scaleView
		imageView.setLayoutParams(params);
		imageView.invalidate();

	}

	// Sacle动画 - 渐变尺寸缩放
	private Animation scaleAnimation = null;
	private String title;
	private List<SubLGModel> subLGModelList;
	private String voicePath;
	private ImageView iv_dv_view;
	private List<String> answerList;

	private float scale = 1f;

	int lastX;
	int lastY;
	private RelativeLayout ll_root;
	private float scaleView;

	@Override
	public boolean onTouch(View v, MotionEvent event) {

//		scale = (float) mizigeWidth / (float) 420;// 缩放比
		ImageView imageView = (ImageView) v;
		imageView.bringToFront();
		Bitmap bitmap = ((BitmapDrawable) (imageView.getDrawable())).getBitmap();
		// if (bitmap.getPixel((int) (event.getX()), ((int) event.getY())) == 0)
		// {
		// String aa = "点击了透明区域";
		// Log.i("test", "" + aa);
		// UiUtil.showToast(context, "" + aa);
		//
		// } else {
		// // imageView.dispatchTouchEvent(event);
		// UiUtil.showToast(context, "没有点击透明区域");
		// }
		SubLGModel model = subLGModelMap.get(imageView);

		int rawX = (int) event.getRawX();
		int rawY = (int) event.getRawY();
		
		// rawX = rawX - imageView.getWidth() / 2;
		// rawY = rawY - relTopHeight - imageView.getHeight() / 2;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: 
			Bitmap bitmap1 = ((BitmapDrawable) (imageView.getDrawable())).getBitmap();
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView .getLayoutParams();
			//点击后放大的具体宽高比例
				params.width = (int) (bitmap1.getWidth()/((float)itemViewWidth/mizigeWidth));
				params.height = (int) (bitmap1.getHeight()/((float)itemViewWidth/mizigeWidth));
				imageView.setLayoutParams(params); 
			lastX = rawX; lastY = rawY;

			playClickSound();
			int[] locations = new int[2];
			rel_top.getLocationInWindow(locations);
			// lin_lin在屏幕中的y
			relTopHeight = locations[1];
			Log.d(TAG, "relTopHeight:" + relTopHeight);
			break;
		case MotionEvent.ACTION_MOVE:
			moveViewWithFinger(imageView, event.getRawX(), event.getRawY());// 动态设置view的位置，拖动效果
			Log.e(TAG, "Width:" + imageView.getWidth() + "    Height:"+ imageView.getHeight());

//			 int offsetX = rawX - lastX; int offsetY = rawY - lastY;
//			 
//			 imageView.offsetLeftAndRight(offsetX);
//			 imageView.offsetTopAndBottom(offsetY);
//			 
//			 lastX = rawX; lastY = rawY;

			break;
		case MotionEvent.ACTION_UP:

			float X = event.getRawX();
			float Y = event.getRawY();

			int[] locationss = new int[2];
			imageView.getLocationInWindow(locationss);
			// lin_lin在屏幕中的y
			int x = locationss[0];
			int y = locationss[1];
			Log.d(TAG, "imageView-X:" + x);
			Log.d(TAG, "imageView-Y:" + y);

			String strRect = model.getStrRect();
			if (strRect != null) {
				Log.d(TAG, "strRect:" + strRect);
				String[] strRectList = strRect.split(",");

				if (strRectList.length == 4) {
					String xStr = strRectList[0];
					String yStr = strRectList[1];
					String wStr = strRectList[2];
					String hStr = strRectList[3];
					int l = 0;
					int t = 0;
					int w = 0;
					int h = 0;
					try {
						l = Integer.parseInt(xStr);
						t = Integer.parseInt(yStr);
						w = Integer.parseInt(wStr);
						h = Integer.parseInt(hStr);

					} catch (Exception e) {
						// TODO: handle exception
					}
					// 缩放后的值
					float l1 = scale * l;// left
					float t1 = scale * t;// top
					float r1 = (420 - l - w) * scale;// right
					float b1 = (420 - t - h) * scale;// bottomscaleView
					

					Log.d(TAG, "缩放后的值l1:" + l1);
					Log.d(TAG, "缩放后的值t1:" + t1);
					Log.d(TAG, "缩放后的值r1:" + r1);
					Log.d(TAG, "缩放后的值b1:" + b1);

					Log.d(TAG, "X:" + X);
					Log.d(TAG, "Y:" + Y);
					Log.d(TAG,
							"UiUtil.dip2px(context, 20):"
									+ UiUtil.dip2px(context, 20));

					Log.d(TAG, "mizigeX:" + mizigeX);
					Log.d(TAG, "mizigeY:" + mizigeY);
					Log.d(TAG, "mizigeWidth:" + mizigeWidth);
					//没用数据库中的位置校验
					if ((x  > (mizigeX + UiUtil.dip2px(context, 20)))
							&& (y  > (mizigeY + relTopHeight))
							&& (x + imageView.getWidth()) < (mizigeX + mizigeWidth + UiUtil
									.dip2px(context, 20))
							&& (y + imageView.getHeight()) < (mizigeY
									+ mizigeHeight + relTopHeight)) {
						// 拖到了米字格的区域
						// UiUtil.showToast(context,
						// "拖到了" + "id:" + model.getWordId());
						choosedSubLGModelMap.put(imageView, model);

					} else {
						// UiUtil.showToast(context, "木拖到");
						moveToOrignPosition(imageView);
						if (choosedSubLGModelMap.containsKey(imageView)) {
							choosedSubLGModelMap.remove(imageView);
						}
					}

					checkAnswer();
				}

			}

			break;
		}
		return true;
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

	private void checkAnswer() {
		StringBuffer buffer = new StringBuffer();
		StringBuffer buffer1 = new StringBuffer();
		for (int i = 0; i < answerList.size(); i++) {
			String idStr = answerList.get(i);
			int id = -1;
			try {
				id = Integer.parseInt(idStr);
				buffer = buffer.append("" + id + ";");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		Log.d(TAG, "正确答案Id:" + buffer.toString());
		Log.d(TAG,
				"---------------------------------------------------------------");

		if (choosedSubLGModelMap.size() > 0) {
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

		for (Map.Entry<ImageView, SubLGModel> entry : choosedSubLGModelMap
				.entrySet()) {
			SubLGModel model = entry.getValue();
			if (model == null) {
				continue;
			}
			int mapId = model.getWordId();
			buffer1 = buffer1.append("" + mapId + ";");
		}
		Log.d(TAG, "选择的Id:" + buffer1.toString());
	}

	@Override
	public boolean isRight() {
		int rightIdCount = 0;
		int chooseIdCount = 0;
		for (int i = 0; i < answerList.size(); i++) {
			String idStr = answerList.get(i);
			int id = -1;
			try {
				id = Integer.parseInt(idStr);
				rightIdCount = rightIdCount + id;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		for (Map.Entry<ImageView, SubLGModel> entry : choosedSubLGModelMap
				.entrySet()) {
			SubLGModel model = entry.getValue();
			if (model == null) {
				continue;
			}
			int mapId = model.getWordId();
			chooseIdCount = chooseIdCount + mapId;
		}
		if (rightIdCount == chooseIdCount) {
			return true;
		}

		return false;
	}

}
