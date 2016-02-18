package com.hw.chineseLearn.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.model.LearnUnitBaseModel;

public class FluentDetailAdapter extends BaseAdapter {
	private Context context;
	public ArrayList<LearnUnitBaseModel> list;
	private LayoutInflater inflater;

	private int width, height;
	private int selectPosition;
	private Resources resources = null;
	int colorWhite = -1;
	int colorBlack = -1;

	public FluentDetailAdapter(Context context,
			ArrayList<LearnUnitBaseModel> list) {
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
			convertView = inflater.inflate(
					R.layout.layout_fluent_detail_list_item, null);
			holder.lin_bg = (LinearLayout) convertView
					.findViewById(R.id.lin_bg);
			holder.lin_content = (LinearLayout) convertView
					.findViewById(R.id.lin_content);
			holder.rel_funcations = (RelativeLayout) convertView
					.findViewById(R.id.rel_funcations);
			holder.rel_funcations.setVisibility(View.GONE);
			holder.txt_sentence_cn = (TextView) convertView
					.findViewById(R.id.txt_sentence_cn);
			holder.txt_sentence_en = (TextView) convertView
					.findViewById(R.id.txt_sentence_en);
			holder.img_play = (ImageView) convertView
					.findViewById(R.id.img_play);
			holder.img_record = (ImageView) convertView
					.findViewById(R.id.img_record);
			holder.img_record_play = (ImageView) convertView
					.findViewById(R.id.img_record_play);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LearnUnitBaseModel model = list.get(position);
		if (model == null) {
			return convertView;
		}

		if (position % 2 == 0) {
			holder.lin_bg.setBackground(context.getResources().getDrawable(
					R.drawable.chat_from_bg_normal));
			holder.lin_content.setGravity(Gravity.LEFT);
		} else {
			holder.lin_bg.setBackground(context.getResources().getDrawable(
					R.drawable.chat_to_bg_normal));
			holder.lin_content.setGravity(Gravity.RIGHT);
		}

		String sentenceCn = model.getUnitName();
		String sentenceEn = model.getDescription();
		String imageName = "" + model.getIconResSuffix();

		holder.txt_sentence_cn.setText("" + sentenceCn);
		holder.txt_sentence_en.setText("" + sentenceEn);

		if (selectPosition == position) {// 选中
			holder.rel_funcations.setVisibility(View.VISIBLE);
			holder.lin_content.setGravity(Gravity.CENTER);
		} else {// 未选中
			holder.rel_funcations.setVisibility(View.GONE);

			if (position % 2 == 0) {
				holder.lin_content.setGravity(Gravity.LEFT);
			} else {
				holder.lin_content.setGravity(Gravity.RIGHT);
			}
		}

		return convertView;
	}

	public class ViewHolder {
		private LinearLayout lin_bg;
		private LinearLayout lin_content;
		private RelativeLayout rel_funcations;
		private TextView txt_sentence_cn;
		private TextView txt_sentence_en;
		private ImageView img_play;
		private ImageView img_record;
		private ImageView img_record_play;
	}
}
