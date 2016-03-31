package com.hw.chineseLearn.tabLearn;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.util.thread.ThreadWithDialogTask;
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
	private List<String> answerList=new ArrayList<String>();

	private String title;

	private TextView txt_name;

	private EditText et_input;

	private String answerChanged;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fragment = this;
		context = getActivity();
		initData();
	}

	private void initData() {
		Bundle bundle = getArguments(); 
		if(bundle!=null){
			if(bundle.containsKey("modelWord")){
				LGModelWord modelWord=(LGModelWord) bundle.getSerializable("modelWord");
				title = modelWord.getTitle();//得到title
				answerList = modelWord.getAnswerList();//得到答案集合
			} 
		}
		
		
//		LessonRepeatRegex lessonRepeatRegex = (LessonRepeatRegex) getArguments().getSerializable("lessonRepeatRegex");
//		//根据lgid查询，lgword,lgword040表, lgword表 wrod pinyin,lgword040 得到answer 切割后为答案
//		//check得到输入框内容 如果为上边之一及正确
//		int lgTableId = lessonRepeatRegex.getLgTableId();
//		try {
//			LGWord lgWord = (LGWord) MyDao.getDao(LGWord.class).queryForId(lgTableId);
//			title = lgWord.getWord()+"/"+lgWord.getPinyin();
//			Map<String, Integer> wordIdMap=new HashMap<String, Integer>();
//			wordIdMap.put("WordId", lgTableId);
//			LGModel_Word_040 word040 = (LGModel_Word_040) MyDao.getDao(LGModel_Word_040.class).queryForFieldValues(wordIdMap).get(0);
//			System.out.println("word040"+word040);
//			String answer = word040.getAnswers();
//			 if(answer.indexOf("!@@@!")!=-1){
//				 String[] splitAnswer = answer.split("!@@@!");
//				 for (int i = 0; i < splitAnswer.length; i++) {
//					 answerList.add(splitAnswer[i]);
//				}
//			 }
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
	public void onStop() {
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
		et_input = (EditText) contentView.findViewById(R.id.et_input);
		txt_name.setText(title);
		et_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				answerChanged = s.toString();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});}

	@Override
	public boolean isRight() {
		
		String stringFilter = UiUtil.StringFilter(answerChanged);
		System.out.println("333333"+answerList.size());
		System.out.println("333333"+stringFilter);
		for (int i = 0; i < answerList.size(); i++) {
			System.out.println("444444"+answerList.get(i));
			if(answerList.get(i).equals(stringFilter)){
				return true;
			}
		}
		return false;
	}

}
