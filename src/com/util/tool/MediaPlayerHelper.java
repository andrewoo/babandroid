package com.util.tool;

import java.io.FileDescriptor;
import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;

import com.hw.chineseLearn.base.CustomApplication;

public class MediaPlayerHelper {

	private SoundPool soundPool;
	private int sound;
	boolean loaded = false;
	
	public MediaPlayerHelper(String assetsPath){
		if(soundPool==null){
			soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC, 5);
		}
		
		AssetManager am = CustomApplication.app.getAssets();
		AssetFileDescriptor afd = null;
		try {
			afd = am.openFd(assetsPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sound = soundPool.load(afd, 1);
	}

	public void play() {
		
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                    int status) {
            	loaded=true;
            	soundPool.stop(sound);
            	soundPool.play(sound,  1, 1, 0, 0, 1);
            }
        });
		if(loaded){
			soundPool.stop(sound);
        	soundPool.play(sound,  1, 1, 0, 0, 1);
		}

	}

	public void stop() {
		soundPool.stop(sound);
	}

	public void release() {
		soundPool.release();
	}

}
