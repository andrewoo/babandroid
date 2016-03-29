package com.hw.chineseLearn.tabLearn;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.hw.chineseLearn.model.LearnUnitBaseModel;
import com.util.thread.ThreadWithDialogTask;

/**
 * 学习-选图片
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LearnImageSelectFragment extends BaseFragment implements
		OnClickListener {
	private View contentView;// 主view

	private ThreadWithDialogTask task;
	public static LearnImageSelectFragment fragment;
	Context context;
	LearnImageSelectAdapter learnImageSelectAdapter;
	ArrayList<LearnUnitBaseModel> listBase = new ArrayList<LearnUnitBaseModel>();
	GridView gv_image;
	TextView txt_name;
	String question = "\"color\"";
	private List<LGWord> lgWordList = new ArrayList<LGWord>();
	private LGModelWord modelWord;//存放当前题

	private int answer;// 此题的答案lgword.getanswer
	private boolean isRight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fragment = this;
		context = getActivity();
		initDBdata();// 初始化数据库数据

		LearnUnitBaseModel modelBase1 = new LearnUnitBaseModel();
		modelBase1.setIconResSuffix("lu1_1_1");
		modelBase1.setUnitName("Basics1");
		modelBase1.setLessonList("1;");
		listBase.add(modelBase1);

		LearnUnitBaseModel modelBase2 = new LearnUnitBaseModel();
		modelBase2.setIconResSuffix("lu0_1_2");
		modelBase2.setUnitName("Basics2");
		modelBase2.setLessonList("1;2;3;4");
		listBase.add(modelBase2);

		LearnUnitBaseModel modelBase3 = new LearnUnitBaseModel();
		modelBase3.setIconResSuffix("lu0_1_3");
		modelBase3.setUnitName("Basics3");
		modelBase3.setLessonList("1;2;3");
		listBase.add(modelBase3);

		LearnUnitBaseModel modelBase4 = new LearnUnitBaseModel();
		modelBase4.setIconResSuffix("lu0_1_4");
		modelBase4.setUnitName("Color");
		modelBase4.setLessonList("1;2;3");
		listBase.add(modelBase4);

	}

	/**
	 * 得到数据库中数据
	 */
	private void initDBdata() {
		modelWord = (LGModelWord) getArguments()
				.getSerializable("modelWorld");
		String title = modelWord.getTitle();
		question=title;
		// 根据lessonRepeatRegex.getId查询LGword得到Translations
//		try {
//			LGWord lgWord = (LGWord) MyDao.getDao(LGWord.class).queryForId(
//					lessonRepeatRegex.getLgTableId());
//			LGModel_Word_010 Word_010 = (LGModel_Word_010) MyDao
//					.getDao(LGModel_Word_010.class).queryBuilder().where()
//					.eq("WordId", lgWord.getWordId()).queryForFirst();
//			String imageOptions = Word_010.getImageOptions();
//			answer = Word_010.getAnswer();
//			String[] splitFenHao = imageOptions.split(";");// 得到4个选项
//			for (int i = 0; i < splitFenHao.length; i++) {
//				LGWord lGWord = (LGWord) MyDao.getDao(LGWord.class).queryForId(
//						splitFenHao[i]);
//				String mainPicName = lGWord.getWordId() + "-"
//						+ lGWord.getMainPic();
//				lGWord.setMainPic(mainPicName);// 把数据库名字和文件中图片名字对应
//				lgWordList.add(lGWord);
//			}
//			Collections.shuffle(lgWordList);//打乱
//			question = lgWord.getTranslations();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
			int wordId = modelWord.getSubLGModelList().get(position).getWordId();
			if (wordId == answer) {
				isRight = true;
			}else{
				isRight=false;
			}
			learnImageSelectAdapter.notifyDataSetChanged();
			LearnUnitBaseModel learnUnitBaseModel = listBase.get(position);
			if (learnUnitBaseModel != null) {
			}
		}
	};

	public boolean isRight() {
		return isRight;
	}

}
