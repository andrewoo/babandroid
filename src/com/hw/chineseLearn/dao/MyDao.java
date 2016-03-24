package com.hw.chineseLearn.dao;

import java.sql.SQLException;

import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.bean.LGModel_Word_010;
import com.hw.chineseLearn.dao.bean.LGWord;
import com.hw.chineseLearn.dao.bean.LessonRepeatRegex;
import com.hw.chineseLearn.db.DatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

public class MyDao {

	private static Dao dao;

	public static Dao getDao(Class clazz) {
		 try {
		 DatabaseHelper helper = DatabaseHelper
		 .getHelper(CustomApplication.app);
		 dao = helper.getDao(clazz);
		 } catch (SQLException e) {
		 e.printStackTrace();
		 }
		 return dao;

	}

	public static String getAnswer(LessonRepeatRegex lessonRepeatRegex) {
		if (lessonRepeatRegex.getLgTable()==0 && lessonRepeatRegex.getRandomSubject()==1) {
			try {
				Dao word010Dao = getDao(LGModel_Word_010.class);
				LGModel_Word_010 word010 = (LGModel_Word_010) word010Dao
						.queryBuilder().where()
						.eq("WordId", lessonRepeatRegex.getLgTableId())
						.queryForFirst();
				System.out.println("word010Dao"+word010Dao);
				LGWord lGWord = (LGWord) getDao(LGWord.class).queryForId(word010.getAnswer());
				return lGWord.getTranslations() + "=" + lGWord.getWord() + "/"
						+ lGWord.getPinyin();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "Unknow";
	}
}
