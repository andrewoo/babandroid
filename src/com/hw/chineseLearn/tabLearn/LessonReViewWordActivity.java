package com.hw.chineseLearn.tabLearn;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Telephony.Sms.Conversations;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.ReviewWordListAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.LGWord;
import com.hw.chineseLearn.dao.bean.TbMyCharacter;
import com.hw.chineseLearn.dao.bean.TbMySentence;
import com.hw.chineseLearn.dao.bean.TbMyWord;
import com.hw.chineseLearn.model.LearnUnitBaseModel;

/**
 * 课程复习-偏旁部首-页面
 * 
 * @author yh
 */
public class LessonReViewWordActivity extends BaseActivity {

	private String TAG = "==LessonReViewWordActivity==";
	public Context context;
	private ListView listView;
	private TextView tv_translations;
	private Resources resources;
	private int width;
	private int height;
	View contentView;
	ReviewWordListAdapter reviewListAdapter;
	ArrayList<LGWord> listBase = new ArrayList<LGWord>();
	ArrayList<TbMyWord> tbMyWordList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_review_word, null);
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

		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Word Review", View.GONE, View.VISIBLE, R.drawable.revie_pen);

		listView = (ListView) contentView.findViewById(R.id.list_view);
		tv_translations = (TextView) contentView
				.findViewById(R.id.tv_translations);
		try {
			tbMyWordList = (ArrayList<TbMyWord>) MyDao.getDaoMy(TbMyWord.class)
					.queryForAll();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < tbMyWordList.size(); i++) {
			TbMyWord model = tbMyWordList.get(i);
			if (model == null) {
				continue;
			}
			int wordId = model.getWordId();
			try {
				LGWord lGWord = (LGWord) MyDao.getDao(LGWord.class).queryForId(
						wordId);
				listBase.add(lGWord);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		reviewListAdapter = new ReviewWordListAdapter(context, listBase);
		listView.setAdapter(reviewListAdapter);
		listView.setOnItemClickListener(onItemclickListener);
		reviewListAdapter.notifyDataSetChanged();
		setTranslations(0);
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
						.finishActivity(LessonReViewWordActivity.this);
				break;

			case R.id.iv_title_right://
				startActivity(new Intent(LessonReViewWordActivity.this,
						LessonFlashCardActivity.class));
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

	/**
	 * @param position
	 */
	private void setTranslations(int position) {
		LGWord model = listBase.get(position);
		if (model != null) {
			String translations = model.getTranslations();
			tv_translations.setText(translations);
		}
	}
}
