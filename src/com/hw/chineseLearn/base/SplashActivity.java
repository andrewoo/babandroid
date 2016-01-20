package com.hw.chineseLearn.base;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.chineseLearn.R;
import com.util.tool.SystemHelper;

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
	private TextView txt_version;

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

		if (!this.isFirstIn) {
			this.mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			this.mHandler
					.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 0) {
			if (-1 != SystemHelper.getNetworkType(SplashActivity.this)) {
				this.initDataAndStartMain();
			} else {
				Toast.makeText(SplashActivity.this, "网络连接仍不可用，无法访问服务器数据！",
						Toast.LENGTH_SHORT).show();
				SplashActivity.this.finish();
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
		// requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
		this.setContentView(R.layout.splash);

		copyfile();
		txt_version = (TextView) findViewById(R.id.txt_version);
		txt_version.setVisibility(View.GONE);
		txt_version.setText("V" + CustomApplication.app.getVersionName());

		iv_bg = (ImageView) findViewById(R.id.iv_bg);
		iv_logo = (ImageView) findViewById(R.id.iv_logo);
		// LayoutParams layoutParams = (LayoutParams) iv_logo.getLayoutParams();
		// layoutParams.width = CustomApplication.app.displayMetrics.widthPixels
		// / 10 * 7;
		// iv_logo.setLayoutParams(layoutParams);
		iv_logo.setImageDrawable(getResources().getDrawable(
				R.drawable.cover_panda));

		if (-1 == SystemHelper.getNetworkType(SplashActivity.this)) {
			this.createSetNetworkDialog().show();
		} else {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					SplashActivity.this.initDataAndStartMain();
				}
			}, 1);
		}

		System.out.println("SplashActivity");
	}
}