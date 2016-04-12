package com.util.tool;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import com.hw.chineseLearn.base.CustomApplication;

public class MediaPlayerHelper {

	private SoundPool soundPool;
	private int sound;
	boolean loaded = false;

	public MediaPlayerHelper(String assetsPath) {
		if (soundPool == null) {
			soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
		}

		AssetManager am = CustomApplication.app.getAssets();
		AssetFileDescriptor afd = null;
		try {
			afd = am.openFd(assetsPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (afd != null) {
			sound = soundPool.load(afd, 1);
		} else {
			sound = soundPool.load(assetsPath, 1);
		}
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true;
				play();
			}
		});
	}

	public void setLoad(String assetsPath) {
		AssetManager am = CustomApplication.app.getAssets();
		try {
			AssetFileDescriptor afd = am.openFd(assetsPath);
			sound = soundPool.load(afd, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void play() {
		if (loaded) {
			soundPool.stop(sound);
			soundPool.play(sound, 1, 1, 0, 0, 1);
		}
	}

	public void stop() {
		soundPool.stop(sound);
		release();
	}

	public void release() {
		soundPool.release();
	}

}
