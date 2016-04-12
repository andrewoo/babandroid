package com.hw.chineseLearn.tabDiscover;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.MyExpandableListAdapterSurvival;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.bean.category;
import com.hw.chineseLearn.model.LearnSurvivalExpandBaseModel;
import com.hw.chineseLearn.model.LearnUnitBaseModel;

/**
 * 救生练习页面
 * 
 * @author yh
 */
public class SurvivalKitDetailActivity extends BaseActivity {

	private String TAG = "==SurvivalKitDetailActivity==";
	private Context context;
	View contentView;
//	private String title = "Title";
	private category cate;
	private ExpandableListView expandableListView;
	private MyExpandableListAdapterSurvival adapter;

	ArrayList<LearnSurvivalExpandBaseModel> data = new ArrayList<LearnSurvivalExpandBaseModel>();
	ArrayList<LearnUnitBaseModel> childList = new ArrayList<LearnUnitBaseModel>();

	private void initData() {
		LearnSurvivalExpandBaseModel model1 = new LearnSurvivalExpandBaseModel();
		model1.setUnitName("Greetings");
		model1.setChildData(childList);
		data.add(model1);

		LearnSurvivalExpandBaseModel model2 = new LearnSurvivalExpandBaseModel();
		model2.setUnitName("Friends");
		model2.setChildData(childList);
		data.add(model2);

		LearnSurvivalExpandBaseModel model3 = new LearnSurvivalExpandBaseModel();
		model3.setUnitName("Wishes");
		model3.setChildData(childList);
		data.add(model3);

		LearnSurvivalExpandBaseModel model4 = new LearnSurvivalExpandBaseModel();
		model4.setUnitName("Question");
		model4.setChildData(childList);
		data.add(model4);
	}

	// public String[] groups = { "Greetings", "Friends", "Wishes", "Question"
	// };
	// public String[][] children = {
	// { "Hello!", "Good morning!", "Good afternoon!", "Good evening!",
	// "Good night!", "GoodBye!", "Thank you!",
	// "You are welcome!", "Sorry(apology)", "it doesn't matter",
	// "Excuse me(Attention)" },
	// { "Miss", "Yes", "No", "Pardon?", "What's your name?",
	// "My name is...", "Nice to meet you!", "How are you",
	// "I am fine", "I am not fine", "and you?",
	// "Where are you from?", "I am from...",
	// "Where do you live?", "I live in Beijing Hotel.",
	// "Where are you going?" },
	// { "C31", "C32", "C44", "C55", "C66", "C77", "C88", "C99", "C10",
	// "C11" },
	// { "A11", "A12", "A13", "A14", "A14", "A14", "A14", "A14", "A14",
	// "A14", "B23" },
	// { "B21", "B22", "B23", "B24", "B24", "B24", "B24", "B24", "B24",
	// "B24", "B23", "B23" },
	// { "C31", "C32", "C44", "C55", "C66", "C77", "C88", "C99", "C10",
	// "C11" },
	// { "A11", "A12", "A13", "A14", "A14", "A14", "A14", "A14", "A14",
	// "A14" },
	// { "B21", "B22", "B23", "B24", "B24", "B24" },
	// { "C31", "C32", "C44", "C55", "C66", "C77", "C88", "C99", "C10",
	// "C11" },
	// { "A11", "A12", "A13", "A14", "A14", "A14", "A14", "A14", "A14",
	// "A14" },
	// { "B21", "B22", "B23", "B24", "B24", "B24", "B24", "B24", "B24",
	// "B24" },
	// { "C31", "C32", "C44", "C55", "C66", "C77", "C88", "C99", "C10",
	// "C11" },
	// { "A11", "A12", "A13", "A14", "A14", "A14", "A14", "A14", "A14",
	// "A14" },
	// { "B21", "B22", "B23", "B24", "B24", "B24", "B24", "B24", "B24",
	// "B24" },
	// { "C31", "C32", "C44", "C55", "C66", "C77", "C88", "C99", "C10",
	// "C11" },
	// { "A11", "A12", "A13", "A14", "A14", "A14", "A14", "A14", "A14",
	// "A14" },
	// { "B21", "B22", "B23", "B24", "B24", "B24", "B24", "B24", "B24",
	// "B24" },
	// { "C31", "C32", "C44", "C55", "C66", "C77", "C88", "C99", "C10",
	// "C11" },
	// { "A11", "A12", "A13", "A14", "A14", "A14", "A14", "A14", "A14",
	// "A14" },
	// { "B21", "B22", "B23", "B24", "B24", "B24", "B24", "B24", "B24",
	// "B24" } };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_survival_kit_detail, null);
		setContentView(contentView);
		CustomApplication.app.addActivity(this);
		context = this;
		Bundle budle = this.getIntent().getExtras();
		if (budle != null) {
			if (budle.containsKey("category")) {
				cate = (category) budle.getSerializable("category");
			}
		}
		init();
	}

	/**
	 * 初始化
	 */
	public void init() {
		setTitle(View.GONE, View.VISIBLE,
				R.drawable.btn_selector_top_left_black, cate.getEng_name(), View.GONE,
				View.GONE, 0);
		System.out.println("cate.getEng_name()"+cate.getEng_name());
		initChildList();
		initData(); 

		expandableListView = (ExpandableListView) contentView
				.findViewById(R.id.expandableListView);
		adapter = new MyExpandableListAdapterSurvival(context, data);
		expandableListView.setAdapter(adapter);
		// 监听父列表的弹出事件
		expandableListView
				.setOnGroupExpandListener(new ExpandableListViewListenerC());
		// 监听父列表的关闭事件
		expandableListView
				.setOnGroupCollapseListener(new ExpandableListViewListenerB());
		// 监听子列表
		expandableListView
				.setOnChildClickListener(new ExpandableListViewListenerA());
	}

	private void initChildList() {

		for (int i = 0; i < 11; i++) {
			LearnUnitBaseModel modelBase1 = new LearnUnitBaseModel();
			modelBase1.setUnitName("" + (i + 1));
			modelBase1.setEnable(false);
			childList.add(modelBase1);
		}
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app
						.finishActivity(SurvivalKitDetailActivity.this);
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

	/**
	 * 监听子级列表的点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	public class ExpandableListViewListenerA implements
			ExpandableListView.OnChildClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View view,
				int groupPosition, int childPosition, long id) {
			// TODO Auto-generated method stub

			adapter.setSelection(groupPosition, childPosition);
			adapter.notifyDataSetChanged();
			Log.d(TAG, "groupPosition:" + groupPosition);
			Log.d(TAG, "childPosition:" + childPosition);
			return true;
		}
	}

	/**
	 * 监听父级列表的关闭事件事件
	 * 
	 * @author Administrator
	 * 
	 */
	public class ExpandableListViewListenerB implements
			ExpandableListView.OnGroupCollapseListener {

		@Override
		public void onGroupCollapse(int groupPosition) {
			// TODO Auto-generated method stub
			Log.d(TAG, "关闭");
		}
	}

	/**
	 * 监听父级列表的弹出事件
	 * 
	 * @author Administrator
	 * 
	 */
	public class ExpandableListViewListenerC implements
			ExpandableListView.OnGroupExpandListener {
		@Override
		public void onGroupExpand(int groupPosition) {
			// TODO Auto-generated method stub
			Log.d(TAG, "弹出");
		}
	}

}
