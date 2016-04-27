package com.hw.chineseLearn.tabDiscover;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbFileDownload;
import com.hw.chineseLearn.db.DatabaseHelper;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.hw.chineseLearn.model.PinyinModel;
import com.hw.chineseLearn.model.PinyinModel.PinyinListModel;
import com.j256.ormlite.dao.Dao;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.util.tool.AudioRecorder;
import com.util.tool.FileTools;
import com.util.tool.MediaPlayUtil;
import com.util.tool.Utility;
import com.util.tool.AudioRecorder.VMChangeListener;
import com.util.weight.CHScrollView;

/**
 * 
 * 带滑动表头与固定列的ListView
 */
public class PinyinExerciseActivity extends Activity implements
		OnCheckedChangeListener {
	private ListView mListView;
	// 方便测试，直接写的public
	public HorizontalScrollView mTouchView;
	// 装入所有的HScrollView
	protected List<CHScrollView> mHScrollViews = new ArrayList<CHScrollView>();

	String[] firstRow = new String[] { "a", "ai", "an", "ang", "ao", "i", "ia",
			"ian", "iang", "iao", "ie", "iong", "iu", "in", "ing", "u", "ua",
			"uai", "uan", "uang", "uei", "uen", "ueng", "uo", "e", "ei", "en",
			"eng", "er", "o", "ou", "ong", "ü", "üe", "üan", "ün" };
	String[] firstColoum = { "-", "b", "p", "m", "f", "d", "t", "n", "l", "g",
			"k", "h", "j", "q", "x", "zh", "ch", "sh", "r", "z", "c", "s" };

	String[][] firstShengMu;
	String[][] firstYunmu = {
			{ "a", "ai", "an", "ang", "ao" },
			{ "i", "ia", "ian", "iang", "iao", "ie", "iong", "iu", "in", "ing" },
			{ "u", "ua", "uai", "uan", "uang", "uei", "uen", "ueng", "uo" },
			{ "e", "ei", "en", "eng", "er" }, { "o", "ou", "ong" },
			{ "ü", "üe", "üan", "ün" } };
	private List<List<String>> shengMuList;// 分组保存所有的firstColoum
	private List<List<String>> yunMuList;// 分组保存所有的firstRow
	private int width;// 单元格的宽和高
	private int height;
	private int popWidth;
	private int popHeight;
	private int geHeight;
	int tone = 0;

	// int soundIndex = 0;// 默认播放的索引
	boolean isRecord = false;// 是否正在录音

	private String lastRecFileName = "kitrecoder";
	private String filePath = DatabaseHelper.CACHE_DIR_SOUND + "/"
			+ lastRecFileName + ".amr";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroll);
		
		CustomApplication.app.addActivity(this);
		http = new HttpUtils();
		for (int i = 1; i <= 6; i++) {
			downLoad(i);
		}

		String jsonFromAssets = Utility.getJsonFromAssets(
				PinyinExerciseActivity.this, "pinyin.json");
		String no_exist = Utility.getJsonFromAssets(
				PinyinExerciseActivity.this, "no_exist_tone.json");
		try {
			jsonObj = new JSONObject(jsonFromAssets);
			no_existObject = new JSONObject(no_exist);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		width = (int) (Utility.getScreenWidth(this) * ((float) 1 / 7));
		height = (int) (Utility.getScreenWidthHeight(this) * ((float) 1 / 10));
		popWidth = (int) (Utility.getScreenWidth(this) * ((float) 7 / 8));
		popHeight = (int) (Utility.getScreenWidthHeight(this) * ((float) 2 / 5));
		geHeight = (int) (Utility.getScreenWidthHeight(this) * ((float)  7/ 8));
		
		initArrayToList();
		initFirstRow();
		initData();
		initViews();
	}

	private void initArrayToList() {
		firstShengMu = new String[][] { { "-", "b", "p", "m", "f" },
				{ "d", "t", "n", "l" }, { "g", "k", "h" }, { "j", "q", "x" },
				{ "zh", "ch", "sh", "r" }, { "z", "c", "s" } };
		firstYunmu = new String[][] {
				{ "a", "ai", "an", "ang", "ao" },
				{ "i", "ia", "ian", "iang", "iao", "ie", "iong", "iu", "in",
						"ing" },
				{ "u", "ua", "uai", "uan", "uang", "uei", "uen", "ueng", "uo" },
				{ "e", "ei", "en", "eng", "er" }, { "o", "ou", "ong" },
				{ "ü", "üe", "üan", "ün" } };

		shengMuList = new ArrayList<List<String>>();
		yunMuList = new ArrayList<List<String>>();
		for (int i = 0; i < firstShengMu.length; i++) {
			List<String> subShengMuList = Arrays.asList(firstShengMu[i]);
			shengMuList.add(subShengMuList);
		}
		for (int i = 0; i < firstYunmu.length; i++) {
			List<String> subYunmuList = Arrays.asList(firstYunmu[i]);
			yunMuList.add(subYunmuList);
		}
	}

	private void initFirstRow() {
		CHScrollView headerScroll = (CHScrollView) findViewById(R.id.item_scroll_title);
		LinearLayout ll_scroll_text = (LinearLayout) findViewById(R.id.ll_scroll_text);// item_title
		TextView item_title = (TextView) findViewById(R.id.item_title);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width,
				height);
		item_title.setLayoutParams(param);
		for (int i = 0; i < firstRow.length; i++) {
			TextView tv = initTextView(i);
			// 如果是数组的第奇数个包含就设置深色 偶数就设置浅色
			for (int j = 0; j < yunMuList.size(); j++) {
				if (yunMuList.get(j).contains(firstRow[i])) {
					if (j % 2 == 0) {
						tv.setBackgroundResource(R.drawable.bg_boder_deep_1px);// 设置第一行的背景
						break;
					} else {
						tv.setBackgroundResource(R.drawable.bg_boder_lower_1px);// 设置第一行的背景
					}
				}
			}
			tv.setText(firstRow[i]);
			ll_scroll_text.addView(tv);// 第一行所有
		}
		// 添加头滑动事件
		mHScrollViews.add(headerScroll);
	}

	// 单元格中所有数据
	private void initData() {
		// int tone = 1;
		coloumList = new ArrayList<List<Object>>();
		for (int i = 0; i < firstColoum.length; i++) {
			ArrayList rowList = new ArrayList();
			for (int j = 0; j < firstRow.length; j++) {

				if (!"j".equals(firstColoum[i]) && !"q".equals(firstColoum[i])
						&& !"x".equals(firstColoum[i])) {
					if (!"-".equals(firstColoum[i])) {
						if ("uei".equals(firstRow[j])) {
							firstRow[j] = "ui";
						} else if ("uen".equals(firstRow[j])) {
							firstRow[j] = "un";
						}
					}
				}
				String optString = jsonObj.optString(firstColoum[i]
						+ firstRow[j]);
				if (!"".equals(optString)) {
					if (tone == 0) {
						List<Integer> checkToneList = new ArrayList<Integer>();
						String checkTone1 = optString + "1";
						String checkTone2 = optString + "2";
						String checkTone3 = optString + "3";
						String checkTone4 = optString + "4";
						String no_existString1 = no_existObject
								.optString(checkTone1);
						String no_existString2 = no_existObject
								.optString(checkTone2);
						String no_existString3 = no_existObject
								.optString(checkTone3);
						String no_existString4 = no_existObject
								.optString(checkTone4);
						if ("".equals(no_existString1)) {
							checkToneList.add(1);
						}
						if ("".equals(no_existString2)) {
							checkToneList.add(2);
						}
						if ("".equals(no_existString3)) {
							checkToneList.add(3);
						}
						if ("".equals(no_existString4)) {
							checkToneList.add(4);
						}
						PinyinModel pinyinModel = new PinyinModel();
						PinyinModel.PinyinListModel pinyinListModel = pinyinModel.new PinyinListModel(
								firstRow[j], optString, checkToneList,
								firstColoum[i]);
						rowList.add(pinyinListModel);

					} else {

						String checkTone = optString + tone;
						String no_existString = no_existObject
								.optString(checkTone);
						if ("".equals(no_existString)) {
							// 把拼音替换成音调
							PinyinModel model = new PinyinModel(firstRow[j],
									optString, tone, firstColoum[i]);
							rowList.add(model);
						} else {
							PinyinModel model = new PinyinModel();
							rowList.add(model);
						}
					}
				} else {
					PinyinModel model = new PinyinModel();
					rowList.add(model);
				}

			}
			coloumList.add(rowList);
		}
	}

	private void initViews() {
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		Map<String, String> data = null;
		//设置表格的宽和高
		
//		LinearLayout ll_root_pinchar = (LinearLayout) findViewById(R.id.ll_root_pinchar);
//		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(popWidth,geHeight);
//		ll_root_pinchar.setLayoutParams(params);
		
		rg_shengdiao = (RadioGroup) findViewById(R.id.rg_shengdiao);
		rg_shengdiao.setOnCheckedChangeListener(this);
		mListView = (ListView) findViewById(R.id.scroll_list);
		
		setTitle(View.GONE, View.VISIBLE,
				R.drawable.back_btn, "Pinyin Exercise",
				View.GONE, View.GONE, 0);
		
		myAdapter = new MyAdapter();
		mListView.setAdapter(myAdapter);

	}

	private TextView initTextView(int i) {
		TextView tv = new TextView(this);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width,
				height);
		tv.setLayoutParams(param);
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(Color.parseColor("#000000"));// 设置文字为黑色
		tv.setText(firstRow[i]);
		tv.setBackgroundResource(R.drawable.bg_boder_1px);
		return tv;
	}

	public void addHViews(final CHScrollView hScrollView) {
		if (!mHScrollViews.isEmpty()) {
			int size = mHScrollViews.size();
			CHScrollView scrollView = mHScrollViews.get(size - 1);
			final int scrollX = scrollView.getScrollX();
			// 第一次满屏后，向下滑动，有一条数据在开始时未加入
			if (scrollX != 0) {
				mListView.post(new Runnable() {
					@Override
					public void run() {
						// 当listView刷新完成之后，把该条移动到最终位置
						hScrollView.scrollTo(scrollX, 0);
					}
				});
			}
		}
		mHScrollViews.add(hScrollView);
	}

	public void onScrollChanged(int l, int t, int oldl, int oldt) {
		for (CHScrollView scrollView : mHScrollViews) {
			// 防止重复滑动
			if (mTouchView != scrollView)
				scrollView.smoothScrollTo(l, t);
		}
	}

	private List<Object> mSuList;
	// 测试点击的事件
	protected View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			if (tone == 0) {
				mSuList = getZeroColoum((Integer) ((TextView) v).getTag());
			} else {
				mSuList = getColoumList((Integer) ((TextView) v).getTag());
			}

			// Toast.makeText(PinyinExerciseActivity.this, ((TextView)
			// v).getTag() + "",
			// Toast.LENGTH_SHORT).show();

			if (!"".equals(((TextView) v).getText())) {// 不是""的时候才弹出对话框
				showDialogAndPlay();
			}
		}
	};

	private List<Object> getZeroColoum(int tag) {
		int row = tag % 10000;
		int coloum = tag / 10000;
		List<Object> suList = new ArrayList<Object>();
		List<Object> list = coloumList.get(coloum);

		PinyinListModel pinyinListModel = null;
		if (list.get(row) instanceof PinyinListModel) {
			pinyinListModel = (PinyinListModel) list.get(row);
			for (int i = 0; i < pinyinListModel.pinyinList.size(); i++) {
				String pinyin = pinyinListModel.pinyinList.get(i).getPinyin();
				if (!TextUtils.isEmpty(pinyin)) {
					suList.add(pinyinListModel.pinyinList.get(i));
					currentIndex = 0;
				}
			}
		}

		return suList;
	}

	int currentIndex = 0;

	/**
	 * 得到所有的列集合
	 * 
	 * @return
	 */
	private List<Object> getColoumList(int tag) {
		// coloumList
		int row = tag % 10000;
		int coloum = tag / 10000;
		List<Object> suList = new ArrayList<Object>();
		for (int i = 0; i < coloumList.size(); i++) {
			List<Object> list = coloumList.get(i);
			if (list.get(row) instanceof PinyinModel) {
				if (!TextUtils.isEmpty(((PinyinModel) list.get(row))
						.getPinyin())) {
					suList.add(list.get(row));
					if (coloum == i) {
						currentIndex = suList.size() - 1;
					}
				}
			}
		}
		return suList;
	}

	private JSONObject jsonObj;
	private List<List<Object>> coloumList;
	private JSONObject no_existObject;
	private RadioGroup rg_shengdiao;
	private MyAdapter myAdapter;

	class MyAdapter extends BaseAdapter {

		private ViewHolder vh;

		@Override
		public int getCount() {
			return firstColoum.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			currentPosition = position;

			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(PinyinExerciseActivity.this)
						.inflate(R.layout.item, null);
				vh.tv = (TextView) convertView.findViewById(R.id.item_title);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						width, height);
				vh.tv.setLayoutParams(params);
				vh.ll = (LinearLayout) convertView
						.findViewById(R.id.ll_scroll_text);
				// 给第一行填充数据
				for (int i = 0; i < firstRow.length; i++) {
					TextView tv = initTextView(i);
					tv.setTag(position * 10000 + i);
					tv.setOnClickListener(clickListener);
					vh.ll.addView(tv);// 下边的行的textview
				}

				addHViews((CHScrollView) convertView
						.findViewById(R.id.item_scroll));
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
				for (int i = 0; i < vh.ll.getChildCount(); i++) {
					int row = ((Integer) (vh.ll.getChildAt(i).getTag())) % 10000;
					vh.ll.getChildAt(i).setTag(position * 10000 + row);
				}
			}

			for (int j = 0; j < shengMuList.size(); j++) {
				if (shengMuList.get(j).contains(firstColoum[position])) {
					if (j % 2 == 0) {
						vh.tv.setBackgroundResource(R.drawable.bg_boder_deep_1px);// 设置第一行的背景
						break;
					} else {
						vh.tv.setBackgroundResource(R.drawable.bg_boder_lower_1px);// 设置第一行的背景
					}
				}
			}
			vh.tv.setText(firstColoum[position]);
			for (int i = 0; i < vh.ll.getChildCount(); i++) {
				TextView tv = (TextView) vh.ll.getChildAt(i);
				// 确定强转类型 设置文本
				if (coloumList.get(position).get(i) instanceof PinyinListModel) {
					tv.setText(((PinyinListModel) coloumList.get(position).get(
							i)).getPinyin());
				} else {
					tv.setText(((PinyinModel) coloumList.get(position).get(i))
							.getPinyin());
				}

				// 过滤背景颜色
				for (int j = 0; j < yunMuList.size(); j++) {
					for (int m = 0; m < shengMuList.size(); m++) {
						if (yunMuList.get(j).contains(firstRow[i])
								&& shengMuList.get(m).contains(
										firstColoum[position])) {

							if (j % 2 == 0 && m % 2 == 0) {
								tv.setBackgroundResource(R.drawable.bg_boder_lower_1px);
								break;
							} else {
								tv.setBackgroundResource(R.drawable.bg_boder_1px);
							}
						}
					}
				}

			}
			return convertView;
		}

	}

	int currentPosition = 0;

	class ViewHolder {
		public LinearLayout ll;
		public TextView tv;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.checked1:
			tone = 1;
			initData();
			if (myAdapter != null) {
				myAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.checked2:
			tone = 2;
			initData();
			if (myAdapter != null) {
				myAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.checked3:
			tone = 3;
			initData();
			if (myAdapter != null) {
				myAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.checked4:
			tone = 4;
			initData();
			if (myAdapter != null) {
				myAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.checked5:
			tone = 0;
			initData();
			if (myAdapter != null) {
				myAdapter.notifyDataSetChanged();
			}
			break;
		}
	}

	AlertDialog mModifyDialog;

	private void showDialogAndPlay() {
		if (mModifyDialog == null) {
			mModifyDialog = new AlertDialog.Builder(this).create();
		}

		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.popwindow_pinyinchart_activity,
				null);
		LinearLayout ll_root = (LinearLayout) view.findViewById(R.id.ll_root);
		ll_root.setBackgroundResource(R.drawable.pop_bg_pinchar);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				popWidth, popHeight);
		ll_root.setLayoutParams(params);

		final TextView tv_pinchar = (TextView) view
				.findViewById(R.id.tv_pinchar);
		final ImageView iv_pinchar_left = (ImageView) view
				.findViewById(R.id.iv_pinchar_left);
		final ImageView iv_pinchar_right = (ImageView) view
				.findViewById(R.id.iv_pinchar_right);
		ImageView iv_record = (ImageView) view.findViewById(R.id.iv_record);
		String text = ((PinyinModel) mSuList.get(currentIndex)).getPinyin();
		tv_pinchar.setText(text);

		// 左箭头的点击事件
		iv_pinchar_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentIndex != 0) {
					currentIndex--;
					PinyinModel model = ((PinyinModel) mSuList
							.get(currentIndex));
					tv_pinchar.setText(model.getPinyin());
					iv_pinchar_right
							.setBackgroundResource(R.drawable.pinyin_next1);
					iv_pinchar_right.setEnabled(true);
					iv_pinchar_left
							.setBackgroundResource(R.drawable.pinyin_last1);
				} else {
					iv_pinchar_left
							.setBackgroundResource(R.drawable.pinyin_last0);
					iv_pinchar_left.setEnabled(false);
				}
				playPre();
			}
		});
		// you箭头的点击事件
		iv_pinchar_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentIndex != mSuList.size() - 1) {
					currentIndex++;
					PinyinModel model = ((PinyinModel) mSuList
							.get(currentIndex));
					tv_pinchar.setText(model.getPinyin());
					iv_pinchar_left
							.setBackgroundResource(R.drawable.pinyin_last1);
					iv_pinchar_right
							.setBackgroundResource(R.drawable.pinyin_next1);
					iv_pinchar_left.setEnabled(true);
				} else {
					iv_pinchar_right
							.setBackgroundResource(R.drawable.pinyin_next0);
					iv_pinchar_right.setEnabled(false);
				}
				playNext();
			}
		});

		// 点击后根据回调改变图片颜色 并且开始录音 录音结束后 播放当前音频
		iv_record.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isRecord = !isRecord;
				setRecoedBg((ImageView) v);
			}
		});

		play();

		mModifyDialog.show();
		// mModifyDialog.getWindow().setLayout(popWidth, popHeight);
		mModifyDialog.setContentView(view);
	}

	private AudioRecorder mr;

	public void setRecoedBg(final ImageView imageView) {

		if (isRecord) {
			imageView.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.recorder_animate_bg_click));
			// flag = "talk";
			try {
				String fileName = lastRecFileName;
				mr = new AudioRecorder(fileName);
				mr.start();
				// 音量变化调节
				mr.setVMChangeListener(new VMChangeListener() {
					@Override
					public int onVMChanged(int value) {

						int volume = mr.recorder.getMaxAmplitude();
						int vmValue = 15 * volume / 32768;
						Message msg = Message.obtain();
						msg.what = vmValue;
						msg.obj = imageView;
						hand.sendMessage(msg);
						return 0;
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			// flag = "listen";
			if (mr != null) {
				mr.reset();
				mr = null;
			}
			playRecoder();// 播放录制的声音
			imageView.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.recorder_animate_bg));
			imageView.setImageDrawable(getResources().getDrawable(
					R.drawable.recorder_animate_01));
		}

	}

	private void playRecoder() {
		MediaPlayUtil instance = MediaPlayUtil.getInstance();
		instance.setPlayOnCompleteListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
					MediaPlayUtil.getInstance().release();
					play();
			}
		});
		instance.play(filePath);
	}

	private void play() { 
		if (mSuList.get(currentIndex) instanceof PinyinModel) {
			PinyinModel pinyinModel = (PinyinModel) mSuList.get(currentIndex);
			String voicePath = pinyinModel.getVoicePath();
			MediaPlayUtil.getInstance().play(
					DatabaseHelperMy.SOUND_PATH + "/pinchar/" + voicePath);
		} else {
			PinyinListModel model = (PinyinListModel) mSuList.get(currentIndex);
			String voicePath = model.pinyinList.get(0).getVoicePath();
			MediaPlayUtil.getInstance().play(
					DatabaseHelperMy.SOUND_PATH + "/pinchar/" + voicePath);
		}
	}

	public void playNext() {
		// if (soundIndex < mSuList.size()-1) {
		// soundIndex++;
		// if(mSuList.get(currentIndex) instanceof PinyinListModel){
		// PinyinListModel model = (PinyinListModel) mSuList.get(currentIndex);
		// MediaPlayUtil.getInstance().play(DatabaseHelperMy.SOUND_PATH+"/pinchar/"+model.pinyinList.get(0).getVoicePath());
		// }else{
		// PinyinModel model = (PinyinModel) mSuList.get(currentIndex);
		// MediaPlayUtil.getInstance().play(DatabaseHelperMy.SOUND_PATH+"/pinchar/"+model.getVoicePath());
		// }
		//
		// }
		play();
	}

	public void playPre() {
		// if (soundIndex > 0) {
		// soundIndex--;
		// if(mSuList.get(currentIndex) instanceof PinyinListModel){
		// PinyinListModel model = (PinyinListModel) mSuList.get(currentIndex);
		// MediaPlayUtil.getInstance().play(DatabaseHelperMy.SOUND_PATH+"/pinchar/"+model.pinyinList.get(0).getVoicePath());
		// }else{
		// PinyinModel model = (PinyinModel) mSuList.get(currentIndex);
		// MediaPlayUtil.getInstance().play(DatabaseHelperMy.SOUND_PATH+"/pinchar/"+model.getVoicePath());
		// }
		// }
		play();
	}

	private void downLoad(final int index) {

		final String urlName = "https://d2kox946o1unj2.cloudfront.net/CPY_Part"
				+ index + ".zip";
		final String filePath = DatabaseHelperMy.CACHE_DIR_DOWNLOAD
				+ "/CPY_Part" + index + ".zip";
		HttpHandler handler = http.download(urlName, filePath, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
				true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
				new RequestCallBack<File>() {
					@Override
					public void onStart() {

					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

						System.out.println("current" + index + "--"
								+ (float) current / total);
					}

					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						// 下载成功后 存表 解压
						TbFileDownload fileDown = new TbFileDownload();
						fileDown.setType(2);
						fileDown.setDlStatus(1);
						fileDown.setFileName("CPY_Part" + index + ".zip");
						fileDown.setFileURL(urlName);
						try {
							MyDao.getDaoMy(TbFileDownload.class)
									.createOrUpdate(fileDown);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("onSuccess--" + downIndex);
						// 下载完后解压到音频目录
						new Thread() {
							public void run() {
								FileTools.unZip(filePath,
										DatabaseHelperMy.SOUND_PATH
												+ "/pinchar/");
							};
						}.start();

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("失败");
					}
				});

	};

	int downIndex = 1;
	private HttpUtils http;

	Handler hand = new Handler() {
		public void handleMessage(Message msg) {
			Resources resources = PinyinExerciseActivity.this.getResources();
			ImageView imageView = (ImageView) msg.obj;
			switch (msg.what) {
			case 0:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_01));
				break;

			case 1:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_01));
				break;

			case 2:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_02));
				break;

			case 3:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_03));
				break;

			case 4:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_04));
				break;

			case 5:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_05));
				break;

			case 6:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_06));
				break;

			case 7:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_07));
				break;
			case 8:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_08));
				break;
			case 9:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_09));
				break;
			case 10:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_10));
				break;
			case 11:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_11));
				break;
			case 12:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_12));
				break;
			case 13:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_13));
				break;
			case 14:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_14));
				break;
			case 15:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_15));
				break;

			default:
				imageView.setImageDrawable(resources
						.getDrawable(R.drawable.recorder_animate_01));
			}

		};
	};
	
	public void setTitle(int textLeft, int imgLeft, int imgLeftDrawable,
			String title, int textRight, int imgRight, int imgRightDrawable) {

		View view_title = (View) this.findViewById(R.id.view_title);
		view_title.setBackgroundColor(getResources().getColor(R.color.pinyinchart));
		Button tv_title = (Button) view_title.findViewById(R.id.btn_title);
		tv_title.setTextColor(Color.WHITE);
		tv_title.setText(title);

		TextView tv_title_left = (TextView) view_title
				.findViewById(R.id.tv_title_left);
		tv_title_left.setVisibility(textLeft);

		ImageView iv_title_left = (ImageView) view_title
				.findViewById(R.id.iv_title_left);
		iv_title_left.setVisibility(imgLeft);
		iv_title_left.setOnClickListener(onClickListener);
		iv_title_left.setImageResource(imgLeftDrawable);

		TextView tv_title_right = (TextView) view_title
				.findViewById(R.id.tv_title_right);
		tv_title_right.setVisibility(textRight);
//		tv_title_right.setOnClickListener(onClickListener);

		ImageView iv_title_right = (ImageView) view_title
				.findViewById(R.id.iv_title_right);
		iv_title_right.setVisibility(imgRight);
		iv_title_right.setImageResource(imgRightDrawable);

	}
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app.finishActivity(PinyinExerciseActivity.this);
				break;

			default:
				break;
			}
		}
	};
}
