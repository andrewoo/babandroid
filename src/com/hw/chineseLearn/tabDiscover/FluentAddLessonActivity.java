package com.hw.chineseLearn.tabDiscover;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.FluentAddLessonAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.base.MainActivity;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbFileDownload;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.hw.chineseLearn.interfaces.AppConstants;
import com.hw.chineseLearn.model.FluentListModel;
import com.hw.chineseLearn.model.FluentModel;
import com.hw.chineseLearn.model.FlunetAudioContentBaseModel;
import com.hw.chineseLearn.model.FlunetListBaseModel;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.util.tool.FileTools;
import com.util.weight.PullToRefreshView;
import com.util.weight.PullToRefreshView.OnFooterRefreshListener;
import com.util.weight.PullToRefreshView.OnRefreshTouchListener;

/**
 * 添加流畅练习课程
 * 
 * @author yh
 */
public class FluentAddLessonActivity extends BaseActivity {

	private String TAG = "==FluentAddLessonActivity==";
	private Context context;
	View contentView;
	ListView lv_add_lesson;
	FluentAddLessonAdapter adapter;
	// 列表-上下拉刷新
	private PullToRefreshView pullToRefreshView;
	private int colorWhite = 0;
	private int colorGrey = 0;

	private LinearLayout[] mLinList;
	private Button[] mBtnList;

