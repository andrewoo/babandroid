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
import com.hw.chineseLearn.adapter.SurvivalKitAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.model.LearnUnitBaseModel;

/**
 * 生存模式
 * 
 * @author yh
 */
public class SurvivalKitActivity extends BaseActivity {

	private String TAG = "==SurvivalKitActivity==";
	private Context context;
	View contentView;
	ListView lv_survival;
	SurvivalKitAdapter adapter;
	ArrayList<LearnUnitBaseModel> listBase = new ArrayList<LearnUnitBaseModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_survival_kit, null);
		setContentView(contentView);
		CustomApplication.app.addActivity(this);
		context = this;
		init();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (CustomApplication.app.favouriteList.size() != 0) {
			LearnUnitBaseModel modelBase = new LearnUnitBaseModel();
			modelBase.setIconResSuffix("favorite_img");
			modelBase.setUnitName("Favourite");
			listBase.set(0, modelBase);
		} else {
			for (int i = 0; i < listBase.size(); i++) {
				LearnUnitBaseModel modelBase = listBase.get(i);

				if (modelBase == null) {
					continue;
				}
				String unitName = modelBase.getUnitName();
				if ("Favourite".equals(unitName)) {
					listBase.remove(i);
					break;
				}
			}
		}
		adapter.notifyDataSetChanged();

	}

	/**
	 * 初始化
	 */
	public void init() {
		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Survival Kit", View.GONE, View.GONE, 0);
		lv_survival = (ListView) contentView.findViewById(R.id.lv_survival);
		lv_survival.setOnItemClickListener(onItemclickListener);
		initDatas();
		adapter = new SurvivalKitAdapter(context, listBase);
		lv_survival.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void initDatas() {
		LearnUnitBaseModel modelBase1 = new LearnUnitBaseModel();
		modelBase1.setIconResSuffix("survival_0_0_1");
		modelBase1.setUnitName("Basics");
		listBase.add(modelBase1);

		LearnUnitBaseModel modelBase2 = new LearnUnitBaseModel();
		modelBase2.setIconResSuffix("survival_0_1_1");
		modelBase2.setUnitName("Flirting");
		listBase.add(modelBase2);

		LearnUnitBaseModel modelBase3 = new LearnUnitBaseModel();
		modelBase3.setIconResSuffix("survival_0_2_1");
		modelBase3.setUnitName("Food");
		listBase.add(modelBase3);

		LearnUnitBaseModel modelBase4 = new LearnUnitBaseModel();
		modelBase4.setIconResSuffix("survival_1_0_0");
		modelBase4.setUnitName("Emergency");
		listBase.add(modelBase4);

		LearnUnitBaseModel modelBase5 = new LearnUnitBaseModel();
		modelBase5.setIconResSuffix("survival_1_1_0");
		modelBase5.setUnitName("Health");
		listBase.add(modelBase5);

		LearnUnitBaseModel modelBase6 = new LearnUnitBaseModel();
		modelBase6.setIconResSuffix("survival_1_2_0");
		modelBase6.setUnitName("Shopping");
		listBase.add(modelBase6);

		LearnUnitBaseModel modelBase7 = new LearnUnitBaseModel();
		modelBase7.setIconResSuffix("survival_2_0_0");
		modelBase7.setUnitName("Entertainment");
		listBase.add(modelBase7);

		LearnUnitBaseModel modelBase8 = new LearnUnitBaseModel();
		modelBase8.setIconResSuffix("survival_2_1_0");
		modelBase8.setUnitName("Sightseeing");
		listBase.add(modelBase8);

		LearnUnitBaseModel modelBase9 = new LearnUnitBaseModel();
		modelBase9.setIconResSuffix("survival_2_2_0");
		modelBase9.setUnitName("Signs in Public");
		listBase.add(modelBase9);

		LearnUnitBaseModel modelBase10 = new LearnUnitBaseModel();
		modelBase10.setIconResSuffix("survival_3_0_0");
		modelBase10.setUnitName("Accommodation");
		listBase.add(modelBase10);

		LearnUnitBaseModel modelBase11 = new LearnUnitBaseModel();
		modelBase11.setIconResSuffix("survival_3_1_0");
		modelBase11.setUnitName("Number");
		listBase.add(modelBase11);

		LearnUnitBaseModel modelBase12 = new LearnUnitBaseModel();
		modelBase12.setIconResSuffix("survival_3_2_0");
		modelBase12.setUnitName("Tools");
		listBase.add(modelBase12);
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app.finishActivity(SurvivalKitActivity.this);
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
			String title = listBase.get(arg2).getUnitName();
			Intent intent = new Intent(SurvivalKitActivity.this,
					SurvivalKitDetailActivity.class);
			intent.putExtra("title", title);
			startActivity(intent);

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
