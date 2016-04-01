package com.hw.chineseLearn.tabLearn;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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
import com.hw.chineseLearn.dao.bean.LGWord;
import com.hw.chineseLearn.dao.bean.LessonRepeatRegex;
import com.hw.chineseLearn.model.LearnUnitBaseModel;
import com.util.thread.ThreadWithDialogTask;
import com.util.tool.MediaPlayerHelper;

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
		mediaPlayerHelper = new MediaPlayerHelper(ASSETS_SOUNDS_PATH+voicePath);
		mediaPlayerHelper.play();
	}

	private void initData() {
		Bundle bundle = getArguments();
		if(bundle.containsKey("modelWorld")){
			modelWorld = (LGModelWord) getArguments().getSerializable("modelWorld");
			voicePath = modelWorld.getVoicePath();
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
		if (mediaPlayerHelper != null) {
			mediaPlayerHelper.stop();
		}
		super.onStop();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// task.RunWithMsg(getActivity(), new LoadNoticesThread(),
		// "Learn is loading…");
	}

	/**
	 * 初始化
	 */
	public void init() {
		txt_name = (TextView) contentView.findViewById(R.id.txt_name);
		txt_name.setText(modelWorld.getTitle());

		listView = (ListView) contentView.findViewById(R.id.list_view);
		adapter = new LearnWordSelectListAdapter(context, modelWorld);
		btn_play_normal = (Button) contentView.findViewById(R.id.btn_play_normal);
		btn_play_normal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mediaPlayerHelper != null) {
					mediaPlayerHelper.play();
				} else {
					mediaPlayerHelper = new MediaPlayerHelper(
							ASSETS_SOUNDS_PATH + voicePath);
					mediaPlayerHelper.play();
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
			answer = modelWorld.getAnswer();
			SubLGModel subLGModel = modelWorld.getSubLGModelList().get(position);
			int wordId = subLGModel.getWordId();
			if (wordId == answer) {
				isRight = true;
			} else {
				isRight = false;
			}
		}
	};

	private LessonRepeatRegex lessonRepeatRegex;

	private LGModelWord modelWorld;
	private String voicePath;
	private Button btn_play_normal;
	private MediaPlayerHelper mediaPlayerHelper;

	public boolean isRight() {
		return isRight;
	}
}
