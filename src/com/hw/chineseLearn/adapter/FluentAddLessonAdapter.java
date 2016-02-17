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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.model.LearnUnitBaseModel;
import com.util.weight.RoundProgressBar;

public class FluentAddLessonAdapter extends BaseAdapter {
	private Context context;
	public ArrayList<LearnUnitBaseModel> list;
	private LayoutInflater inflater;

	private int width, height;
	private int selectPosition;
	private Resources resources = null;
	int colorWhite = -1;
	int colorBlack = -1;

	public FluentAddLessonAdapter(Context context,
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
	public HashMap<Integer, View> mapView = new HashMap<Integer, View>();
	

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.layout_fluent_add_lesson_list_item, null);

			holder.rel_intent = (RelativeLayout) convertView
					.findViewById(R.id.rel_intent);
			holder.iv_tag = (ImageView) convertView.findViewById(R.id.img_tag);
			holder.txt_sentence_cn = (TextView) convertView
					.findViewById(R.id.txt_sentence_cn);
			holder.txt_sentence_en = (TextView) convertView
					.findViewById(R.id.txt_sentence_en);

			holder.img_add_lesson = (ImageView) convertView
					.findViewById(R.id.img_add_lesson);
			holder.img_remove_lesson = (ImageView) convertView
					.findViewById(R.id.img_remove_lesson);
			// holder.img_remove_lesson.setVisibility(View.GONE);

			holder.progress_download = (RoundProgressBar) convertView
					.findViewById(R.id.progress_download);
			// holder.progress_download.setVisibility(View.GONE);
			mapView.put(position, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LearnUnitBaseModel model = list.get(position);
		if (model == null) {
			return convertView;
		}

		String sentenceCn = model.getUnitName();
		String sentenceEn = model.getDescription();
		String imageName = "" + model.getIconResSuffix();

		holder.txt_sentence_cn.setText("" + sentenceCn);
		holder.txt_sentence_en.setText("" + sentenceEn);
		holder.iv_tag.setImageResource(resources.getIdentifier(imageName,
				"drawable", context.getPackageName()));

		// if (selectPosition == position) {// 选中
		// holder.lin_content.setBackground(resources
		// .getDrawable(R.drawable.bg_blue));
		// holder.txt_word_name.setTextColor(colorWhite);
		// holder.iv_tag.setImageDrawable(resources
		// .getDrawable(R.drawable.word_model1_sel));
		// } else {// 未选中
		// holder.lin_content.setBackground(resources
		// .getDrawable(R.drawable.bg_white1));
		// holder.txt_word_name.setTextColor(colorBlack);
		// holder.iv_tag.setImageDrawable(resources
		// .getDrawable(R.drawable.word_model1_unsel));
		// }

		holder.img_remove_lesson.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		return convertView;
	}

	public class ViewHolder {

		private RelativeLayout rel_intent;
		private ImageView iv_tag;
		private TextView txt_sentence_cn;
		private TextView txt_sentence_en;
		private ImageView img_add_lesson;
		private ImageView img_remove_lesson;
		private RoundProgressBar progress_download;
	}
}
