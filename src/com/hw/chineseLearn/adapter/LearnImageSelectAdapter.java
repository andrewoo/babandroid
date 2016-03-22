package com.hw.chineseLearn.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.bean.LGWord;
import com.hw.chineseLearn.model.LearnUnitBaseModel;
import com.util.tool.BitmapLoader;

public class LearnImageSelectAdapter extends BaseAdapter {
	private Context context;
	public ArrayList<LGWord> list;
	private LayoutInflater inflater;

	private int width, height;
	private int selectPosition;
	private Resources resources = null;
	int colorWhite = -1;
	int colorBlack = -1;

	public LearnImageSelectAdapter(Context context,
			List<LGWord> list) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = (ArrayList<LGWord>) list;
		resources = context.getResources();
		colorWhite = resources.getColor(R.color.white);
		colorBlack = resources.getColor(R.color.black);
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;
		height = CustomApplication.app.displayMetrics.heightPixels / 3 * 4;
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
					R.layout.layout_unit_grid_item_exercise, null);
			holder.iv_img = (ImageView) convertView
					.findViewById(R.id.iv_unit_img);
			holder.iv_tag = (ImageView) convertView.findViewById(R.id.iv_tag);

			holder.txt_word_name = (TextView) convertView
					.findViewById(R.id.txt_word_name);
			holder.lin_content = (LinearLayout) convertView
					.findViewById(R.id.lin_content);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// convertView.setLayoutParams(new LinearLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, convertView.getWidth() / 3 * 4));
		LGWord model = list.get(position);
		if (model == null) {
			return convertView;
		}

		String unitName = model.getWord()+"/"+model.getPinyin();
		String imageName = "a" + model.getMainPic();
		if(imageName.indexOf(".")!=-1){
			imageName=imageName.substring(0, imageName.indexOf("."));
		}

		holder.txt_word_name.setText("" + unitName);
		
		
		holder.iv_img.setImageResource(resources.getIdentifier(imageName,
				"drawable", context.getPackageName()));

		if (selectPosition == position) {// 选中
			holder.lin_content.setBackground(resources
					.getDrawable(R.drawable.bg_blue));
			holder.txt_word_name.setTextColor(colorWhite);
			holder.iv_tag.setImageDrawable(resources
					.getDrawable(R.drawable.word_model1_sel));
		} else {// 未选中
			holder.lin_content.setBackground(resources
					.getDrawable(R.drawable.bg_white1));
			holder.txt_word_name.setTextColor(colorBlack);
			holder.iv_tag.setImageDrawable(resources
					.getDrawable(R.drawable.word_model1_unsel));
		}

		return convertView;
	}

	public class ViewHolder {

		public ImageView iv_img;
		private ImageView iv_tag;
		public TextView txt_word_name;
		public LinearLayout lin_content;
	}
}
