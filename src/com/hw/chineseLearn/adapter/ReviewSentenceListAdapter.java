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
import com.hw.chineseLearn.dao.bean.LGSentence;
import com.hw.chineseLearn.dao.bean.LGWord;

public class ReviewSentenceListAdapter extends BaseAdapter {
	private Context context;
	public ArrayList<LGSentence> list;
	private LayoutInflater inflater;

	private int width, height;
	private int selectPosition;
	private Resources resources = null;
	int colorWhite = -1;
	int colorBlack = -1;

	public ReviewSentenceListAdapter(Context context, ArrayList<LGSentence> list) {
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.layout_review_sentence_list_item, null);

			holder.tv_child_no = (TextView) convertView
					.findViewById(R.id.tv_child_no);
			holder.tv_child_translations = (TextView) convertView
					.findViewById(R.id.tv_child_translations);
			holder.tv_child_sentence = (TextView) convertView
					.findViewById(R.id.tv_child_sentence);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LGSentence model = list.get(position);
		if (model == null) {
			return convertView;
		}

		String sentence = model.getSentence();
		String translations = model.getTranslations();

		holder.tv_child_no.setText("" + (position + 1));
		holder.tv_child_translations.setText(translations);
		holder.tv_child_sentence.setText(sentence);
		if (selectPosition == position) {// 选中
			convertView.setBackground(resources
					.getDrawable(R.drawable.btn_grey_no_corners));
			holder.tv_child_no.setBackground(resources
					.getDrawable(R.drawable.strokes_order_lv_index_bg_black));
		} else {// 未选中
			convertView.setBackground(resources
					.getDrawable(R.drawable.btn_white_no_corners));
			holder.tv_child_no.setBackground(resources
					.getDrawable(R.drawable.strokes_order_lv_index_bg_grey));
		}

		return convertView;
	}

	public class ViewHolder {

		public TextView tv_child_no;
		public TextView tv_child_sentence;
		public TextView tv_child_translations;
	}
}
