package com.util.tool;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

/**
 * 
 * @author YH
 */
public class VmChangeRunable implements Runnable {
	int vmValue = 0;
	Context context;
	ImageView imageView;

	public VmChangeRunable(int vmValue, Context context, ImageView imageView) {
		this.vmValue = vmValue;
		this.context = context;
		this.imageView = imageView;
	}

	@Override
	public void run() {
		Looper.prepare();
		while (true) {
			try {
				Looper mainLooper = Looper.getMainLooper();
				MyHandler handler = new MyHandler(mainLooper, context,
						imageView);
				Object obj = null;
				Message msg = Message.obtain();

				switch (vmValue) {
				case 0:
					msg.what = 0;
					msg.obj = obj;
					break;
				case 1:
					msg.what = 1;
					msg.obj = obj;
					break;
				case 2:
					msg.what = 2;
					msg.obj = obj;
					break;
				case 3:
					msg.what = 3;
					msg.obj = obj;

					break;
				case 4:
					msg.what = 4;
					msg.obj = obj;
					break;
				case 5:
					msg.what = 5;
					msg.obj = obj;
					break;
				case 6:
					msg.what = 6;
					msg.obj = obj;
					break;
				case 7:
					msg.what = 7;
					msg.obj = obj;
					break;
				case 8:
					msg.what = 8;
					msg.obj = obj;
					break;
				case 9:
					msg.what = 9;
					msg.obj = obj;
					break;
				case 10:
					msg.what = 10;
					msg.obj = obj;
					break;
				case 11:
					msg.what = 11;
					msg.obj = obj;
					break;
				case 12:
					msg.what = 12;
					msg.obj = obj;
					break;
				case 13:
					msg.what = 13;
					msg.obj = obj;
					break;
				case 14:
					msg.what = 14;
					msg.obj = obj;
					break;
				case 15:
					msg.what = 15;
					msg.obj = obj;
					break;
				}
				handler.sendMessage(msg);
				Thread.sleep(1000);// 1秒钟

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}