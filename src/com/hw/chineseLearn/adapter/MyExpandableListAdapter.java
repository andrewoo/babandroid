package com.hw.chineseLearn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hw.chineseLearn.R;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
	private String[] groups;
	private String[][] children;
	private Context context;
	private LayoutInflater childInflater;
	private LayoutInflater groupInflater;

	private GroupViewHolder gvHolder;

	private ChildViewHolder cvHolder;

	class ChildViewHolder {
		TextView tv_child_no;
		TextView tv_child_char;
		TextView tv_child_pinyin;
		TextView tv_child_en;
		// ImageView img_;
	}

	class GroupViewHolder {
		TextView board_selector;
		TextView tv_father_title;
		TextView tv_child_count;
	}

	public MyExpandableListAdapter(Context context, String[] groups,
			String[][] children) {
		this.groups = groups;
		this.children = children;
		this.context = context;
		this.childInflater = LayoutInflater.from(context);
		this.groupInflater = LayoutInflater.from(context);
	}

	public Object getChild(int groupPosition, int childPosition) {
		return children[groupPosition][childPosition];
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return children[groupPosition].length;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = this.childInflater.inflate(
					R.layout.expend_listview_item_child, null);
			cvHolder = new ChildViewHolder();

			cvHolder.tv_child_no = (TextView) convertView
					.findViewById(R.id.tv_child_no);
			cvHolder.tv_child_char = (TextView) convertView
					.findViewById(R.id.tv_child_char);
			cvHolder.tv_child_pinyin = (TextView) convertView
					.findViewById(R.id.tv_child_pinyin);
			cvHolder.tv_child_en = (TextView) convertView
					.findViewById(R.id.tv_child_en);

			convertView.setTag(cvHolder);
		} else {
			this.cvHolder = (ChildViewHolder) convertView.getTag();
		}
		String child = getChild(groupPosition, childPosition).toString();
		cvHolder.tv_child_no.setText("" + (childPosition + 1));
		cvHolder.tv_child_char.setText(child);
		cvHolder.tv_child_en.setText("English");

		return convertView;
	}

	public Object getGroup(int groupPosition) {
		return groups[groupPosition];
	}

	public int getGroupCount() {
		return groups.length;
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = groupInflater.inflate(
					R.layout.expend_listview_item_father, null);
			gvHolder = new GroupViewHolder();
			gvHolder.tv_father_title = (TextView) convertView
					.findViewById(R.id.tv_father_title);
			gvHolder.board_selector = (TextView) convertView
					.findViewById(R.id.board_selector);
			gvHolder.tv_child_count = (TextView) convertView
					.findViewById(R.id.tv_child_count);

			convertView.setTag(this.gvHolder);
		} else {
			gvHolder = (GroupViewHolder) convertView.getTag();
		}
		String title = getGroup(groupPosition).toString();
		gvHolder.tv_father_title.setText(title);
		gvHolder.tv_child_count.setText("" + getChildrenCount(groupPosition));

		if (isExpanded) {
			gvHolder.board_selector
					.setBackgroundResource(R.drawable.strokes_order_lv_arrow_noclick);
		} else {
			gvHolder.board_selector
					.setBackgroundResource(R.drawable.strokes_order_lv_arrow_onclick);
		}

		return convertView;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {

		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

}
