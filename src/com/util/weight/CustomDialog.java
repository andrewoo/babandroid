package com.util.weight;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hw.chineseLearn.R;

/**
 * 
 * Create custom Dialog windows for your application Custom dialogs rely on
 * custom layouts wich allow you to create and use your own look & feel.
 * 
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 * 
 * <a href="http://my.oschina.net/arthor" target="_blank"
 * rel="nofollow">@author</a> antoine vianey
 *
 */
public class CustomDialog extends Dialog {
	private Activity context;

	public CustomDialog(Activity context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public CustomDialog(Activity context) {
		super(context);
		this.context = context;
	}

	/**
	 * Create the custom dialog
	 */
	public CustomDialog create(View view, boolean isTouchOutsideCancle) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// instantiate the dialog with the custom Theme
		CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
		dialog.setCanceledOnTouchOutside(isTouchOutsideCancle);
		
		dialog.setContentView(view);

		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		
		// WindowManager m = context.getWindowManager();
		// Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		// p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.6
		// p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
		
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels;
		p.width = (int) (width * 0.7); // 宽度设置为屏幕的0.65
		p.height = (int) (height * 0.6); // 高度设置为屏幕的0.6
		p.y = (height - p.height) / 4;
		
		dialogWindow.setAttributes(p);
		
		return dialog;
	}
	
	/**
	 * Create the custom dialog
	 */
	public CustomDialog create(View view, boolean isTouchOutsideCancle, float w, float h, float gravity) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// instantiate the dialog with the custom Theme
		CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
		dialog.setCanceledOnTouchOutside(isTouchOutsideCancle);
		
		dialog.setContentView(view);

		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		
		// WindowManager m = context.getWindowManager();
		// Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		// p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.6
		// p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
		
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels;
		p.width = (int) (width * w); // 宽度设置为屏幕的0.65
		p.height = (int) (height * h); // 高度设置为屏幕的0.6
		p.y = (int) ((height - p.height) * gravity);
		
		dialogWindow.setAttributes(p);
		
		return dialog;
	}
	
	/**
	 * Create the custom dialog
	 */
	public CustomDialog create(View view, boolean isTouchOutsideCancle, float w, int h) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// instantiate the dialog with the custom Theme
		CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
		dialog.setCanceledOnTouchOutside(isTouchOutsideCancle);
		
		dialog.setContentView(view);

		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		
		// WindowManager m = context.getWindowManager();
		// Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		// p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.6
		// p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
		
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels;
		p.width = (int) (width * w); // 宽度设置为屏幕的0.65
		p.height = h;
		p.y = (int) ((height - p.height) * 0.1);
		
		dialogWindow.setAttributes(p);
		
		return dialog;
	}

}