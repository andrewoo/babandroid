package com.hw.chineseLearn.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.util.tool.AppFinal;
import com.util.tool.DateUtil;

public class SQLConnection extends SQLiteOpenHelper {
	private static String TAG = "==SQLConnnection==";
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

	public SQLConnection(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		
		
//		SQLiteDatabase db = db.openOrCreateDatabase("1", null);
		

		if (isFirstRun) {
			Log.v(TAG + "--onCreate()", "程序是第一次运行");
		} else {
			Log.v(TAG + "SQLConnection--onCreate()", "程序不是第一次运行");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		Log.d(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ".");

		switch (newVersion) {

		case 1:
			break;

		default:
			break;
		}
	}

}
