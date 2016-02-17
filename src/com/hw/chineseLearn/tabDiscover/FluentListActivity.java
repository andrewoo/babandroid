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
import android.widget.ListView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.FluentNowAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.model.LearnUnitBaseModel;

/**
 * 流畅练习
 * 
 * @author yh
 */
public class FluentListActivity extends BaseActivity {

	private String TAG = "==FluentActivity==";
	private Context context;
	View contentView;
	ListView lv_fluent_list;
	FluentNowAdapter adapter;
	ArrayList<LearnUnitBaseModel> listBase = new ArrayList<LearnUnitBaseModel>();

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
				"Fluent Now", View.GONE, View.GONE, 0);
		View footerView = LayoutInflater.from(context).inflate(
				R.layout.layout_fluent_list_footer, null);
		lv_fluent_list = (ListView) contentView
				.findViewById(R.id.lv_fluent_list);
		lv_fluent_list.setOnItemClickListener(onItemclickListener);
		lv_fluent_list.addFooterView(footerView);

		footerView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//
				startActivity(new Intent(FluentListActivity.this,
						FluentAddLessonActivity.class));
			}
		});

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

		adapter = new FluentNowAdapter(context, listBase);
		lv_fluent_list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app.finishActivity(FluentListActivity.this);
				break;

			default:
				break;
			}
		}
	};

	OnItemClickListener onItemclickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

			startActivity(new Intent(FluentListActivity.this,
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
