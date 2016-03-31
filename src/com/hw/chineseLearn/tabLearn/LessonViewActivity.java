package com.hw.chineseLearn.tabLearn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.GalleryAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.Lesson;
import com.hw.chineseLearn.dao.bean.LessonRepeatRegex;
import com.hw.chineseLearn.dao.bean.TbLessonMaterialStatus;
import com.hw.chineseLearn.dao.bean.Unit;
import com.util.weight.MyGallery;

/**
 * 课程总览页面
 * 
 * @author yh
 */
public class LessonViewActivity extends BaseActivity implements
		OnItemSelectedListener {

	protected static final int LESSON_QUERY = 100;
	protected static final int TB_LESSON = 200;
	private String TAG = "==LessonViewActivity==";
	public Context context;
	private MyGallery gallery;// CoverFlow
	private GalleryAdapter adapter;
	private ImageView iv_unit_img;
	View contentView;
	int width;
	int height;
	AnimationSet animationSet;
	int selection = 0;
	private List<TbLessonMaterialStatus> lessonStatusList;// tbLesson表
	private Unit mUnit;// 当前Unit列
	private List<Lesson> lessonList;// lesson表

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_view, null);
		setContentView(contentView);
		context = this;
		mUnit = (Unit) getIntent().getSerializableExtra("unit");
		queryLesson();// 查询lesson表
		queryTbLesson();// 查询tbLesson得到状态List
		init();// 初始化gallery和动画

		CustomApplication.app.addActivity(this);
		super.gestureDetector();

		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 5;

	}

	/**
	 * 滑动时调用,得到当前lesson表的repeatRegex对应的beanList
	 * 
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	private List<LessonRepeatRegex> getRepeatRegexBeanList() {
		try {
			lesson=(Lesson) MyDao.getDao(Lesson.class).queryForId(mUnit.getLessonList().split(";")[selection]);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] questions = lesson.getRepeatRegex().split("#");
		List<LessonRepeatRegex> regexes = new ArrayList<LessonRepeatRegex>();
		List<LessonRepeatRegex> subRegexes = new ArrayList<LessonRepeatRegex>();
		for (int i = 0; i < questions.length; i++) {// 遍历把值加入到实体beanList
			LessonRepeatRegex regex = new LessonRepeatRegex();
			String[] splitFenHao = questions[i].split(";");
			if (splitFenHao[0].indexOf("-") != -1) {
				String[] splitLgTableId = splitFenHao[0].split("-");
				for (int j = 0; j < Integer.valueOf(splitFenHao[splitFenHao.length - 1]); j++) {
					if (j == 0) {
						regex.setLgTable(Integer.valueOf(splitLgTableId[j]
								.split(":")[0]));
						regex.setLgTableId(Integer.valueOf(splitLgTableId[j]
								.split(":")[1]));
						regex.setCount(Integer
								.valueOf(splitFenHao[splitFenHao.length - 1]));
						subRegexes.add(regex);
					} else {
						LessonRepeatRegex regex1 = new LessonRepeatRegex();
						regex1.setLgTable(Integer.valueOf(splitLgTableId[j]
								.split(":")[0]));
						regex1.setLgTableId(Integer.valueOf(splitLgTableId[j]
								.split(":")[1]));
						regex1.setCount(Integer
								.valueOf(splitFenHao[splitFenHao.length - 1]));
						subRegexes.add(new Random().nextInt(subRegexes.size()),
								regex1);
					}
				}
				Collections.shuffle(subRegexes);// 打乱顺序
				regexes.addAll(subRegexes);
				subRegexes.clear();
				continue;
			}
			String[] splitMaoHao = splitFenHao[0].split(":");
			regex.setLgTable(Integer.valueOf(splitMaoHao[0]));// -分割后：前的数字
			regex.setLgTableId(Integer.valueOf(splitMaoHao[1]));// -分割后：后的数字
			regex.setCount(Integer.valueOf(splitFenHao[splitFenHao.length - 1]));// 得到最后一个字符
			regexes.add(regex);
		}
		return regexes;
	}

	/**
	 * 查询TbLesson----表
	 */
	private void queryTbLesson() {
		try {
			lessonStatusList = MyDao.getDao(TbLessonMaterialStatus.class)
					.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(lessonStatusList);
	}

	/**
	 * 查询lesson表
	 */
	private void queryLesson() {
		try {
			lessonList = MyDao.getDao(Lesson.class).queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(lessonList);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * 初始化
	 */
	public void init() {

		setTitle(View.GONE, View.VISIBLE,
				R.drawable.btn_selector_top_left_white, mUnit.getUnitName(),
				View.GONE, View.GONE, 0);
		adapter = new GalleryAdapter(this, mUnit, lessonList, lessonStatusList);
		gallery = (MyGallery) findViewById(R.id.gallery);
		gallery.setOnItemClickListener(onItemclickListener);
		gallery.setAdapter(adapter);
		gallery.setAnimationDuration(1500);
		gallery.setSpacing(CustomApplication.app.displayMetrics.widthPixels / 10 * 1);
		gallery.setOnItemSelectedListener(this);
		gallery.setSelection(selection);
		iv_unit_img = (ImageView) findViewById(R.id.iv_unit_img);
		iv_unit_img.setOnClickListener(onClickListener);

		// 创建一个AnimationSet对象（AnimationSet是存放多个Animations的集合）
		animationSet = new AnimationSet(true);

		ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.1f, 1, 0.1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// 设置动画执行之前等待的时间（单位：毫秒）
		scaleAnimation.setStartOffset(1000);
		// 设置动画执行的时间（单位：毫秒）
		scaleAnimation.setDuration(2000);
		// 如果fillAfter设为true，则动画执行后，控件将停留在动画结束的状态
		animationSet.setFillAfter(true);
		// 将ScaleAnimation对象添加到AnimationSet当中
		animationSet.addAnimation(scaleAnimation);
		animationSet.setRepeatCount(3);
		animationSet.setRepeatMode(Animation.REVERSE);
		// 使用ImageView的startAnimation方法开始执行动画
		// iv_unit_img.startAnimation(animationSet);
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
		view_title.setBackgroundColor(context.getResources().getColor(
				R.color.chinese_skill_blue));
		Button tv_title = (Button) view_title.findViewById(R.id.btn_title);
		tv_title.setText(title);
		tv_title.setTextColor(context.getResources().getColor(R.color.white));
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

	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回
				CustomApplication.app.finishActivity(LessonViewActivity.this);
				break;

			case R.id.iv_unit_img:

				break;

			default:
				break;
			}
		}
	};

	OnItemClickListener onItemclickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View convertView,
				final int position, long arg3) {
			// TODO Auto-generated method stub

		}
	};
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// if (msg.arg1 == 1) {
			//
			// } else if (msg.arg1 == 2) {
			//
			// } else if (msg.arg1 == 4) {
			//
			// if (msg.what == 1) {
			//
			// for (Entry<Integer, View> entry : adapter.mapView
			// .entrySet()) {
			//
			// int position = entry.getKey();
			// if (position == msg.arg2) {
			// View views = entry.getValue();
			// LinearLayout lin_c = (LinearLayout) views
			// .findViewById(R.id.lin_c);
			// lin_c.setLayoutParams(new LinearLayout.LayoutParams(
			// width, height));
			// Log.d(TAG, "选中！");
			// } else {
			// View views = entry.getValue();
			// LinearLayout lin_c = (LinearLayout) views
			// .findViewById(R.id.lin_c);
			// lin_c.setLayoutParams(new LinearLayout.LayoutParams(
			// LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT));
			// Log.d(TAG, "未选中！");
			// }
			//
			// }
			// }
			// }
		}
	};
	private Lesson lesson;

	@Override
	public void onItemSelected(AdapterView<?> arg0, View convertView,
			final int position, long arg3) {
		// TODO Auto-generated method stub

		selection = position;
		 final ArrayList<LessonRepeatRegex> regexes= (ArrayList<LessonRepeatRegex>) getRepeatRegexBeanList();// 得到正则表达式中所有题型的集合
		Log.d(TAG, "selection:" + selection);
		adapter.setSelection(position);

		// Message message = new Message();
		// message.arg1 = 4;
		// message.arg2 = position;
		// message.what = 1;
		// mHandler.sendMessageDelayed(message, 5);
		if(convertView==null){
			convertView=View.inflate(this, R.layout.layout_gellay_item, null);
			}
		Button btn_redo = (Button) convertView.findViewById(R.id.btn_redo);
		Button btn_start = (Button) convertView.findViewById(R.id.btn_start);
		Button btn_review = (Button) convertView.findViewById(R.id.btn_review);

		btn_redo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,
						LessonExerciseActivity.class);
				intent.putExtra("utilId", position);
				intent.putExtra("regexes", regexes);
				intent.putExtra("LessonId", lesson.getLessonId());
				startActivityForResult(intent, 0);
				Log.d("GalleryAdapter", "utilId:" + position);
			}
		});
		btn_start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,
						LessonExerciseActivity.class);
				intent.putExtra("utilId", position);
				intent.putExtra("regexes", regexes);
				intent.putExtra("LessonId", lesson.getLessonId());
				startActivityForResult(intent, 0);
				Log.d("GalleryAdapter", "utilId:" + position);
			}
		});

		btn_review.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(context, LessonReViewActivity.class));
			}
		});

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		switch (resultCode) {
		case 0:// LessonExerciseActivity 终止练习
			gallery.setSelection(selection);// 选中当前页面
			break;
		case 1:// LessonExerciseActivity通过课程或者 continue要继续练习

			if (selection == 1) {
				gallery.setSelection(selection);// for bug
			} else {
				gallery.setSelection(selection + 1);// 选中下一个页面
			}
			break;
		default:
			break;
		}
		Log.d(TAG, "resultCode:" + resultCode);
	};

}
