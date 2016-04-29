package com.util.tool;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	public static int getInt(JSONObject jsonObj, String key, int defValue) {
		if (jsonObj == null)
			return defValue;
		if (jsonObj.has(key)) {
			try {
				return jsonObj.getInt(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return defValue;
			}
		}
		return defValue;
	}

	public static long getLong(JSONObject jsonObj, String key, long defValue) {
		if (jsonObj == null)
			return defValue;
		if (jsonObj.has(key)) {
			try {
				return jsonObj.getLong(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return defValue;
			}
		}
		return defValue;
	}

	public static double getDouble(JSONObject jsonObj, String key,
			double defValue) {
		if (jsonObj == null)
			return defValue;
		if (jsonObj.has(key)) {
			try {
				return jsonObj.getDouble(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return defValue;
			}
		}
		return defValue;
	}

	public static String getString(JSONObject jsonObj, String key) {
		if (jsonObj == null)
			return "";
		if (jsonObj.has(key)) {
			try {
				return jsonObj.getString(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return "";
			}
		}
		return "";
	}

}
