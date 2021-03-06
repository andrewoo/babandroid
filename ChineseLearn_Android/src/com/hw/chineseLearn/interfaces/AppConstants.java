package com.hw.chineseLearn.interfaces;


import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.hw.chineseLearn.BuildConfig;
import com.hw.chineseLearn.base.CustomApplication;

import java.util.Locale;

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
//	public static final String BASE_URL = "http://52.9.199.129";//
	public static final String BASE_URL_WEB = "http://52.9.199.129";
//	public static final String BASE_URL_WEB = "http://58.67.154.138:8150";//web请求地址
//	public static final String FDFS_URL = "http://124.172.174.187";//Fast_FDfs 文件系统地址
	public static final String FDFS_URL = "http://52.9.199.129";//Fast_FDfs 文件系统地址


//	public static String dialogUrl=FDFS_URL+"/babble-api-app/v1/dialogues?diffLevel=";//默认请求地址
	public static String dialogUrl=BASE_URL_WEB+"/babble-api-app/v1/dialogues?diffLevel=";//默认请求地址
	public static String DBName="chineselearn.db";

	static {
		if("french".equals(BuildConfig.API_REQUEST_PARAMETER)){//
			dialogUrl=BASE_URL_WEB+"/babble-api-app/v1/dialogues?lang=french&diffLevel=";

			//动态设置语言
			DisplayMetrics dm = CustomApplication.app.getResources().getDisplayMetrics();
			String languageToLoad  = "fr"; // your language
			Locale locale = new Locale(languageToLoad);
			Configuration config = new Configuration();
			config.locale = locale;
			CustomApplication.app.getResources().updateConfiguration(config, dm);
		}else{

		}

		if("fr".equals(CustomApplication.getIntance().getAppLanguage())){//法语地址
//			dialogUrl=FDFS_URL+"/babble-api-app/v1/dialogues?language=fr&diffLevel=";
//			DBName="chineselearnfr.db";
		}else{
//			Log.e("AppConstants", "static initializer: "+CustomApplication.getIntance().getAppLanguage() );
		}
	}


	// 图片主地址
	public static String BASE_IMG_URL = FDFS_URL + "";

	/**************************************
	 ************** 接口地址*****************
	 **************************************/

	/**
	 * 0.退出登录
	 */
	public static String URL_LOGOUT = FDFS_URL
			+ "/ann/front/register/login/logout";

	/**
	 * 1.登录
	 */
	public static String URL_LOGIN = FDFS_URL
			+ "/ann/front/register/login/login";

	public static final String LOGIN_USERNAME = "Login_Username";
	public static final String LOGIN_PWD = "Login_Pwd";
	public static final String LOGIN_TOKEN = "Login_Token";

	// 查询
	public static String URL_SEARCHDEVICE = FDFS_URL + "/";

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
