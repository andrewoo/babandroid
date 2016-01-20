package com.hw.chineseLearn.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.interfaces.NetUtil;
import com.hw.chineseLearn.interfaces.ThreadPoolManager;
import com.hw.chineseLearn.model.RequestVo;
import com.util.thread.ProgressDialog;
import com.util.tool.AppFinal;
import com.util.tool.SharedPreferencesUtil;
import com.util.tool.UiUtil;

public class BaseActivity extends FragmentActivity {

	public List<Fragment> listFragment;

	private List<BaseTaskPost> recordPost = new Vector<BaseTaskPost>();
	private ThreadPoolManager threadPoolManager;
	private static SharedPreferencesUtil spUtil;
	private AlertDialog logoutDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
		// setContentView(R.layout.activity_main);
		listFragment = new ArrayList<Fragment>();
		threadPoolManager = ThreadPoolManager.getInstance();
		spUtil = SharedPreferencesUtil.getInstance(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private boolean isActivityForeground() {
		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);

		String packageName = getApplicationContext().getPackageName();
		List<RunningAppProcessInfo> runningAppProcessInfos = activityManager
				.getRunningAppProcesses();

		if (runningAppProcessInfos == null) {
			return false;
		}

		for (RunningAppProcessInfo runningAppProcess : runningAppProcessInfos) {
			if (runningAppProcess.processName.equals(packageName)
					&& runningAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	private void goMainActivity() {
		Intent mainIntent = new Intent();
		mainIntent.setClass(this, MainActivity.class);
		mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mainIntent.putExtra("reason", "logout");
		startActivity(mainIntent);
	}

	/**
	 * fragment跳转无动画
	 * 
	 * @param fragment
	 */
	public void navigateToNoAnimWithId(Fragment fragment, int id) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.add(id, fragment);
		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
	}

	/**
	 * fragment跳转无动画
	 * 
	 * @param fragment
	 */
	public void navigateToNoAnim(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.add(R.id.container, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	/**
	 * fragment跳转有动画
	 * 
	 * @param fragment
	 */
	public void navigateTo(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_slide_right_enter,
				R.anim.fragment_slide_left_exit,
				R.anim.fragment_slide_left_enter,
				R.anim.fragment_slide_right_exit);
		ft.add(R.id.container, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	/**
	 * fragment替换
	 * 
	 * @param fragment
	 */
	public void replaceTo(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		// ft.setCustomAnimations(R.anim.fragment_slide_right_enter,
		// R.anim.fragment_slide_left_exit,
		// R.anim.fragment_slide_left_enter,
		// R.anim.fragment_slide_right_exit);
		// ft.add(R.id.content,fragment);
		ft.replace(R.id.container, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	/**
	 * fragment跳转
	 * 
	 * @param fragment
	 * @param tag
	 */
	public void navigateTo(Fragment fragment, String tag) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_slide_right_enter,
				R.anim.fragment_slide_left_exit,
				R.anim.fragment_slide_left_enter,
				R.anim.fragment_slide_right_exit);
		ft.add(R.id.container, fragment, tag);
		ft.addToBackStack(null);
		ft.commit();
	}

	public void enterFragment(Fragment fragment) {
		if (listFragment.size() >= 1) {
			Fragment topFragment = listFragment.get(listFragment.size() - 1);
			if (topFragment != null) {
				getSupportFragmentManager().beginTransaction().hide(fragment)
						.commit();
			}
		}
		listFragment.add(fragment);
	}

	@Override
	public void onBackPressed() {
		if (listFragment.size() > 1) {
			Fragment preFragment = listFragment.get(listFragment.size() - 2);
			getSupportFragmentManager().beginTransaction().show(preFragment)
					.commit();
			listFragment.remove(listFragment.size() - 1);
		} else {
			overridePendingTransition(R.anim.fragment_slide_left_enter,
					R.anim.fragment_slide_right_exit);
			this.finish();
		}
		super.onBackPressed();
	}

	public abstract interface DataCallback<T> {
		public abstract void processData(T paramObject, boolean paramBoolean);
	}

	/**
	 * /** 从服务器上获取数据，并回调处理
	 * 
	 * @param <T>
	 * @param reqVo
	 * @param callBack
	 */
	protected void getDataByPost(RequestVo reqVo, DataCallback<?> callBack) {
		if (reqVo.showDlg) {
			this.showProgressDialog();
		}
		BaseHandler handler = new BaseHandler(this, callBack, reqVo);
		BaseTaskPost taskThread = new BaseTaskPost(this, reqVo, handler);
		recordPost.add(taskThread);
		this.threadPoolManager.addTask(taskThread);
	}

	/**
	 * 
	 * @author yh
	 * 
	 */
	class BaseHandler extends Handler {

		private Context context;
		private DataCallback callBack;// interface

		public BaseHandler(Context context, DataCallback callBack,
				RequestVo reqVo) {
			this.context = context;
			this.callBack = callBack;

		}

		@Override
		public void handleMessage(Message msg) {
			closeProgressDialog();
			if (msg.what == AppFinal.SUCCESS) {
				if (msg.obj == null) {
					UiUtil.showToast(context, "请求数据超时，或网络连接失败！");
				} else {
					if (callBack != null) {
						try {
							callBack.processData(msg.obj, true);
						} catch (Exception ee) {
							ee.printStackTrace();
						}
					}
				}
			} else if (msg.what == AppFinal.NET_FAILED) {
				UiUtil.showToast(context, "网络连接失败");
			}

		}
	}

	class BaseTaskPost implements Runnable {
		private Context context;
		private RequestVo reqVo;
		private Handler handler;

		public BaseTaskPost(Context context, RequestVo reqVo, Handler handler) {
			this.context = context;
			this.reqVo = reqVo;
			this.handler = handler;
		}

		@Override
		public void run() {
			Object obj = null;
			Message msg = Message.obtain();
			try {
				if (NetUtil.hasNetwork(context)) {
					obj = NetUtil.post(reqVo);
					msg.what = AppFinal.SUCCESS;
					msg.obj = obj;
					if (handler != null) {
						handler.sendMessage(msg);
					}
					if (recordPost == null || recordPost.size() == 0) {
						return;
					}
					recordPost.remove(this);
				} else {
					msg.what = AppFinal.NET_FAILED;
					msg.obj = obj;
					if (handler != null) {
						handler.sendMessage(msg);
					}
					if (recordPost == null || recordPost.size() == 0) {
						return;
					}
					recordPost.remove(this);
				}
			} catch (Exception e) {
				System.out
						.println("BaseActivity---BaseTaskPost-run()--Exception!!!");
				if (recordPost == null || recordPost.size() == 0) {
					return;
				}
				recordPost.remove(this);
			}
		}

	}

	ProgressDialog progressDialog;
	String msg = "正在执行，请稍后...";

	/*
	 * 显示提示框
	 */
	protected void showProgressDialog() {

		if ((!isFinishing()) && (this.progressDialog == null)) {
			progressDialog = new ProgressDialog(this, R.style.dialog,
					R.layout.progress_dialog, R.anim.rotate_refresh, 0,
					R.drawable.refresh, 0, msg, false);
		}
		this.progressDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
		progressDialog.show();

	}

	/**
	 * 关闭提示框
	 */
	protected void closeProgressDialog() {
		if (this.progressDialog != null)
			this.progressDialog.dismiss();
	}

	/**
	 * 销毁数据
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (logoutDialog != null && logoutDialog.isShowing()) {
			logoutDialog.dismiss();
		}

		recordPost.clear();
		recordPost = null;
		threadPoolManager = null;
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}
