package com.util.tool;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

public class NotificationManagerTools {
	private static final String TAG = "NmTools";

	private static Context mContext;
	private static NotificationManagerTools mInstance;

	private NotificationManagerTools(Context context) {
		mContext = context;
	}

	public static void init(Context context) {
		mInstance = new NotificationManagerTools(context);
	}

	public static NotificationManagerTools getInstance() {
		return mInstance;
	}

    
    private void showSound(){
		try {
			Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			final AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
				final MediaPlayer mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(mContext, uri);
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer player) {
						mediaPlayer.release();
					}
				});
				mediaPlayer.prepare();
				mediaPlayer.start();
			}
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void showVibrate(){
    	Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    	long[] pattern = new long[]{100,200,50,500};
    	vibrator.vibrate(pattern, -1);
    }

}
