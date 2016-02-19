package com.hw.chineseLearn.tabMe;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.interfaces.HttpInterfaces;
import com.hw.chineseLearn.model.SimpleModel;
import com.util.thread.ThreadWithDialogTask;
import com.util.weight.NumberPicker;
import com.util.weight.NumberPicker.Formatter;
import com.util.weight.NumberPicker.OnScrollListener;
import com.util.weight.NumberPicker.OnValueChangeListener;

/**
 * 提醒设置
 * 
 * @author yh
 */
public class MySettingRemindersActivity extends BaseActivity implements
		OnValueChangeListener, OnScrollListener, Formatter {

	private String TAG = "==MySettingRemindersActivity==";
	private Context context;

	private ThreadWithDialogTask tdt;
	HttpInterfaces interfaces;
	SimpleModel simpleModel;
	TextView txt_time;
	LinearLayout lin_time_picker;
	NumberPicker hourPicker, minutePicker;
	Button btn_ok, btn_cancel;
	private String timeString;
	private int hour = 9;
	private int minute = 30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_remind);
		context = this;
		tdt = new ThreadWithDialogTask();
		interfaces = new HttpInterfaces(this);
		init();
		CustomApplication.app.addActivity(this);
		super.gestureDetector();
	}

	/**
	 * 初始化
	 */
	public void init() {
		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Reminders", View.GONE, View.GONE, 0);
		txt_time = (TextView) findViewById(R.id.txt_time);
		txt_time.setOnClickListener(onClickListener);

		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_ok.setOnClickListener(onClickListener);
		btn_cancel.setOnClickListener(onClickListener);

		lin_time_picker = (LinearLayout) findViewById(R.id.lin_time_picker);
		lin_time_picker.setVisibility(View.GONE);
		hourPicker = (NumberPicker) findViewById(R.id.hourpicker);
		minutePicker = (NumberPicker) findViewById(R.id.minuteicker);

		timeString = CustomApplication.app.preferencesUtil.getValue(
				"timeString", "09:30");
		String time[] = timeString.split(":");

		hour = Integer.parseInt(time[0]);
		minute = Integer.parseInt(time[1]);

		txt_time.setText(setTimeString(hour, minute));

		hourPicker.setFormatter(this);
		hourPicker.setOnValueChangedListener(this);
		hourPicker.setOnScrollListener(this);
		hourPicker.setMaxValue(24);
		hourPicker.setMinValue(0);
		hourPicker.setValue(hour);

		minutePicker.setFormatter(this);
		minutePicker.setOnValueChangedListener(this);
		minutePicker.setOnScrollListener(this);
		minutePicker.setMaxValue(59);
		minutePicker.setMinValue(0);
		minutePicker.setValue(minute);

	}

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

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app
						.finishActivity(MySettingRemindersActivity.this);
				break;

			case R.id.rel_reminders:

				break;

			case R.id.txt_time:
				lin_time_picker.setVisibility(View.VISIBLE);
				txt_time.setVisibility(View.GONE);
				break;
			case R.id.btn_ok:
				lin_time_picker.setVisibility(View.GONE);
				txt_time.setVisibility(View.VISIBLE);
				txt_time.setText(timeString);

				break;
			case R.id.btn_cancel:

				break;
			default:
				break;
			}
		}
	};

	@Override
	public String format(int value) {
		// TODO Auto-generated method stub
		String tmpStr = String.valueOf(value);
		if (value < 10) {
			tmpStr = "0" + tmpStr;
		}
		return tmpStr;
	}

	@Override
	public void onScrollStateChange(NumberPicker view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		// TODO Auto-generated method stub

		switch (picker.getId()) {

		case R.id.hourpicker:
			hour = newVal;
			break;
		case R.id.minuteicker:
			minute = newVal;
			break;

		default:
			break;
		}
		setTimeString(hour, minute);
	}

	/**
	 * 
	 * 设置选择的时间
	 * 
	 * @param hour
	 * @param minute
	 * @return timeString
	 */
	private String setTimeString(int hour, int minute) {
		String hourString = "";
		String minuteString = "";
		if (hour < 10) {
			hourString = "0" + hour;
		} else {
			hourString = "" + hour;
		}

		if (minute < 10) {
			minuteString = "0" + minute;
		} else {
			minuteString = "" + minute;
		}

		return timeString = hourString + ":" + minuteString;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		CustomApplication.app.preferencesUtil
				.setValue("timeString", timeString);
		Log.d(TAG + "onDestroy", "timeString:" + timeString);

	}

}
