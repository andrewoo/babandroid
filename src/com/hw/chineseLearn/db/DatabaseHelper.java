package com.hw.chineseLearn.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hw.chineseLearn.base.CustomApplication;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

  
public class DatabaseHelper extends OrmLiteSqliteOpenHelper 
{
	

	private static DatabaseHelper instance;
	
	public static final String DATABASE_PATH = CustomApplication.app.getFilesDir() + "/chineselearn.db";
	
	 private AndroidConnectionSource connectionSource;

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
		super(context, "chineselearn.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		
	}
	
	public static synchronized DatabaseHelper getHelper(Context context)
	{
		if (instance == null)
		{
			synchronized (DatabaseHelper.class)
			{
				if (instance == null)
					instance = new DatabaseHelper(context);
			}
		}
		return instance;
	}
	
}  
