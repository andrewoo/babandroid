package com.hw.chineseLearn.interfaces;


import com.hw.chineseLearn.BuildConfig;
import com.hw.chineseLearn.base.CustomApplication;

/**
 * App常量配置
 * 
 * @author
 */
public class AppConstants {
	/******************************************
	 ************* Server Address ************
	 *****************************************/

	/**
	 * 域名
	 */
	public static final String BASE_URL = "http://52.9.199.129";//
	public static String dialogUrl=BASE_URL+"/babble-api-app/v1/dialogues?diffLevel=";//默认请求地址
	public static String DBName="chineselearn.db";

	static {
		if("french".equals(BuildConfig.API_REQUEST_PARAMETER)){//
			dialogUrl=BASE_URL+"/babble-api-app/v1/dialogues?lang=french&diffLevel=";
		}else{

		}

		if("fr".equals(CustomApplication.getIntance().getAppLanguage())){//法语地址
//			dialogUrl=BASE_URL+"/babble-api-app/v1/dialogues?language=fr&diffLevel=";
//			DBName="chineselearnfr.db";
		}else{
//			Log.e("AppConstants", "static initializer: "+CustomApplication.getIntance().getAppLanguage() );
		}
	}


	// 图片主地址
	public static String BASE_IMG_URL = BASE_URL + "";

	/**************************************
	 ************** 接口地址*****************
	 **************************************/

	/**
	 * 0.退出登录
	 */
	public static String URL_LOGOUT = BASE_URL
			+ "/ann/front/register/login/logout";

	/**
	 * 1.登录
	 */
	public static String URL_LOGIN = BASE_URL
			+ "/ann/front/register/login/login";

	public static final String LOGIN_USERNAME = "Login_Username";
	public static final String LOGIN_PWD = "Login_Pwd";
	public static final String LOGIN_TOKEN = "Login_Token";

	// 查询
	public static String URL_SEARCHDEVICE = BASE_URL + "/";

	public static final String CHECKLOG = "checkLog";
	public static final String UID = "uid";

	public static final String NotSaveUsername = "NotSaveUsername";
	public static final String CURRENT_MODE = "current_mode";
	public static final String MODE_VEHICLE = "mode_vehicle";
	public static final String MODE_SLEEP = "mode_sleep";
	public static final String MODE_OXYGEN = "mode_oxygen";
	public static final String MODE_TIMING = "mode_timing";

	public static String lowPrice = "";
	public static String highPrice = "";
	public static String positions = "";

	public static String OptionNames = "";
	public static String OptionValues = "";

	// shardPreference里的键名
	public static String OPTIONNAMES = "OPTIONNAMES";
	public static String OPTIONVALUES = "OPTIONVALUES";

	public static boolean isShare = false;
}
