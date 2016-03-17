package com.hw.chineseLearn.dao;

import java.sql.SQLException;

import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.db.DatabaseHelper;
import com.j256.ormlite.dao.Dao;

public class MyDao {

	private static Dao dao;

	public static Dao getDao(Class clazz) {

		if (dao == null)
			try {
				DatabaseHelper helper = DatabaseHelper
						.getHelper(CustomApplication.app);
				dao = helper.getDao(clazz);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		return dao;
	}
}
