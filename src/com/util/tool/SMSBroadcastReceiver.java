package com.util.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSBroadcastReceiver extends BroadcastReceiver {
	private static MessageListener mMessageListener;
	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

	public SMSBroadcastReceiver() {
		super();
	}

	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for (Object pdu : pdus) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
				String sender = smsMessage.getDisplayOriginatingAddress();
				// 短信内容
				String content = smsMessage.getDisplayMessageBody();
				long date = smsMessage.getTimestampMillis();
				Date tiemDate = new Date(date);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String time = simpleDateFormat.format(tiemDate);
				Log.d("发送者：", "" + sender);

				// 注册验证码
				if ("106905705705043760".equals(sender)) {

					Log.d("短信内容：", "" + content);
					// 【有通告】验证码:5417，欢迎您注册有通告，10分钟内有效！
					if (content != null && !"".equals(content)) {
						content = content.substring(9, 13);
						Log.d("截取后的短信内容：", "" + content);
						mMessageListener.onReceived(content);
					}
				}
			}
		}

	}

	// 回调接口
	public interface MessageListener {
		public void onReceived(String message);
	}

	public void setOnReceivedMessageListener(MessageListener messageListener) {
		this.mMessageListener = messageListener;
	}
}
