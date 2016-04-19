package com.util.tool;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

public class MediaPlayUtil {
	private static MediaPlayUtil mMediaPlayUtil;
	private MediaPlayer mMediaPlayer;

	public static MediaPlayUtil getInstance() {
		if (mMediaPlayUtil == null) {
			mMediaPlayUtil = new MediaPlayUtil();
		}
		return mMediaPlayUtil;
	}

	private MediaPlayUtil() {
		mMediaPlayer = new MediaPlayer();
	}

//	AssetFileDescriptor afd = am.openFd("shit.mp3");  
//    m2 = new MediaPlayer();  
//    m2.setDataSource(afd.getFileDescriptor());  
//    m2.prepare();
	public void play(AssetFileDescriptor afd) {
		if (mMediaPlayer == null) {
			return;
		}
		try {
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(afd.getFileDescriptor(),  afd.getStartOffset(),afd.getLength());
			mMediaPlayer.prepare();
			mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					mMediaPlayer.start();
				}
			});
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void play(String soundFilePath) {
		if (mMediaPlayer == null) {
			return;
		}
		try {
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(soundFilePath);
			mMediaPlayer.prepare();
			mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					mMediaPlayer.start();
				}
			});
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		if (mMediaPlayer != null) {
			mMediaPlayer.pause();
		}
	}

	public void stop() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
		}
	}

	public int getCurrentPosition() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			return mMediaPlayer.getCurrentPosition();
		} else {
			return 0;
		}
	}

	public int getDutation() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			return mMediaPlayer.getDuration();
		} else {
			return 0;
		}
	}
	
	public void release(){
		if (mMediaPlayer != null) {
			mMediaPlayUtil=null;
			mMediaPlayer.release();
		}
	}

	public boolean isPlaying() {
		if (mMediaPlayer != null) {
			return mMediaPlayer.isPlaying();
		} else {
			return false;
		}
	}

	public void setPlayOnCompleteListener(
			MediaPlayer.OnCompletionListener playOnCompleteListener) {
		if (mMediaPlayer != null) {
			mMediaPlayer.setOnCompletionListener(playOnCompleteListener);
		}
	}

}
