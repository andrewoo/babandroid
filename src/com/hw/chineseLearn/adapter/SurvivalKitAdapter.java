package com.hw.chineseLearn.adapter;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.bean.category;
import com.hw.chineseLearn.model.SurvivalKitModel;
import com.util.weight.TasksCompletedView;

public class SurvivalKitAdapter extends BaseAdapter {
	private Context context;
	public List<SurvivalKitModel> modelList;
	private LayoutInflater inflater;

	private int width, height;
	private int selectPosition;
	private Resources resources = null;
	int colorWhite = -1;
	int colorBlack = -1;
	private boolean isUploading;

	public SurvivalKitAdapter(Context context,
			List<SurvivalKitModel> modelList) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.modelList = modelList;
		resources = context.getResources();
		colorWhite = resources.getColor(R.color.white);
		colorBlack = resources.getColor(R.color.black);
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;
		height = CustomApplication.app.displayMetrics.heightPixels / 3 * 4;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return modelList == null ? 0 : modelList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return modelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setSelection(int position) {
		this.selectPosition = position;
	}
	

	int count = 1;
	// 存放view的集合
	HashMap<Integer, View> mapView = new HashMap<Integer, View>();
	

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.layout_survival_kit_list_item, null);
			holder.iv_tag = (ImageView) convertView.findViewById(R.id.img_tag);
			holder.txt_word_name = (TextView) convertView.findViewById(R.id.txt_word_name);
			holder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
			holder.circleView = (TasksCompletedView) convertView.findViewById(R.id.tasks_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// convertView.setLayoutParams(new LinearLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, convertView.getWidth() / 3 * 4));
		SurvivalKitModel survivalKitModel = modelList.get(position);
		if (modelList == null) {
			return convertView;
		}

		String unitName = survivalKitModel.getItemName();
		 String imageName = survivalKitModel.getImageName();
		 if(survivalKitModel.getState()==0){
			 holder.circleView.setVisibility(View.GONE);
			 holder.iv_arrow.setVisibility(View.VISIBLE);
			 holder.iv_arrow .setBackgroundResource(R.drawable.ls_dl_btn);
		 }else if(survivalKitModel.getState()==1){
			 holder.circleView.setVisibility(View.GONE);
			 holder.iv_arrow.setVisibility(View.VISIBLE);
			 holder.iv_arrow .setBackgroundResource(R.drawable.arrow);
		 }else if(survivalKitModel.getState()==2){ 
			 holder.iv_arrow .setVisibility(View.GONE);
			 holder.circleView.setVisibility(View.VISIBLE);
			 if(survivalKitModel.getPositionTag()==position){
				 float progress = modelList.get(position).getProgress();
				 System.out.println("progress"+position+"--"+progress);
				 holder.circleView.setProgress(progress);
			 }
			
		 }
//		String imageName="";
//		if(cate.getComplete_dl()==0){
//			imageName = "survival_" + cate.getId();
//			if(isUploading){
//				holder.iv_arrow .setBackgroundResource(R.drawable.item_show_arrow);//暂时测试用 下载中的图片
//			}else{
//				holder.iv_arrow .setBackgroundResource(R.drawable.ls_dl_btn);
//			}
//		}else{
//			imageName = "survival_" + cate.getId()+"_hit";
//			holder.iv_arrow .setBackgroundResource(R.drawable.arrow);
//		}

		holder.txt_word_name.setText("" + unitName);
		holder.iv_tag.setImageResource(resources.getIdentifier(imageName,
				"drawable", context.getPackageName()));

		// if (selectPosition == position) {// 选中
		// holder.lin_content.setBackground(resources
		// .getDrawable(R.drawable.bg_blue));
		// holder.txt_word_name.setTextColor(colorWhite);
		// holder.iv_tag.setImageDrawable(resources
		// .getDrawable(R.drawable.word_model1_sel));
		// } else {// 未选中
		// holder.lin_content.setBackground(resources
		// .getDrawable(R.drawable.bg_white1));
		// holder.txt_word_name.setTextColor(colorBlack);
		// holder.iv_tag.setImageDrawable(resources
		// .getDrawable(R.drawable.word_model1_unsel));
		// }

		return convertView;
	}

	public class ViewHolder {

		public ImageView iv_tag;
		public TextView txt_word_name;
		public ImageView iv_arrow;
		public TasksCompletedView circleView;
	}
}
