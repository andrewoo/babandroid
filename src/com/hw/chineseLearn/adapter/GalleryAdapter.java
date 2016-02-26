package com.hw.chineseLearn.adapter;

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
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;

public class GalleryAdapter extends BaseAdapter {
	Context mContext;
	public int selectItem;
	LayoutInflater inflater;
	// 存放view的集合
	public Map<Integer, View> mapView = new HashMap<Integer, View>();
	int width;
	int height;
	Animation animation;

	public GalleryAdapter(Context mContext) {
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
		width = CustomApplication.app.displayMetrics.widthPixels / 10 * 7;
		height = CustomApplication.app.displayMetrics.heightPixels / 10 * 5;
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
		return 2; // 这里的目的是可以让图片循环浏览
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
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
			convertView = inflater.inflate(R.layout.layout_gellay_item, null);

			holder.lin_c = (LinearLayout) convertView.findViewById(R.id.lin_c);

			holder.tv_description = (TextView) convertView
					.findViewById(R.id.tv_description);
			holder.tv_description = (TextView) convertView
					.findViewById(R.id.tv_description);

			holder.tv_description = (TextView) convertView
					.findViewById(R.id.tv_description);
			holder.btn_redo = (Button) convertView.findViewById(R.id.btn_redo);
			holder.btn_start = (Button) convertView
					.findViewById(R.id.btn_start);
			holder.btn_review = (Button) convertView
					.findViewById(R.id.btn_review);

			mapView.put(position, convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// if (selectItem == position) {
		// holder.lin_c.setLayoutParams(new RelativeLayout.LayoutParams(width,
		// height));
		// } else {
		// // 未选中
		// holder.lin_c.setLayoutParams(new RelativeLayout.LayoutParams(width,
		// height / 5 * 4));
		// }

		if (holder.btn_redo.getVisibility() == View.VISIBLE) {
			holder.btn_start.setVisibility(View.GONE);
		}
		if (holder.btn_start.getVisibility() == View.VISIBLE) {
			holder.btn_redo.setVisibility(View.GONE);
			holder.btn_review.setVisibility(View.GONE);
		}

		return convertView;
	}

	public class ViewHolder {

		public LinearLayout lin_c;
		public TextView tv_title;
		public TextView tv_no;
		public TextView tv_description;
		public Button btn_redo;
		public Button btn_start;
		public Button btn_review;
	}
}