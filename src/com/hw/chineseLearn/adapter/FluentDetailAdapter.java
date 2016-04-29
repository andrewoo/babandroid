package com.hw.chineseLearn.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.db.DatabaseHelper;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.hw.chineseLearn.model.FluentDetailListModel;
import com.hw.chineseLearn.model.FluentDetailListWordsModel;
import com.util.tool.AudioRecorder;
import com.util.tool.AudioRecorder.VMChangeListener;
import com.util.tool.MediaPlayUtil;
import com.util.tool.MediaPlayerHelper;

public class FluentDetailAdapter extends BaseAdapter {
	String TAG = "FluentDetailAdapter";
	private Context context;
	public ArrayList<FluentDetailListModel> list;
	private LayoutInflater inflater;

	private int width, height;
	private int selectPosition;
	private Resources resources = null;
	int colorWhite = -1;
	int colorBlack = -1;
	boolean isRecord = false;// 是否正在录音
	private MediaPlayerHelper mediaPlayerHelper;
	private String awsId;
	private String fluentId;

	public FluentDetailAdapter(Context context,
			ArrayList<FluentDetailListModel> list, String fluentId, String awsId) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.fluentId = fluentId;
		this.awsId = awsId;
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
	HashMap<Integer, View> mapView = new HashMap<Integer, View>();
	String display_state = "0";
	String font_size = "14";
	ViewHolder holder = null;

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.layout_fluent_detail_list_item, null);
			holder.lin_bg = (LinearLayout) convertView
					.findViewById(R.id.lin_bg);
			holder.lin_content = (LinearLayout) convertView
					.findViewById(R.id.lin_content);
			holder.rel_funcations = (RelativeLayout) convertView
					.findViewById(R.id.rel_funcations);
			holder.rel_funcations.setVisibility(View.GONE);
			holder.txt_sentence_cn = (TextView) convertView
					.findViewById(R.id.txt_sentence_cn);
			holder.txt_sentence_en = (TextView) convertView
					.findViewById(R.id.txt_sentence_en);
			holder.img_play = (ImageView) convertView
					.findViewById(R.id.img_play);
			holder.img_record = (ImageView) convertView
					.findViewById(R.id.img_record);
			holder.img_loop = (ImageView) convertView
					.findViewById(R.id.img_loop);

			holder.txt_sentence_cn.setTextSize(14.0f);
			holder.txt_sentence_en.setTextSize(14.0f);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FluentDetailListModel model = list.get(position);
		if (model == null) {
			return convertView;
		}
		display_state = CustomApplication.app.preferencesUtil.getValue(
				"display_state", "0");
		font_size = CustomApplication.app.preferencesUtil.getValue("font_size",
				"14");

		String STRE = model.getSTRE();
		ArrayList<FluentDetailListWordsModel> words = model.getWords();

		if (words != null) {

			StringBuffer bufferSW = new StringBuffer();
			StringBuffer bufferPY = new StringBuffer();
			StringBuffer bufferTW = new StringBuffer();

			for (int i = 0; i < words.size(); i++) {
				FluentDetailListWordsModel listWordsModel = words.get(i);
				if (i == 0) {
					if ("A".equals(listWordsModel.getSW())) {
						holder.lin_bg.setBackground(context.getResources()
								.getDrawable(R.drawable.chat_from_bg_normal));
						holder.lin_content.setGravity(Gravity.LEFT);
					} else {
						holder.lin_bg.setBackground(context.getResources()
								.getDrawable(R.drawable.chat_to_bg_normal));
						holder.lin_content.setGravity(Gravity.RIGHT);
					}
				} else {
					String SW = listWordsModel.getSW();
					String PY = listWordsModel.getPY();
					String TW = listWordsModel.getTW();

					bufferSW = bufferSW.append(SW);
					bufferPY = bufferPY.append(" " + PY);
					bufferTW = bufferTW.append(TW);
				}
			}

			if ("0".equals(display_state)) {
				holder.txt_sentence_cn.setText("" + bufferSW.toString());
				holder.txt_sentence_en.setText("" + STRE);
			} else if ("1".equals(display_state)) {
				holder.txt_sentence_cn.setText("" + bufferSW.toString());
				holder.txt_sentence_en.setText("" + bufferPY.toString());
			} else {
				holder.txt_sentence_cn.setText("" + STRE);
				holder.txt_sentence_en.setText("" + bufferPY.toString());
			}

			if ("10".equals(font_size)) {
				holder.txt_sentence_cn.setTextSize(10.0f);
				holder.txt_sentence_en.setTextSize(10.0f);
			} else if ("14".equals(font_size)) {
				holder.txt_sentence_cn.setTextSize(14.0f);
				holder.txt_sentence_en.setTextSize(14.0f);
			} else {// 22
				holder.txt_sentence_cn.setTextSize(22.0f);
				holder.txt_sentence_en.setTextSize(22.0f);
			}
		}

		if (selectPosition == position) {// 选中
			holder.rel_funcations.setVisibility(View.VISIBLE);
			holder.lin_content.setGravity(Gravity.CENTER);
			playVoice(position);
		} else {// 未选中
			holder.rel_funcations.setVisibility(View.GONE);
			if (position % 2 == 0) {
				holder.lin_content.setGravity(Gravity.LEFT);
			} else {
				holder.lin_content.setGravity(Gravity.RIGHT);
			}
		}

		return convertView;
	}
 
	/**
	 * 播放声音
	 * 
	 * @param position
	 */
	private void playVoice(final int position) {
		String soundPath = DatabaseHelperMy.SOUND_PATH + "/" + awsId + "/"
				+ awsId + "-" + (position + 1) + ".mp3";
		Log.d(TAG, "soundPath:" + soundPath);
		MediaPlayUtil instance = MediaPlayUtil.getInstance();
		instance.setPlayOnCompleteListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				MediaPlayUtil.getInstance().release();
			}
		});
		instance.play(soundPath);
	}

	public class ViewHolder {
		private LinearLayout lin_bg;
		private LinearLayout lin_content;
		private RelativeLayout rel_funcations;
		private TextView txt_sentence_cn;
		private TextView txt_sentence_en;
		private ImageView img_play;
		private ImageView img_record;
		private ImageView img_loop;
	}
}
