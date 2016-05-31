package com.hw.chineseLearn.tabLearn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.LearnWordSelectListAdapter;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.dao.bean.LGModelWord;
import com.hw.chineseLearn.dao.bean.LGModelWord.SubLGModel;
import com.hw.chineseLearn.dao.bean.LessonRepeatRegex;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.hw.chineseLearn.model.LearnUnitBaseModel;
import com.util.thread.ThreadWithDialogTask;
import com.util.tool.HttpHelper;
import com.util.tool.MediaPlayUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * 句子选择
 * 
 * @author yh
 */
/**
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class LearnWordSelectFragment extends BaseFragment implements
		OnClickListener {
	private String TAG = "LearnWordSelectFragment";
	private static final String ASSETS_SOUNDS_PATH = "sounds/";
	private View contentView;// 主view

	private ThreadWithDialogTask task;
	public static LearnWordSelectFragment fragment;
	Context context;
	TextView txt_name;
	String question = "我是男人";
	private ListView listView;
	LearnWordSelectListAdapter adapter;
	ArrayList<LearnUnitBaseModel> listBase = new ArrayList<LearnUnitBaseModel>();// testdata
	private boolean isRight;
	private int answer;// 此题的答案lgword.getanswer
	String filePath = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fragment = this;
		context = getActivity();
		initData();
//		play();
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

	private void initData() {
		Bundle bundle = getArguments();
		if (bundle.containsKey("modelWorld")) {
			modelWord = (LGModelWord) getArguments().getSerializable(
					"modelWorld");
			voicePath = modelWord.getVoicePath();

			filePath = DatabaseHelperMy.LESSON_SOUND_PATH + "/" + voicePath;
			Log.d(TAG + "initData()", "filePath:" + filePath);
			File file = new File(filePath);
			if (!file.exists()) {
				// 下载并播放
				HttpHelper.downLoadLessonVoices(voicePath, true);
			} else {
				play();
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_lesson_word_select,
				null);
		task = new ThreadWithDialogTask();
		init();
		return contentView;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();

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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 初始化
	 */
	public void init() {
		txt_name = (TextView) contentView.findViewById(R.id.txt_name);
		txt_name.setText(modelWord.getTitle());

		listView = (ListView) contentView.findViewById(R.id.list_view);
		adapter = new LearnWordSelectListAdapter(context, modelWord);
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
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(onItemclickListener);
		adapter.notifyDataSetChanged();
	}

	OnItemClickListener onItemclickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View convertView,
				int position, long arg3) {

			adapter.setSelection(position);
			adapter.notifyDataSetChanged();
			answer = modelWord.getAnswer();
			SubLGModel subLGModel = modelWord.getSubLGModelList().get(position);
			int wordId = subLGModel.getWordId();
			if (wordId == answer) {
				isRight = true;
			} else {
				isRight = false;
			}
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
		}
	};

	private LessonRepeatRegex lessonRepeatRegex;

	private LGModelWord modelWord;
	private String voicePath;
	private Button btn_play_normal;

	public boolean isRight() {
		return isRight;
	}
}
