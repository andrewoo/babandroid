package com.hw.chineseLearn.tabDiscover;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.db.CharPartDao;
import com.hw.chineseLearn.db.CharacterDao;
import com.hw.chineseLearn.model.CharPartBaseModel;
import com.hw.chineseLearn.model.CharacterBaseModel;
import com.util.svgandroid.SVGParser;
import com.util.tool.PathView;
import com.util.tool.UiUtil;

/**
 * 笔画练习
 * 
 * @author yh
 */
public class StrokesOrderExerciseActivity extends BaseActivity {

	private String TAG = "==StrokesOrderExerciseActivity==";
	private Context context;
	private String title = "";
	View contentView;
	CharPartDao charPartDao;
	CharacterDao characterDao;
	List<CharPartBaseModel> CharPartList = new ArrayList<CharPartBaseModel>();
	List<CharacterBaseModel> CharacterList = new ArrayList<CharacterBaseModel>();
	com.util.tool.PathView pv;
	// com.util.tool.SVGView pv;
	private LinearLayout root_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_strokes_order_exercise, null);
		setContentView(contentView);
		CustomApplication.app.addActivity(this);
		context = this;
		charPartDao = new CharPartDao(this);
		characterDao = new CharacterDao(this);
		Bundle budle = this.getIntent().getExtras();
		if (budle != null) {
			if (budle.containsKey("title")) {
				title = budle.getString("title");
			}
		}
		screenWidth = CustomApplication.app.displayMetrics.widthPixels;
		screenHeight = CustomApplication.app.displayMetrics.heightPixels;
		init();
	}

	SVGParser SVGParser;
	LinearLayout lin_mizige;
	int screenWidth, screenHeight;
	Path path;

	/**
	 * 初始化
	 */
	public void init() {
		root_view = (LinearLayout) contentView.findViewById(R.id.root_view);
		lin_mizige = (LinearLayout) contentView.findViewById(R.id.lin_mizige);
		int mizigeWidth = screenWidth - (UiUtil.dip2px(context, 60));
		LayoutParams ly = lin_mizige.getLayoutParams();
		ly.width = mizigeWidth;
		ly.height = mizigeWidth;
		lin_mizige.setLayoutParams(ly);
		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				title, View.GONE, View.GONE, 0);

		String whereStr = " where CharId = 116 ";
		CharPartList = charPartDao.getCharPartVo(whereStr);
		CharacterList = characterDao.getCharacterVo(whereStr);
		Log.d(TAG, "CharPartList.size()" + CharPartList.size());
		Log.d(TAG, "CharacterList.size()" + CharacterList.size());
		pv = (PathView) contentView.findViewById(R.id.pathView);
		String charPathStr="M427.3,316.4c-5.5,28.9-18,50-37.5,63.3c-19.6,13.3-23.1,10.9-10.5-7c12.5-17.9,20.7-43.7,24.6-77.3c3.9-33.6,1.5-56.3-7-68c-8.6-11.7-8.6-17.2,0-16.4c8.6,0.8,17.2,2.7,25.8,5.9c8.6,3.1,21.1,2.7,37.5-1.2c16.4-3.9,28.5-9.4,36.3-16.4c7.8-7,16-8.2,24.6-3.5c8.6,4.7,17.6,9.8,27,15.2c9.4,5.5,7.8,12.5-4.7,21.1c-12.5,8.6-19.2,27-19.9,55.1c-0.8,28.1,5.1,43,17.6,44.5c12.5,1.6,23-0.4,31.6-5.9c8.6-5.5,18.8-3.1,30.5,7c11.7,10.2,9,17.6-8.2,22.3c-17.2,4.7-36.7,5.9-58.6,3.5c-21.9-2.3-33.6-14.4-35.2-36.3c-1.6-21.9-1.6-43,0-63.3c1.5-20.3-1.2-30.8-8.2-31.6c-7-0.8-26.2,2.7-57.4,10.5C435.5,261.3,432.8,287.5,427.3,316.4z";
//		= CharacterList.get(0).getCharPath();
//		Log.v(TAG, "" + charPathStr);
		SVGParser = new SVGParser();
		// pv.setFillAfter(true);
		pv.setFill(true);
		pv.setFillColor(context.getResources().getColor(R.color.mid_grey));
		pv.setPercentage(1.0f);
		pv.getPathAnimator().delay(1000).duration(4000)
				.interpolator(new BounceInterpolator())
				.listenerStart(new PathView.AnimatorBuilder.ListenerStart() {
					@Override
					public void onAnimationStart() {
						Log.e("TAG", "start");
					}
				}).listenerEnd(new PathView.AnimatorBuilder.ListenerEnd() {
					@Override
					public void onAnimationEnd() {
						Log.e("TAG", "end");
					}
				}).start();

		path = com.util.svgandroid.SVGParser.parsePath(charPathStr);
		pv.setPath(path);

	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app
						.finishActivity(StrokesOrderExerciseActivity.this);
				break;

			default:
				break;
			}
		}
	};

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

	}

}
