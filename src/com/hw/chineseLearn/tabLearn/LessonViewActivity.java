package com.hw.chineseLearn.tabLearn;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.GalleryAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.util.weight.CoverFlow;
import com.util.weight.MyGallery;

/**
 * 课程总览页面
 * 
 * @author yh
 */
public class LessonViewActivity extends BaseActivity implements
		OnItemSelectedListener {

	private String TAG = "==LessonViewActivity==";
	public Context context;
	private MyGallery gallery;// CoverFlow
	private GalleryAdapter adapter;
	private ImageView iv_unit_img;
	View contentView;
	int width;
	int height;
	AnimationSet animationSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_lesson_view, null);
		setContentView(contentView);
		context = this;
		init();
		CustomApplication.app.addActivity(this);
		super.gestureDetector();

		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 5;

	}

	/**
	 * 初始化
	 */
	public void init() {

		setTitle(View.GONE, View.VISIBLE,
				R.drawable.btn_selector_top_left_white, "Lesson", View.GONE,
				View.GONE, 0);

		gallery = (MyGallery) findViewById(R.id.gallery);
		adapter = new GalleryAdapter(this);
		gallery.setAdapter(adapter);
		gallery.setAnimationDuration(1500);
		gallery.setSpacing(CustomApplication.app.displayMetrics.widthPixels / 10 * 1);
		gallery.setOnItemSelectedListener(this);

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
		iv_unit_img.startAnimation(animationSet);
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

	@Override
	public void onItemSelected(AdapterView<?> arg0, View convertView,
			int position, long arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
