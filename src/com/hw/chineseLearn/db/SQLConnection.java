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
					.getAbsolutePath() + "/chineseSkill";
		} else {
			CACHE_DIR = Environment.getRootDirectory().getAbsolutePath()
					+ "/chineseSkill";
		}
		// /storage/emulated/0/SportsOnLine
		CACHE_DIR_DB = CACHE_DIR + "/db";
		CACHE_DIR_DOWNLOAD = CACHE_DIR + "/download";

		CACHE_DIR_IMAGE = CACHE_DIR + "/image";
		DATABASE_NAME = CACHE_DIR_DB + "/redbaby.db";
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

			try {
				String command = "chmod 777 " + DATABASE_NAME;
				Log.i(TAG, "command = " + command);

				// Runtime runtime = Runtime.getRuntime();
				// Process proc = runtime.exec(command);

				// Process process = Runtime.getRuntime().exec("su");
				// DataOutputStream os = new
				// DataOutputStream(process.getOutputStream());
				// os.writeBytes(command + "\n");
				// os.writeBytes("exit\n");
				// os.flush();
				// process.waitFor();
			} catch (Exception e) {
				Log.d(TAG, "chmod fail!!!!");
				e.printStackTrace();
			}
			// catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			Log.d(TAG, "chmod ok");

		}
	}

	public SQLConnection(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {

		if (isFirstRun) {
			Log.v(TAG + "--onCreate()", "程序是第一次运行");
			// createTableSync(db);
			// createTableMembers(db);
			// createTableMatch(db);
			// createTablePlan(db);
			// createTableDicts(db);
			// createTableRecords(db);
			// createTabResultSelection(db);
			// createSpecialDataReport(db);
			// createTabTrainRecord(db);
			// createTabTeamManagerGroup(db);
			// initPlayers(db);
			// initSyncTables(db);
		} else {
			Log.v(TAG + "SQLConnection--onCreate()", "程序不是第一次运行");
		}
	}

	/**
	 * 2015.5.27 新版足球新增字段
	 * chief_coach主教练，leader领队，assistant_coach助理教练，scientific_reserch科研
	 * doctor队医，manager管理
	 */
	public void initPlayers(SQLiteDatabase db) {
		Log.v("SQLConnection--onCreate()", "initPlayers");
		db.execSQL("insert into leoet_team (the_id,team_name,summary,coach_id,op_type,op_time,chief_coach,leader,assistant_coach,scientific_reserch,doctor,manager,team_gender) values (1,'团队1','团结奋进，争创一流！挑战运动极限，演绎健美人学！',1,1,"
				+ DateUtil.getNowTime() + ",'1','1','1','1','1','1'," + 1 + ")");

		// sql = " CREATE TABLE " + SQLColumnFinal.TABLE_PLAYER + " ("
		// + " [the_id] INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
		//
		// + " [op_type]       INTEGER," + " [op_time]        INTEGER,"
		// + " [address]       TEXT," + " [zhiye]         TEXT,"
		// + " [player_group]         TEXT," + " [project]       TEXT,"
		// + " [base_heart]       TEXT," + " [max_heart]       TEXT,"
		// + " [wing_span]     TEXT," + " [best_num]      TEXT,"
		// + " [best_grade]    TEXT," + " [phone]         TEXT,"
		// + " [path] 			TEXT," + " [player_rId]      INTEGER,"
		// + " [player_name]     TEXT," + " [gender]          INTEGER,"
		// + " [player_number]       INTEGER,"
		// + " [player_position]       INTEGER,"
		// + " [birthday]        TEXT," + " [join_team_time]  TEXT,"
		// + " [height]          INTEGER," + " [weight]          REAL,"
		// + " [work_time]       TEXT," + " [grade]           TEXT,"
		// + " [norm1]           REAL," + " [norm2]           REAL,"
		// + " [norm3]           REAL," + " [team_id]         INTEGER,"
		// + " [last_tempo]    REAL," + " [last_heart_rate] REAL,"
		// + " [last_speed]      REAL," + " [blood_style]  TEXT,"
		// + " [id_number]    TEXT );";

		for (int i = 1; i <= 26; i++) {
			String num = "" + i;
			if (i < 10) {
				num = "0" + num;
			}
			db.execSQL("insert into leoet_player (the_id,gender,height,player_name,player_number,team_id,op_type,op_time,"
					+ "address,player_group,base_heart,wing_span,best_grade,path,birthday,join_team_time,work_time,grade,blood_style) values ("
					+ i
					+ ",1,178,'队员"
					+ num
					+ "',"
					+ i
					+ ",1,1,"
					+ DateUtil.getNowTime()
					+ ",'','','','','','','','','','','')");
		}
	}

	/**
	 * 初始化同步表
	 * 
	 * @param db
	 */
	private void initSyncTables(SQLiteDatabase db) {
		Log.v("SQLConnection--onCreate()", "initSyncTables，创建同步表");
		// TODO Auto-generated method stub
		// String sql = "CREATE TABLE "+ SQLColumnFinal.TABLE_LEOET_SYNC
		// + " (" + " [the_id] 	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
		// + " [pad_sn]       TEXT,"
		// + " [table_name] 	TEXT,"
		// + " [update_time] 	INTEGER,"
		// + " [sync_time] 	INTEGER);";

		List<String> tableNames = new ArrayList<String>();
		tableNames.add(SQLColumnFinal.TABLE_COACH);
		tableNames.add(SQLColumnFinal.TABLE_DEVICE);
		tableNames.add(SQLColumnFinal.TABLE_LEOET_DICT_MAIN);
		tableNames.add(SQLColumnFinal.TABLE_LEOET_DICT_SUB);
		tableNames.add(SQLColumnFinal.TABLE_RESULT_SELECTION_TABLE);
		tableNames.add(SQLColumnFinal.TABLE_LEOET_MATCH_COLLECT);
		tableNames.add(SQLColumnFinal.TABLE_LEOET_MATCH);
		tableNames.add(SQLColumnFinal.TABLE_LEOET_MATCH_SHIP);
		tableNames.add(SQLColumnFinal.TABLE_LEOET_MATCH_PERSON);
		tableNames.add(SQLColumnFinal.TABLE_LEOET_HEART_BEAT);
		tableNames.add(SQLColumnFinal.TABLE_PLAYER);
		tableNames.add(SQLColumnFinal.TABLE_TEAM);
		tableNames.add(SQLColumnFinal.TABLE_USER);
		tableNames.add(SQLColumnFinal.TABLE_GROUP);
		tableNames.add("leo_train_record");

		// for (int i = 1; i <= tableNames.size(); i++) {
		// String tabName = tableNames.get(i - 1);
		// db.execSQL("insert into " + SQLColumnFinal.TABLE_LEOET_SYNC
		// + " (the_id,pad_sn,table_name,update_time,sync_time) "
		// + "values (" + i + ",'" + uniqueId + "','" + tabName + "',"
		// + DateUtil.getNowTime() + ",0)");
		// }
	}

	/*
	 * 数据同步表 在pad端每个数据表中都添加字段 op_time（最后操作时间）
	 * op_type（最后执行的操作1:insert，2:update，0:delete） 执行流程：
	 * 1、首先PAD发起请求获取服务端的数据同步表数据。 2、使用PAD数据同步表（leoet_sync）中的时间进行对比。
	 * a)如果PAD中的update_time大于 服务器sync_time ，此时执行Pad端到服务端的同步。
	 * 同步成功后服务器修改sync_time的值为PAD的table_time时间, 同时PAD修改sync_time更新为table_time
	 * b)如果PAD中的sync_time 小于 服务器update_time ，
	 * 此时执行服务器端到PAD中的同步，同步完成后修改sync_time的值以最新的为准。
	 */
	private void createTableSync(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.v("SQLConnection--onCreate()", "createTableSync，创建同步表");
		String sql = "CREATE TABLE  " + SQLColumnFinal.TABLE_LEOET_SYNC + " ("
				+ " [the_id] 	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
				+ " [pad_sn]       TEXT," + " [table_name] 	TEXT,"
				+ " [update_time] 	INTEGER," + " [sync_time] 	INTEGER);";
		db.execSQL(sql);
	}

	private void createTabResultSelection(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.v("SQLConnection--onCreate()", "createTabResultSelection，创建时间选择的表");

		String sql = "CREATE TABLE "
				+ SQLColumnFinal.TABLE_RESULT_SELECTION_TABLE + " ("
				+ " [the_id] 	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ " [match_id] 	INTEGER," + "[ship_id] INTEGER,"
				+ " [start_time] 	INTEGER," + " [end_time] 	INTEGER);";
		db.execSQL(sql);
	}

	/**
	 * 成员相关表创建
	 */
	public void createTableMembers(SQLiteDatabase db) {
		Log.v("SQLConnection--onCreate()", "createTableMembers，创建成员相关表");
		/**
		 * 消息记录表,id:人员ID，formId：消息来源 用户表 uid 用户编号， username 用户账号 ，password 用户密码，
		 * regIp 用户注册IP regTime 用户注册时间，lastloginIp 用户最后登录IP ，realName 用户真实姓名
		 * email 用户email ， mobile 用户手机，lastloginTime 用户最后登录时间 loginCount
		 * 登录次数，status 用户状态，1：启用，0：禁用 summary text 用户描述 ，gym_id 用户所属部门
		 * login_count 用户登录次数
		 */

		String sql = "CREATE TABLE "
				+ SQLColumnFinal.TABLE_USER
				+ " ("
				+ " [the_id]          INTEGER   PRIMARY KEY AUTOINCREMENT UNIQUE,"
				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ " [user_name]       TEXT," + " [password]        TEXT,"
				+ "	[reg_ip]          TEXT," + " [reg_time]        INTEGER,"
				+ " [last_login_ip]   TEXT," + " [real_name]       TEXT,"
				+ " [email]           TEXT," + " [mobile]       	  TEXT,"
				+ " [last_login_time] INTEGER," + " [login_count]     INTEGER,"
				+ " [status]          INTEGER," + " [summary]         TEXT,"
				+ " [gym_id]          INTEGER);";
		db.execSQL(sql);

		/**
		 * 教练表 the_id 教练编号, uid 教练对应的账号ID coachName 教练姓名,gender 教练性别：男，女
		 * birthday 出生日期, workTime 工作工龄 is_ retire 是否退休，0：未退休，1：已退休 grade
		 * 教练等级1为一级教练，一次类推
		 */

		sql = "CREATE TABLE " + SQLColumnFinal.TABLE_COACH + " ( "
				+ " [the_id]  INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, "
				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ " [uid]     INTEGER," + " [coach_name]    TEXT,"
				+ " [birthday]  		INTEGER," + " [work_time]  	INTEGER,"
				+ " [gender]  		TEXT," + " [is_retire] 		INTEGER,"
				+ " [grade]     		TEXT );";
		db.execSQL(sql);

		/**
		 * 设备表
		 */
		sql = "CREATE TABLE " + SQLColumnFinal.TABLE_DEVICE + " ("
				+ " [the_id] 	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ " [dev_id] 	INTEGER," + " [num] 	TEXT,"
				+ " [dev_name] 	TEXT);";
		db.execSQL(sql);

		/**
		 * 团队表 team_id 团队id，teamName 团队名称 (gender ：队伍性别) coach_id 教练id summary
		 * 
		 * 2015.5.27 新版足球新增字段
		 * chief_coach主教练，leader领队，assistant_coach助理教练，scientific_reserch科研
		 * doctor队医，manager管理 团队的描述
		 */
		sql = "CREATE TABLE " + SQLColumnFinal.TABLE_TEAM + " ("
				+ " [the_id] 	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ " [team_name] 	TEXT," + " [coach_id] 	INTEGER,"
				+ " [chief_coach] 	TEXT," + " [leader] 	TEXT,"
				+ " [assistant_coach] 	TEXT," + " [scientific_reserch] 	TEXT,"
				+ " [doctor] 	TEXT," + " [manager] 	TEXT,"
				+ " [team_gender]          INTEGER," + " [summary]  	TEXT );";
		db.execSQL(sql);

		/**
		 * 运动员表 player_id path 运动员的相片 ,运动员标号 player_rId 运动员实际编号 playerName
		 * 运动员真实姓名， gender性别：男，女 birthday 出生日期， joinTeamTime 运动员入队时间 height
		 * 运动员身高 （单位cm）， weight运动员体重（单位：kg） workTime tinyint(1) 运动员职业生涯 grade
		 * 运动员等级，1为一级运动员， 一次类推 norm1 指标一，，norm2 指标二，norm3 指标三 team_id 该运动员所属小队
		 * last_tempo最后一次设置 的浆频last_heart_rate最后一次设置的心跳 last_speed最后一次设置的速度
		 * 
		 * player_position 运动员位置 0：前锋 1：中场 2：后卫 3：守门员
		 * 
		 * player_number 运动员编号（整数） 1到24 Detials:
		 * http://zhidao.baidu.com/question/41057584.html
		 * 
		 * 2015.5.27 新版足球新增字段 player_state球员状态,player_positions常踢位置
		 * 
		 */
		sql = " CREATE TABLE " + SQLColumnFinal.TABLE_PLAYER + " ("
				+ " [the_id] INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"

				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ " [address]       TEXT," + " [zhiye]         TEXT,"
				+ " [player_group]         TEXT," + " [project]       TEXT,"
				+ " [base_heart]       TEXT," + " [max_heart]       TEXT,"
				+ " [wing_span]     TEXT," + " [best_num]      TEXT,"
				+ " [best_grade]    TEXT," + " [phone]         TEXT,"
				+ " [path] 			TEXT," + " [player_rId]      INTEGER,"
				+ " [player_name]     TEXT," + " [gender]          INTEGER,"
				+ " [player_number]       INTEGER,"
				+ " [player_position]       INTEGER,"
				+ " [birthday]        TEXT," + " [join_team_time]  TEXT,"
				+ " [height]          INTEGER," + " [weight]          REAL,"
				+ " [work_time]       TEXT," + " [grade]           TEXT,"
				+ " [norm1]           REAL," + " [norm2]           REAL,"
				+ " [norm3]           REAL," + " [team_id]         INTEGER,"
				+ " [last_tempo]    REAL," + " [last_heart_rate] REAL,"
				+ " [last_speed]      REAL," + " [blood_style]  TEXT,"
				+ " [player_state]      TEXT," + " [player_positions]  TEXT,"
				+ " [id_number]    TEXT );";
		db.execSQL(sql);

	}

	/**
	 * 字典相关表创建
	 */
	public void createTableDicts(SQLiteDatabase db) {
		Log.v("SQLConnection--onCreate()", "createTableDicts，字典相关表创建");
		/* leoet_dict_main 属性项表 */

		// the_id int NOT NULL 属性ID
		// the_key text NOT NULL 属性英文名称对应键值表
		// the_text text NOT NULL 显示文本
		// the_desc text NOT NULL 属性说明

		String msgs = "CREATE TABLE " + SQLColumnFinal.TABLE_LEOET_DICT_MAIN
				+ " ( [the_id] INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ "[the_key]  TEXT ," + "[the_text] TEXT ,"
				+ "[the_desc] TEXT) ";
		db.execSQL(msgs);

		/* leoet_dict_sub 属性键值表 */

		// the_id int NOT NULL
		// main_key text NOT NULL 属性英文名称对应键值表
		// sub_key text NOT NULL 属性类别对应值
		// the_text text NOT NULL 显示文本
		// the_desc text NOT NULL 属性说明
		// tag text NOT NULL 附加数据

		String msg = "CREATE TABLE " + SQLColumnFinal.TABLE_LEOET_DICT_SUB
				+ " ([the_id] INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ "[main_key] TEXT ," + "[sub_key]  TEXT ,"
				+ "[the_text] TEXT ," + "[the_desc] TEXT ," + "[tag] TEXT) ";
		db.execSQL(msg);
	}

	/**
	 * 比赛相关表创建
	 */

	public void createTableMatch(SQLiteDatabase db) {
		Log.v("SQLConnection--onCreate()", "createTableMatch，比赛相关表创建");
		// sql语句
		String sql = "";
		/*
		 * 比赛表 coach_id int(11) NOT NULL 教练ID create_time int(11) NOT NULl
		 * 比赛创建时间 item_gender varcahr(2) NOT NULL 比赛日程项目性别 item_type int(11) NOT
		 * NULL 比赛类型，1：静水项目，2：激流回转 item_ship int(11) NOT NULL 船型 item_person
		 * int(4) NOT NULL 划艇人数 item_distance int(4) NOT NULL 项目距离 ship_count
		 * tinyint(1) NOT NULL 赛艇个数 item_count int(4) NOT NULL 比赛次数 status
		 * int(4) NOT NULL 日程状态
		 */
		sql = "CREATE TABLE  " + SQLColumnFinal.TABLE_LEOET_MATCH
				+ "  ( [the_id] INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ,"
				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ " [coach_id] INTEGER ," + " [file_name] TEXT ,"
				+ " [nick_name] TEXT ," + " [collect_state] INTEGER ,"
				+ " [start_time] INTEGER ," + " [end_time] INTEGER ,"
				+ " [params_values] TEXT," + " [model] INTEGER )";
		db.execSQL(sql);

		/*
		 * 比赛船 ship_id int(11) NOT NULL 比赛日对应船只索引ID sc_id int(11) NOT NULL
		 * 比赛日程ID ship_count tinyint(1) NOT NULL 船只人数 startTime int(11) NOT NULL
		 * 比赛开始时间 endTime int(11) NOT NULL 比赛结束时间 status tinyint(1) NOT NULL 1
		 * 船只状态,1：正常，0：故障 start_lat,start_lon,
		 */
		sql = "CREATE TABLE " + SQLColumnFinal.TABLE_LEOET_MATCH_SHIP
				+ " ( [the_id] INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ,"
				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ " [sc_id] INTEGER ," + " [boat_name] TEXT ,"
				+ " [boat_track] INTEGER ," + " [person_count] INTEGER ,"
				+ " [start_lat] REAL ," + " [start_lon] REAL ,"
				+ " [start_time] INTEGER ," + " [end_time] INTEGER ,"
				+ " [time_count] INTEGER , " + " [params_values] TEXT ,"
				+ " [status] INTEGER )";
		db.execSQL(sql);

		/*
		 * 
		 * 船、人、设备 sp_id int(11) NOT NULL 索引ID sc_id int(11) NOT NULL 比赛日程ID
		 * ship_id int(1) NOT NULL 船号 player_id int(11) NOT NULL 运动员ID
		 * min_paddleHz float NOT NULL 0 运动员最低桨频0为默认 max_ paddleHz float NOT
		 * NULL 0 运动员最高浆频0为无限大 min_heartRate float NOT NULL 0 最低心跳 max_heartRate
		 * float NOT NULL 0 最高心跳 min_speed float NOT NULL 0 最低速度 max_speed float
		 * NOT NULL 0 最高速度 device_id int(11) NOT NULL 对应设备ID
		 */
		sql = "CREATE TABLE " + SQLColumnFinal.TABLE_LEOET_MATCH_PERSON
				+ " ( [the_id] INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ " [sc_id] INTEGER ," + " [ship_id] INTEGER ,"
				+ " [player_id] INTEGER ," + " [player_name] TEXT ,"
				+ " [min_paddleHz] REAL ," + " [max_paddleHz] REAL ,"
				+ " [min_hearRete] REAL ," + " [max_hearRate] REAL ,"
				+ " [min_speed] REAL ," + " [max_speed] REAL ,"
				+ " [device_id] INTEGER ," + " [device_name] TEXT ,"
				+ " [device_state] INTEGER ," + " [player_position] INTEGER ,"
				+ " [formation_row] INTEGER , " + " [formation_col] INTEGER , "
				+ " [number_inboat] TEXT)";
		db.execSQL(sql);

		/*
		 * 收藏表
		 */
		sql = "CREATE TABLE " + SQLColumnFinal.TABLE_LEOET_MATCH_COLLECT
				+ " ( [the_id] INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ,"
				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ " [match_id] INTEGER ," + " [user_id] INTEGER ,"
				+ " [create_time] INTEGER )";
		db.execSQL(sql);
	}

	/** 创建的训练计划的相关表 **/
	public void createTablePlan(SQLiteDatabase db) {
		Log.v("SQLConnection--onCreate()", "createTablePlan，创建的训练计划的相关表");
		// sql语句
		String sql = "";
		/*
		 * create table leoet_plan( theid integer primary key AUTOINCREMENT,
		 * train_date integer, time_tag varchar, place varchar, formation
		 * varchar )
		 */
		// 计划表
		sql = "  create table leoet_plan(   "
				+ " [the_id]  integer primary key AUTOINCREMENT,    "
				+ " [train_date] integer,      " + " [time_tag] TEXT,    "
				+ " [place] TEXT,    " + " [formation]  TEXT,   "
				+ " [op_type]       INTEGER, [op_time]        INTEGER)";
		db.execSQL(sql);

		/*
		 * create table leoet_plan_content( theid integer primary key
		 * AUTOINCREMENT, plan_id integer, train_type varchar, start_time
		 * integer, end_time integer, hr_select varchar )
		 */
		// 计划内容表
		sql = "  create table leoet_plan_content(   "
				+ " [the_id]  integer primary key AUTOINCREMENT,    "
				+ " [plan_id] integer,      " + " [train_type] TEXT,    "
				+ " [start_time] integer,    " + " [end_time] integer,    "
				+ " [hr_select]  TEXT,   "
				+ " [op_type]       INTEGER, [op_time]        INTEGER)";
		db.execSQL(sql);

		/*
		 * create table leoet_plan_player( the_id integer primary key
		 * AUTOINCREMENT, plan_id integer, team_tag integer, player_id integer,
		 * dev_id integer, formation_row integer, formation_col integer )
		 */
		// 计划人员和设备表
		sql = "  create table leoet_plan_player(   "
				+ " [the_id]  integer primary key AUTOINCREMENT,    "
				+ " [plan_id] integer,      " + " [team_tag] integer,    "
				+ " [dev_id] integer,      " + " [player_id] integer,    "
				+ " [formation_row] integer,    "
				+ " [formation_col] integer,    "
				+ " [op_type]       INTEGER, [op_time]        INTEGER)";
		db.execSQL(sql);
		// 计划的团队表
		sql = "  create table leoet_plan_group(   "
				+ " [the_id]  integer primary key AUTOINCREMENT,    "
				+ " [plan_id] integer,      " + " [team_tag] integer,    "
				+ " [formation] TEXT,      " + " [color] TEXT,   "
				+ " [group_name] TEXT,"
				+ " [op_type]       INTEGER, [op_time]        INTEGER)";
		db.execSQL(sql);

	}

	/**
	 * 数据记录相关表创建
	 * 
	 * @param db
	 */
	public void createTableRecords(SQLiteDatabase db) {
		Log.v("SQLConnection--onCreate()", "createTableRecords，数据记录相关表创建");
		// the_id int NOT NULL 数据记录ID
		// match_id int NOT NULL 比赛ID
		// uid int NOT NULL 人员对应关系ID
		// write_time int NOT NULL 记录录入时间
		// ori_time int NOT NULL 原始数据时间
		// tempo int NOT NULL 浆频
		// heartRate int NOT NULL 心率
		// speed real NOT NULL 速度
		// speed_add real NOT NULL 加速度

		// dev_id int NOT NULL 设备id
		// dev_number text NOT NULL 设备序列号
		// gps_time int NOT NULL GPS时间
		// lat real NOT NULL 纬度
		// lon real NOT NULL 经度
		// dev_elec int NOT NULL 设备电量百分比
		// elec_belt int NOT NULL 心率带电量百分比

		String msgs = "CREATE TABLE  " + SQLColumnFinal.TABLE_LEOET_HEART_BEAT
				+ " ( [the_id] INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ "[match_id] INTEGER," + "[uid] INTEGER ,"
				+ "[write_time] INTEGER ," + "[ori_time] INTEGER ,"
				+ "[tempo] INTEGER ," + "[distance] INTEGER ," + "[power] REAL"
				+ "[heart_rate] INTEGER ," + "[speed] REAL," + "[speed2] REAL,"
				+ "[speed3] REAL," + "[speed4] REAL," + "[speed5] REAL,"
				+ "[speed6] REAL," + "[speed7] REAL," + "[speed8] REAL,"
				+ "[speed9] REAL," + "[speed10] REAL," + "[speed_add] REAL,"
				+ "[dev_id] INTEGER," + "[dev_number] TEXT,"
				+ "[gps_time] INTEGER," + "[lat] REAL," + "[lon] REAL,"
				+ "[elec_belt] INTEGER," + "[dev_elec] INTEGER)";
		db.execSQL(msgs);

	}

	/**
	 * 专项训练数据报告表
	 */
	private void createSpecialDataReport(SQLiteDatabase db) {
		Log.v("SQLConnection--onCreate()",
				"createSpecialDataReport，创建专项训练数据报告表");
		String msgs = "CREATE TABLE "
				+ SQLColumnFinal.TABLE_SPECIAL_DATA_REPORT
				+ " ( [the_id] INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ " [match_id] INTEGER,"// 比赛Id
				+ " [train_type] TEXT,"// 曲线类型
				+ " [start_time] INTEGER,"// 比赛开始时间
				+ " [end_time] INTEGER,"// 比赛结束时间
				+ " [device_id] INTEGER,"// 设备Id
				+ " [player_id] INTEGER ,"// 运动员Id
				+ " [player_name] TEXT ,"// 运动员名称
				+ " [total_score] INTEGER,"// 总评分
				+ " [auto_advise] TEXT ,"// pad按照评分标准给出的自动评价
				+ " [coach_advise] TEXT "// 教练给出的评价
				+ ")";
		db.execSQL(msgs);
	}

	/**
	 * 训练日志表
	 * 
	 * @author hao
	 */
	private void createTabTrainRecord(SQLiteDatabase db) {
		Log.v("SQLConnection--onCreate()", "createTabTrainRecord，创建训练日志表");
		String msgs = "CREATE TABLE [leo_train_record]( [the_id] INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
				+ " [op_type]       INTEGER," + " [record_type]       INTEGER,"// 记录类型1系统
																				// 0手动录入
				+ " [op_time]        INTEGER," + " [match_id] INTEGER,"// 训练Id
				+ " [start_time] INTEGER,"// 训练开始时间
				+ " [end_time] INTEGER,"// 训练结束时间
				+ " [record_time] INTEGER, "// 记录的时间
				+ " [record_text] TEXT "// 记录的内容
				+ ")";
		db.execSQL(msgs);
	}

	/**
	 * @param db
	 *            团队管理中的分组
	 */
	private void createTabTeamManagerGroup(SQLiteDatabase db) {
		Log.v("SQLConnection--onCreate()",
				"createTabTeamManagerGroup，创建团队管理中的分组表");
		/**
		 * 2015.5.27 新版足球新增表 分组表 group_id 分组id，group_name 分组名称 ， team_id 所属团队id
		 * ， group_number 分组的序号，player_ids 所包含的运动员的id.
		 */
		String sql = "CREATE TABLE " + SQLColumnFinal.TABLE_GROUP + " ("
				+ " [the_id] 	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
				+ " [op_type]       INTEGER," + " [op_time]        INTEGER,"
				+ " [group_name] 	TEXT," + " [team_id] 	INTEGER,"
				+ " [group_number] 	TEXT,[player_ids] 	TEXT);";
		db.execSQL(sql);
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

		case 2:
			try {
				db.beginTransaction();
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}
			break;

		case 3:
			try {
				db.beginTransaction();
				// db.execSQL("ALTER TABLE " + SQLColumnFinal.TABLE_PLAYER
				// + " ADD COLUMN  player_number INTEGER");
				// db.execSQL("ALTER TABLE " + SQLColumnFinal.TABLE_PLAYER
				// + " ADD COLUMN  player_position INTEGER");
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}
			break;

		case 4:
			try {
				db.beginTransaction();

				// db.execSQL("delete from leoet_dict_main");
				// db.execSQL("delete from leoet_dict_sub");
				//
				// db.execSQL("ALTER TABLE "
				// + SQLColumnFinal.TABLE_LEOET_MATCH_PERSON
				// + " ADD COLUMN  player_position INTEGER");
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}

			break;

		case 5:
			try {
				db.beginTransaction();

				// db.execSQL("delete from leoet_dict_main");
				// db.execSQL("delete from leoet_dict_sub");

				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}

			break;
		case 6:
			try {
				db.beginTransaction();

				// db.execSQL("delete from leoet_dict_main");
				// db.execSQL("delete from leoet_dict_sub");

				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}

			break;

		case 7:
			try {
				db.beginTransaction();

				// db.execSQL("delete from leoet_dict_main");
				// db.execSQL("delete from leoet_dict_sub");

				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}

			break;

		case 8:
			try {
				db.beginTransaction();

				// db.execSQL("ALTER TABLE " + SQLColumnFinal.TABLE_TEAM
				// + " ADD COLUMN  team_gender INTEGER");

				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}

			break;

		case 9:
			try {
				db.beginTransaction();

				// db.execSQL("delete from leoet_dict_main");
				// db.execSQL("delete from leoet_dict_sub");

				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}

			break;
		case 10:
			try {
				db.beginTransaction();

				// db.execSQL("delete from leoet_dict_main");
				// db.execSQL("delete from leoet_dict_sub");

				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}

			break;

		case 11:

			try {
				db.beginTransaction();

				// db.execSQL("delete from leoet_dict_main");
				// db.execSQL("delete from leoet_dict_sub");

				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}
			break;

		case 12:

			try {
				db.beginTransaction();

				// db.execSQL("delete from leoet_dict_main");
				// db.execSQL("delete from leoet_dict_sub");

				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}
			break;
		case 13:

			try {
				db.beginTransaction();

				// db.execSQL("delete from leoet_device");

				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}
			break;

		case 14:

			try {
				db.beginTransaction();
				// db.execSQL("delete from leoet_dict_main");
				// db.execSQL("delete from leoet_dict_sub");

				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}
			break;
		case 15:

			try {
				db.beginTransaction();
				// db.execSQL("delete from leoet_dict_main");
				// db.execSQL("delete from leoet_dict_sub");

				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}
			break;

		case 16:

			try {
				db.beginTransaction();
				// createTabTrainRecord(db);
				db.setTransactionSuccessful();
			} catch (Throwable ex) {
				ex.printStackTrace();
				break;
			} finally {
				db.endTransaction();
			}
			break;
		case 17:

			// 2015.5.28新版足球【团队管理】 升级数据库

			/**
			 * 新增训练计划的3个表
			 * ***/
			try {
				db.beginTransaction();
				createTabTeamManagerGroup(db);
				// db.execSQL("ALTER TABLE " + SQLColumnFinal.TABLE_TEAM
				// + " ADD COLUMN  team_gender INTEGER");

				// db.execSQL("delete from leoet_dict_main");
				// db.execSQL("delete from leoet_dict_sub");
				//
				// db.execSQL("ALTER TABLE " + SQLColumnFinal.TABLE_TEAM
				// + " ADD COLUMN  chief_coach TEXT");
				// db.execSQL("ALTER TABLE " + SQLColumnFinal.TABLE_TEAM
				// + " ADD COLUMN  leader TEXT");
				// db.execSQL("ALTER TABLE " + SQLColumnFinal.TABLE_TEAM
				// + " ADD COLUMN  assistant_coach TEXT");
				// db.execSQL("ALTER TABLE " + SQLColumnFinal.TABLE_TEAM
				// + " ADD COLUMN  scientific_reserch TEXT");
				// db.execSQL("ALTER TABLE " + SQLColumnFinal.TABLE_TEAM
				// + " ADD COLUMN  doctor TEXT");
				// db.execSQL("ALTER TABLE " + SQLColumnFinal.TABLE_TEAM
				// + " ADD COLUMN  manager TEXT");
				// db.execSQL("ALTER TABLE " + SQLColumnFinal.TABLE_PLAYER
				// + " ADD COLUMN  player_state TEXT");
				// db.execSQL("ALTER TABLE " + SQLColumnFinal.TABLE_PLAYER
				// + " ADD COLUMN  player_positions TEXT");
				db.setTransactionSuccessful();

				this.createTablePlan(db);

			} catch (Throwable ex) {
				ex.printStackTrace();
				break;
			} finally {
				db.endTransaction();
			}
			break;

		default:
			break;
		}
	}

}
