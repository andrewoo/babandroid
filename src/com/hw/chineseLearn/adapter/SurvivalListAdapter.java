package com.hw.chineseLearn.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.dao.bean.item;
import com.hw.chineseLearn.db.DatabaseHelper;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.util.tool.AudioRecorder;
import com.util.tool.AudioRecorder.VMChangeListener;
import com.util.tool.MediaPlayUtil;
import com.util.tool.MediaPlayerHelper;

public class SurvivalListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<item> data;
	private ViewHolder cvHolder;
	private int selectPosition;
	private boolean checked;
	private TextView txt_sentence_cn;
	private TextView txt_sentence_en;
	boolean isRecord = false;//是否正在录音
	private Resources resources;
	private MediaPlayerHelper mediaPlayerHelper;
	private String flag = "listen";//录音按钮 默认为听的状态 
	private String lastRecFileName = "kitrecoder";
	private  String filePath = DatabaseHelper.CACHE_DIR_SOUND + "/" + lastRecFileName+ ".amr";
	private AudioRecorder mr;
	Thread thread = null;
	
	int vmValue = 0;

	public SurvivalListAdapter(Context context, ArrayList<item> data) {
		this.context = context;
		this.data = data;
		resources = context.getResources();
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	public void setSelection(int position) {
		this.selectPosition = position;
	}

	// @Override
	// public View getGroupView(int groupPosition, boolean isExpanded,
	// View convertView, ViewGroup parent) {
	//
	// if (convertView == null) {
	// convertView = groupInflater.inflate(
	// R.layout.expend_listview_item_father_survivla_kit, null);
	// gvHolder = new GroupViewHolder();
	// gvHolder.tv_father_title = (TextView) convertView
	// .findViewById(R.id.tv_father_title);
	// gvHolder.board_selector = (TextView) convertView
	// .findViewById(R.id.board_selector);
	//
	// convertView.setTag(this.gvHolder);
	// } else {
	// gvHolder = (GroupViewHolder) convertView.getTag();
	// }
	// gvHolder.tv_father_title.setText(getGroup(groupPosition).getUnitName());
	//
	// if (isExpanded) {
	// gvHolder.board_selector
	// .setBackgroundResource(R.drawable.strokes_order_lv_arrow_noclick);
	// } else {
	// gvHolder.board_selector
	// .setBackgroundResource(R.drawable.strokes_order_lv_arrow_onclick);
	// }
	//
	// return convertView;
	// }

	@Override
	public int getCount() {

		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.expend_listview_item_child_survival_kit, null);
			cvHolder = new ViewHolder();

			cvHolder.tv_child_en = (TextView) convertView
					.findViewById(R.id.tv_child_en);
			cvHolder.lin_bg = (LinearLayout) convertView
					.findViewById(R.id.lin_bg);
			cvHolder.lin_bg.setVisibility(View.GONE);
			cvHolder.ck_collect = (CheckBox) convertView
					.findViewById(R.id.ck_collect);
			cvHolder.txt_sentence_cn = (TextView) convertView
					.findViewById(R.id.txt_sentence_cn);
			cvHolder.txt_sentence_en = (TextView) convertView
					.findViewById(R.id.txt_sentence_en); 
			// 找到3个按钮 分别实现点击事件
			cvHolder.img_record = (ImageView) convertView
					.findViewById(R.id.img_record);// 录音按钮
			cvHolder.img_loop = (ImageView) convertView
					.findViewById(R.id.img_loop);// 是否循环按钮
			cvHolder.img_record_play = (ImageView) convertView
					.findViewById(R.id.img_record_play);// 慢速播放按钮
			
			initEvent(position);// checkbox 录音 慢速 循环的点击事件
			convertView.setTag(cvHolder);
		} else {
			this.cvHolder = (ViewHolder) convertView.getTag();
		}

		if (data == null) {
			return convertView;
		}

		final item model = data.get(position);

		cvHolder.tv_child_en.setText(model.getEng_item());
		cvHolder.txt_sentence_cn.setText(model.getPinyin());
		cvHolder.txt_sentence_en.setText(model.getChn_item());

		if (selectPosition == position) {
			cvHolder.lin_bg.setVisibility(View.VISIBLE);
			// 播放当前语音
			// new
			// MediaPlayerHelper(DatabaseHelperMy.CACHE_DIR_DOWNLOAD+"/kit/1/"+model.getPron_file_name()).play();
			MediaPlayUtil.getInstance().play(
					DatabaseHelperMy.CACHE_DIR_DOWNLOAD + "/kit/"
							+ model.getCid() + "/" + model.getPron_file_name());

		} else {
			cvHolder.lin_bg.setVisibility(View.GONE);
		}

		return convertView;
	}

	private void initEvent(final int position) {
		
		cvHolder.img_record.setOnClickListener(new OnClickListener() {// 录音的点击事件

			@Override
			public void onClick(View v) { 
				//点击后录音 再次点击停止录音 并播放正确录音
					isRecord = !isRecord;
				setRecoedBg((ImageView) v);
			}
		});

		cvHolder.img_record_play.setOnClickListener(new OnClickListener() {// 慢速

					@Override
					public void onClick(View v) {
						//播放对应的慢速的语音
						playSlowVoice(position);
					}
				});

		cvHolder.img_loop.setOnClickListener(new OnClickListener() {// 循环

					@Override
					public void onClick(View v) {
						//设置是否循环 点击后图标改变 停止正在循环的录音 判断当前语音是否循环
					}
				});

		cvHolder.ck_collect
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean checked) {
						// TODO Auto-generated method stub
						// if (checked) {
						// LearnUnitBaseModel modelBase = new
						// LearnUnitBaseModel();
						// CustomApplication.app.favouriteList.add(modelBase);
						arg0.setChecked(checked);
						// checked=!checked;
						// } else {
						// cvHolder.ck_collect.setChecked(checked);
						// checked=!checked;
						// 从中找到这一个对象，删除
						// for (int i = 0; i <
						// CustomApplication.app.favouriteList
						// .size(); i++) {
						// LearnUnitBaseModel modelBase =
						// CustomApplication.app.favouriteList
						// .get(i);
						//
						// if (modelBase == null) {
						// continue;
						// }
						// // 暂时 用name做校验
						// String unitName = modelBase.getUnitName();
						// if ("Favourite".equals(unitName)) {
						// CustomApplication.app.favouriteList
						// .remove(i);
						// checked=false;
						// break;
						// }
						// }
						// }
						// SurvivalListAdapter.this.notifyDataSetChanged();
					}
				});

	}

	protected void setRecoedBg(ImageView imageView) {
		if (isRecord) {
			imageView.setBackgroundDrawable(resources.getDrawable(R.drawable.recorder_animate_bg_click));
			flag = "talk";
			try {
				String fileName = lastRecFileName;
				mr = new AudioRecorder(fileName);
				mr.start();
				// 音量变化调节
				mr.setVMChangeListener(new VMChangeListener() {
					@Override
					public int onVMChanged(int value) {

						int volume = mr.recorder.getMaxAmplitude();
						vmValue = 15 * volume / 32768;
						thread = new Thread(new VmChangeRunAble());
						thread.start();
						return 0;
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {

			flag = "listen";
			try {
				if (mr != null) {
					mr.stop();
					mr = null;
				}
				MediaPlayUtil.getInstance().play(filePath);
				imageView.setBackgroundDrawable(resources.getDrawable(R.drawable.recorder_animate_bg));
				imageView.setImageDrawable(resources.getDrawable(R.drawable.recorder_animate_01));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class VmChangeRunAble implements Runnable {
		@Override
		public void run() {
			Looper.prepare();
			while (true) {
				try {
					Looper mainLooper = Looper.getMainLooper();
					MyHandler handler = new MyHandler(mainLooper);
					Object obj = null;
					Message msg = Message.obtain();
					switch (vmValue) {
					case 0:
						msg.what = 0;
						msg.obj = obj;
						break;
					case 1:
						msg.what = 1;
						msg.obj = obj;
						break;
					case 2:
						msg.what = 2;
						msg.obj = obj;
						break;
					case 3:
						msg.what = 3;
						msg.obj = obj;

						break;
					case 4:
						msg.what = 4;
						msg.obj = obj;
						break;
					case 5:
						msg.what = 5;
						msg.obj = obj;
						break;
					case 6:
						msg.what = 6;
						msg.obj = obj;
						break;
					case 7:
						msg.what = 7;
						msg.obj = obj;
						break;
					case 8:
						msg.what = 8;
						msg.obj = obj;
						break;
					case 9:
						msg.what = 9;
						msg.obj = obj;
						break;
					case 10:
						msg.what = 10;
						msg.obj = obj;
						break;
					case 11:
						msg.what = 11;
						msg.obj = obj;
						break;
					case 12:
						msg.what = 12;
						msg.obj = obj;
						break;
					case 13:
						msg.what = 13;
						msg.obj = obj;
						break;
					case 14:
						msg.what = 14;
						msg.obj = obj;
						break;
					case 15:
						msg.what = 15;
						msg.obj = obj;
						break;
					}
					handler.sendMessage(msg);
					Thread.sleep(1000);// 1秒钟

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 用来处理消息,改变图片
	 */
	private class MyHandler extends Handler {

		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) // 处理消息
		{
			switch (msg.what) {

			case 0:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_01));
				break;

			case 1:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_01));
				break;

			case 2:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_02));
				break;

			case 3:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_03));
				break;

			case 4:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_04));
				break;

			case 5:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_05));
				break;

			case 6:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_06));
				break;

			case 7:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_07));
				break;
			case 8:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_08));
				break;
			case 9:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_09));
				break;
			case 10:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_10));
				break;
			case 11:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_11));
				break;
			case 12:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_12));
				break;
			case 13:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_13));
				break;
			case 14:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_14));
				break;
			case 15:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_15));
				break;

			default:
				cvHolder.img_record.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_01));
			}

		}
	}
	
	
	private void playSlowVoice(final int position) {
		String fileName = data.get(position).getSlow_pron_file_name();
		int cid = data.get(position).getCid();
		String slowPath=DatabaseHelperMy.SOUND_PATH+"/"+cid+"/"+fileName;
		MediaPlayUtil instance = MediaPlayUtil.getInstance();
		instance.setPlayOnCompleteListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});
		instance.play(slowPath);//播放慢速路径
	}
	
	class ViewHolder {
		public ImageView img_record_play;
		public ImageView img_loop;
		public ImageView img_record;
		TextView tv_child_char;
		TextView tv_child_pinyin;
		TextView tv_child_en;
		CheckBox ck_collect;
		LinearLayout lin_bg;
		TextView txt_sentence_cn;
		TextView txt_sentence_en;
	}

}
