package com.hw.chineseLearn.base;

import java.sql.SQLException;
import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.Unit;
import com.hw.chineseLearn.interfaces.HttpInterfaces;
import com.hw.chineseLearn.tabDiscover.DiscoverFragment;
import com.hw.chineseLearn.tabLearn.LearnFragment;
import com.hw.chineseLearn.tabLearn.LearnReViewActivity;
import com.hw.chineseLearn.tabMe.MineFragment;
import com.j256.ormlite.dao.Dao;
import com.util.thread.ThreadWithDialogTask;
import com.util.tool.SystemHelper;
import com.util.tool.UiUtil;
import com.util.weight.CustomDialog;

public class MainActivity extends BaseActivity implements OnClickListener {
	private String TAG = "==MainActivity==";
	Context context;
	View contentView;
	public int selectIndex = -1;
	private int[] bottomLinIds;
	private int[] bottomBtnIds;
	private int[] bottomTvIds;
	private LinearLayout[] mLinList;
	private Button[] mBtnList;
	private TextView[] mTvList;

	private FrameLayout[] containers;
	private int[] containerIds;

	private MyReceiver receiverNet = null;

	private ThreadWithDialogTask tdt;
	HttpInterfaces interfaces = null;
	public static MainActivity mainActivity;

	public static int loading_process;
	InputMethodManager imm;

	private AlertDialog mModifyDialog;
	public DiscoverFragment discoverFragment = null;
	public LearnFragment learnFragment = null;
	public MineFragment mineFragment = null;

