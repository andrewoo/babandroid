package com.hw.chineseLearn.tabDiscover;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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
import com.hw.chineseLearn.dao.bean.LGCharacter;
import com.hw.chineseLearn.dao.bean.LGModelFlashCard;
import com.hw.chineseLearn.dao.bean.TbMyCharacter;
import com.hw.chineseLearn.dao.bean.TbMyFluentNow;
import com.hw.chineseLearn.dao.bean.TbMySentence;
import com.hw.chineseLearn.dao.bean.TbMyWord;
import com.hw.chineseLearn.tabLearn.LessonFlashCardOpActivity;
import com.hw.chineseLearn.tabLearn.LessonFlashCardResultActivity;
import com.util.svgandroid.HwAnim;
import com.util.svgandroid.HwWriting;
import com.util.svgandroid.SVGParser;
import com.util.tool.MediaPlayUtil;
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
	private ImageView img_play, img_explain;

	LinearLayout lin_mizige;
	int screenWidth, screenHeight;
	boolean isStartHwViewAnim = true;
	boolean isStartHwViewWrite = false;
	String charId;
	String dirCode;
	private Resources resourse;
	LGCharacter lGCharacter;

	LinearLayout lin_is_gorget;
	TextView btn_remember;
	TextView btn_forget;

	LinearLayout lin_remember_level;
	TextView btn_remembered_perfectly;
	TextView btn_remembered;
	TextView btn_barely_remembered;

	LinearLayout lin_forgot_level;
	TextView btn_remembered_almost;
	TextView btn_forgot;
	TextView btn_dont_know;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_strokes_order_exercise, null);
		setContentView(contentView);
		CustomApplication.app.addActivity(this);
		context = this;
		resourse = this.getResources();
		screenWidth = CustomApplication.app.displayMetrics.widthPixels;
		screenHeight = CustomApplication.app.displayMetrics.heightPixels;
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("model")) {
				model = (Character) bundle.getSerializable("model");
			}
		}
		if (model != null) {
			title = model.getPinyin();
			charId = model.getCharId();
		}
		Log.d(TAG, "title:" + title);
		Log.d(TAG, "charId:" + charId);

		try {
			lGCharacter = (LGCharacter) MyDao.getDao(LGCharacter.class)
					.queryForId(charId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (lGCharacter != null) {
			dirCode = lGCharacter.getDirCode();
		} else {
			Log.e(TAG, "lGCharacter==null");
		}
		Log.d(TAG, "dirCode:" + dirCode);
		init();
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {

		lin_is_gorget = (LinearLayout) contentView
				.findViewById(R.id.lin_is_gorget);
		lin_is_gorget.setVisibility(View.VISIBLE);
		btn_remember = (TextView) contentView.findViewById(R.id.btn_remember);
		btn_forget = (TextView) contentView.findViewById(R.id.btn_forget);

		lin_remember_level = (LinearLayout) contentView
				.findViewById(R.id.lin_remember_level);
		lin_remember_level.setVisibility(View.GONE);

		int widthLin = screenWidth / 3;
		LinearLayout.LayoutParams lylin = new LinearLayout.LayoutParams(
				widthLin, widthLin);

		btn_remembered_perfectly = (TextView) contentView
				.findViewById(R.id.btn_remembered_perfectly);
		btn_remembered = (TextView) contentView
				.findViewById(R.id.btn_remembered);
		btn_barely_remembered = (TextView) contentView
				.findViewById(R.id.btn_barely_remembered);

		lin_forgot_level = (LinearLayout) contentView
				.findViewById(R.id.lin_forgot_level);
		lin_forgot_level.setVisibility(View.GONE);
		btn_remembered_almost = (TextView) contentView
				.findViewById(R.id.btn_remembered_almost);
		btn_forgot = (TextView) contentView.findViewById(R.id.btn_forgot);
		btn_dont_know = (TextView) contentView.findViewById(R.id.btn_dont_know);

		btn_remember.setOnClickListener(onClickListener);
		btn_forget.setOnClickListener(onClickListener);

		btn_remembered_perfectly.setOnClickListener(onClickListenerOp);
		btn_remembered.setOnClickListener(onClickListenerOp);
		btn_barely_remembered.setOnClickListener(onClickListenerOp);

		btn_remembered_almost.setOnClickListener(onClickListenerOp);
		btn_forgot.setOnClickListener(onClickListenerOp);
		btn_dont_know.setOnClickListener(onClickListenerOp);

		btn_remembered_perfectly.setLayoutParams(lylin);
		btn_remembered.setLayoutParams(lylin);
		btn_barely_remembered.setLayoutParams(lylin);

		btn_remembered_almost.setLayoutParams(lylin);
		btn_forgot.setLayoutParams(lylin);
		btn_dont_know.setLayoutParams(lylin);

		root_view = (LinearLayout) contentView.findViewById(R.id.root_view);
		lin_mizige = (LinearLayout) contentView.findViewById(R.id.lin_mizige);
		int width = (UiUtil.dip2px(context, 40)) * screenWidth / 1080;
		Log.d(TAG, "width:" + width);
		LinearLayout.LayoutParams ly = new LinearLayout.LayoutParams(width,
				width);
		img_play = (ImageView) contentView.findViewById(R.id.img_play);
		img_play.setLayoutParams(ly);
		img_play.setOnClickListener(onClickListener);
		img_explain = (ImageView) contentView.findViewById(R.id.img_explain);
		img_explain.setLayoutParams(ly);
		// img_explain.setVisibility(View.GONE);
		img_explain.setOnClickListener(onClickListener);
		int width1 = (UiUtil.dip2px(context, 60)) * screenWidth / 1080;
		Log.d(TAG, "width1:" + width1);
		LinearLayout.LayoutParams ly1 = new LinearLayout.LayoutParams(width1,
				width1);
		btn_play = (Button) contentView.findViewById(R.id.btn_play);
		btn_play.setLayoutParams(ly1);
		btn_play.setOnClickListener(onClickListener);

		int width2 = (UiUtil.dip2px(context, 80)) * screenWidth / 1080;
		Log.d(TAG, "width2:" + width2);
		LinearLayout.LayoutParams ly2 = new LinearLayout.LayoutParams(width2,
				width2);
		btn_write = (Button) contentView.findViewById(R.id.btn_write);
		btn_write.setLayoutParams(ly2);
		btn_write.setOnClickListener(onClickListener);

		btn_visible = (Button) contentView.findViewById(R.id.btn_visible);
		btn_visible.setLayoutParams(ly1);
		btn_visible.setOnClickListener(onClickListener);
		int mizigeWidth = screenWidth - (UiUtil.dip2px(context, 60));
		LayoutParams lymizige = lin_mizige.getLayoutParams();
		lymizige.width = mizigeWidth;
		lymizige.height = mizigeWidth;
		lin_mizige.setLayoutParams(lymizige);
		pv = (PathView) contentView.findViewById(R.id.pathView_bg);
		pv.setLayoutParams(lymizige);

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
				// UiUtil.showToast(context, "画完了");
				isStartHwViewAnim = false;
				setPlayBtnBg();
			}
		});

		pv.setWritingListener(new HwWriting.OnWritingListener() {
			public void onEnd() {
				// mStrokesWriteBtn.setBackgroundResource(2130838302);
				// UiUtil.showToast(context, "写完了");
				isStartHwViewWrite = false;
				setWriteBtnBg();
			}
		});
		isStartHwViewAnim = true;
		isStartHwViewWrite = false;
		pv.startHwAnim();
	}

	@SuppressWarnings("deprecation")
	private void setPlayBtnBg() {
		if (isStartHwViewAnim) {
			btn_play.setBackgroundDrawable(resourse
					.getDrawable(R.drawable.strokes_order_replay_onclick));
		} else {
			btn_play.setBackgroundDrawable(resourse
					.getDrawable(R.drawable.strokes_order_replay_noclick));
		}

	}

	@SuppressWarnings("deprecation")
	private void setWriteBtnBg() {
		if (isStartHwViewWrite) {
			btn_write.setBackgroundDrawable(resourse
					.getDrawable(R.drawable.strokes_order_write_onclick));
		} else {
			btn_write.setBackgroundDrawable(resourse
					.getDrawable(R.drawable.strokes_order_write_noclick));
		}
	}

	OnClickListener onClickListener = new OnClickListener() {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app
						.finishActivity(StrokesOrderExerciseActivity.this);
				break;

			case R.id.img_play:
				img_play.setBackgroundDrawable(resourse
						.getDrawable(R.drawable.strokes_order_paly_sound_onclick));
				assetPlay();
				break;

			case R.id.btn_play:

				isStartHwViewAnim = true;
				isStartHwViewWrite = false;
				pv.startHwAnim();
				setPlayBtnBg();
				setWriteBtnBg();
				break;
			case R.id.btn_write:
				pv.enableHandwriting();
				isStartHwViewWrite = true;
				setWriteBtnBg();
				setPlayBtnBg();
				break;

			case R.id.btn_visible:

				if (pv.mBgVisible) {
					pv.setBgHanziVisibility(false);
					btn_visible
							.setBackgroundDrawable(resourse
									.getDrawable(R.drawable.strokes_order_dismiss_bg_noclick));
				} else {
					pv.setBgHanziVisibility(true);
					btn_visible
							.setBackgroundDrawable(resourse
									.getDrawable(R.drawable.strokes_order_dismiss_bg_onclick));

				}
				pv.invalidate();
				break;

			case R.id.btn_remember:
				lin_is_gorget.setVisibility(View.GONE);
				lin_remember_level.setVisibility(View.VISIBLE);
				lin_forgot_level.setVisibility(View.GONE);
				break;

			case R.id.btn_forget:
				lin_is_gorget.setVisibility(View.GONE);
				lin_remember_level.setVisibility(View.GONE);
				lin_forgot_level.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	};

	OnClickListener onClickListenerOp = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			// remember
			case R.id.btn_remembered_perfectly:
				lin_remember_level.setVisibility(View.GONE);
				updateDb(1);
				break;
			case R.id.btn_remembered:
				lin_remember_level.setVisibility(View.GONE);
				updateDb(2);
				break;
			case R.id.btn_barely_remembered:
				lin_remember_level.setVisibility(View.GONE);
				updateDb(3);
				break;
			// forgot
			case R.id.btn_remembered_almost:
				lin_forgot_level.setVisibility(View.GONE);
				updateDb(4);
				break;
			case R.id.btn_forgot:
				lin_forgot_level.setVisibility(View.GONE);
				updateDb(5);
				break;
			case R.id.btn_dont_know:
				lin_forgot_level.setVisibility(View.GONE);
				updateDb(6);
				break;

			default:
				break;
			}

		}
	};

	/**
	 * 
	 * @param Proficient
	 *            熟练度
	 */
	@SuppressWarnings("unchecked")
	private void updateDb(int proficient) {
		lin_is_gorget.setVisibility(View.VISIBLE);
		// if (index < datas.size()) {
		//
		// LGModelFlashCard lGModelFlashCard = datas.get(index);
		// if (lGModelFlashCard != null) {
		//
		// TbMyCharacter tbMyCharacter = lGModelFlashCard.getMyCharacter();
		// TbMySentence tbMySentence = lGModelFlashCard.getMySentence();
		// TbMyWord tbMyWord = lGModelFlashCard.getMyWord();
		//
		// if (tbMyCharacter != null) {
		// tbMyCharacter.setProficient(proficient);
		// try {
		// int C = MyDao.getDaoMy(TbMyCharacter.class).update(
		// tbMyCharacter);
		// // Log.d(TAG, "更新熟练度C:" + C);
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// if (tbMySentence != null) {
		// try {
		// tbMySentence.setProficient(proficient);
		// int S = MyDao.getDaoMy(TbMySentence.class).update(
		// tbMySentence);
		// // Log.d(TAG, "更新熟练度S:" + S);
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// if (tbMyWord != null) {
		// try {
		// tbMyWord.setProficient(proficient);
		// int W = MyDao.getDaoMy(TbMyWord.class).update(tbMyWord);
		// // Log.d(TAG, "更新熟练度W:" + W);
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }
		// index++;
		// Log.d(TAG, "index:" + index + "  defaultNumber:" +
		// defaultNumber);
		// if (index == defaultNumber) {
		// // 做完了
		// Intent intent = new Intent(LessonFlashCardOpActivity.this,
		// LessonFlashCardResultActivity.class);
		// startActivityForResult(intent, 1);
		// } else {
		// gallery.setSelection(index);// 选中当前页面
		// setText();
		// assetPlay();
		// }
		//
		// }
	}

	private static final String ASSETS_SOUNDS_PATH = "sounds/";
	private String voicePath;

	/**
	 * 播放asset里的声音文件
	 */
	private void assetPlay() {
		voicePath = "c-" + charId + "-" + dirCode + ".mp3";
		MediaPlayUtil.getInstance().setPlayOnCompleteListener(
				new OnCompletionListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onCompletion(MediaPlayer mp) {
						MediaPlayUtil.getInstance().release();
						img_play.setBackgroundDrawable(resourse
								.getDrawable(R.drawable.strokes_order_play_sound_noclick));
					}
				});
		AssetManager am = getAssets();
		try {
			AssetFileDescriptor afd = am.openFd(ASSETS_SOUNDS_PATH + voicePath);
			MediaPlayUtil.getInstance().play(afd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

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
