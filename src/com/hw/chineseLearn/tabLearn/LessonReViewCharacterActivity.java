package com.hw.chineseLearn.tabLearn;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.ReviewCharListAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.LGCharacter;
import com.hw.chineseLearn.dao.bean.TbMyCharacter;
import com.util.tool.BitmapLoader;
import com.util.tool.UiUtil;

/**
 * 课程复习-偏旁部首-页面
 * 
 * @author yh
 */
public class LessonReViewCharacterActivity extends BaseActivity {

	private String TAG = "==LessonReViewCharacterActivity==";
	public Context context;
	private ListView listView;
	private Resources resources;
	private int mizigeWidth;
	private int mizigeHeight;
	View contentView;
	private TextView tv_pinyin;
	private TextView tv_translation;
	private ImageView img_record, img_loop;
	RelativeLayout rel_mizige;

	ReviewCharListAdapter reviewListAdapter;
	ArrayList<LGCharacter> listBase = new ArrayList<LGCharacter>();
	ArrayList<TbMyCharacter> tbMyCharList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_review_character, null);
		setContentView(contentView);
		context = this;
		CustomApplication.app.addActivity(this);
		super.gestureDetector();
		mizigeWidth = (CustomApplication.app.displayMetrics.widthPixels / 10 * 5)
				- UiUtil.dip2px(getApplicationContext(), 20);// padding =20dp
		mizigeHeight = mizigeWidth;
		resources = context.getResources();
		init();
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {

		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Character Review", View.GONE, View.VISIBLE,
				R.drawable.revie_pen);
		rel_mizige = (RelativeLayout) contentView.findViewById(R.id.rel_mizige);
		tv_pinyin = (TextView) contentView.findViewById(R.id.tv_pinyin);
		tv_translation = (TextView) contentView
				.findViewById(R.id.tv_translation);
		img_record = (ImageView) contentView.findViewById(R.id.img_record);
		img_loop = (ImageView) contentView.findViewById(R.id.img_loop);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				mizigeWidth, mizigeWidth);
		rel_mizige.setLayoutParams(layoutParams);

		listView = (ListView) contentView.findViewById(R.id.list_view);

		try {
			tbMyCharList = (ArrayList<TbMyCharacter>) MyDao.getDaoMy(
					TbMyCharacter.class).queryForAll();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < tbMyCharList.size(); i++) {
			TbMyCharacter model = tbMyCharList.get(i);
			if (model == null) {
				continue;
			}
			int charId = model.getCharId();
			try {
				LGCharacter lGCharacter = (LGCharacter) MyDao.getDao(
						LGCharacter.class).queryForId(charId);
				listBase.add(lGCharacter);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		reviewListAdapter = new ReviewCharListAdapter(context, listBase);
		listView.setAdapter(reviewListAdapter);
		listView.setOnItemClickListener(onItemclickListener);
		reviewListAdapter.notifyDataSetChanged();
		setPartAnswer(0);
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
						.finishActivity(LessonReViewCharacterActivity.this);
				break;

			case R.id.iv_title_right://
				Intent intent = new Intent(LessonReViewCharacterActivity.this,
						LessonReviewExerciseActivity.class);
				intent.putExtra("lgTable", 2);
				startActivity(intent);
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
			setPartAnswer(position);
		}
	};
	private static final String ASSETS_LGCHARACTERPART_PATH = "data/character_part/";

	/**
	 * @param position
	 */
	private void setPartAnswer(int position) {
		if (listBase != null && listBase.size() != 0) {

			LGCharacter model = listBase.get(position);
			if (model != null) {
				String partAnswer = model.getPartAnswer();
				Log.d(TAG, "partAnswer:" + partAnswer);
				// 772;773;
				String pngs[] = UiUtil.getListFormString(partAnswer);
				rel_mizige.removeAllViews();
				for (int i = 0; i < pngs.length; i++) {

					String picName = pngs[i];

					if ("".equals(picName))
						continue;

					picName = "pp-" + picName + ".png";

					Bitmap imageFromAssetsFile = BitmapLoader
							.getImageFromAssetsFile(ASSETS_LGCHARACTERPART_PATH
									+ picName);
					ImageView imgageView = new ImageView(context);
					imgageView.setImageBitmap(imageFromAssetsFile);
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
							mizigeWidth, mizigeWidth);
					imgageView.setLayoutParams(layoutParams);

					rel_mizige.addView(imgageView);

				}
				String pinyin = model.getPinyin();
				tv_pinyin.setText(pinyin);

				String translation = model.getTranslation();
				tv_translation.setText(translation);
			}

		} else {
			Log.e(TAG, "listBase == null || listBase.size() == 0");
		}
	}
}
