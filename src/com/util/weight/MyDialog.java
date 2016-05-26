package com.util.weight;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hw.chineseLearn.R;

/**
 * 自定义对话框实现
 * @author lp
 *
 */
public class MyDialog extends Dialog {
	private Activity context;
	private LinearLayout layout_header;
	private LinearLayout layout_body;
	private LinearLayout btn_group;
	private Button btn_close, btn_cancel, btn_ok;
	private ImageView img_icon;
	private TextView text_title, text_content;

	private MyDialogListener defaultListener = null;
	private MyDialogListener cancleListener = null;
	private MyDialogListener okListener = null;

	private float heightProportion = 0.12f;
	private float widthProportion = 0.7f;

	public MyDialog(Activity context) {
		super(context);
		this.context = context;
		initDialog();
	}

	public MyDialog(Activity context, int theme) {
		super(context, theme);
		this.context = context;
		initDialog();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void initDialog() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标头
		getWindow().setContentView(R.layout.dialog);// 加载xml
		getWindow().getDecorView().getBackground().setAlpha(0);// 设置透明度

		layout_header = (LinearLayout) findViewById(R.id.dialog_header);
		layout_body = (LinearLayout) findViewById(R.id.dialog_body);
		btn_group = (LinearLayout) findViewById(R.id.btn_group);
		btn_close = (Button) findViewById(R.id.dialog_button_close);
		btn_cancel = (Button) findViewById(R.id.dialog_button_cancel);
		btn_ok = (Button) findViewById(R.id.dialog_button_ok);
		img_icon = (ImageView) findViewById(R.id.dialog_title_image);
		text_title = (TextView) findViewById(R.id.dialog_text_title);
		text_content = (TextView) findViewById(R.id.dialog_text_content);

		btn_close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (defaultListener != null) {
					defaultListener.onClick();
				}
			}
		});
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				dismiss();
				if (cancleListener != null) {
					cancleListener.onClick();
				}
			}
		});
		btn_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				dismiss();
				if (okListener != null) {
					okListener.onClick();
				}
			}
		});
		setDialogSize(widthProportion, heightProportion, Gravity.CENTER);
	}

	public void setIcon(int rid) {
		layout_header.setVisibility(View.VISIBLE);
		img_icon.setVisibility(View.VISIBLE);
		img_icon.setImageDrawable(context.getResources().getDrawable(rid));
	}

	public void setTitle(String title_text) {
		layout_header.setVisibility(View.GONE);
		text_title.setText(title_text);
	}

	public void setMessage(String message_text) {
		text_content.setText(message_text);
	}

	public void setContentView(View view) {
		layout_body.removeAllViews();
		layout_body.addView(view);
	}

	public void setOnDefaultButtonClick(MyDialogListener listener) {
		btn_close.setVisibility(View.VISIBLE);
		btn_group.setVisibility(View.GONE);
		defaultListener = listener;
	}

	public void setOnDefaultButtonClick(String btn_desc,
			MyDialogListener listener) {
		btn_close.setVisibility(View.VISIBLE);
		btn_group.setVisibility(View.GONE);
		btn_close.setText(btn_desc);
		defaultListener = listener;
	}

	public void setOnButtonGroupClick(MyDialogListener listener_left_cancle,
			MyDialogListener listener_right_ok) {
		btn_close.setVisibility(View.GONE);
		btn_group.setVisibility(View.VISIBLE);
		cancleListener = listener_left_cancle;
		okListener = listener_right_ok;
	}

	public void setOnButtonGroupClick(String btn_left_cancle,
			MyDialogListener listener_left_cancle, String btn_right_ok,
			MyDialogListener listener_right_ok) {
		btn_close.setVisibility(View.GONE);
		btn_group.setVisibility(View.VISIBLE);
		btn_cancel.setText(btn_left_cancle);
		btn_ok.setText(btn_right_ok);
		cancleListener = listener_left_cancle;
		okListener = listener_right_ok;
	}

	public interface MyDialogListener {
		public void onClick();
	}

	public void setDialogSize(float widthProportion, float heightProportion,
			int gravityValue) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenHeigh = dm.heightPixels;
		int screenWidth = dm.widthPixels;
		
		LinearLayout.LayoutParams layoutParams = (LayoutParams) layout_body
				.getLayoutParams();
		layoutParams.height = (int) (screenHeigh * heightProportion);// 高度设置为屏幕高度比
		// layoutParams.width = (int) (screenWidth * widthProportion);// 宽度设置为屏幕的宽度比
		// layoutParams.width = layoutParams.width;
		layoutParams.gravity = gravityValue;
		layout_body.setLayoutParams(layoutParams);

		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// lp.height = (int) (screenHeigh * heightProportion); // 高度设置为屏幕高度比
		lp.width = (int) (screenWidth * widthProportion); // 宽度设置为屏幕的宽度比
		dialogWindow.setAttributes(lp);

	}
}
