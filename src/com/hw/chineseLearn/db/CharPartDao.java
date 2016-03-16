package com.hw.chineseLearn.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hw.chineseLearn.model.CharPartBaseModel;

public class CharPartDao {
	private static final String TAG = "CharPartDao";
	private static final String tabName = "CharPart";
	private SQLConnection dbOpenHelper;

	// 在personDao被new出来的时候 就完成初始化

	public CharPartDao(Context context) {
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
			Cursor cursor = db.rawQuery("select * from " + CharPartDao.tabName
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

	public List<CharPartBaseModel> getCharPartVo(String whereStr) {
		List<CharPartBaseModel> vos = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		try {
			if (db.isOpen()) {
				vos = new ArrayList<CharPartBaseModel>();
				Cursor cursor = db.rawQuery("select * from "
						+ CharPartDao.tabName + " " + whereStr, null);
				while (cursor.moveToNext()) {
					CharPartBaseModel vo = new CharPartBaseModel();
					// private int CharId;//
					// private String PartDirection;//
					// private int PartId;
					// private int PartIndex;//
					// private String PartPath;
					// private int Version;
					int idIndex = cursor.getColumnIndex("CharId");
					int PartDirectionIndex = cursor
							.getColumnIndex("PartDirection");
					int PartIdIndex = cursor.getColumnIndex("PartId");
					int PartIndexIndex = cursor.getColumnIndex("PartIndex");
					int PartPathIndex = cursor.getColumnIndex("PartPath");
					int VersionIndex = cursor.getColumnIndex("Version");

					int CharId = cursor.getInt(idIndex);
					String PartDirection = cursor.getString(PartDirectionIndex);
					int PartId = cursor.getInt(PartIdIndex);
					int PartIndex = cursor.getInt(PartIndexIndex);
					String PartPath = cursor.getString(PartPathIndex);
					int Version = cursor.getInt(VersionIndex);

					vo.setCharId(CharId);
					vo.setPartDirection(PartDirection);
					vo.setPartId(PartId);
					vo.setPartIndex(PartIndex);
					vo.setPartPath(PartPath);
					vo.setVersion(Version);

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