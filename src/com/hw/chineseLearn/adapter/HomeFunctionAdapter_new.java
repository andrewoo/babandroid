package com.hw.chineseLearn.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.hw.chineseLearn.R;

public class HomeFunctionAdapter_new extends SimpleAdapter {
    private View v;

    private int selectedPosition = -1;// 选中的位置

    int x = 0;

    int[] images, images2;

    String colorUnSel = "#838ca0";
    String colorSel = "#FF0000";

    public HomeFunctionAdapter_new(Context context,
            List<? extends Map<String, ?>> data, int resource, String[] from,
            int[] to) {
        super(context, data, resource, from, to);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        this.v = super.getView(position, convertView, parent);
        this.x++;

        View textSign = this.v.findViewById(R.id.itemSign);
        HashMap<String, Object> map = (HashMap<String, Object>) super
                .getItem(position);
        int theSingCount = (Integer) map.get("itemSign");
        if (theSingCount <= 0) {
            textSign.setVisibility(View.GONE);
        } else {
            textSign.setVisibility(View.VISIBLE);
        }

        return this.v;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }
}