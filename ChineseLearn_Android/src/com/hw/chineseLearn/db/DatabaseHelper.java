package com.hw.chineseLearn.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.interfaces.AppConstants;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.util.tool.FileTools;

import java.io.File;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static DatabaseHelper instance;

//	public static final String DATABASE_PATH = CustomApplication.app.getFilesDir() + "/chineselearn.db";
	public static final String DATABASE_PATH = CustomApplication.app.getFilesDir() + "/"+ AppConstants.DBName;

	private AndroidConnectionSource connectionSource;
	private static String TAG = "==DatabaseHelper==";
	public static final int DATABASE_VERSION = 1;// 数据库版本

	public static final String CACHE_DIR_LOG;
	public static final String CACHE_DIR;
	public static final String CACHE_DIR_DB;
	public static final String CACHE_DIR_SOUND;
	public static final String CACHE_DIR_DOWNLOAD;
	public static final String CACHE_DIR_MAPPKGS;

	public static final String CACHE_DIR_IMAGE;
	public static final String DATABASE_NAME; // 数据库名称
	public static final String REPORT_FORM;// 数据报表
	static Boolean isFirstRun = false;
	public static Boolean isFirstRunForApplication = false;

	static {

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			CACHE_DIR = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/babbleApp";
		} else {
			CACHE_DIR = Environment.getRootDirectory().getAbsolutePath()
					+ "/babbleApp";
		}
		CACHE_DIR_DB = CACHE_DIR + "/db";
		CACHE_DIR_DOWNLOAD = CACHE_DIR + "/download";

		CACHE_DIR_IMAGE = CACHE_DIR + "/image";
		DATABASE_NAME = CACHE_DIR_DB + "/babbleApp.db";
		REPORT_FORM = CACHE_DIR + "/ReportForm";
		CACHE_DIR_LOG = CACHE_DIR + "/log";
		CACHE_DIR_SOUND = CACHE_DIR + "/rec";
		CACHE_DIR_MAPPKGS = CACHE_DIR + "/mapPkgs";

		File file = new File(CACHE_DIR);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(CACHE_DIR_LOG);
		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(CACHE_DIR_DB);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(CACHE_DIR_SOUND);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(CACHE_DIR_DOWNLOAD);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(CACHE_DIR_IMAGE);
		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(REPORT_FORM);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 创建map文件夹
		file = new File(CACHE_DIR_MAPPKGS);
		if (!file.exists()) {
			file.mkdirs();
		}
		if (!(new File(DATABASE_NAME).exists())) {
			isFirstRun = true;
			isFirstRunForApplication = true;

		} else {
			isFirstRun = false;
			isFirstRunForApplication = false;
		}

		FileTools.copyDb("Babble_ub.db");
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		return SQLiteDatabase.openDatabase(DATABASE_PATH, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	public synchronized SQLiteDatabase getReadableDatabase() {
		return SQLiteDatabase.openDatabase(DATABASE_PATH, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	public DatabaseHelper(Context context) {
		super(context, AppConstants.DBName, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		if (isFirstRun) {
			Log.v(TAG + "--onCreate()", "程序是第一次运行");
		} else {
			Log.v(TAG + "SQLConnection--onCreate()", "程序不是第一次运行");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource arg1,
			int oldVersion, int newVersion) {
		Log.d(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ".");

		switch (newVersion) {

		case 1:
			break;

		default:
			break;
		}
	}

	public static synchronized DatabaseHelper getHelper(Context context) {
		if (instance == null) {
			synchronized (DatabaseHelper.class) {
				if (instance == null)
					instance = new DatabaseHelper(context);
			}
		}
		return instance;
	}

}
