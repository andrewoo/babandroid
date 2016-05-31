package com.hw.chineseLearn.tabLearn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.LearnImageSelectAdapter;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.dao.bean.LGModelWord;
import com.hw.chineseLearn.dao.bean.LGModelWord.SubLGModel;
import com.hw.chineseLearn.dao.bean.LGWord;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.util.thread.ThreadWithDialogTask;
import com.util.tool.HttpHelper;
import com.util.tool.MediaPlayUtil;
import com.util.tool.MediaPlayerHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 学习-选图片
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LearnImageSelectFragment extends BaseFragment implements
		OnClickListener {
	private static final String TAG = "LearnImageSelectFragment";
	private static final String ASSETS_SOUNDS_PATH = "sounds/";

	private View contentView;// 主view

	private ThreadWithDialogTask task;
	public static LearnImageSelectFragment fragment;
	Context context;
	LearnImageSelectAdapter learnImageSelectAdapter;
	GridView gv_image;
	TextView txt_name;
	String question = "\"color\"";
	private List<LGWord> lgWordList = new ArrayList<LGWord>();
	private LGModelWord modelWord;// 存放当前题
	MediaPlayerHelper meidiaPlayer;
	// MediaPlayer mediaPlayer = null;

	private int answer;// 此题的答案lgword.getanswer
	private boolean isRight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fragment = this;
		context = getActivity();
		initDBdata();// 初始化数据库数据
	}

	/**
	 * 得到数据库中数据
	 */
	private void initDBdata() {
		modelWord = (LGModelWord) getArguments().getSerializable("modelWorld");
		voicePath = modelWord.getVoicePath();
		String title = modelWord.getTitle();
		question = title;

		// 下载语音文件
		List<SubLGModel> subLGModelList = modelWord.getSubLGModelList();
		if (subLGModelList != null && subLGModelList.size() != 0) {
			for (int i = 0; i < subLGModelList.size(); i++) {
				SubLGModel subLGModel = subLGModelList.get(i);
				if (subLGModel == null) {
					continue;
				}
				String voicePath = subLGModel.getSubVoicePath();

				String filePath = DatabaseHelperMy.LESSON_SOUND_PATH + "/"
						+ voicePath;
				Log.d("filePath", "filePath:" + filePath);
				File file = new File(filePath);
				if (!file.exists()) {
					// 下载不播放
					HttpHelper.downLoadLessonVoices(voicePath, false);
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_lesson_image_select,
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 初始化
	 */
	public void init() {
		// TODO Auto-generated method stub
		txt_name = (TextView) contentView.findViewById(R.id.txt_name);
		txt_name.setText(question);

		gv_image = (GridView) contentView.findViewById(R.id.gv_image);

		learnImageSelectAdapter = new LearnImageSelectAdapter(context,
				modelWord);
		gv_image.setAdapter(learnImageSelectAdapter);
		gv_image.setOnItemClickListener(itemClickListener);
		learnImageSelectAdapter.notifyDataSetChanged();

	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			learnImageSelectAdapter.setSelection(position);
			Integer answer = modelWord.getAnswer();
			int wordId = modelWord.getSubLGModelList().get(position)
					.getWordId();
			if (wordId == answer) {
				isRight = true;
			} else {
				isRight = false;
			}

			String voiceP = modelWord.getSubLGModelList().get(position)
					.getSubVoicePath();
			String filePath = DatabaseHelperMy.LESSON_SOUND_PATH + "/" + voiceP;
			Log.d("filePath", "filePath:" + filePath);
			File file = new File(filePath);
			if (!file.exists()) {
				// 下载并播放
				HttpHelper.downLoadLessonVoices(voiceP, true);
			} else {
				// 直接播放
				MediaPlayUtil.getInstance().play(filePath);
			}

			learnImageSelectAdapter.notifyDataSetChanged();

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

	private String voicePath;

	public boolean isRight() {
		return isRight;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MediaPlayUtil.getInstance().release();
		MediaPlayUtil.getInstance().stop();

	}

}
