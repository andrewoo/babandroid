package com.util.tool;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

import com.hw.chineseLearn.R;

/**
 * 
 * @author YH
 */
public class MyHandler extends Handler {
	Context context;
	ImageView imageView;

	public MyHandler(Looper looper, Context context, ImageView imageView) {
		super(looper);
		this.context = context;
		this.imageView = imageView;
	}

	@Override
	public void handleMessage(Message msg) // 处理消息
	{
		switch (msg.what) {

		case 0:
			// imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.recorder_animate_01));
			break;

		case 1:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_01));
			break;

		case 2:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_02));
			break;

		case 3:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_03));
			break;

		case 4:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_04));
			break;

		case 5:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_05));
			break;

		case 6:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_06));
			break;

		case 7:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_07));
			break;
		case 8:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_08));
			break;
		case 9:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_09));
			break;
		case 10:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_10));
			break;
		case 11:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_11));
			break;
		case 12:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_12));
			break;
		case 13:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_13));
			break;
		case 14:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_14));
			break;
		case 15:
			imageView.setImageDrawable(context.getResources().getDrawable(
					R.drawable.recorder_animate_15));
			break;
		}

	}
}