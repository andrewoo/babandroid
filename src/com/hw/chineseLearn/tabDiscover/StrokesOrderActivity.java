package com.hw.chineseLearn.tabDiscover;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.MyExpandableListAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;

/**
 * 拼写练习
 * 
 * @author yh
 */
public class StrokesOrderActivity extends BaseActivity {

	private String TAG = "==StrokesOrderActivity==";
	private Context context;
	View contentView;
	private ExpandableListView expandableListView;
	private MyExpandableListAdapter adapter;
	public String[] groups = { "一", "人", "八", "么", "儿", "儿", "儿", "儿", "儿",
			"儿", "儿", "儿", "儿", "儿", "儿", "儿", "儿", "儿", "儿", "儿" };
	public String[][] children = {
			{ "A11", "A12", "A13", "A14", "A14", "A14", "A14", "A14" },
			{ "B21", "B22", "B23", "B24", "B24", "B24", "B24", "B24", "B24",
					"B24" },
			{ "C31", "C32", "C44", "C55", "C66", "C77", "C88", "C99", "C10",
					"C11" },
			{ "A11", "A12", "A13", "A14", "A14", "A14", "A14", "A14", "A14",
					"A14", "B23" },
			{ "B21", "B22", "B23", "B24", "B24", "B24", "B24", "B24", "B24",
					"B24", "B23", "B23" },
			{ "C31", "C32", "C44", "C55", "C66", "C77", "C88", "C99", "C10",
					"C11" },
			{ "A11", "A12", "A13", "A14", "A14", "A14", "A14", "A14", "A14",
					"A14" },
			{ "B21", "B22", "B23", "B24", "B24", "B24" },
			{ "C31", "C32", "C44", "C55", "C66", "C77", "C88", "C99", "C10",
					"C11" },
			{ "A11", "A12", "A13", "A14", "A14", "A14", "A14", "A14", "A14",
					"A14" },
			{ "B21", "B22", "B23", "B24", "B24", "B24", "B24", "B24", "B24",
					"B24" },
			{ "C31", "C32", "C44", "C55", "C66", "C77", "C88", "C99", "C10",
					"C11" },
			{ "A11", "A12", "A13", "A14", "A14", "A14", "A14", "A14", "A14",
					"A14" },
			{ "B21", "B22", "B23", "B24", "B24", "B24", "B24", "B24", "B24",
					"B24" },
			{ "C31", "C32", "C44", "C55", "C66", "C77", "C88", "C99", "C10",
					"C11" },
			{ "A11", "A12", "A13", "A14", "A14", "A14", "A14", "A14", "A14",
					"A14" },
			{ "B21", "B22", "B23", "B24", "B24", "B24", "B24", "B24", "B24",
					"B24" },
			{ "C31", "C32", "C44", "C55", "C66", "C77", "C88", "C99", "C10",
					"C11" },
			{ "A11", "A12", "A13", "A14", "A14", "A14", "A14", "A14", "A14",
					"A14" },
			{ "B21", "B22", "B23", "B24", "B24", "B24", "B24", "B24", "B24",
					"B24" } };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_strokes_order, null);
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
				"Strokes Order", View.GONE, View.GONE, 0);
		expandableListView = (ExpandableListView) contentView
				.findViewById(R.id.expandableListView);
		adapter = new MyExpandableListAdapter(context, groups, children);
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

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app.finishActivity(StrokesOrderActivity.this);
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
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			// TODO Auto-generated method stub
			setTitle(children[groupPosition][childPosition]);
			return false;
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
			setTitle(groups[groupPosition] + "关闭");
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
			setTitle(groups[groupPosition] + "弹出");
		}
	}

}
