package com.util.tool;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Handler;

import com.hw.chineseLearn.db.DatabaseHelper;

/**
 * 
 * @author 
 * 录音类
 * 
 */
public class AudioRecorder
{

	public static String RecRoot = "";
	/**
	 * 录音后缀
	 */
	public static String RecExt = ".amr";

	/**
	 * 录制音频/视频的类
	 */
	public final MediaRecorder recorder = new MediaRecorder();

	final String path;
	final String TAG = "AudioRecorder";
	public Handler mHandler = null;
	public Runnable mUpdateTimer = null;
	private VMChangeListener mVMChangListener;

	public AudioRecorder(String fileName)
	{
		AudioRecorder.RecRoot = DatabaseHelper.CACHE_DIR_SOUND + "/";
		this.path = sanitizePath(AudioRecorder.RecRoot + fileName);

	}

	private String sanitizePath(String path)
	{
		if (!path.startsWith("/"))
		{
			path = "/" + path;
		}
		if (!path.contains("."))
		{
			path += AudioRecorder.RecExt;
		}
		return path;
	}

	public void start() throws IOException
	{
		String state = android.os.Environment.getExternalStorageState();
		if (!state.equals(android.os.Environment.MEDIA_MOUNTED))
		{
			// throw new IOException("SD Card is not mounted,It is  " + state +
			// ".");
		}
		File dirRoot = new File(path).getParentFile().getParentFile();
		if (!dirRoot.exists() && !dirRoot.mkdirs())
		{
			throw new IOException("Path to file could not be created");
		}
		File directory = new File(path).getParentFile();
		if (!directory.exists() && !directory.mkdirs())
		{
			throw new IOException("Path to file could not be created");
		}

		// 设置音频的来源，其值可以通过MediaRecoder内部类的
		// MediaRecorder.AudioSource的几个常量来设置，
		// 通常设置的值MIC：来源于麦克风
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

		// 设置输出文件的格式，其值可以通过MediaRecoder内部类MediaRecorder.OutputFormat的一些常量字段来设置。

		recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
		// 设置刻录的音频编码，
		// 其值可以通过MediaRecoder内部类的MediaRecorder.AudioEncoder的几个常量：AAC、AMR_NB、AMR_WB、DEFAULT
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		// 设置输出文件的路径
		recorder.setOutputFile(path);
		recorder.prepare();
		recorder.start();
		stoped = false;
		mHandler = new Handler();

		mUpdateTimer = new Runnable() {
			public void run()
			{
				updateVolume();
			}
		};

		Thread t1 = new Thread(mUpdateTimer);
		t1.start();

	}

	public void stop() throws IOException 
	{
		stoped = true;
		recorder.stop();  

		recorder.release();
	}
	
	public void reset() 
	{
		stoped = true;

		recorder.reset();
	}

	boolean stoped = false;

	public interface VMChangeListener
	{
		public int onVMChanged(int value);
	}

	public void setVMChangeListener(VMChangeListener listener)
	{
		mVMChangListener = listener;
	}

	private void updateVolume()
	{
		if (stoped)
			return;
		if (mVMChangListener != null)
		{
			mVMChangListener.onVMChanged(1);
		}

		mHandler.postDelayed(mUpdateTimer, 500);
	}
}
