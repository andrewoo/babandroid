package com.hw.chineseLearn.interfaces;

import com.hw.chineseLearn.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseParser<T> {

	public abstract T parseJSON(String paramString) throws JSONException;
	/**
	 * 
	 * @param res
	 * @throws JSONException
	 */
	public String checkResponse(String paramString) throws JSONException {

		String res=null;
		if (paramString != null)
		{
			JSONObject jsonObject = new JSONObject(paramString);
			if(jsonObject.has("response"))
			{
				res = ""+jsonObject.getString("response");
				if("error".equals(res))
				{
					res = null;
				}
			}
			else
			{
				res = "other";
			}
		}
		return res;
	}
}
