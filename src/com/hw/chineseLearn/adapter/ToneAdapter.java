package com.hw.chineseLearn.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.dao.bean.TbMyPinyinTone;
import com.hw.chineseLearn.model.LearnUnitBaseModel;

public class ToneAdapter extends BaseAdapter {
	private Context context;
	public ArrayList<TbMyPinyinTone> list;
	private LayoutInflater inflater;
	private int colorWhite, colorGrey;

	public ToneAdapter(Context context, ArrayList<TbMyPinyinTone> list) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		colorWhite = context.getResources().getColor(R.color.white);
		colorGrey = context.getResources().getColor(R.color.min_grey);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	int count = 1;
	// 存放view的集合
	HashMap<Integer, View> mapView = new HashMap<Integer, View>();

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.e("________", "count:" + count++);
		ViewHolder holder = null;
		if (mapView.get(position) == null) {
			holder = new ViewHolder();
			convertView = inflater
					.inflate(R.layout.layout_tone_grid_item, null);
			holder.iv_img = (ImageView) convertView
					.findViewById(R.id.iv_tone_no);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.txt_tone_game);
			holder.lin_dots = (LinearLayout) convertView
					.findViewById(R.id.lin_content);
			mapView.put(position, convertView);
			convertView.setTag(holder);
		} else {
			convertView = mapView.get(position);
			holder = (ViewHolder) convertView.getTag();
		}
		TbMyPinyinTone model = list.get(position);
		if (model == null) {
			return convertView;
		}
		int status = model.getStatus();
		String imageName = "" + model.getIconResSuffix();

		holder.iv_img.setImageResource(context.getResources().getIdentifier(
				imageName, "drawable", context.getPackageName()));
		if (status == 1) {
			holder.tv_name.setTextColor(colorWhite);
			holder.lin_dots.setBackground(context.getResources().getDrawable(
					R.drawable.pinyintone_bg_blue));
		} else {
			holder.tv_name.setTextColor(colorGrey);
			holder.lin_dots.setBackground(context.getResources().getDrawable(
					R.drawable.pinyintone_bg_white));
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView iv_img;
		public TextView tv_name;
		public LinearLayout lin_dots;
	}
}
