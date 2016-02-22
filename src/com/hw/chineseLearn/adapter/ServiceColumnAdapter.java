package com.hw.chineseLearn.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.model.TitleEntity;

public class ServiceColumnAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<TitleEntity> list;
	private Context context;
	private int selectIndex = -1;

	public ServiceColumnAdapter(Context c, List<TitleEntity> l) {
		mInflater = LayoutInflater.from(c);
		context = c;
		list = l;

	}

	public void addItems(TitleEntity entity) {
		list.add(entity);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
		selectIndex = position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		TitleEntity entity = list.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.column_edit_items, null);

			viewHolder = new ViewHolder();
			viewHolder.tvNewsId = (TextView) convertView
					.findViewById(R.id.column_tv_id);
			viewHolder.tvNewsTitle = (TextView) convertView
					.findViewById(R.id.column_tv_newstitle);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// if (selectIndex == position) {
		// convertView.setBackground(context.getResources().getDrawable(
		// R.drawable.bg_border_grey));
		// viewHolder.tvNewsTitle.setText("");
		//
		// }else {
		// convertView.setBackground(context.getResources().getDrawable(
		// R.drawable.bg_blue));
		// viewHolder.tvNewsTitle.setText(entity.getTitleName());
		// }

		viewHolder.tvNewsId.setTag(position);
		viewHolder.tvNewsTitle.setText(entity.getTitleName());
		viewHolder.tvNewsId.setText(entity.getTitleid());

		return convertView;
	}

	static class ViewHolder {
		public TextView tvNewsId;
		public TextView tvNewsTitle;
	}
}