	private int[] leftLinIds;
	private int[] leftBtnIds;
	int selectIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_fluent_add_lesson, null);
		setContentView(contentView);
		CustomApplication.app.addActivity(this);
		context = this;
		colorWhite = context.getResources().getColor(R.color.white);
		colorGrey = context.getResources().getColor(
				R.color.chinese_skill_activity_bg);
		init();
	}

	/**
	 * 初始化
	 */
	public void init() {

		leftLinIds = new int[] { R.id.lin_level1, R.id.lin_level2,
				R.id.lin_level3 };
		leftBtnIds = new int[] { R.id.rb_level1, R.id.rb_level2, R.id.rb_level3 };

		mLinList = new LinearLayout[leftLinIds.length];
		mBtnList = new Button[leftBtnIds.length];

		for (int i = 0; i < leftLinIds.length; i++) {
			mLinList[i] = (LinearLayout) contentView
					.findViewById(leftLinIds[i]);
			mLinList[i].setOnClickListener(onClickListener);
		}

		for (int i = 0; i < leftBtnIds.length; i++) {
			mBtnList[i] = (Button) contentView.findViewById(leftBtnIds[i]);
			mBtnList[i].setClickable(false);
		}
		setLeftBtnColor(selectIndex);

		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Add Lesson", View.GONE, View.GONE, 0);
		pullToRefreshView = (PullToRefreshView) contentView
				.findViewById(R.id.pullToRefreshView);
		lv_add_lesson = (ListView) contentView.findViewById(R.id.lv_add_lesson);

		pullToRefreshView
				.setOnFooterRefreshListener(new OnFooterRefreshListener() {
					@Override
					public void onFooterRefresh(PullToRefreshView view) {
						view.onFooterRefreshComplete();
						// if (IsEnd) {
						// Toast.makeText(getActivity(), "暂无更多数据",
						// Toast.LENGTH_SHORT).show();
						// } else {
						// task.RunWithMsg(getActivity(),
						// new LoadNoticesThread(), "正在加载…");
						// }
					}
				});

		pullToRefreshView
				.setOnRefreshTouchListener(new OnRefreshTouchListener() {

					@Override
					public void onTouchListener(PullToRefreshView view) {

					}
				});
		getDataFromServer("" + (selectIndex + 1));
		if (adapter == null) {
			adapter = new FluentAddLessonAdapter(context, datas);
		}
		lv_add_lesson.setAdapter(adapter);
		adapter.notifyDataSetChanged();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	private void setLeftBtnColor(int selectIndex) {

		for (int i = 0; i < mLinList.length; i++) {

			if (selectIndex == i) {
				mLinList[i].setBackgroundColor(colorWhite);
				mBtnList[i].setSelected(true);

			} else {
				mLinList[i].setBackgroundColor(colorGrey);
				mBtnList[i].setSelected(false);
			}
		}
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				CustomApplication.app
						.finishActivity(FluentAddLessonActivity.this);

				break;

			case R.id.lin_level1:
				selectIndex = 0;
				setLeftBtnColor(selectIndex);
				getDataFromServer("" + (selectIndex + 1));
				break;

			case R.id.lin_level2:
				selectIndex = 1;
				setLeftBtnColor(selectIndex);
				getDataFromServer("" + (selectIndex + 1));
				break;

			case R.id.lin_level3:
				selectIndex = 2;
				setLeftBtnColor(selectIndex);
				getDataFromServer("" + (selectIndex + 1));
				break;

			default:
				break;
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

	ArrayList<FlunetListBaseModel> datas = new ArrayList<FlunetListBaseModel>();

	/**
	 * @param diffLevel难度级别
	 */
	private void getDataFromServer(String diffLevel) {

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET, AppConstants.BASE_URL
				+ "/babble-api-app/v1/dialogues?diffLevel=" + diffLevel,
				new RequestCallBack<String>() {

					Gson gson = new Gson();

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						// Log.d(TAG, "" + current + "/" + total);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// Log.d(TAG, "" + responseInfo.result);

						FluentModel dataObject = gson.fromJson(
								responseInfo.result, FluentModel.class);
						if (dataObject == null) {
							Log.e(TAG, "请求失败");
						} else {
							boolean isScuess = dataObject.isSuccess();
							Log.d(TAG, "isScuess:" + isScuess);
							if (isScuess) {

							}
							FluentListModel fluentListModel = dataObject
									.getResults();
							if (fluentListModel != null) {
								datas = fluentListModel.getList();
								if (datas != null) {
									Log.d(TAG, "" + datas);
									adapter.list = datas;
									adapter.notifyDataSetChanged();
								}
							} else {
								Log.e(TAG, "fluentListModel==null");
							}
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(HttpException error, String msg) {
					}
				});
	}

	String dlFileName = "";
	String dlDirId = "";

	private void downLoadAudioFiles(final FlunetListBaseModel listBaseModel) {

		if (listBaseModel != null) {
			dlDirId = listBaseModel.getDirId();
			FlunetAudioContentBaseModel audioContent = listBaseModel
					.getAudioContent();
			if (audioContent != null) {
				dlFileName = audioContent.getFileName();// audio fileName
			}
		}

		HttpUtils http = new HttpUtils();
		final String filePath = DatabaseHelperMy.CACHE_DIR_DOWNLOAD + "/"
				+ dlFileName;
		final String fileUrl = AppConstants.BASE_URL + "/" + dlFileName;
		HttpHandler handler = http.download(fileUrl, filePath, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
				false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
				new RequestCallBack<File>() {
					TbFileDownload tbFileDownload;

					@Override
					public void onStart() {

					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						// 在数据库中插入记录
						// 先查询 有了就更新 没有了再new
						try {
							if (tbFileDownload != null) {// 如果数据库存在
								tbFileDownload = (TbFileDownload) MyDao
										.getDaoMy(TbFileDownload.class)
										.queryBuilder().where()
										.eq("fileName", dlFileName)
										.queryForFirst();
								tbFileDownload.setCurFileContentSize(current);
								tbFileDownload.setFileContentSize(total);
								MyDao.getDaoMy(TbFileDownload.class)
										.createOrUpdate(tbFileDownload);
							} else {// 如果数据库不存在 就插入
								tbFileDownload = new TbFileDownload();
								tbFileDownload.setCwsId(Integer
										.parseInt(dlDirId));
								tbFileDownload.setCurFileContentSize(current);
								tbFileDownload.setFileContentSize(total);// cssc_"+(position+1)+".zip
								tbFileDownload.setFileName(dlFileName);
								tbFileDownload.setFilePath(filePath);
								tbFileDownload.setFileURL(fileUrl);
								tbFileDownload.setType(1);
								tbFileDownload.setDlStatus(0);
								MyDao.getDaoMy(TbFileDownload.class)
										.createOrUpdate(tbFileDownload);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						adapter.notifyDataSetChanged();
					}

					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						try {
							tbFileDownload.setDlStatus(1);
							MyDao.getDaoMy(TbFileDownload.class)
									.createOrUpdate(tbFileDownload);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						adapter.notifyDataSetChanged();
						// 下载完后解压到音频目录
						new Thread() {
							public void run() {
								FileTools.unZip(filePath,
										DatabaseHelperMy.SOUND_PATH);
							};
						}.start();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// modelList.get(position).setState(0);
						adapter.notifyDataSetChanged();
					}
				});

	}
}
