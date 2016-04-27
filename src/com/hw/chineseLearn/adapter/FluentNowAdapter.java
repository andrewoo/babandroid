package com.hw.chineseLearn.adapter;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.hw.chineseLearn.dao.bean.TbFileDownload;
import com.hw.chineseLearn.dao.bean.TbMyFluentNow;
import com.hw.chineseLearn.model.LearnUnitBaseModel;

public class FluentNowAdapter extends BaseAdapter {
	private Context context;
	public ArrayList<TbMyFluentNow> list;
	private LayoutInflater inflater;

	private int width, height;
	private int selectPosition;
	private Resources resources = null;
	int colorWhite = -1;
	int colorBlack = -1;

	public FluentNowAdapter(Context context, ArrayList<TbMyFluentNow> list) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
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
			convertView = inflater.inflate(R.layout.layout_fluent_list_item,
					null);
			holder.iv_tag = (ImageView) convertView.findViewById(R.id.img_tag);
			holder.txt_sentence_cn = (TextView) convertView
					.findViewById(R.id.txt_sentence_cn);
			holder.txt_sentence_en = (TextView) convertView
					.findViewById(R.id.txt_sentence_en);
			holder.txt_time = (TextView) convertView
					.findViewById(R.id.txt_time);
			holder.img_level = (ImageView) convertView
					.findViewById(R.id.img_level);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TbMyFluentNow model = list.get(position);
		if (model == null) {
			return convertView;
		}

		String sentenceCn = model.getTitle_CN();
		String sentenceEn = model.getTitle_EN();
		int level = model.getLevel();

		holder.txt_sentence_cn.setText("" + sentenceCn);
		holder.txt_sentence_en.setText("" + sentenceEn);
		holder.iv_tag.setImageDrawable(resources
				.getDrawable(R.drawable.ls_catt_16));
		if (level == 1) {
			holder.img_level.setImageDrawable(resources
					.getDrawable(R.drawable.ls_diff_img_0));
		} else if (level == 2) {
			holder.img_level.setImageDrawable(resources
					.getDrawable(R.drawable.ls_diff_img_1));
		} else if (level == 3) {
			holder.img_level.setImageDrawable(resources
					.getDrawable(R.drawable.ls_diff_img_2));
		} else {
			holder.img_level.setImageDrawable(resources
					.getDrawable(R.drawable.ls_diff_img_0));
		}

		return convertView;
	}

	public class ViewHolder {

		private ImageView iv_tag;
		private TextView txt_sentence_cn;
		private TextView txt_sentence_en;
		private TextView txt_time;
		private ImageView img_level;
	}
}
