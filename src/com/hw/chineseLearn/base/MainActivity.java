package com.hw.chineseLearn.base;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.interfaces.HttpInterfaces;
import com.hw.chineseLearn.tabDiscover.DiscoverFragment;
import com.hw.chineseLearn.tabLearn.LearnFragment;
import com.hw.chineseLearn.tabMe.MineFragment;
import com.util.thread.ThreadWithDialogTask;
import com.util.tool.SystemHelper;
import com.util.tool.UiUtil;

public class MainActivity extends BaseActivity implements OnClickListener {
	private String TAG = "==MainActivity==";
	Context context;
	View contentView;
	public int selectIndex = -1;
	private int[] bottomBtnIds;
	private Button[] mBtnList;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		contentView = LayoutInflater.from(context).inflate(
				R.layout.activity_main_tab, null);
		setContentView(contentView);
		mainActivity = this;
		init();
	}

	public void init() {
		containerIds = new int[] { R.id.container1, R.id.container2,
				R.id.container3 };
		containers = new FrameLayout[3];
		for (int i = 0; i < containerIds.length; i++) {
			containers[i] = (FrameLayout) findViewById(containerIds[i]);
		}

		bottomBtnIds = new int[] { R.id.home_rb_nav01, R.id.home_rb_nav02,
				R.id.home_rb_nav03 };

		mBtnList = new Button[bottomBtnIds.length];
		for (int i = 0; i < bottomBtnIds.length; i++) {
			mBtnList[i] = (Button) findViewById(bottomBtnIds[i]);
			mBtnList[i].setOnClickListener(this);

		}
		performClickBtn(1);

		if (tdt == null) {
			tdt = new ThreadWithDialogTask();
		}
		if (interfaces == null) {
			interfaces = new HttpInterfaces(MainActivity.this);
		}

		setTitle(View.GONE, View.GONE, "Learn", View.GONE, View.GONE);

	}

	// 顶部标题栏
	private View view_title;

	/**
	 * @param textLeft
	 *            是否显示左边文字
	 * @param imgLeft
	 *            是否显示左边图片
	 * @param title
	 *            标题
	 * @param textRight
	 *            是否显示右边文字
	 * @param imgRight
	 *            是否显示右边图片
	 */
	public void setTitle(int textLeft, int imgLeft, String title,
			int textRight, int imgRight) {
		view_title = (View) this.findViewById(R.id.view_title);
		TextView tv_title = (TextView) view_title.findViewById(R.id.tv_title);
		tv_title.setText(title);

		TextView tv_title_left = (TextView) view_title
				.findViewById(R.id.tv_title_left);
		tv_title_left.setVisibility(textLeft);

		ImageView iv_title_left = (ImageView) view_title
				.findViewById(R.id.iv_title_left);
		iv_title_left.setVisibility(imgLeft);
		iv_title_left.setOnClickListener(onClickListener);
		// iv_title_left.setImageResource(R.drawable.btn_selector_top_left);

		TextView tv_title_right = (TextView) view_title
				.findViewById(R.id.tv_title_right);
		tv_title_right.setVisibility(textRight);
		tv_title_right.setOnClickListener(onClickListener);

		ImageView iv_title_right = (ImageView) view_title
				.findViewById(R.id.iv_title_right);
		iv_title_right.setVisibility(imgRight);

	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				finish();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		int v_id = v.getId();
		switch (v_id) {
		case R.id.home_rb_nav01:
			selectIndex = 0;
			if (discoverFragment == null) {
				discoverFragment = new DiscoverFragment();
				navigateToNoAnimWithId(discoverFragment, R.id.container1);
			}
			setTitle(View.GONE, View.GONE, "Discover", View.GONE, View.GONE);
			break;
		case R.id.home_rb_nav02:
			selectIndex = 1;

			if (learnFragment == null) {
				learnFragment = new LearnFragment();
				navigateToNoAnimWithId(learnFragment, R.id.container2);
			}
			setTitle(View.GONE, View.GONE, "Learn", View.GONE, View.GONE);
			break;
		case R.id.home_rb_nav03:
			selectIndex = 2;

			if (mineFragment == null) {
				mineFragment = new MineFragment();
				navigateToNoAnimWithId(mineFragment, R.id.container3);
			}
			setTitle(View.GONE, View.GONE, "Me", View.GONE, View.GONE);
			break;

		default:
			break;
		}
		setButtonColor(selectIndex);

	}

	private boolean IsClick;

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
				mBtnList[i].setSelected(true);
			} else {
				mBtnList[i].setSelected(false);
			}
		}

	}

	public void performClickBtn(int i) {
		mBtnList[i].performClick();
	}

	long mExitTime = 0;

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {
			if (selectIndex != 1) {// 回到通告fragment
				performClickBtn(1);
				return true;
			}

			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(MainActivity.this, "Press back key to quit",
						Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
				return true;
			} else {
				// ExitApplication.getInstance().exit(); // 调用退出方法
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

}
