package com.hw.chineseLearn.adapter;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.tabLearn.LessonExerciseActivity;
import com.hw.chineseLearn.tabLearn.LessonViewActivity;
import com.hw.chineseLearn.tabMe.MyForgotPswActivity;
import com.util.tool.UiUtil;

public class GalleryAdapter extends BaseAdapter {
	Context mContext;
	public int selectItem;
	LayoutInflater inflater;
	// 存放view的集合
	HashMap<Integer, View> mapView = new HashMap<Integer, View>();
	int width;
	int height;
	Animation animation;

	public GalleryAdapter(Context mContext) {
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 5;
		ScaleAnimation scaleAnimation;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3; // 这里的目的是可以让图片循环浏览
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setSelectItem(int selectItem) {

		this.selectItem = selectItem;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		if (mapView.get(position) == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.layout_gellay_item, null);
			holder.tv_description = (TextView) convertView
					.findViewById(R.id.tv_description);
			holder.tv_description = (TextView) convertView
					.findViewById(R.id.tv_description);

			holder.tv_description = (TextView) convertView
					.findViewById(R.id.tv_description);
			holder.btn_redo = (Button) convertView.findViewById(R.id.btn_redo);
			holder.btn_start = (Button) convertView
					.findViewById(R.id.btn_start);
			mapView.put(position, convertView);
			convertView.setTag(holder);
		} else {
			convertView = mapView.get(position);
			holder = (ViewHolder) convertView.getTag();
		}
		// if (selectItem == position) {
		// convertView
		// .setLayoutParams(new Gallery.LayoutParams(width, height));
		// } else {
		// // 未选中
		// // animation = AnimationUtils.loadAnimation(mContext,
		// // R.anim.scale_small); // 实现动画效果
		// // convertView.startAnimation(animation); // 未选中时，这是设置的比较小
		// convertView.setLayoutParams(new Gallery.LayoutParams(width,
		// height / 5 * 4));
		//
		// }

		 holder.btn_redo.setOnClickListener(new View.OnClickListener() {
		
		 @Override
		 public void onClick(View arg0) {
		 // TODO Auto-generated method stub
		 (mContext).startActivity(new Intent(mContext,
		 LessonExerciseActivity.class));
		 }
		 });
		 holder.btn_start.setOnClickListener(new View.OnClickListener() {
		
		 @Override
		 public void onClick(View arg0) {
		 // TODO Auto-generated method stub
		 Log.d("11111", "2222222");
		 }
		 });

		return convertView;
	}

	public class ViewHolder {

		public TextView tv_title;
		public TextView tv_no;
		public TextView tv_description;
		public Button btn_redo;
		public Button btn_start;
	}
}