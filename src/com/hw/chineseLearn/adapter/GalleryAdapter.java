package com.hw.chineseLearn.adapter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.Lesson;
import com.hw.chineseLearn.dao.bean.TbLessonMaterialStatus;
import com.hw.chineseLearn.dao.bean.Unit;

public class GalleryAdapter extends BaseAdapter {
	Context mContext;
	public int selectItem;
	LayoutInflater inflater;
	// 存放view的集合
	public Map<Integer, View> mapView = new HashMap<Integer, View>();
	int width;
	int height;
	Animation animation;
	private Unit unit;
	private List<Lesson> lessonList;
	private List<TbLessonMaterialStatus> lessonStatusList;

	public GalleryAdapter(Context mContext,Unit unit,List<Lesson> lessonList,List<TbLessonMaterialStatus> lessonStatusList) {
		this.mContext = mContext;
		this.unit = unit;
		this.lessonList = lessonList;
		this.lessonStatusList = lessonStatusList;
		inflater = LayoutInflater.from(mContext);
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 5;
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {

			} else if (msg.arg1 == 2) {

			} else if (msg.arg1 == 4) {

				if (msg.what == 1) {

					// View view = gallery.getChildAt(msg.arg2);
					// View view = adapter.mapView.get(msg.arg2);

					for (Entry<Integer, View> entry : mapView.entrySet()) {

						int position = entry.getKey();
						if (position == msg.arg2) {
							View views = entry.getValue();
							LinearLayout lin_c = (LinearLayout) views
									.findViewById(R.id.lin_c);
							lin_c.setLayoutParams(new RelativeLayout.LayoutParams(
									width, height));
						} else {
							View views = entry.getValue();
							LinearLayout lin_c = (LinearLayout) views
									.findViewById(R.id.lin_c);
							lin_c.setLayoutParams(new RelativeLayout.LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT));
						}

					}
				}
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return unit.getLessonList().split(";").length; // 这里的目的是可以让图片循环浏览
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setSelection(int position) {
		this.selectItem = position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.layout_gellay_item, null);

			holder.lin_c = (LinearLayout) convertView.findViewById(R.id.lin_c);

			holder.tv_description = (TextView) convertView
					.findViewById(R.id.tv_description);
			holder.tv_no = (TextView) convertView
					.findViewById(R.id.tv_no);

//			holder.tv_description = (TextView) convertView
//					.findViewById(R.id.tv_description);
			
			holder.btn_redo = (Button) convertView.findViewById(R.id.btn_redo);
			holder.btn_start = (Button) convertView
					.findViewById(R.id.btn_start);
			holder.btn_review = (Button) convertView
					.findViewById(R.id.btn_review);
			holder.btn_lock = (Button) convertView
			.findViewById(R.id.btn_lock);

			mapView.put(position, convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// if (selectItem == position) {
		// holder.lin_c.setLayoutParams(new RelativeLayout.LayoutParams(width,
		// height));
		// } else {
		// // 未选中
		// holder.lin_c.setLayoutParams(new RelativeLayout.LayoutParams(width,
		// height / 5 * 4));
		// }

		//判断显示start redo review
		
		if(lessonStatusList!=null){
			if(lessonStatusList.get(position).getStatus()==1){
				holder.btn_start.setVisibility(View.VISIBLE);
				holder.btn_redo.setVisibility(View.GONE);
				holder.btn_review.setVisibility(View.GONE);
				holder.btn_lock .setVisibility(View.GONE);
			}else if(lessonStatusList.get(position).getStatus()==0){
				holder.btn_lock .setVisibility(View.VISIBLE);
				holder.btn_start.setVisibility(View.GONE);
				holder.btn_redo.setVisibility(View.GONE);
				holder.btn_review.setVisibility(View.GONE);
			}
		}
		
		if (holder.btn_redo.getVisibility() == View.VISIBLE) {
			holder.btn_start.setVisibility(View.GONE);
		}
		if (holder.btn_start.getVisibility() == View.VISIBLE) {
			holder.btn_redo.setVisibility(View.GONE);
			holder.btn_review.setVisibility(View.GONE);
		}
		
		//设置description 属性文本
		String[] spitList = unit.getLessonList().split(";");
		Integer lessonId = Integer.valueOf(spitList[position]);
		Lesson lesson = null;
		try {
			lesson = (Lesson) MyDao.getDao(Lesson.class).queryForId(lessonId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			String description = lesson.getDescription();
			String[] split = description.split("!@@@!");
			String desc = "";
			for (int i = 0; i < split.length; i++) {
				desc+=split[i]+"\n";
			}
			holder.tv_description.setText(desc);
		holder.tv_no.setText((position+1)+"of"+unit.getLessonList().split(";").length);//1 of 2

		return convertView;
	}

	public class ViewHolder {

		public Button btn_lock;
		public LinearLayout lin_c;
		public TextView tv_title;
		public TextView tv_no;
		public TextView tv_description;
		public Button btn_redo;
		public Button btn_start;
		public Button btn_review;
	}
}