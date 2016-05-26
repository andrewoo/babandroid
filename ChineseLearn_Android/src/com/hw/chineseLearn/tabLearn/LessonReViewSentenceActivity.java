package com.hw.chineseLearn.tabLearn;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.ReviewSentenceListAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.LGSentence;
import com.hw.chineseLearn.dao.bean.TbMySentence;
import com.hw.chineseLearn.db.DatabaseHelper;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.util.tool.AudioRecorder;
import com.util.tool.AudioRecorder.VMChangeListener;
import com.util.tool.MediaPlayUtil;

/**
 * 课程复习-偏旁部首-页面
 * 
 * @author yh
 */
public class LessonReViewSentenceActivity extends BaseActivity {

	private String TAG = "==LessonReViewSentenceActivity==";
	public Context context;
	private ListView listView;
	private Resources resources;
	private int width;
	private int height;
	View contentView;
	ReviewSentenceListAdapter reviewListAdapter;
	ArrayList<LGSentence> listBase = new ArrayList<LGSentence>();
	ArrayList<TbMySentence> tbMySentenceList;

	private TextView tv_pinyin, tv_sentence, tv_translation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_review_sentence, null);
		setContentView(contentView);
		context = this;
		CustomApplication.app.addActivity(this);
		super.gestureDetector();
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 5;
		resources = context.getResources();
		init();
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {
		img_record = (ImageView) contentView.findViewById(R.id.img_record);
		img_record.setOnClickListener(onClickListener);

		img_loop = (ImageView) contentView.findViewById(R.id.img_loop);
		img_loop.setVisibility(View.GONE);
		img_loop.setOnClickListener(onClickListener);

		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Sentence Review", View.GONE, View.VISIBLE,
				R.drawable.revie_pen);
		tv_pinyin = (TextView) contentView.findViewById(R.id.tv_pinyin);
		tv_sentence = (TextView) contentView.findViewById(R.id.tv_sentence);
		tv_translation = (TextView) contentView
				.findViewById(R.id.tv_translation);

		listView = (ListView) contentView.findViewById(R.id.list_view);

		try {
			tbMySentenceList = (ArrayList<TbMySentence>) MyDao.getDaoMy(
					TbMySentence.class).queryForAll();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < tbMySentenceList.size(); i++) {
			TbMySentence model = tbMySentenceList.get(i);
			if (model == null) {
				continue;
			}
			int sentenceId = model.getSentenceId();
			try {
				LGSentence lGSentence = (LGSentence) MyDao.getDao(
						LGSentence.class).queryForId(sentenceId);
				listBase.add(lGSentence);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (reviewListAdapter == null) {
			reviewListAdapter = new ReviewSentenceListAdapter(context, listBase);
			listView.setAdapter(reviewListAdapter);
			reviewListAdapter.notifyDataSetChanged();
			setTranslations(0);
		}
		listView.setOnItemClickListener(onItemclickListener);
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
		iv_title_right.setOnClickListener(onClickListener);

	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回
				CustomApplication.app
						.finishActivity(LessonReViewSentenceActivity.this);
				break;

			case R.id.iv_title_right://
				Intent intent = new Intent(LessonReViewSentenceActivity.this,
						LessonReviewExerciseActivity.class);
				intent.putExtra("lgTable", 1);
				startActivity(intent);
				break;
			case R.id.img_record:

				if (isRecord) {
					isRecord = false;
				} else {
					isRecord = true;
				}
				setRecoedBg();
				break;

			case R.id.img_loop:
				if (loop) {
					img_loop.setImageDrawable(resources
							.getDrawable(R.drawable.unloop_play));
					loop = false;
				} else {
					img_loop.setImageDrawable(resources
							.getDrawable(R.drawable.loop_play));
					loop = true;
				}
				break;
			default:
				break;
			}
		}
	};
	OnItemClickListener onItemclickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View convertView,
				int position, long arg3) {
			// TODO Auto-generated method stub

			reviewListAdapter.setSelection(position);
			reviewListAdapter.notifyDataSetChanged();
			setTranslations(position);
		}
	};

	private ImageView img_record, img_loop;
	private static final String ASSETS_SOUNDS_PATH = "sounds/";
	private String voicePath;
	boolean isRecord = false;
	String lastRecFileName = "";
	private AudioRecorder mr;
	private String flag = "listen";
	Thread thread = null;
	/**
	 * 获取的音量大小
	 */
	int vmValue = 0;
	String filePath = "";
	boolean loop = true;

	private void deleteRecord() {
		File file = new File(filePath);
		if (file.exists()) {
			Log.d(TAG, "file.exists()！");
			file.delete();
		}
	}

	/**
	 * 播放asset里的声音文件
	 */
	private void assetPlay() {
		MediaPlayUtil.getInstance().setPlayOnCompleteListener(
				new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						if (loop) {
							MediaPlayUtil.getInstance().release();
							recordPlay();
						}
					}
				});
		MediaPlayUtil.getInstance().play(
				DatabaseHelperMy.LESSON_SOUND_PATH + "/" + voicePath);
	}

	/**
	 * 播放录音文件
	 */
	private void recordPlay() {
		filePath = DatabaseHelper.CACHE_DIR_SOUND + "/" + lastRecFileName
				+ ".amr";
		MediaPlayUtil.getInstance().setPlayOnCompleteListener(
				new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						// deleteRecord();
						MediaPlayUtil.getInstance().release();
						assetPlay();
					}
				});
		MediaPlayUtil.getInstance().play(filePath);
	}

	@SuppressLint("NewApi")
	private void setRecoedBg() {
		if (isRecord) {
			img_record.setBackground(resources
					.getDrawable(R.drawable.recorder_animate_bg_click));
			img_loop.setVisibility(View.GONE);
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
						Log.e(TAG, "volume " + volume);
						vmValue = 15 * volume / 32768;
						Log.e(TAG, "vmValue " + vmValue);
						thread = new Thread(new VmChangeRunableInner());
						thread.start();
						return 0;
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {

			flag = "listen";
			try {
				if (mr != null) {
					mr.stop();
					mr = null;
				}
				img_record.setBackground(resources
						.getDrawable(R.drawable.recorder_animate_bg));
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_01));
				recordPlay();
				img_loop.setVisibility(View.VISIBLE);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @author yh
	 * 
	 */
	class VmChangeRunableInner implements Runnable {
		@Override
		public void run() {
			Looper.prepare();
			while (true) {
				try {
					Looper mainLooper = Looper.getMainLooper();
					MyHandler handler = new MyHandler(mainLooper);
					Object obj = null;
					Message msg = Message.obtain();
					switch (vmValue) {
					case 0:
						msg.what = 0;
						msg.obj = obj;
						break;
					case 1:
						msg.what = 1;
						msg.obj = obj;
						break;
					case 2:
						msg.what = 2;
						msg.obj = obj;
						break;
					case 3:
						msg.what = 3;
						msg.obj = obj;

						break;
					case 4:
						msg.what = 4;
						msg.obj = obj;
						break;
					case 5:
						msg.what = 5;
						msg.obj = obj;
						break;
					case 6:
						msg.what = 6;
						msg.obj = obj;
						break;
					case 7:
						msg.what = 7;
						msg.obj = obj;
						break;
					case 8:
						msg.what = 8;
						msg.obj = obj;
						break;
					case 9:
						msg.what = 9;
						msg.obj = obj;
						break;
					case 10:
						msg.what = 10;
						msg.obj = obj;
						break;
					case 11:
						msg.what = 11;
						msg.obj = obj;
						break;
					case 12:
						msg.what = 12;
						msg.obj = obj;
						break;
					case 13:
						msg.what = 13;
						msg.obj = obj;
						break;
					case 14:
						msg.what = 14;
						msg.obj = obj;
						break;
					case 15:
						msg.what = 15;
						msg.obj = obj;
						break;
					}
					handler.sendMessage(msg);
					Thread.sleep(500);//

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 用来处理消息,改变图片
	 */
	private class MyHandler extends Handler {

		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) // 处理消息
		{
			switch (msg.what) {

			case 0:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_01));
				break;

			case 1:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_01));
				break;

			case 2:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_02));
				break;

			case 3:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_03));
				break;

			case 4:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_04));
				break;

			case 5:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_05));
				break;

			case 6:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_06));
				break;

			case 7:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_07));
				break;
			case 8:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_08));
				break;
			case 9:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_09));
				break;
			case 10:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_10));
				break;
			case 11:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_11));
				break;
			case 12:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_12));
				break;
			case 13:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_13));
				break;
			case 14:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_14));
				break;
			case 15:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_15));
				break;

			default:
				img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_01));
			}

		}
	}

	/**
	 * @param position
	 */
	private void setTranslations(int position) {
		LGSentence model = listBase.get(position);
		img_loop.setVisibility(View.GONE);
		if (model != null) {
			// tv_pinyin
			String sentence = model.getSentence();
			tv_sentence.setText(sentence);
			String translations = model.getTranslations();
			tv_translation.setText(translations);

			int sentenceId = model.getSentenceId();
			String dirCode = model.getDirCode();
			voicePath = "s-" + sentenceId + "-" + dirCode + ".mp3";
			deleteRecord();
			assetPlay();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MediaPlayUtil.getInstance().stop();
		MediaPlayUtil.getInstance().release();
		deleteRecord();
	}
}
