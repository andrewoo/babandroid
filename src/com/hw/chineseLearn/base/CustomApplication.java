package com.hw.chineseLearn.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.hw.chineseLearn.dao.bean.Unit;
import com.hw.chineseLearn.db.DatabaseHelper;
import com.hw.chineseLearn.db.SQLConnection;
import com.hw.chineseLearn.model.LearnUnitBaseModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.util.tool.SharedPreferencesUtil;
import com.util.tool.UiUtil;

public class CustomApplication extends Application {
	private String TAG = "CustomApplication";
	/**
	 * 网络是否连接
	 */
	public boolean isNetConnect = false;
	/**
	 * 是否登录
	 */
	public boolean isLogin = false;
	/**
	 * 登录凭证 （测试值）
	 */
	public String checkLog = "";
	/**
	 * 用户名
	 */
	public String userPhone = "";

	public String openId = "";
	public String weChatNickName = "";
	public String weChatHeadImgFileName = "";

	public static CustomApplication app = null;
	public SharedPreferencesUtil preferencesUtil;// 文件保存

	/**
	 * 屏幕尺寸属性对象
	 */
	public DisplayMetrics displayMetrics;

	public String apkName = "";// App名称

	public String IMEI = "";

	private static Stack<Activity> activityStack;

	public ArrayList<LearnUnitBaseModel> favouriteList = new ArrayList<LearnUnitBaseModel>();

	/**
	 * Unit jihe
	 */
	public List<Unit> unitList = new ArrayList<Unit>();
	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		preferencesUtil = SharedPreferencesUtil.getInstance(this);
		// initImgLoader();
		getTelephoneInfo();
		getScreenSize();
		new SQLConnection(this);
		initDB();
	}

	private void initDB() {
		File f = new File(DatabaseHelper.DATABASE_PATH);
        if (!f.exists()) {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
            		DatabaseHelper.DATABASE_PATH,null);
            DatabaseHelper orm = new DatabaseHelper(this);
            orm.onCreate(db);
            db.close();
        }
		
	}

	/**
	 * 得到手机硬件信息
	 */
	private void getTelephoneInfo() {
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(TELEPHONY_SERVICE);
		IMEI = tm.getDeviceId();
		Log.d("IMEI:", IMEI);
	}

	/**
	 * 获取屏幕的宽高度（像素）
	 */
	public DisplayMetrics getScreenSize() {
		displayMetrics = this.getResources().getDisplayMetrics();
		System.out.println("当前设备屏幕高度（像素）:" + displayMetrics.heightPixels);
		System.out.println("当前设备屏幕宽度（像素）:" + displayMetrics.widthPixels);
		return displayMetrics;
	}

	/**
	 * 获得当前进程的名字
	 * 
	 * @param context
	 * @return 进程号
	 */
	public static String getCurProcessName(Context context) {

		int pid = android.os.Process.myPid();

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {

			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	public static CustomApplication getIntance() {
		if (app == null) {
			app = new CustomApplication();
		}
		return app;
	}

	public void initImgLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(0).showImageOnFail(0).cacheInMemory(true)
				.cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)//
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	public void showTimeoutMsg(String msg) {
		UiUtil.showToast(getApplicationContext(), msg);
	}

	/**
	 * 获取版本号
	 */
	public String getVersionName() {
		String version = "";
		// 获取packagemanager对象
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = null;
		try {
			// getPackageName()获取包名和版本号
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 获取程序名称
	 * 
	 * @return
	 */
	public String getApplicationName() {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager
				.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有的Activity
	 */
	public void finishAllActivity() {
		if (activityStack != null) {

			for (int i = 0; i < activityStack.size(); i++) {
				Activity activity = activityStack.get(i);

				if (activity == null) {
					continue;
				}
				Log.d(TAG + "finishAllActivity()", "ActivityName：" + activity);

				activity.finish();
			}
			activityStack.clear();
		}

	}

	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			finishAllActivity();
			System.exit(0);
		} catch (Exception e) {
		}
	}
}