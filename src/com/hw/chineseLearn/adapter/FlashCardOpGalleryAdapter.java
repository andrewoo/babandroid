package com.hw.chineseLearn.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.hw.chineseLearn.dao.bean.LGModelFlashCard;

public class FlashCardOpGalleryAdapter extends BaseAdapter {
	Context mContext;
	public int selectItem;
	LayoutInflater inflater;
	// 存放view的集合
	public Map<Integer, View> mapView = new HashMap<Integer, View>();
	int width;
	int height;
	private ArrayList<LGModelFlashCard> datas;
	int defaultNumber = 0;

	public FlashCardOpGalleryAdapter(Context mContext,
			ArrayList<LGModelFlashCard> datas, int defaultNumber) {
		this.mContext = mContext;
		this.defaultNumber = defaultNumber;
		this.datas = datas;
		inflater = LayoutInflater.from(mContext);
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;// gallery的宽和高
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 4;
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {

			} else if (msg.arg1 == 2) {

			} else if (msg.arg1 == 4) {

				if (msg.what == 1) {

					// View view = gallery.getChildAt(msg.arg2);
					// View view = adapter.mapView.get(msg.arg2);

					for (Entry<Integer, View> entry : mapView.entrySet()) {

						int position = entry.getKey();
						if (position == msg.arg2) {
							View views = entry.getValue();
							LinearLayout lin_c = (LinearLayout) views
									.findViewById(R.id.lin_c);
							lin_c.setLayoutParams(new RelativeLayout.LayoutParams(
									width, height));
						} else {
							View views = entry.getValue();
							LinearLayout lin_c = (LinearLayout) views
									.findViewById(R.id.lin_c);
							lin_c.setLayoutParams(new RelativeLayout.LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT));
						}

					}
				}
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return defaultNumber; //
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

	public void setCount(int defaultNumber) {
		this.defaultNumber = defaultNumber;
	}

	public void setSelection(int position) {
		this.selectItem = position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.layout_falsh_card_item,
					null);
			holder.lin_1 = (LinearLayout) convertView.findViewById(R.id.lin_1);
			holder.lin_1.setLayoutParams(new LinearLayout.LayoutParams(// 设置当前view的大小
					width, height));
			holder.tv_word = (TextView) convertView.findViewById(R.id.tv_word);
			holder.tv_translation = (TextView) convertView
					.findViewById(R.id.tv_translation);
			holder.tv_pinyin = (TextView) convertView
					.findViewById(R.id.tv_pinyin);
			holder.img_play = (ImageView) convertView
					.findViewById(R.id.img_play);
			mapView.put(position, convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LGModelFlashCard lGModelFlashCard = datas.get(position);
		if (lGModelFlashCard == null) {
			return convertView;
		}
		// id = lGModelFlashCard.getCharId();
		holder.tv_translation.setText("" + lGModelFlashCard.getEnglish());
		holder.tv_word.setText("" + lGModelFlashCard.getChinese());
		holder.tv_pinyin.setText("" + lGModelFlashCard.getPinyin());

		// if (selectItem == position) {
		// holder.lin_1.setLayoutParams(new LinearLayout.LayoutParams(width,
		// height));
		// } else {
		// // 未选中
		// holder.lin_1.setLayoutParams(new LinearLayout.LayoutParams(
		// width / 5 * 4, height));
		// }

		return convertView;
	}

	public class ViewHolder {

		public LinearLayout lin_1;
		public ImageView img_play;
		public TextView tv_translation;
		public TextView tv_pinyin;
		public TextView tv_word;
	}
}