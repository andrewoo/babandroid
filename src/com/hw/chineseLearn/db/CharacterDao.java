package com.hw.chineseLearn.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hw.chineseLearn.model.CharPartBaseModel;
import com.hw.chineseLearn.model.CharacterBaseModel;

public class CharacterDao {
	private static final String TAG = "CharacterDao";
	private static final String tabName = "Character";
	private SQLConnection dbOpenHelper;

	// 在personDao被new出来的时候 就完成初始化

	public CharacterDao(Context context) {
		dbOpenHelper = new SQLConnection(context);
	}

	/**
	 * 往数据库添加一条数据
	 */
	// public boolean add(CharPartBaseModel vo, String type)
	// {
	// SQLiteDatabase db = null;
	// boolean result = find(vo.getCharId());
	// if (!result){//不存在再添加
	// System.out.println("可以添加好友");
	// try{
	// db = dbOpenHelper.getWritableDatabase();
	// if (db.isOpen()){
	//
	// db.execSQL(
	// "insert into "
	// + CharPartDao.tabName
	// +
	// " (id,pic,realName,uname,nicheng,address,mobile,type) values (?,?,?,?,?,?,?,?)",
	// new Object[]
	// { vo.getId(), vo.getPic(), vo.getRealName(), vo.getUname(),
	// vo.getNicheng(), vo.getAddress(), vo.getMobile(), type });
	// // 关闭数据库 释放数据库的链接
	// db.close();
	// System.out.println("插入好友成功！");
	// return true;
	// }
	// } catch (Exception e)
	// {
	// e.printStackTrace();
	// System.out.println("插入好友失败！失败原因："+e);
	// return false;
	// }finally{
	// db.close();
	// }
	// }else{
	// System.out.println("好友已存在，无需插入");
	// }
	// return false;
	// }

	/**
	 * 更新数据库,应该查检是否存在,如果不存在就添加,如果存在就更新
	 * 
	 * @param vo
	 * @param type
	 */
	// public void update(CharPartBaseModel vo, String type)
	// {
	// boolean result = find(vo.getCharId());
	// if (result){
	// System.out.println("数据已存在，不用更新！");
	// return;
	// }
	//
	// try
	// {
	// SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	//
	// String sql = "update " + CharPartDao.tabName
	// +
	// " set id = ?, pic = ?, realName = ?,uname = ?,nicheng = ?,address = ?,mobile = ?,type = ?";
	// Object args[] = new Object[] {
	// vo.getId(), vo.getPic(), vo.getRealName(), vo.getUname(),
	// vo.getNicheng(), vo.getAddress(), vo.getMobile(), type
	//
	// };
	//
	// if (db.isOpen())
	// {
	//
	// db.execSQL(sql, args);
	// // db.execSQL(
	// // "update "
	// // + ChatFriendDao.tabName
	// // +
	// " set id,pic,realName,uname,nicheng,address,mobile,type values (?,?,?,?,?,?,?,?)",
	// // new Object[]
	// // { vo.getId(), vo.getPic(), vo.getRealName(), vo.getUname(),
	// // vo.getNicheng(), vo.getAddress(), vo.getMobile(), type });
	//
	// // 关闭数据库 释放数据库的链接
	// db.close();
	// }
	//
	// } catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 查找数据库的操作
	 */
	public boolean find(int id) {
		boolean result = false;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + CharacterDao.tabName
					+ " where id=?", new String[] { "" + id });

