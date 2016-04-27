package com.hw.chineseLearn.tabDiscover;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.CharPart;
import com.hw.chineseLearn.dao.bean.Character;
import com.hw.chineseLearn.dao.bean.TbMyFluentNow;
import com.util.svgandroid.HwAnim;
import com.util.svgandroid.HwWriting;
import com.util.svgandroid.SVGParser;
import com.util.tool.PathView;
import com.util.tool.UiUtil;

/**
 * 笔画练习
 * 
 * @author yh
 */
public class StrokesOrderExerciseActivity extends BaseActivity {

	private String TAG = "==StrokesOrderExerciseActivity==";
	private Context context;
	private String title = "";
	private Character model;
	View contentView;
	List<Path> charPartList = new ArrayList<Path>();
	List<Path> directionList = new ArrayList<Path>();

	com.util.tool.PathView pv;
	private LinearLayout root_view;
	private Button btn_play, btn_write, btn_visible;

	LinearLayout lin_mizige;
	int screenWidth, screenHeight;
	boolean isStartHwViewAnim = true;
	boolean isStartHwViewWrite = false;
	String charId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_strokes_order_exercise, null);
		setContentView(contentView);
		CustomApplication.app.addActivity(this);
		context = this;

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("model")) {
				model = (Character) bundle.getSerializable("model");
			}
		}

		if (model != null) {
			title = model.getCEE();
			charId = model.getCharId();
		}
		Log.d(TAG, "title:" + title);
		Log.d(TAG, "charId:" + charId);
		screenWidth = CustomApplication.app.displayMetrics.widthPixels;
		screenHeight = CustomApplication.app.displayMetrics.heightPixels;
		init();
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {
		root_view = (LinearLayout) contentView.findViewById(R.id.root_view);
		lin_mizige = (LinearLayout) contentView.findViewById(R.id.lin_mizige);

		btn_play = (Button) contentView.findViewById(R.id.btn_play);
		btn_play.setOnClickListener(onClickListener);

		btn_write = (Button) contentView.findViewById(R.id.btn_write);
		btn_write.setOnClickListener(onClickListener);

		btn_visible = (Button) contentView.findViewById(R.id.btn_visible);
		btn_visible.setOnClickListener(onClickListener);
		int mizigeWidth = screenWidth - (UiUtil.dip2px(context, 60));
		LayoutParams ly = lin_mizige.getLayoutParams();
		ly.width = mizigeWidth;
		ly.height = mizigeWidth;
		lin_mizige.setLayoutParams(ly);
		pv = (PathView) contentView.findViewById(R.id.pathView_bg);
		pv.setLayoutParams(ly);

		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				title, View.GONE, View.GONE, 0);
		List<CharPart> CharPartList = new ArrayList<CharPart>();
		Character character = new Character();
		try {
			CharPartList = (ArrayList<CharPart>) MyDao.getDao(CharPart.class)
					.queryBuilder().where().eq("CharId", charId).query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(TAG, "CharPartList.size()" + CharPartList.size());

		try {
			character = (Character) MyDao.getDao(Character.class).queryForId(
					charId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String charPathStr = "";
		if (character != null) {
			charPathStr = character.getCharPath();
		} else {
			Log.e(TAG, "character = null");
		}

		String[] str = charPathStr.split("z");
		Path path = new Path();
		for (int i = 0; i < str.length; i++) {
			String pathStr = str[i];
			pathStr = pathStr + "z";
			Path path1 = SVGParser.parsePath(pathStr);
			path.addPath(path1);
		}

		for (int i = 0; i < CharPartList.size(); i++) {
			CharPart model = CharPartList.get(i);
			if (model == null) {
				continue;
			}

			String pathStr = model.getPartPath();
			String temp1 = "M" + pathStr;
			String temp2 = temp1.trim().replace(" ", "L");
			Path partPath = SVGParser.parsePath(temp2);
			charPartList.add(partPath);

			String directionStr = model.getPartDirection();
			Path directionPartPath = SVGParser.parsePath(directionStr);
			directionList.add(directionPartPath);
		}

		pv.setCharPath(path);
		pv.setPartPaths(charPartList);
		pv.setDirectionPaths(directionList);

		pv.setAnimListener(new HwAnim.OnAnimListener() {
			public void onEnd() {
				UiUtil.showToast(context, "画完了");
				// mStrokesReplayBtn.setClickable(true);
				// mStrokesReplayBtn.setBackgroundResource(2130838298);
				// StrokesOrder.access$202(StrokesOrder.this, true);
				// mStrokesWriteBtn.setClickable(true);
				// if (mEnv.strokesOrderAudioPlayMode == 2)
				// checkFileReady(mCurrentChar.Pinyin);
			}
		});

		pv.setWritingListener(new HwWriting.OnWritingListener() {
			public void onEnd() {
				// mStrokesWriteBtn.setBackgroundResource(2130838302);
				UiUtil.showToast(context, "写完了");
			}
		});
		isStartHwViewAnim = true;
		isStartHwViewWrite = false;
		pv.startHwAnim();
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app
						.finishActivity(StrokesOrderExerciseActivity.this);
				break;

			case R.id.btn_play:

				isStartHwViewAnim = true;
				pv.startHwAnim();
				break;
			case R.id.btn_write:
				pv.enableHandwriting();
				break;

			case R.id.btn_visible:
				if (pv.mBgVisible) {
					pv.setBgHanziVisibility(false);
				} else {
					pv.setBgHanziVisibility(true);
				}
				pv.invalidate();
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 顶部标题栏
	 * 
	 * @param textLeft
	 *            是否显示左边文字
	 * @param imgLeft
	 *            是否显示左边图片
	 * @param title
	 *            标题
	 * @param imgLeftDrawable
	 *            左边图片资源
	 * @param textRight
	 *            是否显示右边文字
	 * @param imgRight
	 *            是否显示右边图片
	 * @param imgRightDrawable
	 *            右边图片资源
	 */
	public void setTitle(int textLeft, int imgLeft, int imgLeftDrawable,
			String title, int textRight, int imgRight, int imgRightDrawable) {

		View view_title = (View) this.findViewById(R.id.view_title);
		Button tv_title = (Button) view_title.findViewById(R.id.btn_title);
		tv_title.setText(title);

		TextView tv_title_left = (TextView) view_title
				.findViewById(R.id.tv_title_left);
		tv_title_left.setVisibility(textLeft);

		ImageView iv_title_left = (ImageView) view_title
				.findViewById(R.id.iv_title_left);
		iv_title_left.setVisibility(imgLeft);
		iv_title_left.setOnClickListener(onClickListener);
		iv_title_left.setImageResource(imgLeftDrawable);

		TextView tv_title_right = (TextView) view_title
				.findViewById(R.id.tv_title_right);
		tv_title_right.setVisibility(textRight);
		tv_title_right.setOnClickListener(onClickListener);

		ImageView iv_title_right = (ImageView) view_title
				.findViewById(R.id.iv_title_right);
		iv_title_right.setVisibility(imgRight);
		iv_title_right.setImageResource(imgRightDrawable);

	}

}
