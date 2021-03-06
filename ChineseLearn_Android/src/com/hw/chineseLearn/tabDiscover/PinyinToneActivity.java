package com.hw.chineseLearn.tabDiscover;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.ToneAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.LGCharacter;
import com.hw.chineseLearn.dao.bean.LGWord;
import com.hw.chineseLearn.dao.bean.TbMyPinyinTone;
import com.hw.chineseLearn.model.PinyinToneLessonExerciseModel;
import com.util.tool.FileTools;
import com.util.tool.JsonUtil;
import com.util.weight.SelfGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 音调练习
 * 
 * @author yh
 */
public class PinyinToneActivity extends BaseActivity {

	protected static final int FLUSH_UI = 100;
	private String TAG = "==PinyinToneActivity==";
	private Context context;
	View contentView;
	ToneAdapter adapter;
	SelfGridView centGridView;
	ArrayList<TbMyPinyinTone> listBase = new ArrayList<TbMyPinyinTone>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_pinyin_tone, null);
		setContentView(contentView);
		CustomApplication.app.addActivity(this);
		context = this;
		showDialog();//弹出进度对话框 等待数据加载
		init();
	}
	
	private void showDialog() {
		mModifyDialog2 = new AlertDialog.Builder(this).create();

		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.progress,null);
		View findViewById = view.findViewById(R.id.pro);
		FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity=Gravity.CENTER;
		mModifyDialog2.show(); 
		mModifyDialog2.setContentView(view,params);		
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {
		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				getString(R.string.title_pinyin_tone), View.GONE, View.GONE, 0);

		centGridView = (SelfGridView) contentView
				.findViewById(R.id.gv_tone_gridview);
		new Thread(){
			public void run() {
				getDatasFronFile();
				handler.sendEmptyMessage(FLUSH_UI);
			};
		}.start();
		
	}

	@SuppressWarnings("unchecked")
	private void getDatasFronFile() {
		String results = FileTools.getJsonFromAsset(getApplicationContext(),
				"pinyin_tone.json");
		if (!"".equals(results)) {
			try {
				JSONObject jsObj = new JSONObject(results);

				int jsObjCount = jsObj.length();
				Log.d(TAG, "jsObjCount:" + jsObjCount);

				for (int i = 0; i < jsObjCount; i++) {
					TbMyPinyinTone tbMyPinyinTone = null;
					JSONArray jsonArray = jsObj.getJSONArray("" + (i + 1));

					tbMyPinyinTone = (TbMyPinyinTone) MyDao.getDaoMy(
							TbMyPinyinTone.class).queryForId((i + 1));
					if (tbMyPinyinTone == null) {
						tbMyPinyinTone = new TbMyPinyinTone();
						if (i == 0) {
							tbMyPinyinTone.setIconResSuffix("pinyintone_"
									+ (i + 1));
							tbMyPinyinTone.setStatus(1);
						} else if (i == 1) {
							tbMyPinyinTone.setIconResSuffix("pinyintone_l"
									+ (i + 1));
							tbMyPinyinTone.setStatus(0);
						} else {
							tbMyPinyinTone.setIconResSuffix("pinyintone_"
									+ (i + 1));
							tbMyPinyinTone.setStatus(0);
						}
					}
					if (i == 0) {
						tbMyPinyinTone
								.setIconResSuffix("pinyintone_" + (i + 1));
					} else if (i == 1) {
						tbMyPinyinTone.setIconResSuffix("pinyintone_l"
								+ (i + 1));
					} else {
						tbMyPinyinTone
								.setIconResSuffix("pinyintone_" + (i + 1));
					}

					tbMyPinyinTone.setId(i + 1);
					MyDao.getDaoMy(TbMyPinyinTone.class).createOrUpdate(
							tbMyPinyinTone);

					int arrayLength = jsonArray.length();
					// Log.d(TAG, "arrayLength:" + arrayLength);
					listBase.add(tbMyPinyinTone);
					ArrayList<PinyinToneLessonExerciseModel> lessonModels = new ArrayList<PinyinToneLessonExerciseModel>();
					for (int j = 0; j < arrayLength; j++) {
						PinyinToneLessonExerciseModel lessonModel = new PinyinToneLessonExerciseModel();
						JSONObject jsObj2 = (JSONObject) jsonArray.opt(j);
						Iterator iterator = jsObj2.keys();
						if (iterator.hasNext()) {
							String key = (String) iterator.next();
							String value = JsonUtil.getString(jsObj2, key);
							// Log.d("key", "key：" + key);
							// Log.d("value", "value：" + value);

							String voicePath = "";
							String dirCode = "";
							String py = "";
							String cn = "";
							String en = "";
							if ("word".equals(key)) {
								LGWord lGWord = (LGWord) MyDao.getDao(
										LGWord.class).queryForId(value);
								dirCode = lGWord.getDirCode();
								py = lGWord.getPinyin();
								cn = lGWord.getWord();
								en = lGWord.getTranslations();
								voicePath = "w-" + value + "-" + dirCode
										+ ".mp3";

							} else if ("character".equals(key)) {

								LGCharacter lGCharacter = (LGCharacter) MyDao
										.getDao(LGCharacter.class).queryForId(
												value);
								if (lGCharacter != null) {
									dirCode = lGCharacter.getDirCode();
									py = lGCharacter.getPinyin();
									cn = lGCharacter.getCharacter();
									en = lGCharacter.getTranslation();
									voicePath = "c-" + value + "-" + dirCode
											+ ".mp3";
								}
							}

							lessonModel.setVoicePath(voicePath);
							lessonModel.setCn(cn);
							lessonModel.setEn(en);
							lessonModel.setPy(py);
							lessonModels.add(lessonModel);
						}
						tbMyPinyinTone.setLessonModels(lessonModels);
					}

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app.finishActivity(PinyinToneActivity.this);
				break;

			default:
				break;
			}
		}
	};
	private TbMyPinyinTone tbMyPinyinToneCurrentClicked = null;
	private int indexCurrentClicked;
	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

			indexCurrentClicked = arg2;
			tbMyPinyinToneCurrentClicked = listBase.get(arg2);
			if (tbMyPinyinToneCurrentClicked != null) {
				int status = tbMyPinyinToneCurrentClicked.getStatus();
				if (status == 1) {
					Intent intent = new Intent(PinyinToneActivity.this,
							PinyinToneExerciseActivity.class);
					intent.putExtra("model", tbMyPinyinToneCurrentClicked);
					startActivityForResult(intent, 1);
				}
			} else {
				Log.e(TAG, "tbMyPinyinToneCurrentClicked == null");
			}
		}
	};

	/**
	 * 顶部标题栏
	 * 
	 * @param textLeft
	 *            是否显示左边文字
	 * @param imgLeft
	 *            是否显示左边图片
	 * @param title
	 *            标题
	 * @param imgLeftDrawable
	 *            左边图片资源
	 * @param textRight
	 *            是否显示右边文字
	 * @param imgRight
	 *            是否显示右边图片
	 * @param imgRightDrawable
	 *            右边图片资源
	 */
	public void setTitle(int textLeft, int imgLeft, int imgLeftDrawable,
			String title, int textRight, int imgRight, int imgRightDrawable) {

		View view_title = (View) this.findViewById(R.id.view_title);
		Button tv_title = (Button) view_title.findViewById(R.id.btn_title);
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
		tv_title_right.setOnClickListener(onClickListener);

		ImageView iv_title_right = (ImageView) view_title
				.findViewById(R.id.iv_title_right);
		iv_title_right.setVisibility(imgRight);
		iv_title_right.setImageResource(imgRightDrawable);

	}

	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		switch (resultCode) {
		case 1:// passed，unlock next lesson
			TbMyPinyinTone tbMyPinyinTone = null;
			int nextIndex = indexCurrentClicked + 1;
			if (nextIndex < listBase.size()) {
				tbMyPinyinTone = listBase.get(nextIndex);
				tbMyPinyinTone.setStatus(1);// update
				if (tbMyPinyinTone != null) {
					try {
						int isSucess = MyDao.getDaoMy(TbMyPinyinTone.class)
								.update(tbMyPinyinTone);
						Log.d(TAG, "isSucess:" + isSucess);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					listBase.set(nextIndex, tbMyPinyinTone);
					adapter.notifyDataSetChanged();
				}

			}

			break;

		default:
			break;
		}
		Log.d(TAG, "resultCode:" + resultCode);
	};
	
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FLUSH_UI:
				adapter = new ToneAdapter(context, listBase);
				centGridView.setAdapter(adapter);
				centGridView.setOnItemClickListener(itemClickListener);
				adapter.notifyDataSetChanged();
				mModifyDialog2.dismiss();
				break;

			default:
				break;
			}
		};
	};
	private AlertDialog mModifyDialog2;
}
