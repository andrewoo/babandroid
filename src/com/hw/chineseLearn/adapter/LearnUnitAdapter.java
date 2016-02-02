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
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.model.LearnUnitBaseModel;

public class LearnUnitAdapter extends BaseAdapter {
	private Context context;
	public ArrayList<LearnUnitBaseModel> list;
	private LayoutInflater inflater;

	public LearnUnitAdapter(Context context, ArrayList<LearnUnitBaseModel> list) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
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
					.inflate(R.layout.layout_unit_grid_item, null);
			holder.iv_img = (ImageView) convertView
					.findViewById(R.id.iv_unit_img);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.txt_unit_name);
			holder.lin_dots = (LinearLayout) convertView
					.findViewById(R.id.lin_dots);
			mapView.put(position, convertView);
			convertView.setTag(holder);
		} else {
			convertView = mapView.get(position);
			holder = (ViewHolder) convertView.getTag();
		}
		LearnUnitBaseModel model = list.get(position);
		if (model == null) {
			return convertView;
		}
		String unitName = model.getUnitName();
		String imageName = "" + model.getIconResSuffix();
		String lessonList = model.getLessonList();

		if (model.isEnable()) {
			if (lessonList != null) {
				String[] lessonId = lessonList.split(";");

				Log.e("________", "lessonId.length:" + lessonId.length);
				for (int i = 0; i < lessonId.length; i++) {
					ImageView imageView = new ImageView(context);
					LayoutParams layoutParams = new LayoutParams(15, 15);
					layoutParams.setMargins(5, 5, 5, 5);
					imageView.setLayoutParams(layoutParams);
					imageView.setBackground(context.getResources().getDrawable(
							R.drawable.bg_circle_blue));
					holder.lin_dots.addView(imageView);
				}
			}
		}

		holder.tv_name.setText("" + unitName);
		holder.iv_img.setImageResource(context.getResources().getIdentifier(
				imageName, "drawable", context.getPackageName()));

		return convertView;
	}

	public class ViewHolder {

		public ImageView iv_img;
		public TextView tv_name;
		public LinearLayout lin_dots;
	}
}
