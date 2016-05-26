package com.hw.chineseLearn.interfaces;

import java.io.File;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public abstract class NetworkHelperInterface {
	
	private NetworkHelperInterface nhInterface;
	public static RequestCallBack<File> requestCallBack;
	
	public abstract void onmStart();
	public abstract void onmLoading(long total, long current, boolean isUploading);
	public abstract void onmSuccess(ResponseInfo<File> responseInfo);
	public abstract void onmFailure(HttpException error, String msg);
	
	public RequestCallBack loadDownListener(){
		
		 requestCallBack = new RequestCallBack<File>() {

		        @Override
		        public void onStart() {
//		            testTextView.setText("conn...");
		        	onmStart();
		        }

		        @Override
		        public void onLoading(long total, long current, boolean isUploading) {
//		            testTextView.setText(current + "/" + total);
		        	onmLoading(total,current,isUploading);
		        }

		        @Override
		        public void onSuccess(ResponseInfo<File> responseInfo) {
//		            testTextView.setText("downloaded:" + responseInfo.result.getPath());
		        	onmSuccess(responseInfo);
		        }


		        @Override
		        public void onFailure(HttpException error, String msg) {
//		            testTextView.setText(msg);
		        	onmFailure(error,msg);
		        }
		};
		
		return requestCallBack;
		
	}

}
