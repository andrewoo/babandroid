package com.hw.chineseLearn.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.dao.bean.CharGroup;
import com.hw.chineseLearn.dao.bean.Character;

public class MyExpandableListAdapterStrokes extends BaseExpandableListAdapter {

	ArrayList<CharGroup> charGroupList = new ArrayList<CharGroup>();
	ArrayList<ArrayList<Character>> characterLists = new ArrayList<ArrayList<Character>>();
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

	public MyExpandableListAdapterStrokes(Context context,
			ArrayList<CharGroup> charGroupList,
			ArrayList<ArrayList<Character>> characterLists) {
		this.charGroupList = charGroupList;
		this.characterLists = characterLists;
		this.context = context;
		this.childInflater = LayoutInflater.from(context);
		this.groupInflater = LayoutInflater.from(context);
	}

	public Character getChild(int groupPosition, int childPosition) {
		return characterLists.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return characterLists.get(groupPosition).size();
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = this.childInflater.inflate(
					R.layout.expend_listview_item_child_strokes_order, null);
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
		Character child = getChild(groupPosition, childPosition);
		String pinyin = child.getPinyin();
		String wCharacter = child.getWCharacter();
		String cee = child.getCEE();

		cvHolder.tv_child_no.setText("" + (childPosition + 1));
		cvHolder.tv_child_char.setText(wCharacter);
		cvHolder.tv_child_pinyin.setText("" + pinyin);
		cvHolder.tv_child_en.setText("" + cee);

		return convertView;
	}

	public Object getGroup(int groupPosition) {
		return charGroupList.get(groupPosition);
	}

	public int getGroupCount() {
		return charGroupList.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = groupInflater.inflate(
					R.layout.expend_listview_item_father_strokes_order, null);
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

		CharGroup charGroup = charGroupList.get(groupPosition);

		String title = charGroup.getPartGroupName();
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
