package com.hw.chineseLearn.tabDiscover;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.FluentAddLessonAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.model.LearnUnitBaseModel;
import com.util.weight.PullToRefreshView;
import com.util.weight.PullToRefreshView.OnFooterRefreshListener;
import com.util.weight.PullToRefreshView.OnRefreshTouchListener;
import com.util.weight.RoundProgressBar;

/**
 * 添加流畅练习课程
 * 
 * @author yh
 */
public class FluentAddLessonActivity extends BaseActivity {

	private String TAG = "==FluentAddLessonActivity==";
	private Context context;
	View contentView;
	ListView lv_add_lesson;
	FluentAddLessonAdapter adapter;
	ArrayList<LearnUnitBaseModel> listBase = new ArrayList<LearnUnitBaseModel>();
	// 列表-上下拉刷新
	private PullToRefreshView pullToRefreshView;
	private int colorWhite = 0;
	private int colorGrey = 0;

	private LinearLayout[] mLinList;
	private Button[] mBtnList;

	private int[] leftLinIds;
	private int[] leftBtnIds;
	int selectIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_fluent_add_lesson, null);
		setContentView(contentView);
		CustomApplication.app.addActivity(this);
		context = this;
		colorWhite = context.getResources().getColor(R.color.white);
		colorGrey = context.getResources().getColor(
				R.color.chinese_skill_activity_bg);
		init();
	}

	/**
	 * 初始化
	 */
	public void init() {

		leftLinIds = new int[] { R.id.lin_level1, R.id.lin_level2,
				R.id.lin_level3 };
		leftBtnIds = new int[] { R.id.rb_level1, R.id.rb_level2, R.id.rb_level3 };

		mLinList = new LinearLayout[leftLinIds.length];
		mBtnList = new Button[leftBtnIds.length];

		for (int i = 0; i < leftLinIds.length; i++) {
			mLinList[i] = (LinearLayout) contentView
					.findViewById(leftLinIds[i]);
			mLinList[i].setOnClickListener(onClickListener);
		}

		for (int i = 0; i < leftBtnIds.length; i++) {
			mBtnList[i] = (Button) contentView.findViewById(leftBtnIds[i]);
			mBtnList[i].setClickable(false);
		}
		setLeftBtnColor(selectIndex);

		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Add Lesson", View.GONE, View.GONE, 0);
		pullToRefreshView = (PullToRefreshView) contentView
				.findViewById(R.id.pullToRefreshView);
		lv_add_lesson = (ListView) contentView.findViewById(R.id.lv_add_lesson);
		lv_add_lesson.setOnItemClickListener(onItemclickListener);

		LearnUnitBaseModel modelBase1 = new LearnUnitBaseModel();
		modelBase1.setIconResSuffix("ls_catt_16");
		modelBase1.setUnitName("你好！");
		modelBase1.setDescription("Hello!");
		listBase.add(modelBase1);

		LearnUnitBaseModel modelBase2 = new LearnUnitBaseModel();
		modelBase2.setIconResSuffix("ls_catt_8");
		modelBase2.setUnitName("请问邮局在哪儿？");
		modelBase2.setDescription("Where is the post office?");
		listBase.add(modelBase2);

		adapter = new FluentAddLessonAdapter(context, listBase);
		lv_add_lesson.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		pullToRefreshView
				.setOnFooterRefreshListener(new OnFooterRefreshListener() {
					@Override
					public void onFooterRefresh(PullToRefreshView view) {
						view.onFooterRefreshComplete();
						// if (IsEnd) {
						// Toast.makeText(getActivity(), "暂无更多数据",
						// Toast.LENGTH_SHORT).show();
						// } else {
						// task.RunWithMsg(getActivity(),
						// new LoadNoticesThread(), "正在加载…");
						// }
					}
				});

		pullToRefreshView
				.setOnRefreshTouchListener(new OnRefreshTouchListener() {

					@Override
					public void onTouchListener(PullToRefreshView view) {

					}
				});

	}

	private void setLeftBtnColor(int selectIndex) {

		for (int i = 0; i < mLinList.length; i++) {

			if (selectIndex == i) {
				mLinList[i].setBackgroundColor(colorWhite);
				mBtnList[i].setSelected(true);

			} else {
				mLinList[i].setBackgroundColor(colorGrey);
				mBtnList[i].setSelected(false);
			}
		}

	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app
						.finishActivity(FluentAddLessonActivity.this);
				break;

			case R.id.lin_level1:
				selectIndex = 0;
				setLeftBtnColor(0);
				break;

			case R.id.lin_level2:
				selectIndex = 1;
				setLeftBtnColor(1);
				break;

			case R.id.lin_level3:
				selectIndex = 2;
				setLeftBtnColor(2);
				break;

			default:
				break;
			}
		}
	};

	OnItemClickListener onItemclickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View convertView,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			// convertView = adapter.mapView.get(arg2);
			// ImageView img_add_lesson = (ImageView) convertView
			// .findViewById(R.id.img_add_lesson);
			// final RoundProgressBar progress_download = (RoundProgressBar)
			// convertView
			// .findViewById(R.id.progress_download);
			// img_add_lesson.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// // img_add_lesson.setVisibility(View.GONE);
			// progress_download.setVisibility(View.VISIBLE);
			// new Thread(new Runnable() {
			// private int progress = 0;
			//
			// @Override
			// public void run() {
			// while (progress <= 100) {
			// progress += 3;
			// System.out.println(progress);
			// progress_download.setProgress(progress);
			// try {
			// Thread.sleep(100);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// }
			//
			// }
			// }).start();
			// }
			// });

			startActivity(new Intent(FluentAddLessonActivity.this,
					FluentDetailActivity.class));

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
