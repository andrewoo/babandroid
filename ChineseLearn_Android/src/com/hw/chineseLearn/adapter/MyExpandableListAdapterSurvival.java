package com.hw.chineseLearn.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.model.LearnSurvivalExpandBaseModel;
import com.hw.chineseLearn.model.LearnUnitBaseModel;

public class MyExpandableListAdapterSurvival extends BaseExpandableListAdapter {
	private String[] groups;
	private String[][] children;
	private ArrayList<LearnSurvivalExpandBaseModel> data;
	private Context context;
	private LayoutInflater childInflater;
	private LayoutInflater groupInflater;

	private GroupViewHolder gvHolder;
	private ChildViewHolder cvHolder;

	// 用来装载某个item是否被选中
	SparseBooleanArray selected;
	private int groupSelectPosition = -1;
	private int childSelectPosition = -1;

	class ChildViewHolder {
		TextView tv_child_char;
		TextView tv_child_pinyin;
		TextView tv_child_en;
		CheckBox ck_collect;
		LinearLayout lin_bg;
	}

	class GroupViewHolder {
		TextView board_selector;
		TextView tv_father_title;
		TextView tv_child_count;

	}

	public MyExpandableListAdapterSurvival(Context context,
			ArrayList<LearnSurvivalExpandBaseModel> data) { 
		this.data = data;
		this.context = context;
		this.childInflater = LayoutInflater.from(context);
		this.groupInflater = LayoutInflater.from(context);
		selected = new SparseBooleanArray();
	}

	@Override
	public LearnUnitBaseModel getChild(int groupPosition, int childPosition) {
		// return children[groupPosition][childPosition];
		return data.get(groupPosition).getChildData().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// return children[groupPosition].length;
		return data.get(groupPosition).getChildData().size();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = this.childInflater.inflate(
					R.layout.expend_listview_item_child_survival_kit, null);
			cvHolder = new ChildViewHolder();

			cvHolder.tv_child_en = (TextView) convertView
					.findViewById(R.id.tv_child_en);
			cvHolder.lin_bg = (LinearLayout) convertView
					.findViewById(R.id.lin_bg);
			cvHolder.lin_bg.setVisibility(View.GONE);
			cvHolder.ck_collect = (CheckBox) convertView
					.findViewById(R.id.ck_collect);

			convertView.setTag(cvHolder);
		} else {
			this.cvHolder = (ChildViewHolder) convertView.getTag();
		}
		final LearnUnitBaseModel model = getChild(groupPosition, childPosition);

		cvHolder.tv_child_en.setText(model.getUnitName());

		if (selected.get(childPosition)
				&& groupPosition == this.groupSelectPosition) {
			cvHolder.lin_bg.setVisibility(View.VISIBLE);

		} else {
			cvHolder.lin_bg.setVisibility(View.GONE);
		}
		cvHolder.ck_collect.setChecked(model.isEnable());
		cvHolder.ck_collect
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean checked) {
						// TODO Auto-generated method stub
						if (checked) {
							LearnUnitBaseModel modelBase = new LearnUnitBaseModel();
							CustomApplication.app.favouriteList.add(modelBase);
							model.setEnable(true);
						} else {
							// 从中找到这一个对象，删除
							for (int i = 0; i < CustomApplication.app.favouriteList
									.size(); i++) {
								LearnUnitBaseModel modelBase = CustomApplication.app.favouriteList
										.get(i);

								if (modelBase == null) {
									continue;
								}
								// 暂时 用name做校验
								String unitName = modelBase.getUnitName();
								if ("Favourite".equals(unitName)) {
									CustomApplication.app.favouriteList
											.remove(i);
									model.setEnable(false);
									break;
								}
							}
						}
						notifyDataSetChanged();
					}
				});

		return convertView;
	}

	@Override
	public LearnSurvivalExpandBaseModel getGroup(int groupPosition) {
		return data.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return data.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public void setSelection(int groupSelectPosition, int childSelectPosition) {
		this.groupSelectPosition = groupSelectPosition;

		if (this.childSelectPosition != -1) {
			this.selected.put(this.childSelectPosition, false);
		}
		this.selected.put(childSelectPosition, true);
		this.childSelectPosition = childSelectPosition;

	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = groupInflater.inflate(
					R.layout.expend_listview_item_father_survivla_kit, null);
			gvHolder = new GroupViewHolder();
			gvHolder.tv_father_title = (TextView) convertView
					.findViewById(R.id.tv_father_title);
			gvHolder.board_selector = (TextView) convertView
					.findViewById(R.id.board_selector);

			convertView.setTag(this.gvHolder);
		} else {
			gvHolder = (GroupViewHolder) convertView.getTag();
		}
		gvHolder.tv_father_title.setText(getGroup(groupPosition).getUnitName());

		if (isExpanded) {
			gvHolder.board_selector
					.setBackgroundResource(R.drawable.strokes_order_lv_arrow_noclick);
		} else {
			gvHolder.board_selector
					.setBackgroundResource(R.drawable.strokes_order_lv_arrow_onclick);
		}

		return convertView;
	}

	/** ExpandableListView 如果子条目需要响应click事件,必需返回true */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

}
