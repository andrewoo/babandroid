package com.hw.chineseLearn.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.model.MeiTuanGuessBaseModel;
import com.hw.chineseLearn.model.MeiTuanShopBaseModel;
import com.util.tool.UiUtil;

public class NoticesListAdapter extends BaseAdapter {
    private Context context;
    public ArrayList<MeiTuanGuessBaseModel> noticeList;
    private LayoutInflater inflater;

    public NoticesListAdapter(Context context,
            ArrayList<MeiTuanGuessBaseModel> noticeList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.noticeList = noticeList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return noticeList == null ? 0 : noticeList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return noticeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    // "id": "7940470",
    // "categoryId": "3",
    // "shopId": "318",
    // "cityId": "2266",
    // "title": "仅售168元，市场价294元的阿龙饭店6-8人餐，不限时段，美味尽在这里",
    // "sortTitle": "阿龙饭店：6-8人餐，不限时段",
    // "imgUrl": "http://s1.lashouimg.com/zt/2013/12/31/si_138848135566906.jpg",
    // "startTime": "1388851200",
    // "value": "294.00",
    // "price": "168.00",
    // "ribat": "5.7",
    // "bought": "0",
    // "maxQuota": "9999999",
    // "post": "no",
    // "soldOut": "no",
    // "tip": "",
    // "endTime": "1396540800",

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_notice_item, null);
            holder.iv_tag = (ImageView) convertView.findViewById(R.id.iv_tag);
            holder.iv_picture = (ImageView) convertView
                    .findViewById(R.id.iv_picture);
            holder.tv_title = (TextView) convertView
                    .findViewById(R.id.tv_title);

            holder.tv_distance = (TextView) convertView
                    .findViewById(R.id.tv_distance);
            holder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content);

            holder.tv_new_price = (TextView) convertView
                    .findViewById(R.id.tv_new_price);
            holder.tv_old_price = (TextView) convertView
                    .findViewById(R.id.tv_old_price);
            holder.tv_has_sold = (TextView) convertView
                    .findViewById(R.id.tv_has_sold);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MeiTuanGuessBaseModel noticeModel = noticeList.get(position);
        if (noticeModel == null) {
            return convertView;
        }
        String titleString = noticeModel.getTitle();
        String shortTitleString = noticeModel.getSortTitle();
        String imgUrlString = noticeModel.getImgUrl();
        String value = noticeModel.getValue();// 价格
        String price = noticeModel.getPrice();
        String bought = noticeModel.getBought();// 已售

        holder.tv_title.setText("" + shortTitleString);
        holder.tv_content.setText("" + titleString);
        holder.iv_picture.setTag("" + imgUrlString);
        holder.tv_new_price.setText("￥" + price);
        holder.tv_old_price.setText("￥" + value);
        holder.tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 画线

        UiUtil.async(context, holder.iv_picture, false, 0, false, 0, false, 0);

        holder.tv_has_sold.setText("已售 " + bought);

        MeiTuanShopBaseModel shop = noticeModel.getShop();
        if (shop != null) {
            String nameString = shop.getName();//
            if (nameString != null) {
                // holder.tv_title.setText(nameString);
            }
        }

        return convertView;
    }

    public class ViewHolder {

        public ImageView iv_tag;
        public ImageView iv_picture;

        public TextView tv_title;
        public TextView tv_distance;
        public TextView tv_content;

        public TextView tv_new_price;
        public TextView tv_old_price;
        public TextView tv_has_sold;
    }
}
