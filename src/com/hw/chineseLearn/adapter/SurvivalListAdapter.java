package com.hw.chineseLearn.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.dao.bean.item;

public class SurvivalListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<item> data;
	private ViewHolder cvHolder;
	private int selectPosition;
	private boolean checked;
	private TextView txt_sentence_cn;
	private TextView txt_sentence_en;

	public SurvivalListAdapter(Context context,
			ArrayList<item> data) { 
		this.context = context; 
		this.data = data;
	}
	
	public void setSelection(int position) {
		this.selectPosition = position;
	}

//	@Override
//	public View getGroupView(int groupPosition, boolean isExpanded,
//			View convertView, ViewGroup parent) {
//
//		if (convertView == null) {
//			convertView = groupInflater.inflate(
//					R.layout.expend_listview_item_father_survivla_kit, null);
//			gvHolder = new GroupViewHolder();
//			gvHolder.tv_father_title = (TextView) convertView
//					.findViewById(R.id.tv_father_title);
//			gvHolder.board_selector = (TextView) convertView
//					.findViewById(R.id.board_selector);
//
//			convertView.setTag(this.gvHolder);
//		} else {
//			gvHolder = (GroupViewHolder) convertView.getTag();
//		}
//		gvHolder.tv_father_title.setText(getGroup(groupPosition).getUnitName());
//
//		if (isExpanded) {
//			gvHolder.board_selector
//					.setBackgroundResource(R.drawable.strokes_order_lv_arrow_noclick);
//		} else {
//			gvHolder.board_selector
//					.setBackgroundResource(R.drawable.strokes_order_lv_arrow_onclick);
//		}
//
//		return convertView;
//	}



	@Override
	public int getCount() {
		
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.expend_listview_item_child_survival_kit, null);
			cvHolder = new ViewHolder();

			cvHolder.tv_child_en = (TextView) convertView.findViewById(R.id.tv_child_en);
			cvHolder.lin_bg = (LinearLayout) convertView.findViewById(R.id.lin_bg);
			cvHolder.lin_bg.setVisibility(View.GONE);
			cvHolder.ck_collect = (CheckBox) convertView.findViewById(R.id.ck_collect);
			cvHolder.txt_sentence_cn = (TextView) convertView.findViewById(R.id.txt_sentence_cn);
			cvHolder.txt_sentence_en = (TextView) convertView.findViewById(R.id.txt_sentence_en);
			convertView.setTag(cvHolder);
		} else {
			this.cvHolder = (ViewHolder) convertView.getTag();
		}
		
		if (data == null) {
			return convertView;
		}
		
		final item model= data.get(position);

		cvHolder.tv_child_en.setText(model.getChn_item());
		cvHolder.txt_sentence_cn.setText(model.getPinyin());
		cvHolder.txt_sentence_en.setText(model.getChn_item());

		if (selectPosition==position) {
			cvHolder.lin_bg.setVisibility(View.VISIBLE);

		} else {
			cvHolder.lin_bg.setVisibility(View.GONE);
		}
		cvHolder.ck_collect.setChecked(checked);
		cvHolder.ck_collect
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean checked) {
						// TODO Auto-generated method stub
						if (checked) {
//							LearnUnitBaseModel modelBase = new LearnUnitBaseModel();
//							CustomApplication.app.favouriteList.add(modelBase);
							checked=!checked;
						} else {
							checked=!checked;
							// 从中找到这一个对象，删除
//							for (int i = 0; i < CustomApplication.app.favouriteList
//									.size(); i++) {
//								LearnUnitBaseModel modelBase = CustomApplication.app.favouriteList
//										.get(i);
//
//								if (modelBase == null) {
//									continue;
//								}
//								// 暂时 用name做校验
//								String unitName = modelBase.getUnitName();
//								if ("Favourite".equals(unitName)) {
//									CustomApplication.app.favouriteList
//											.remove(i);
//									checked=false;
//									break;
//								}
//							}
						}
						SurvivalListAdapter.this.notifyDataSetChanged();
					}
				});

		
		return convertView;
	}
	class ViewHolder {
		TextView tv_child_char;
		TextView tv_child_pinyin;
		TextView tv_child_en;
		CheckBox ck_collect;
		LinearLayout lin_bg;
		TextView txt_sentence_cn;
		TextView txt_sentence_en;
	}



}
