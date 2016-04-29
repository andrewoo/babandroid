package com.hw.chineseLearn.tabDiscover;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.FluentDetailAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.bean.TbMyFluentNow;
import com.hw.chineseLearn.db.DatabaseHelper;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.hw.chineseLearn.model.FluentDetailListModel;
import com.hw.chineseLearn.model.FluentDetailListWordsModel;
import com.hw.chineseLearn.model.FluentDetailModel;
import com.hw.chineseLearn.model.LearnUnitBaseModel;
import com.util.tool.AudioRecorder;
import com.util.tool.FileTools;
import com.util.tool.JsonUtil;
import com.util.tool.MediaPlayUtil;
import com.util.tool.MediaPlayerHelper;
import com.util.tool.AudioRecorder.VMChangeListener;
import com.util.weight.SlideSwitch;
import com.util.weight.SlideSwitch.SlideListener;

/**
 * 流畅练习对话课程
 * 
 * @author yh
 */
public class FluentDetailActivity extends BaseActivity {

	private String TAG = "==FluentDetailActivity==";
	private Context context;
	View contentView;
	ListView lv_fluent_list;
	FluentDetailAdapter adapter;
	ArrayList<FluentDetailListModel> sents = new ArrayList<FluentDetailListModel>();
	int selectIndex = 0;
	String title = "Title";
	String fluentIdStr, awsId;
	TbMyFluentNow model = null;
	boolean isRecord = false;// 是否正在录音
	private MediaPlayerHelper mediaPlayerHelper;
	private String flag = "listen";// 录音按钮 默认为听的状态
	private String lastRecFileName = "kitrecoder";
	private String filePath = DatabaseHelper.CACHE_DIR_SOUND + "/"
			+ lastRecFileName + ".amr";
	private AudioRecorder mr;
	private boolean isLoop = false;
	private Resources resources = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_fluent_detail, null);
		setContentView(contentView);
		CustomApplication.app.addActivity(this);
		context = this;
		resources = context.getResources();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("model")) {
				model = (TbMyFluentNow) bundle.getSerializable("model");
			}
		}
		init();
	}

	/**
	 * 初始化
	 */
	public void init() {
		title = model.getTitle_CN();
		setTitle(View.GONE, View.VISIBLE,
				R.drawable.btn_selector_top_left_white, title, View.GONE,
				View.VISIBLE, R.drawable.img_setting_white);
		lv_fluent_list = (ListView) contentView
				.findViewById(R.id.lv_fluent_list);
		lv_fluent_list.setSelector(context.getResources().getDrawable(
				R.drawable.bg_touming));
		lv_fluent_list.setOnItemClickListener(onItemclickListener);
		initJsonDataFromFile();
		adapter = new FluentDetailAdapter(context, sents, fluentIdStr, awsId);
		lv_fluent_list.setAdapter(adapter);
		adapter.notifyDataSetChanged();

	}

	/**
	 * 从文件中读取json文件，解析并设置到实体中。
	 */
	private void initJsonDataFromFile() {
		if (model != null) {
			int fluentId = model.getFluentID();
			Log.d(TAG, "fluentId:" + fluentId);
			fluentIdStr = "" + fluentId;

			int AWS_ID = model.getAWS_ID();
			Log.d(TAG, "AWS_ID:" + AWS_ID);
			awsId = "" + AWS_ID;

			String jsonPath = DatabaseHelperMy.CONTENT_JSON_PATH + "/result"
					+ fluentId + ".json";
			Log.d(TAG, "jsonPath:" + jsonPath);
			String results = FileTools.getJsonFromFile(getApplicationContext(),
					jsonPath);
			// Log.d(TAG + "results:", results);
			FluentDetailModel detailModel = new FluentDetailModel();
			ArrayList<FluentDetailListModel> sentens = new ArrayList<FluentDetailListModel>();
			if (!"".equals(results)) {

				JSONObject jsObj;
				try {
					jsObj = new JSONObject(results);
					String CATN = JsonUtil.getString(jsObj, "CATN");
					String TRE = JsonUtil.getString(jsObj, "TRE");
					String MD5C = JsonUtil.getString(jsObj, "MD5C");
					String PUBD = JsonUtil.getString(jsObj, "PUBD");
					String CATT = JsonUtil.getString(jsObj, "CATT");
					String TRJ = JsonUtil.getString(jsObj, "TRJ");
					String EID = JsonUtil.getString(jsObj, "EID");
					String TRK = JsonUtil.getString(jsObj, "TRK");
					String TRF = JsonUtil.getString(jsObj, "TRF");
					String CID = JsonUtil.getString(jsObj, "CID");
					String TRS = JsonUtil.getString(jsObj, "TRS");
					String LVLT = JsonUtil.getString(jsObj, "LVLT");
					String ST = JsonUtil.getString(jsObj, "ST");
					String TT = JsonUtil.getString(jsObj, "TT");
					String Sents = JsonUtil.getString(jsObj, "Sents");

					detailModel.setCATN(CATN);
					detailModel.setTRE(TRE);
					detailModel.setMD5C(MD5C);
					detailModel.setPUBD(PUBD);
					detailModel.setCATT(CATT);
					detailModel.setTRJ(TRJ);
					detailModel.setEID(EID);
					detailModel.setTRK(TRK);
					detailModel.setTRF(TRF);
					detailModel.setCID(CID);
					detailModel.setTRS(TRS);
					detailModel.setLVLT(LVLT);
					detailModel.setST(ST);
					detailModel.setTT(TT);

					JSONObject obj1 = new JSONObject(Sents);
					int obj1Count = obj1.length();
					Log.d(TAG, "obj1Count:" + obj1Count);
					for (int i = 0; i < obj1Count; i++) {
						FluentDetailListModel fluentDetailListModel = new FluentDetailListModel();
						ArrayList<FluentDetailListWordsModel> Words = new ArrayList<FluentDetailListWordsModel>();
						String obj2Str = obj1.getString("" + i);
						if ("".equals(obj2Str) || "{}".equals(obj2Str)) {
							continue;
						}
						JSONObject obj2 = new JSONObject(obj2Str);
						String STRE = JsonUtil.getString(obj2, "STRE");
						String STRJ = JsonUtil.getString(obj2, "STRJ");
						String STRK = JsonUtil.getString(obj2, "STRK");
						fluentDetailListModel.setSTRE(STRE);
						fluentDetailListModel.setSTRJ(STRJ);
						fluentDetailListModel.setSTRK(STRK);

						String WordsStr = JsonUtil.getString(obj2, "Words");
						if ("".equals(WordsStr) || "{}".equals(WordsStr)) {
							continue;
						}
						JSONObject obj3 = new JSONObject(WordsStr);
						int wordsCount = obj3.length();
						for (int j = 0; j < wordsCount; j++) {
							FluentDetailListWordsModel fluentDetailListWordsModel = new FluentDetailListWordsModel();
							String obj4Str = obj3.getString("" + j);
							if ("".equals(obj4Str) || "{}".equals(obj4Str)) {
								continue;
							}
							JSONObject obj4 = new JSONObject(obj4Str);
							String SW = JsonUtil.getString(obj4, "SW");
							String PY = JsonUtil.getString(obj4, "PY");
							String TW = JsonUtil.getString(obj4, "TW");
							fluentDetailListWordsModel.setSW(SW);
							fluentDetailListWordsModel.setPY(PY);
							fluentDetailListWordsModel.setTW(TW);
							Words.add(fluentDetailListWordsModel);
						}
						fluentDetailListModel.setWords(Words);
						sentens.add(fluentDetailListModel);
					}
					detailModel.setSents(sentens);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (detailModel != null) {
				sents = detailModel.getSents();
				if (sents != null) {
					Log.d(TAG, "sents.size():" + sents.size());
				} else {
					Log.e(TAG, "sents==null");
				}

			} else {
				Log.e(TAG, "detailModel==null");
			}

			Log.e(TAG, "--------------");
		} else {
			Log.e(TAG, "model==null");
		}
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app.finishActivity(FluentDetailActivity.this);
				break;

			case R.id.iv_title_right://
				showPopupWindowMenu();
				break;
			default:
				break;
			}
		}
	};

	OnItemClickListener onItemclickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View convertView,
				final int position, long arg3) {
			// TODO Auto-generated method stub
			adapter.setIsControl(false);
			adapter.setSelection(position);
			adapter.notifyDataSetChanged();

			ImageView img_play = (ImageView) convertView
					.findViewById(R.id.img_play);

			img_play.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					playVoice(position);
					adapter.setIsControl(false);
				}
			});
			ImageView img_record = (ImageView) convertView
					.findViewById(R.id.img_record);
			img_record.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// 点击后录音 再次点击停止录音 并播放正确录音
					isRecord = !isRecord;
					setRecoedBg((ImageView) v, position);
				}
			});
			final ImageView img_loop = (ImageView) convertView
					.findViewById(R.id.img_loop);
			img_loop.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// 设置是否循环 点击后图标改变 停止正在循环的录音 判断当前语音是否循环
					if (isLoop) {
						img_loop.setImageResource(R.drawable.loop_play);
						Log.d(TAG, "isLoop==true");
					} else {
						img_loop.setImageResource(R.drawable.unloop_play);
						Log.d(TAG, "isLoop==false");
					}
					isLoop = !isLoop;
					adapter.setIsControl(false);
					adapter.notifyDataSetChanged();
				}
			});

		}
	};

	@SuppressWarnings("deprecation")
	protected void setRecoedBg(final ImageView imageView, int position) {
		if (isRecord) {
			imageView.setBackgroundDrawable(resources
					.getDrawable(R.drawable.recorder_animate_bg_click));
			flag = "talk";
			deleteRecord();
			try {
				String fileName = lastRecFileName;
				mr = new AudioRecorder(fileName);
				mr.start();
				// 音量变化调节
				mr.setVMChangeListener(new VMChangeListener() {
					@Override
					public int onVMChanged(int value) {

						int volume = mr.recorder.getMaxAmplitude();
						int vmValue = 15 * volume / 32768;
						Message msg = Message.obtain();
						msg.what = vmValue;
						msg.obj = imageView;
						handler.sendMessage(msg);
						return 0;
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {

			flag = "listen";
			if (mr != null) {
				mr.reset();
				mr = null;
			}
			playRecoder(position);// 播放录制的声音
			imageView.setBackgroundDrawable(resources
					.getDrawable(R.drawable.recorder_animate_bg));
			imageView.setImageDrawable(resources
					.getDrawable(R.drawable.recorder_animate_01));
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ImageView imageView = (ImageView) msg.obj;
			switch (msg.what) {

			case 100:
				playVoice(msg.arg1);
				break;

			case 0:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_01));
				break;

			case 1:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_01));
				break;

			case 2:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_02));
				break;

			case 3:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_03));
				break;

			case 4:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_04));
				break;

			case 5:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_05));
				break;

			case 6:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_06));
				break;

			case 7:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_07));
				break;
			case 8:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_08));
				break;
			case 9:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_09));
				break;
			case 10:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_10));
				break;
			case 11:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_11));
				break;
			case 12:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_12));
				break;
			case 13:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_13));
				break;
			case 14:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_14));
				break;
			case 15:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_15));
				break;

			default:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_01));
			}

		};
	};

	/**
	 * 删除录音
	 */
	private void deleteRecord() {
		File file = new File(filePath);
		if (file.exists()) {
			Log.d(TAG, "file.exists()！");
			file.delete();
		}
	}

	/**
	 * 播放录音
	 * 
	 * @param position
	 */
	private void playRecoder(final int position) {
		final MediaPlayUtil instance = MediaPlayUtil.getInstance();
		instance.setPlayOnCompleteListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				MediaPlayUtil.getInstance().release();// 释放之前的mediaplayer
				playVoice(position);// 不然这里播放完会重复执行这个监听
				// Message msg=Message.obtain();
				// msg.arg1=position;
				// msg.what=100;
				// handler.sendMessage(msg);
			}
		});
		instance.play(filePath);
	}

	/**
	 * 播放声音
	 * 
	 * @param position
	 */
	private void playVoice(final int position) {
		String soundPath = DatabaseHelperMy.SOUND_PATH + "/" + awsId + "/"
				+ awsId + "-" + (position + 1) + ".mp3";
		Log.d(TAG, "soundPath:" + soundPath);
		MediaPlayUtil instance = MediaPlayUtil.getInstance();
		instance.setPlayOnCompleteListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// if (isLoop) {
				MediaPlayUtil.getInstance().release();
				// playRecoder(position);
				// }
			}
		});
		instance.play(soundPath);
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
		view_title.setBackgroundColor(context.getResources().getColor(
				R.color.chinese_skill_blue));
		Button tv_title = (Button) view_title.findViewById(R.id.btn_title);
		tv_title.setText(title);
		tv_title.setTextColor(context.getResources().getColor(R.color.white));

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
		iv_title_right.setOnClickListener(onClickListener);

	}

	View view;
	String display_state = "0";
	String font_size = "17";

	public void showPopupWindowMenu() {

		adapter.setIsControl(true);

		display_state = CustomApplication.app.preferencesUtil.getValue(
				"display_state", "0");
		font_size = CustomApplication.app.preferencesUtil.getValue("font_size",
				"17");

		view = LayoutInflater.from(this).inflate(R.layout.layout_title_menu3,
				null);

		int width = CustomApplication.app.displayMetrics.widthPixels / 10 * 6;
		final PopupWindow popupWindow = new PopupWindow(view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		LinearLayout lin_content = (LinearLayout) view
				.findViewById(R.id.lin_content);
		LayoutParams ly = lin_content.getLayoutParams();
		ly.width = width;
		lin_content.setLayoutParams(ly);

		LinearLayout lin_aa = (LinearLayout) view.findViewById(R.id.lin_aa);
		lin_aa.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
			}
		});
		RadioGroup rg_display = (RadioGroup) view.findViewById(R.id.rg_display);
		rg_display.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {

				case R.id.rb_CN_EG://
					CustomApplication.app.preferencesUtil.setValue(
							"display_state", "0");
					break;
				case R.id.rb_CN_aa:
					CustomApplication.app.preferencesUtil.setValue(
							"display_state", "1");
					break;
				case R.id.rb_EN_aa:
					CustomApplication.app.preferencesUtil.setValue(
							"display_state", "2");
					break;

				}
				adapter.setDisplayAndFont(display_state, font_size);
				adapter.notifyDataSetChanged();
			}
		});
		RadioButton rb_CN_EG = (RadioButton) view.findViewById(R.id.rb_CN_EG);
		RadioButton rb_CN_aa = (RadioButton) view.findViewById(R.id.rb_CN_aa);
		RadioButton rb_EN_aa = (RadioButton) view.findViewById(R.id.rb_EN_aa);

		if ("0".equals(display_state)) {
			rb_CN_EG.setChecked(true);
		} else if ("1".equals(display_state)) {
			rb_CN_aa.setChecked(true);
		} else {
			rb_EN_aa.setChecked(true);
		}

		RadioGroup rg_font = (RadioGroup) view.findViewById(R.id.rg_font);
		rg_font.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {

				case R.id.rb_Small://
					CustomApplication.app.preferencesUtil.setValue("font_size",
							"10");
					break;
				case R.id.rb_Medium:
					CustomApplication.app.preferencesUtil.setValue("font_size",
							"14");
					break;
				case R.id.rb_Large:
					CustomApplication.app.preferencesUtil.setValue("font_size",
							"22");
					break;
				}
				adapter.setDisplayAndFont(display_state, font_size);
				adapter.notifyDataSetChanged();
			}
		});
		RadioButton rb_Small = (RadioButton) view.findViewById(R.id.rb_Small);
		RadioButton rb_Medium = (RadioButton) view.findViewById(R.id.rb_Medium);
		RadioButton rb_Large = (RadioButton) view.findViewById(R.id.rb_Large);

		if ("10".equals(font_size)) {
			rb_Small.setChecked(true);
		} else if ("14".equals(font_size)) {
			rb_Medium.setChecked(true);
		} else {// 22
			rb_Large.setChecked(true);
		}
		popupWindow.setTouchable(true);
		popupWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i("mengdd", "onTouch : ");
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bg_touming));

		// popupWindow.showAsDropDown(iv_title_right, 0, 0);
		popupWindow.showAtLocation(contentView, Gravity.RIGHT, 0, 60);

		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MediaPlayUtil.getInstance().stop();
		MediaPlayUtil.getInstance().release();

	}
}
