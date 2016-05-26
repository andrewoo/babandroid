package com.hw.chineseLearn.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.model.LearnUnitBaseModel;

public class TestOutGalleryAdapter extends BaseAdapter {
	private Context mContext;
	LayoutInflater inflater;
	int mGalleryItemBackground;
	int selectItem;
	int width;
	int height;
	public ArrayList<LearnUnitBaseModel> list;

	public TestOutGalleryAdapter(Context context,
			ArrayList<LearnUnitBaseModel> list) {
		this.mContext = context;
		this.list = list;
		TypedArray typedArray = context
				.obtainStyledAttributes(R.styleable.Gallery);
		mGalleryItemBackground = typedArray.getResourceId(
				R.styleable.Gallery_android_galleryItemBackground, 0);
		inflater = LayoutInflater.from(mContext);
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 2;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 2;
	}

	// 第1点改进，返回一个很大的值，例如，Integer.MAX_VALUE
	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void setSelect(int selectItem) {
		this.selectItem = selectItem;
	}

	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.layout_gellay_item_test_out, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.img_tag = (ImageView) convertView.findViewById(R.id.img_tag);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LearnUnitBaseModel model = list.get(position);
		String unitName = model.getUnitName();
		String imageName = "" + model.getIconResSuffix();

		holder.tv_name.setText("" + unitName);
		holder.img_tag.setImageResource(mContext.getResources().getIdentifier(
				imageName, "drawable", mContext.getPackageName()));

		if (selectItem == position) {
			holder.tv_name.setTextSize(12.0f);
			holder.img_tag.setLayoutParams(new LinearLayout.LayoutParams(width,
					height));
		} else {
			// 未选中时，这是设置的比较小
			holder.tv_name.setTextSize(9.0f);
			holder.img_tag.setLayoutParams(new LinearLayout.LayoutParams(
					width / 5 * 4, height / 5 * 4));

		}

		return convertView;
	}

	public class ViewHolder {

		public ImageView img_tag;
		public TextView tv_name;
	}

}