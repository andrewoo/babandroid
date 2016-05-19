package com.util.tool;

import java.io.File;

import android.content.Context;

import com.hw.chineseLearn.interfaces.NetworkHelperInterface;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class NetWorkHelper {
	
	private static NetWorkHelper instance;
	private static Context context;
   	private HttpUtils http;
	
	private NetWorkHelper(){
		if(http==null){
			http = new HttpUtils();
		}
	}
	
	public static NetWorkHelper getInstance(Context context) {
		
		NetWorkHelper.context = context;
		if(instance==null){
			instance=new NetWorkHelper();
		}
		
		return instance;
	}

	/**
	 * url 网络地址
	 * path 下载到哪里及文件名
	 * 
	 * @param url
	 */
	public void downLoad(String url,String path,NetworkHelperInterface nwhi){
		
		HttpHandler handler = http.download(url,
				path,
			    true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
			    false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
			    nwhi.loadDownListener());
	}
}