	private List<Unit> unitList;// learn页面图标名字的集合

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		contentView = LayoutInflater.from(context).inflate(
				R.layout.activity_main_tab, null);
		setContentView(contentView);
		mainActivity = this;
		init();
		getUnitData();// 查询数据库Unit表得到所有girdView需要字段
	}

	public void init() {
		containerIds = new int[] { R.id.container1, R.id.container2,
				R.id.container3, R.id.container4 };
		containers = new FrameLayout[containerIds.length];
		for (int i = 0; i < containerIds.length; i++) {
			containers[i] = (FrameLayout) findViewById(containerIds[i]);
		}

		bottomLinIds = new int[] { R.id.lin_0, R.id.lin_1, R.id.lin_2,
				R.id.lin_3 };
		bottomBtnIds = new int[] { R.id.home_rb_nav00, R.id.home_rb_nav01,
				R.id.home_rb_nav02, R.id.home_rb_nav03 };
		bottomTvIds = new int[] { R.id.home_tv_nav00, R.id.home_tv_nav01,
				R.id.home_tv_nav02, R.id.home_tv_nav03 };

		mLinList = new LinearLayout[bottomLinIds.length];
		mBtnList = new Button[bottomBtnIds.length];
		mTvList = new TextView[bottomTvIds.length];

		for (int i = 0; i < bottomLinIds.length; i++) {
			mLinList[i] = (LinearLayout) findViewById(bottomLinIds[i]);
			mLinList[i].setOnClickListener(this);
		}
		mLinList[1].setVisibility(View.GONE);
		
		for (int i = 0; i < bottomBtnIds.length; i++) {
			mBtnList[i] = (Button) findViewById(bottomBtnIds[i]);
			mBtnList[i].setClickable(false);
		}
		for (int i = 0; i < bottomTvIds.length; i++) {
			mTvList[i] = (TextView) findViewById(bottomTvIds[i]);
			mTvList[i].setClickable(false);

		}

		performClickBtn(0);

		if (tdt == null) {
			tdt = new ThreadWithDialogTask();
		}
		if (interfaces == null) {
			interfaces = new HttpInterfaces(MainActivity.this);
		}

		setTitle(View.GONE, View.GONE, 0, "Learn", View.GONE, View.VISIBLE,
				R.drawable.review_button, true);

	}

	// 顶部标题栏
	private View view_title;

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
	@SuppressWarnings("deprecation")
	public void setTitle(int textLeft, int imgLeft, int imgLeftDrawable,
			String title, int textRight, int imgRight, int imgRightDrawable,
			boolean isShow) {

		View view_title = (View) this.findViewById(R.id.view_title);// rel_title_view

		if (isShow) {
			view_title.setVisibility(View.VISIBLE);
		} else {
			view_title.setVisibility(View.GONE);
		}
		Button btn_title = (Button) view_title.findViewById(R.id.btn_title);
		btn_title.setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.btn_white_no_corners));
		btn_title.setText(title);
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
		iv_title_right.setOnClickListener(onClickListener);
		iv_title_right.setImageResource(imgRightDrawable);
	}

	@Override
	public void onClick(View v) {
		int v_id = v.getId();
		switch (v_id) {

		case R.id.lin_0:
			selectIndex = 0;
			if (learnFragment == null) {
				learnFragment = new LearnFragment();
				navigateToNoAnimWithId(learnFragment, R.id.container1);
			}
			View view_title = (View) this.findViewById(R.id.view_title);// rel_title_view
			view_title.setVisibility(View.VISIBLE);
			setTitle(View.GONE, View.GONE, 0, "Learn", View.GONE, View.VISIBLE,
					R.drawable.review_button, true);

			break;

		case R.id.lin_1:
			selectIndex = 1;
			// if (learnFragment == null) {
			// learnFragment = new LearnFragment();
			// navigateToNoAnimWithId(learnFragment, R.id.container2);
			// }
			setTitle(View.GONE, View.GONE, 0, "Complete", View.GONE, View.GONE,
					0, true);
			break;
		case R.id.lin_2:
			selectIndex = 2;

			if (discoverFragment == null) {
				discoverFragment = new DiscoverFragment();
				navigateToNoAnimWithId(discoverFragment, R.id.container3);
			}
			setTitle(View.GONE, View.GONE, 0, "Discover", View.GONE, View.GONE,
					R.drawable.img_share, true);
			break;
		case R.id.lin_3:
			selectIndex = 3;
			if (mineFragment == null) {
				mineFragment = new MineFragment();
				navigateToNoAnimWithId(mineFragment, R.id.container4);
			}
			setTitle(View.GONE, View.GONE, 0, "Me", View.GONE, View.GONE, 0,
					false);
			break;

		default:
			break;
		}
		setButtonColor(selectIndex);

	}

	/**
	 * 
	 * @param index
	 */
	private void setButtonColor(int index) {
		for (int i = 0; i < containerIds.length; i++) {
			if (index == i) {
				containers[i].setVisibility(View.VISIBLE);
			} else {
				containers[i].setVisibility(View.GONE);
			}
		}

		for (int i = 0; i < bottomBtnIds.length; i++) {
			if (index == i) {
				mLinList[i].setSelected(true);
				mBtnList[i].setSelected(true);
				mTvList[i].setSelected(true);

			} else {
				mLinList[i].setSelected(false);
				mBtnList[i].setSelected(false);
				mTvList[i].setSelected(false);

			}
		}

		for (int i = 0; i < mLinList.length; i++) {
			if (index == i) {
				mLinList[i].setClickable(false);
			} else {
				mLinList[i].setClickable(true);
			}
		}

	}

	public void performClickBtn(int i) {
		mLinList[i].performClick();
	}

	long mExitTime = 0;

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {
			if (selectIndex != 0) {// 回到Learn fragment
				performClickBtn(0);
				return true;
			}

			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(MainActivity.this, "Press back key to quit",
						Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
				return true;
			} else {
				// ExitApplication.getInstance().exit(); // 调用退出方法
				CustomApplication.app.finishAllActivity();
				finish();
				// android.os.Process.killProcess(android.os.Process.myPid());
				// System.exit(0);

				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 网络监听的广播
	 */
	private void registerNetReceiver() {
		receiverNet = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiverNet, intentFilter);
		System.out.println("主页--【网络监听】广播已经注册");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mModifyDialog != null && mModifyDialog.isShowing()) {
			mModifyDialog.dismiss();
			mModifyDialog = null;
		}

		if (null != receiverNet) {
			this.unregisterReceiver(receiverNet);
		} else {
			Log.e(TAG, "onDestroy(),网络监听注销失败");
		}
	}

	private void showShareDialog() {
		// 一个自定义的布局，作为显示的内容
		View pview = LayoutInflater.from(context).inflate(
				R.layout.layout_learn_share_dialog, null);

		LinearLayout lin_copy_link = (LinearLayout) pview
				.findViewById(R.id.lin_copy_link);
		LinearLayout lin_messenger = (LinearLayout) pview
				.findViewById(R.id.lin_messenger);
		LinearLayout lin_twitter = (LinearLayout) pview
				.findViewById(R.id.lin_twitter);
		LinearLayout lin_mail = (LinearLayout) pview
				.findViewById(R.id.lin_mail);
		LinearLayout lin_whatsapp = (LinearLayout) pview
				.findViewById(R.id.lin_whatsapp);
		LinearLayout lin_line = (LinearLayout) pview
				.findViewById(R.id.lin_line);

		final CustomDialog builder = new CustomDialog(MainActivity.this,
				R.style.my_dialog).create(pview, true, 1f, 1f, 1);
		builder.show();
		builder.setCancelable(true);

		lin_copy_link.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UiUtil.showToast(context, "Copyed");
				builder.dismiss();
			}
		});

		builder.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
			}
		});

	}

	/**
	 * 自定义广播接收器
	 * 
	 * @author win7
	 */
	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 接收
			if (intent.getAction().equals(
					"android.net.conn.CONNECTIVITY_CHANGE")) {

				if (SystemHelper.isConnected(MainActivity.this) == true && // 如果网络连接正常
						SystemHelper.getNetworkType(MainActivity.this) != -1) {
					CustomApplication.app.isNetConnect = true;
					// 提示网络类型
					if (SystemHelper.getNetworkType(getApplicationContext()) == 0) {// GPRS
						UiUtil.showToast(getApplicationContext(),
								"当前连接方式为GPRS网络");
					}
					if (SystemHelper.getNetworkType(getApplicationContext()) == 1) {// WIFi
						UiUtil.showToast(getApplicationContext(),
								"当前连接方式为WIFI网络");
					}

				} else {

					CustomApplication.app.isNetConnect = false;
					UiUtil.showToast(getApplicationContext(), "没有网络连接");

				}
			}
		}

	}

	/**
	 * 标题栏监听
	 */
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:

				break;

			case R.id.iv_title_right:

				switch (selectIndex) {

				case 0:// review
					startActivity(new Intent(MainActivity.this,
							LearnReViewActivity.class));
					break;
				case 1:

					break;
				case 2:// share
					showShareDialog();
					break;
				case 3:

					break;

				default:
					break;
				}

				break;

			default:
				break;
			}
		}
	};

	/**
	 * 查询数据库Unit表得到所有字段
	 * 
	 * @return
	 * @return
	 */
	public List<Unit> getUnitData() {

		Dao unitdao = MyDao.getDao(Unit.class);
		// unitdao.queryBuilder().where().eq("UnitId", 1).query();//查询一行
		// List<Unit> iconUnitList =
		// unitdao.queryBuilder().selectColumns("IconResSuffix").query();//查询IconResSuffix列
		try {
			unitList = unitdao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		CustomApplication.app.unitList = unitList;
		return unitList;
	}
	

}
