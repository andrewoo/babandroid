package com.hw.chineseLearn.tabLearn;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.SoundPool;
import android.os.Bundle;
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
import com.hw.chineseLearn.dao.bean.LGWord;
import com.util.thread.ThreadWithDialogTask;
import com.util.tool.MediaPlayerHelper;

/**
 * 学习-选图片
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LearnImageSelectFragment extends BaseFragment implements
		OnClickListener {
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
		// play();
	}

	private void play() {
		// mediaPlayerHelper = new MediaPlayerHelper("");
		// mediaPlayerHelper.play();

		// soundPool = new SoundPool(5,AudioManager.STREAM_MUSIC, 5);
		// AssetManager am = CustomApplication.app.getAssets();
		// AssetFileDescriptor afd = null;
		// try {
		// afd = am.openFd("w-11-104372091955652092.mp3");
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// int load = soundPool.load(afd, 1);
		// soundPool.play(load, 1, 1, 0, 0, 1);

	}

	/**
	 * 得到数据库中数据
	 */
	private void initDBdata() {
		modelWord = (LGModelWord) getArguments().getSerializable("modelWorld");
		voicePath = modelWord.getVoicePath();
		String title = modelWord.getTitle();
		question = title;
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

			meidiaPlayer = new MediaPlayerHelper(ASSETS_SOUNDS_PATH
					+ modelWord.getSubLGModelList().get(position)
							.getSubVoicePath());
//			meidiaPlayer.setLoad(ASSETS_SOUNDS_PATH
//					+ modelWord.getSubLGModelList().get(position)
//							.getSubVoicePath());
			meidiaPlayer.play();

			learnImageSelectAdapter.notifyDataSetChanged();
		}
	};

	private String voicePath;

	private SoundPool soundPool;

	private MediaPlayerHelper mediaPlayerHelper;

	public boolean isRight() {
		return isRight;
	}

}
