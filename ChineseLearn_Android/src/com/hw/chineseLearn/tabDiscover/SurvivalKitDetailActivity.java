package com.hw.chineseLearn.tabDiscover;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import com.hw.chineseLearn.adapter.SurvivalListAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbMyCategory;
import com.hw.chineseLearn.dao.bean.category;
import com.hw.chineseLearn.dao.bean.item;
import com.util.tool.MediaPlayUtil;

/**
 * 救生练习页面
 * 
 * @author yh
 */
public class SurvivalKitDetailActivity extends BaseActivity {

	private String TAG = "==SurvivalKitDetailActivity==";
	private Context context;
	View contentView;
	// private String title = "Title";
	private category cate;
	// private ExpandableListView expandableListView;
	private SurvivalListAdapter adapter;

	List<item> data = new ArrayList<item>();

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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (MediaPlayUtil.getInstance()!=null) {
			MediaPlayUtil.getInstance().stop();
			MediaPlayUtil.getInstance().release();
		}
		
	}

	/**
	 * 初始化
	 */
	public void init() {
		setTitle(View.GONE, View.VISIBLE,
				R.drawable.btn_selector_top_left_black, cate.getEng_name(),
				View.GONE, View.GONE, 0);
		// initChildList();
		initData();
		initView();

		// expandableListView = (ExpandableListView) contentView
		// .findViewById(R.id.expandableListView);
		// adapter = new MyExpandableListAdapterSurvival(context, data);
		// expandableListView.setAdapter(adapter);
		// // 监听父列表的弹出事件
		// expandableListView
		// .setOnGroupExpandListener(new ExpandableListViewListenerC());
		// // 监听父列表的关闭事件
		// expandableListView
		// .setOnGroupCollapseListener(new ExpandableListViewListenerB());
		// // 监听子列表
		// expandableListView
		// .setOnChildClickListener(new ExpandableListViewListenerA());
	}

	private void initView() {
		listView = (ListView) contentView.findViewById(R.id.listview);
		adapter = new SurvivalListAdapter(this, (ArrayList<item>) data);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				adapter.setSelection(position);
				adapter.notifyDataSetChanged();
			}

		});
	}

	// private void initChildList() {
	//
	// for (int i = 0; i < 11; i++) {
	// LearnUnitBaseModel modelBase1 = new LearnUnitBaseModel();
	// modelBase1.setUnitName("" + (i + 1));
	// modelBase1.setEnable(false);
	// childList.add(modelBase1);
	// }
	// }

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app.finishActivity(SurvivalKitDetailActivity.this);
				break;

			default:
				break;
			}
		}
	};
	private ListView listView;

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

	private void initData() {
		try {
			data = MyDao.getDao(item.class).queryBuilder().where()
					.eq("cid", cate.getId()).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 监听子级列表的点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	// public class ExpandableListViewListenerA implements
	// ExpandableListView.OnChildClickListener {
	//
	// @Override
	// public boolean onChildClick(ExpandableListView parent, View view,
	// int groupPosition, int childPosition, long id) {
	// // TODO Auto-generated method stub
	//
	// adapter.setSelection(groupPosition, childPosition);
	// adapter.notifyDataSetChanged();
	// Log.d(TAG, "groupPosition:" + groupPosition);
	// Log.d(TAG, "childPosition:" + childPosition);
	// return true;
	// }
	// }

	/**
	 * 监听父级列表的关闭事件事件
	 * 
	 * @author Administrator
	 * 
	 */
	// public class ExpandableListViewListenerB implements
	// ExpandableListView.OnGroupCollapseListener {
	//
	// @Override
	// public void onGroupCollapse(int groupPosition) {
	// // TODO Auto-generated method stub
	// Log.d(TAG, "关闭");
	// }
	// }

	/**
	 * 监听父级列表的弹出事件
	 * 
	 * @author Administrator
	 * 
	 */
	// public class ExpandableListViewListenerC implements
	// ExpandableListView.OnGroupExpandListener {
	// @Override
	// public void onGroupExpand(int groupPosition) {
	// // TODO Auto-generated method stub
	// Log.d(TAG, "弹出");
	// }
	// }

}
