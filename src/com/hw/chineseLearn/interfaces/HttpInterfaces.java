package com.hw.chineseLearn.interfaces;

import org.json.JSONException;

import android.content.Context;

import com.google.gson.Gson;
import com.hw.chineseLearn.interfaces.RestClient.RequestMethod;
import com.hw.chineseLearn.model.NoticeModel;

public class HttpInterfaces {
    private Context context;
    private Gson gson;

    public HttpInterfaces(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    /**
     * @return
     */
    public NoticeModel getNotices(int pageIndex, int pageSize) {
        NoticeModel dataObject = null;
        String result = "";
        try {
            // http://182.92.102.79/ls-server/api/goods?city=2419&page=1&size=10
            RestClient client = new RestClient(
                    "http://182.92.102.79/ls-server/api/goods?city=2419");
            client.AddParam("page", "" + pageIndex);
            client.AddParam("size", "" + pageSize);
            client.Execute(RequestMethod.POST);
            result = client.getResponse();

            gson = new Gson();
            dataObject = gson.fromJson(result, NoticeModel.class);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataObject;
    }
}
