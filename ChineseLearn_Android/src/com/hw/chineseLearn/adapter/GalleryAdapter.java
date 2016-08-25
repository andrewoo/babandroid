package com.hw.chineseLearn.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.bean.Unit;
import com.util.tool.UiUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlowAdapter;

public class GalleryAdapter extends FancyCoverFlowAdapter {
	Context mContext;
	public int selectItem;
	LayoutInflater inflater;
	// 存放view的集合
	public Map<Integer, View> mapView = new HashMap<Integer, View>();
	int width;
	int height;
	Animation animation;
	private Unit unit;
	private List<String> descList;
	private List<Integer> lessonStatus;

	public GalleryAdapter(Context mContext,Unit unit,List<Integer> lessonStatus,List<String> descList) {
		this.mContext = mContext;
		this.unit = unit;
		this.descList = descList;
		this.lessonStatus = lessonStatus;
		inflater = LayoutInflater.from(mContext);
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;//gallery的宽和高
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
		return UiUtil.getListFormString( unit.getLessonList()).length; // 这里的目的是可以让图片循环浏览
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


	public class ViewHolder {

		public LinearLayout lin_c;
		public TextView tv_title;
		public TextView tv_no;
		public TextView tv_description;
	}

	@Override
	public View getCoverFlowItem(int position, View reusableView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		if (reusableView == null) {
			holder = new ViewHolder();
			reusableView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_gellay_item, parent, false);
			holder.lin_c = (LinearLayout) reusableView.findViewById(R.id.lin_c);
			holder.tv_title = (TextView) reusableView.findViewById(R.id.tv_title);
			reusableView.setLayoutParams(new FancyCoverFlow.LayoutParams(width,height));
//			holder.lin_c.setLayoutParams(new RelativeLayout.LayoutParams(//设置当前view的大小
//					width, height));
			holder.tv_description = (TextView) reusableView.findViewById(R.id.tv_description);
			
			mapView.put(position, reusableView);

			reusableView.setTag(holder);
		} else {
			holder = (ViewHolder) reusableView.getTag();
		}

		//判断显示start redo review
		if(lessonStatus!=null){
			if(lessonStatus.get(position)==1){//start
				holder.lin_c.setBackgroundResource(R.drawable.shape_white_coner_stroke);
				holder.tv_description.setTextColor(mContext.getResources().getColor(R.color.floralwhite));
				holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.floralwhite));
			}else if(lessonStatus.get(position)==0){//lock
				holder.lin_c.setBackgroundResource(R.drawable.shape_white_coner_stroke);
				holder.tv_description.setTextColor(mContext.getResources().getColor(R.color.floralwhite));
				holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.floralwhite));
			}
			else if(lessonStatus.get(position)==2){//review
				holder.lin_c.setBackgroundResource(R.drawable.shape_white_corner_bg);
				holder.tv_description.setTextColor(mContext.getResources().getColor(R.color.chinese_skill_green));
				holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.chinese_skill_green));
			}
		}
		//分割去掉数据库中特殊字符
		//todo 死数据需要改
			String description = descList.get(position);
			String[] split = description.split("!@@@!");
			String desc = "";
			
			for (int i = 0; i < split.length; i++) {
				String str = split[i]; 
				if(str.contains("Sentence Pattern:")){
					str=str.replace("Sentence Pattern:", "");
				}if(str.contains("Words:")){
					str=str.replace("Words:", "");
				}
				
				desc+=str+"\n";
			}
			String ofNumber=(position+1)+mContext.getString(R.string.learn_of)+UiUtil.getListFormString(unit.getLessonList()).length;
			holder.tv_title.setText(mContext.getString(R.string.learn_lesson)+" "+ofNumber);
			holder.tv_description.setText(desc);
		return reusableView;
	}
}