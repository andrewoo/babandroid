package com.hw.chineseLearn.tabLearn;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;

/**
 * FlashCard页面
 * 
 * @author yh
 */
public class LessonFlashCardActivity extends BaseActivity {

	private String TAG = "==LessonFlashCardActivity==";
	public Context context;
	private Button btn_go;
	private Resources resources;
	private int width;
	private int height;
	View contentView;
	int characterCount = 0;
	int wordsCount = 0;
	int sentenceCount = 0;
	int chooseCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_flashcard, null);
		setContentView(contentView);
		context = this;
		CustomApplication.app.addActivity(this);
		super.gestureDetector();
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 5;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 5;
		resources = context.getResources();
		init();

	}

	boolean isCharacterChecked = false;
	boolean isSentenceChecked = false;
	boolean isWordChecked = false;
	boolean isAutoPlay = true;

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {


	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
