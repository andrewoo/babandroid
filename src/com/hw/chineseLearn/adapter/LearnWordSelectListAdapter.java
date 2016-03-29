package com.hw.chineseLearn.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.bean.LGModelWord;
import com.hw.chineseLearn.dao.bean.LGModelWord.SubLGModel;
import com.hw.chineseLearn.dao.bean.LGWord;

public class LearnWordSelectListAdapter extends BaseAdapter {
	private Context context;
	public LGModelWord lgModelWord;
	private LayoutInflater inflater;

	private int width, height;
	private int selectPosition=-1;
	private Resources resources = null;
	int colorWhite = -1;
	int colorBlack = -1;

	public LearnWordSelectListAdapter(Context context,
			LGModelWord lgModelWord) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.lgModelWord = lgModelWord;
		resources = context.getResources();
		colorWhite = resources.getColor(R.color.white);
		colorBlack = resources.getColor(R.color.black);
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;
		height = CustomApplication.app.displayMetrics.heightPixels / 3 * 4;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lgModelWord.getSubLGModelList() == null ? 0 : lgModelWord.getSubLGModelList().size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setSelection(int position) {
		this.selectPosition = position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.layout_learn_word_select_list_item, null);

			holder.tv_child_no = (TextView) convertView
					.findViewById(R.id.tv_child_no);
			holder.tv_child_char = (TextView) convertView
					.findViewById(R.id.tv_child_char);
			holder.tv_child_pinyin = (TextView) convertView
					.findViewById(R.id.tv_child_pinyin);
			holder.tv_child_en = (TextView) convertView
					.findViewById(R.id.tv_child_en);
			holder.view_split = (View) convertView
					.findViewById(R.id.view_split);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (lgModelWord.getSubLGModelList() == null) {
			return convertView;
		}
		
		SubLGModel subLGModel = lgModelWord.getSubLGModelList().get(position);
		holder.tv_child_char.setText("" + subLGModel.getOption());

		if (selectPosition == position) {// 选中
			convertView.setBackground(resources
					.getDrawable(R.drawable.btn_bg_blue));
			holder.tv_child_no.setBackground(resources
					.getDrawable(R.drawable.strokes_order_lv_index_bg_grey));
			holder.view_split.setVisibility(View.INVISIBLE);
			holder.tv_child_char.setTextColor(context.getResources().getColor(
					R.color.white));
		} else {// 未选中
			convertView.setBackground(resources
					.getDrawable(R.drawable.btn_touming_no_corners));
			holder.tv_child_no.setBackground(resources
					.getDrawable(R.drawable.strokes_order_lv_index_bg_black));
			holder.view_split.setVisibility(View.VISIBLE);
			holder.tv_child_char.setTextColor(context.getResources().getColor(
					R.color.black));
		}

		return convertView;
	}

	public class ViewHolder {

		public TextView tv_child_no;
		public TextView tv_child_char;
		public TextView tv_child_pinyin;
		public TextView tv_child_en;
		public View view_split;
	}
}
