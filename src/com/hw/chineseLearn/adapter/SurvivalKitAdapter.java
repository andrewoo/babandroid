package com.hw.chineseLearn.adapter;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.bean.category;
import com.hw.chineseLearn.model.LearnUnitBaseModel;

public class SurvivalKitAdapter extends BaseAdapter {
	private Context context;
	public List<category> categoryList;
	private LayoutInflater inflater;

	private int width, height;
	private int selectPosition;
	private Resources resources = null;
	int colorWhite = -1;
	int colorBlack = -1;

	public SurvivalKitAdapter(Context context,
			List<category> categoryList) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.categoryList = categoryList;
		resources = context.getResources();
		colorWhite = resources.getColor(R.color.white);
		colorBlack = resources.getColor(R.color.black);
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;
		height = CustomApplication.app.displayMetrics.heightPixels / 3 * 4;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return categoryList == null ? 0 : categoryList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return categoryList.get(position);
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
			holder.txt_word_name = (TextView) convertView
					.findViewById(R.id.txt_word_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// convertView.setLayoutParams(new LinearLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, convertView.getWidth() / 3 * 4));
		category cate = categoryList.get(position);
		if (cate == null) {
			return convertView;
		}

		String unitName = cate.getEng_name();
		String imageName="";
		if(cate.getComplete_dl()==0){
			imageName = "survival_" + cate.getId();
		}else{
			imageName = "survival_" + cate.getId()+"_hit";
		}

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

		private ImageView iv_tag;
		public TextView txt_word_name;
	}
}
