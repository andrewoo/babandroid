package com.hw.chineseLearn.base;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbMyCategory;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.util.tool.FileTools;
import com.util.tool.SystemHelper;
import com.util.tool.UiUtil;

public class SplashActivity extends BaseActivity {

	boolean isFirstIn = false;// 判断是否是第一次登陆

	private static final int GO_HOME = 1000;

	private static final int GO_GUIDE = 1001;

	private static final long SPLASH_DELAY_MILLIS = 3000;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				SplashActivity.this.goHome();
				break;
			case GO_GUIDE:
				// SplashActivity.this.goGuide();
				SplashActivity.this.goHome();
				break;
			}
			super.handleMessage(msg);
		}
	};

	private ImageView iv_bg, iv_logo;

	/**
	 * 弹出设置网络连接的提示
	 * 
	 * @param activity
	 */
	public AlertDialog createSetNetworkDialog() {

		Builder builder = new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info).setTitle("请打开网络连接")
				.setMessage("网络连接不可用，立即设置？");

		builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();

				SplashActivity.this.startActivityForResult(new Intent(
						Settings.ACTION_WIRELESS_SETTINGS), 0);// 进入无线网络配置界面

				SplashActivity.this.finish();
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();

				SplashActivity.this.initDataAndStartMain();

			}
		});
		return builder.create();
	}

	private void goGuide() {
		// Intent intent = new Intent(SplashActivity.this,
		// AppGuideActivity.class);
		// SplashActivity.this.startActivity(intent);
		// SplashActivity.this.finish();
	}

	private void goHome() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	private void initDataAndStartMain() {

		SharedPreferences preferences = this.getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		this.isFirstIn = preferences.getBoolean("isFirstIn", true);

		FileTools.copyDb("chineselearn.db");
		if (!this.isFirstIn) {
			this.mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			this.mHandler
					.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}
	}

	private String TAG = "SplashActivity";

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 0) {
			if (-1 != SystemHelper.getNetworkType(SplashActivity.this)) {
				this.initDataAndStartMain();
			} else {
				// Toast.makeText(SplashActivity.this, "网络连接仍不可用，无法访问服务器数据！",
				// Toast.LENGTH_SHORT).show();
				// SplashActivity.this.finish();
				Log.d(TAG, "网络连接仍不可用");
			}
		}
	}

	protected void copyfile() {
		String temp = null;
		byte[] buffer = null;
		try {
			InputStream in = getResources().openRawResource(R.raw.hello);
			int length = in.available();
			buffer = new byte[length];
			in.read(buffer);
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		writeData("a.txt", buffer);
	}

	void writeData(String filename, byte[] buffer) {
		try {
			FileOutputStream fos = this.openFileOutput(filename, MODE_PRIVATE);
			fos.write(buffer);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.splash);
		// copyfile();
		initDB();
		iv_bg = (ImageView) findViewById(R.id.iv_bg);

		LayoutParams layoutParams1 = (LayoutParams) iv_bg.getLayoutParams();
		layoutParams1.width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;
		layoutParams1.height = layoutParams1.width / 10 * 4;
		iv_bg.setLayoutParams(layoutParams1);
		iv_bg.setImageDrawable(getResources().getDrawable(
				R.drawable.welcome_text));

		iv_logo = (ImageView) findViewById(R.id.iv_logo);
		LayoutParams layoutParams = (LayoutParams) iv_logo.getLayoutParams();
		layoutParams.width = CustomApplication.app.displayMetrics.widthPixels / 10 * 9;
		layoutParams.height = layoutParams.width;
		iv_logo.setLayoutParams(layoutParams);
		iv_logo.setImageDrawable(getResources().getDrawable(
				R.drawable.cover_elephant));

		if (-1 == SystemHelper.getNetworkType(SplashActivity.this)) {
			// this.createSetNetworkDialog().show();
			UiUtil.showToast(getApplicationContext(),
					"NetWork is not available");
		} else {

		}

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				SplashActivity.this.initDataAndStartMain();
			}
		}, 1);

	}

	/**
	 * 复制数据库和插入初始值
	 */
	private void initDB() {
		FileTools.copyDb("chineselearn.db");
		TbMyCategory tb=new TbMyCategory();
		tb.setId(1);
		tb.setComplete_dl(1);
		try {
			MyDao.getDaoMy(TbMyCategory.class).update(tb);//初始化TbMyCategory表
			FileTools.unZip(this,"downsound/cssc_1.zip", DatabaseHelperMy.SOUND_PATH);//解压第一个文件CACHE_DIR_DOWNLOAD
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}