			int count = cursor.getCount();
			if (count > 0) {
				result = true;
			}
			/*
			 * if (cursor.moveToFirst()) { // 得到name在表中是第几列 int index =
			 * cursor.getColumnIndex("name"); String name =
			 * cursor.getString(index); Log.i(TAG, "name =" + name); result =
			 * true; }
			 */
			// 记得关闭掉 cursor
			cursor.close();
			// 释放数据库的链接
			db.close();
		}
		return result;
	}

	/**
	 * 删除一条记录
	 * 
	 * @param name
	 */
	// public void delete(Long id, String type)
	// {
	//
	// if (!find(id, type))
	// {
	// Log.e(TAG, "无此数据");
	// return;
	// }
	// SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	// if (db.isOpen())
	// {
	// try
	// {
	// db.execSQL("delete from " + CharPartDao.tabName +
	// " where id =? and type = ?",
	// new Object[]
	// { id, "'"+type+"'" });
	//
	// Log.e("chatUserDao", "删除成功");
	// } catch (Exception e)
	// {
	// Log.e("chatUserDao", "无此记录");
	// } finally
	// {
	// db.close();
	// }
	//
	// }
	// }

	// public boolean delete(String where)
	// {
	// SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	// if (db.isOpen())
	// {
	// try
	// {
	// db.execSQL("delete from " + CharPartDao.tabName + " " + where);
	// return true;
	// } catch (Exception e)
	// {
	// Log.e("chatUserDao", "无此记录");
	// return false;
	// } finally
	// {
	// db.close();
	// }
	//
	// }
	// return false;
	// }

	public List<CharacterBaseModel> getCharacterVo(String whereStr) {
		List<CharacterBaseModel> vos = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		try {
			if (db.isOpen()) {
				vos = new ArrayList<CharacterBaseModel>();
				Cursor cursor = db.rawQuery("select * from "
						+ CharacterDao.tabName + " " + whereStr, null);
				while (cursor.moveToNext()) {
					CharacterBaseModel vo = new CharacterBaseModel();
					// private String CEE;
					// private String CEJ;
					// private String CEK;
					// private String CES;
					// private int CharId;//
					// private String CharPath;
					// private int LevelIndex;//
					// private String PEE;
					// private String PEJ;
					// private String PEK;
					// private String PES;
					// private String Pinyin;//
					// private int Version;
					// private String WCharacter;

					int CEEIndex = cursor.getColumnIndex("CEE");
					int CEJIndex = cursor.getColumnIndex("CEJ");
					int CEKIndex = cursor.getColumnIndex("CEK");
					int CESIndex = cursor.getColumnIndex("CES");
					int idIndex = cursor.getColumnIndex("CharId");
					int CharPathIndex = cursor.getColumnIndex("CharPath");
					int LevelIndexIndex = cursor.getColumnIndex("LevelIndex");
					int PEEIndex = cursor.getColumnIndex("PEE");
					int PEJIndex = cursor.getColumnIndex("PEJ");
					int PEKIndex = cursor.getColumnIndex("PEK");
					int PESIndex = cursor.getColumnIndex("PES");
					int PinyinIndex = cursor.getColumnIndex("Pinyin");
					int VersionIndex = cursor.getColumnIndex("Version");
					int WCharacterIndex = cursor.getColumnIndex("WCharacter");

					String CEE = cursor.getString(CEEIndex);
					String CEJ = cursor.getString(CEJIndex);
					String CEK = cursor.getString(CEKIndex);
					String CES = cursor.getString(CESIndex);

					int CharId = cursor.getInt(idIndex);
					String CharPath = cursor.getString(CharPathIndex);
					int LevelIndex = cursor.getInt(LevelIndexIndex);

					String PEE = cursor.getString(PEEIndex);
					String PEJ = cursor.getString(PEJIndex);
					String PEK = cursor.getString(PEKIndex);
					String PES = cursor.getString(PESIndex);

					String Pinyin = cursor.getString(PinyinIndex);
					int Version = cursor.getInt(VersionIndex);
					String WCharacter = cursor.getString(WCharacterIndex);

					vo.setCEE(CEE);
					vo.setCEJ(CEJ);
					vo.setCEK(CEK);
					vo.setCES(CES);
					vo.setCharId(CharId);
					vo.setCharPath(CharPath);
					vo.setLevelIndex(LevelIndex);
					vo.setPEE(PEE);
					vo.setPEJ(PEJ);
					vo.setPEK(PEK);
					vo.setPES(PES);
					vo.setPinyin(Pinyin);
					vo.setVersion(Version);
					vo.setWCharacter(WCharacter);
					vos.add(vo);
				}
				cursor.close();
				db.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "无此项");
		} finally {
			db.close();
		}

		return vos;
	}
}