package com.hw.chineseLearn.tabDiscover;

import java.util.ArrayList;

import android.content.Context;
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
import com.hw.chineseLearn.adapter.FluentDetailAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.model.LearnUnitBaseModel;
import com.util.weight.PullToRefreshView;
import com.util.weight.PullToRefreshView.OnFooterRefreshListener;
import com.util.weight.PullToRefreshView.OnRefreshTouchListener;
import com.util.weight.RoundProgressBar;

/**
 * 流畅练习对话课程
 * 
 * @author yh
 */
public class FluentDetailActivity extends BaseActivity {

	private String TAG = "==FluentDetailActivity==";
	private Context context;
	View contentView;
	ListView lv_add_lesson;
	FluentDetailAdapter adapter;
	ArrayList<LearnUnitBaseModel> listBase = new ArrayList<LearnUnitBaseModel>();
	// 列表-上下拉刷新
	int selectIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_fluent_now, null);
		setContentView(contentView);
		CustomApplication.app.addActivity(this);
		context = this;
		init();
	}

	/**
	 * 初始化
	 */
	public void init() {

		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Add Lesson", View.GONE, View.GONE, 0);
		lv_add_lesson = (ListView) contentView
				.findViewById(R.id.lv_fluent_list);
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

		LearnUnitBaseModel modelBase3 = new LearnUnitBaseModel();
		modelBase3.setIconResSuffix("ls_catt_16");
		modelBase3.setUnitName("你好！");
		modelBase3.setDescription("Hello!");
		listBase.add(modelBase3);

		LearnUnitBaseModel modelBase4 = new LearnUnitBaseModel();
		modelBase4.setIconResSuffix("ls_catt_8");
		modelBase4.setUnitName("请问邮局在哪儿？");
		modelBase4.setDescription("Where is the post office?");
		listBase.add(modelBase4);

		adapter = new FluentDetailAdapter(context, listBase);
		lv_add_lesson.setAdapter(adapter);
		adapter.notifyDataSetChanged();

	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app.finishActivity(FluentDetailActivity.this);
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
