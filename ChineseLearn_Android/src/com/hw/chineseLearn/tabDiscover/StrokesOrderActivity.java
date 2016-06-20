package com.hw.chineseLearn.tabDiscover;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.MyExpandableListAdapterStrokes;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.CharGroup;
import com.hw.chineseLearn.dao.bean.Character;
import com.util.tool.UiUtil;

import java.sql.SQLException;
import java.util.ArrayList;

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
	private MyExpandableListAdapterStrokes adapter;
	ArrayList<CharGroup> charGroupList = new ArrayList<CharGroup>();
	ArrayList<ArrayList<Character>> characterLists = new ArrayList<ArrayList<Character>>();
	public static final int FLUSH_UI = 100;
	Handler handler=new Handler(){


		public void handleMessage(Message msg) {
			switch (msg.what) {
				case FLUSH_UI:

					setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
							"Strokes Order", View.GONE, View.GONE, 0);
					expandableListView = (ExpandableListView) contentView
							.findViewById(R.id.expandableListView);
					adapter = new MyExpandableListAdapterStrokes(context, charGroupList,
							characterLists);
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

					mModifyDialog2.dismiss();
					break;

				default:
					break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_strokes_order, null);
		setContentView(contentView);
		CustomApplication.app.addActivity(this);
		context = this;
		showDialog();//弹出进度对话框 等待数据加载
		init();
	}
	private AlertDialog mModifyDialog2;
	private void showDialog() {
		mModifyDialog2 = new AlertDialog.Builder(this).create();

		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.progress,null);
		FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity= Gravity.CENTER;
		mModifyDialog2.show();
		mModifyDialog2.setContentView(view,params);
	}


	/**
	 * 初始化
	 */
	public void init() {
		new Thread(){
			@Override
			public void run() {
				initDatasFromDb();
				handler.sendEmptyMessage(FLUSH_UI);
			}
		}.start();


	}

	/**
	 * 从数据库中取出数据
	 */
	@SuppressWarnings("unchecked")
	private void initDatasFromDb() {

		try {
			charGroupList = (ArrayList<CharGroup>) MyDao
					.getDao(CharGroup.class).queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (charGroupList != null && charGroupList.size() != 0) {
			for (int i = 0; i < charGroupList.size(); i++) {
				CharGroup charGroup = charGroupList.get(i);
				if (charGroup == null) {
					continue;
				}
				String partGroupListStr = charGroup.getPartGroupList();
				String[] partGroupList = UiUtil
						.getListFormString(partGroupListStr);
				if (partGroupList != null) {
					ArrayList<Character> characterList = new ArrayList<Character>();
					for (int j = 0; j < partGroupList.length; j++) {
						String charId = partGroupList[j];
						try {
							Character character = (Character) MyDao.getDao(
									Character.class).queryForId(charId);
							characterList.add(character);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					characterLists.add(characterList);
				}
			}
		}

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
			Intent intent = new Intent(StrokesOrderActivity.this,
					StrokesOrderExerciseActivity.class);
			Character character = adapter
					.getChild(groupPosition, childPosition);
			intent.putExtra("model", character);
			startActivity(intent);
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
		}
	}

}
