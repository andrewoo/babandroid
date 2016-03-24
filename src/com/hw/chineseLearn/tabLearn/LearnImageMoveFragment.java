package com.hw.chineseLearn.tabLearn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.LGCharacter;
import com.hw.chineseLearn.dao.bean.LGCharacterPart;
import com.hw.chineseLearn.dao.bean.LessonRepeatRegex;
import com.util.thread.ThreadWithDialogTask;
import com.util.tool.BitmapLoader;
import com.util.tool.UiUtil;

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
	private RelativeLayout rel_top;
	private TextView tv_word;
	private int relTopHeight;
	private int screenWidth, screenHeight;
	int mizigeX;
	int mizigeY;
	private List<String> picList = new ArrayList<String>();
	private List<String> randomList = new ArrayList<String>();
	private List<String> partPicNameList = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fragment = this;
		context = getActivity();
		initData();// 得到传过来的参数
		screenWidth = CustomApplication.app.displayMetrics.widthPixels
				- (dip2px(context, 40));
		screenHeight = CustomApplication.app.displayMetrics.heightPixels;
		viewMagin = dip2px(context, 10);
		itemViewWidth = (screenWidth - viewMagin * 2) / 3;

		initView();

		initBgViews();
		initMoveViews();
	}

	private void initView() {
		contentView = LayoutInflater.from(context).inflate(
				R.layout.fragment_lesson_image_move, null);
		task = new ThreadWithDialogTask();
		tv_word=(TextView) contentView.findViewById(R.id.tv_word);
		tv_word.setText(title);
		rel_root = (RelativeLayout) contentView.findViewById(R.id.rel_root);
		rel_top = (RelativeLayout) contentView.findViewById(R.id.rel_top);
	}

	private void initData() {

		try {
			LessonRepeatRegex lessonRepeatRegex = (LessonRepeatRegex) getArguments()
					.getSerializable("lessonRepeatRegex");
			List<String> characPicList = getCharacPicList(lessonRepeatRegex);// 得到需要显示的5张图片集合
			for (int i = 0; i < characPicList.size(); i++) {
				LGCharacterPart lgCharacterPart = (LGCharacterPart) MyDao
						.getDao(LGCharacterPart.class).queryForId(
								Integer.valueOf(characPicList.get(i)));
				String picName = lgCharacterPart.getPartName();
				partPicNameList.add(picName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return contentView;
	}

	private int viewMagin;
	private int itemViewWidth;

	ArrayList<Point> pointList = new ArrayList<Point>();
	ArrayList<ImageView> bgViewList = new ArrayList<ImageView>();

	ArrayList<ImageView> moveViewList = new ArrayList<ImageView>();
	ArrayList<Integer> orignViewX = new ArrayList<Integer>();
	ArrayList<Integer> orignViewY = new ArrayList<Integer>();
	ImageView mizigeView;

	private void initBgViews() {

		int x = 0;
		int y = dip2px(context, 65);// topView y坐标的初始位置

		for (int i = 0; i < 5; i++) {

			ImageView imageView = new ImageView(context);
			RelativeLayout.LayoutParams ly = new RelativeLayout.LayoutParams(
					itemViewWidth, itemViewWidth);
			imageView.setLayoutParams(ly);
			imageView.setBackground(context.getResources().getDrawable(
					R.drawable.bg_white1));
			imageView.setFocusable(false);
			moveViewWithFingerUp(imageView, x, y);
			bgViewList.add(imageView);
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
			Log.d(TAG, "x:" + x);
			Log.d(TAG, "y:" + y);
			Log.d(TAG, "itemViewWidth:" + itemViewWidth);

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
		RelativeLayout.LayoutParams ly = new RelativeLayout.LayoutParams(
				screenWidth - itemViewWidth - viewMagin, screenWidth
						- itemViewWidth - viewMagin);
		mizigeView.setLayoutParams(ly);
		mizigeView.setBackground(context.getResources().getDrawable(
				R.drawable.mizige));
		mizigeView.setFocusable(false);
		rel_root.addView(mizigeView);

		mizigeX = pointList.get(3).x + itemViewWidth + viewMagin;
		mizigeY = pointList.get(3).y;
		moveViewWithFingerUp(mizigeView, mizigeX, mizigeY);
	}

	private void initMoveViews() {

		int x = 0;
		int y = dip2px(context, 65);// topView y坐标的初始位置

		for (int i = 0; i < 5; i++) {

			ImageView imageView = new ImageView(context);
			RelativeLayout.LayoutParams ly = new RelativeLayout.LayoutParams(
					itemViewWidth, itemViewWidth);
			imageView.setLayoutParams(ly);
			// imageView.setBackground(context.getResources().getDrawable(
			// R.drawable.bg_white1));
			imageView.setTag(i);
			imageView.setFocusable(false);
			imageView.setOnTouchListener(this);
			// imageView.setImageResource(context.getResources().getIdentifier(
			// "pp_" + (19 + i), "drawable", context.getPackageName()));
			String imageName = partPicNameList.get(i);
			Bitmap bitmap = BitmapLoader
					.getImageFromAssetsFile(ASSETS_LGCHARACTERPART_PATH
							+ imageName);
			imageView.setImageBitmap(bitmap);
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
			Log.d(TAG, "x:" + x);
			Log.d(TAG, "y:" + y);
			Log.d(TAG, "itemViewWidth:" + itemViewWidth);

			orignViewX.add(x1);
			orignViewY.add(y1);
			moveViewWithFingerUp(imageView, x1, y1);
			x = x1;
			x += (itemViewWidth + viewMagin);
			y = y1;

		}
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
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
		params.width = itemViewWidth * 2;
		params.height = itemViewWidth * 2;
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
	 * @param textView
	 */
	private void moveToOrignPosition(ImageView imageView) {

		int tag = (Integer) imageView.getTag();
		int x = 0, y = 0;
		x = orignViewX.get(tag);
		y = orignViewY.get(tag);
		// moveViewWithFingerUp(imageView, x, y);

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView
				.getLayoutParams();
		params.leftMargin = x;
		params.topMargin = y;
		params.width = itemViewWidth;
		params.height = itemViewWidth;
		imageView.setLayoutParams(params);
		imageView.invalidate();

	}

	// Sacle动画 - 渐变尺寸缩放
	private Animation scaleAnimation = null;
	private String title;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView imageView = (ImageView) v;
		imageView.bringToFront();
		Bitmap bitmap = ((BitmapDrawable) (imageView.getDrawable()))
				.getBitmap();

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

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			int[] locations = new int[2];
			rel_top.getLocationInWindow(locations);
			// lin_lin在屏幕中的y
			relTopHeight = locations[1];
			Log.d(TAG, "relTopHeight:" + relTopHeight);
			break;
		case MotionEvent.ACTION_MOVE:
			moveViewWithFinger(imageView, event.getRawX(), event.getRawY());// 动态设置view的位置，拖动效果

			break;
		case MotionEvent.ACTION_UP:

			float X = event.getRawX();
			float Y = event.getRawY();

			Log.d(TAG, "X：" + X);
			Log.d(TAG, "mizigeX：" + mizigeX);
			Log.d(TAG, "mizigeView.getWidth()：" + mizigeView.getWidth());

			Log.d(TAG, "Y：" + Y);
			Log.d(TAG, "mizigeY：" + mizigeY);
			Log.d(TAG, "mizigeView.getHeight()：" + mizigeView.getHeight());

			if ((X > (mizigeX + imageView.getWidth() / 2) && X < (mizigeX + mizigeView
					.getWidth()))
					&& (Y > (mizigeY + imageView.getHeight() / 2))
					&& Y < (mizigeY + mizigeView.getHeight())) {
				// 拖到了米字格的区域
				UiUtil.showToast(context, "拖到了");
			} else {
				UiUtil.showToast(context, "木拖到");
				moveToOrignPosition(imageView);
			}

			break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

	/**
	 * 随机取出需要的5张部首图片
	 * 
	 * @param lessonRepeatRegex
	 * @return
	 * @throws SQLException
	 */
	private List<String> getCharacPicList(LessonRepeatRegex lessonRepeatRegex)
			throws SQLException {

		int lgTableId = lessonRepeatRegex.getLgTableId();
		LGCharacter lGCharacterPart = (LGCharacter) MyDao.getDao(
				LGCharacter.class).queryForId(lgTableId);
		title = getTitle(lGCharacterPart);
		String[] picArray = lGCharacterPart.getPartOptions().split(";");
		String[] answerPicArray = lGCharacterPart.getPartAnswer().split(";");
		for (int i = 0; i < picArray.length; i++) {
			for (int j = 0; j < answerPicArray.length; j++) {
				System.out.println("picArray[i]" + picArray[i]);
				System.out.println("answerPicArray[j]" + answerPicArray[j]);
				if (Integer.valueOf(picArray[i].trim()) == Integer
						.valueOf(answerPicArray[j].trim())) {
					picList.add(picArray[i]);
				}
			}
			if (randomList.indexOf(picArray[i]) == -1
					&& picList.indexOf(picArray[i]) == -1) {
				randomList.add(picArray[i]);
			}
		}
		System.out.println("picList" + picList);
		System.out.println("randomList" + randomList);
		if (picList.size() < 5) {
			Collections.shuffle(randomList);
			int x = 5 - picList.size();
			for (int i = 0; i < x; i++) {
				picList.add(randomList.get(i));
			}
		}
		System.out.println("picList2222" + picList);
		return picList;
	}

	@Override
	public boolean isRight() {
		// TODO Auto-generated method stub
		return false;
	}

	private String getTitle(LGCharacter lGCharacterPart) {
		String pinyin = lGCharacterPart.getPinyin();
		String replace = lGCharacterPart.getTranslation().replace(";", "/");
		return "["+pinyin+"]"+replace;
	}
}
