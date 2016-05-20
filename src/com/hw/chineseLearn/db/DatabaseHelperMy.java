package com.hw.chineseLearn.db;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.hw.chineseLearn.base.CustomApplication;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

public class DatabaseHelperMy extends OrmLiteSqliteOpenHelper {

	private static DatabaseHelperMy instance;

	public static final String DATABASE_PATH = CustomApplication.app
			.getFilesDir() + "/Babble_ub.db";

	private AndroidConnectionSource connectionSource;
	private static String TAG = "==DatabaseHelper==";
	public static final int DATABASE_VERSION = 1;// 数据库版本

	public static final String CACHE_DIR_LOG;
	public static final String CACHE_DIR;
	public static final String CACHE_DIR_DB;
	public static final String CACHE_DIR_DOWNLOAD;
	public static final String CACHE_DIR_MAPPKGS;

	public static final String CACHE_DIR_IMAGE;
	public static final String DATABASE_NAME; // 数据库名称
	public static final String REPORT_FORM;// 数据报表
	static Boolean isFirstRun = false;
	public static Boolean isFirstRunForApplication = false;

	public static final String SOUND_PATH;
	public static final String CONTENT_JSON_PATH;
	public static final String LESSON_SOUND_PATH;

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
		CACHE_DIR_MAPPKGS = CACHE_DIR + "/mapPkgs";

		SOUND_PATH = CACHE_DIR_DOWNLOAD + "/kit";// kit界面 解压后声音存放位置
		CONTENT_JSON_PATH = CACHE_DIR_DOWNLOAD + "/contentJson";// 解压fluent内容zip后存放的路径
		LESSON_SOUND_PATH = CACHE_DIR_DOWNLOAD + "/lessonVoice";// 解压fluent内容zip后存放的路径

		File file = new File(CONTENT_JSON_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(LESSON_SOUND_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(CACHE_DIR);
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

	public DatabaseHelperMy(Context context) {
		super(context, "chineselearn.db", null, 1);
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

	public static synchronized DatabaseHelperMy getHelper(Context context) {
		if (instance == null) {
			synchronized (DatabaseHelperMy.class) {
				if (instance == null)
					instance = new DatabaseHelperMy(context);
			}
		}
		return instance;
	}

}
