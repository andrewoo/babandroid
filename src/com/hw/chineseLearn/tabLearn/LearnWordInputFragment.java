package com.hw.chineseLearn.tabLearn;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.LearnUnitAdapter;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.LGModelWord;
import com.hw.chineseLearn.dao.bean.LGModel_Word_040;
import com.hw.chineseLearn.dao.bean.LGWord;
import com.hw.chineseLearn.dao.bean.LessonRepeatRegex;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.util.thread.ThreadWithDialogTask;
import com.util.tool.HttpHelper;
import com.util.tool.MediaPlayUtil;
import com.util.tool.MediaPlayerHelper;
import com.util.tool.UiUtil;

/**
 * 文字输入视图模版
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LearnWordInputFragment extends BaseFragment implements
		OnClickListener {
	private View contentView;// 主view

	private ThreadWithDialogTask task;
	public static LearnWordInputFragment fragment;
	Context context;
	LearnUnitAdapter learnUnit1Adapter, learnUnit2Adapter;
	private List<String> answerList = new ArrayList<String>();

	private String title;

	private TextView txt_name;

	private EditText et_input;

	private String answerChanged;
	private static final String ASSETS_SOUNDS_PATH = "sounds/";

	private String voicePath;

	private Button btn_play_normal;

	String filePath = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fragment = this;
		context = getActivity();
		initData();
		play();
	}

	private void play() {
		MediaPlayUtil.getInstance().play(filePath);
	}

	private void initData() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			if (bundle.containsKey("modelWord")) {
				LGModelWord modelWord = (LGModelWord) bundle
						.getSerializable("modelWord");
				voicePath = modelWord.getVoicePath();
				title = modelWord.getTitle();// 得到title
				answerList = modelWord.getAnswerList();// 得到答案集合

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
		contentView = inflater.inflate(R.layout.fragment_lesson_word_input,
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// task.RunWithMsg(getActivity(), new LoadNoticesThread(),
		// "Learn is loading…");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MediaPlayUtil.getInstance().release();
		MediaPlayUtil.getInstance().stop();

	}

	/**
	 * 初始化
	 */
	public void init() {
		btn_play_normal = (Button) contentView
				.findViewById(R.id.btn_play_normal);
		btn_play_normal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				play();
			}
		});
		txt_name = (TextView) contentView.findViewById(R.id.txt_name);
		et_input = (EditText) contentView.findViewById(R.id.et_input);
		txt_name.setText(title);
		et_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				answerChanged = s.toString();
				if (!"".equals(answerChanged)) {
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
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public boolean isRight() {

		String stringFilter = UiUtil.StringFilter(answerChanged);
		System.out.println("333333" + answerList.size());
		System.out.println("333333" + stringFilter);
		for (int i = 0; i < answerList.size(); i++) {
			System.out.println("444444" + answerList.get(i));
			if (answerList.get(i).equals(stringFilter)) {
				return true;
			}
		}
		return false;
	}

}
