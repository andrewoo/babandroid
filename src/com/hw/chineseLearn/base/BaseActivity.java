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
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.interfaces.NetUtil;
import com.hw.chineseLearn.interfaces.ThreadPoolManager;
import com.hw.chineseLearn.model.RequestVo;
import com.util.thread.ProgressDialog;
import com.util.tool.AppFinal;
import com.util.tool.SharedPreferencesUtil;
import com.util.tool.UiUtil;

public class BaseActivity extends FragmentActivity {
	private String TAG = "BaseActivity";
	public List<Fragment> listFragment;
//record 123
	private List<BaseTaskPost> recordPost = new Vector<BaseTaskPost>();
	private ThreadPoolManager threadPoolManager;
	private static SharedPreferencesUtil spUtil;
	private AlertDialog logoutDialog;
	private View contentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
		// setContentView(R.layout.activity_main);
		contentView = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.activity_main, null);
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

	GestureDetector mGestureDetector;

	/**
	 * 手势识别器
	 */
	protected void gestureDetector() {
		mGestureDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {// e1: 第一次按下的位置
																// e2 当手离开屏幕
																// 时的位置
																// velocityX 沿x
																// 轴的速度
																// velocityY：
																// 沿Y轴方向的速度
						// 判断竖直方向移动的大小
						if (Math.abs(e1.getRawY() - e2.getRawY()) > 100) {
							// Toast.makeText(getApplicationContext(), 动作不合法,
							// 0).show();
							return true;
						}
						if (Math.abs(velocityX) < 150) {
							// Toast.makeText(getApplicationContext(), 移动的太慢,
							// 0).show();
							return true;
						}

						if ((e1.getRawX() - e2.getRawX()) > 200) {// 表示向右滑动
							return true;
						}

						if ((e2.getRawX() - e1.getRawX()) > 200) { // 向左滑动 表示
							UiUtil.showToast(getApplicationContext(), "向左滑动");
							finish();
							return true;// 消费掉当前事件 不让当前事件继续向下传递
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
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
	 * fragment替换
	 * 
	 * @param fragment
	 */
	public void replaceTo1(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		// ft.setCustomAnimations(R.anim.fragment_slide_right_enter,
		// R.anim.fragment_slide_left_exit,
		// R.anim.fragment_slide_left_enter,
		// R.anim.fragment_slide_right_exit);
		// ft.add(R.id.content,fragment);
		ft.replace(R.id.container1, fragment);
		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
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
					UiUtil.showToast(context, "time out！");
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
				UiUtil.showToast(context, "network failed");
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
	String msg = "loading...";

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

	protected void closeWindowSoftInput(View view) {

		InputMethodManager imm = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isSoftActive = imm.isActive();
		Log.d("-MianWebActivity-", "isSoftActive：" + isSoftActive);
		if (isSoftActive) {
			imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0); // 强制隐藏键盘
			Log.d(TAG, "强制隐藏键盘");
		}
	}

	// 重写activity的触摸事件
	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// // 2.让手势识别器生效
	// mGestureDetector.onTouchEvent(event);
	// return super.onTouchEvent(event);
	// }
}
