package com.hw.chineseLearn.adapter;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbFileDownload;
import com.hw.chineseLearn.dao.bean.TbMyFluentNow;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.hw.chineseLearn.model.FlunetAudioContentBaseModel;
import com.hw.chineseLearn.model.FlunetListBaseModel;
import com.hw.chineseLearn.model.FlunetTextContentBaseModel;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.util.tool.FileTools;
import com.util.weight.RoundProgressBar;

public class FluentAddLessonAdapter extends BaseAdapter {
	private String TAG = "FluentAddLessonAdapter";
	private Context context;
	public ArrayList<FlunetListBaseModel> list;
	private LayoutInflater inflater;

	private int width, height;
	private int selectPosition;
	private Resources resources = null;
	int colorWhite = -1;
	int colorBlack = -1;

	public FluentAddLessonAdapter(Context context,
			ArrayList<FlunetListBaseModel> list) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
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

			holder.progress_download = (RoundProgressBar) convertView
					.findViewById(R.id.progress_download);
			holder.progress_download.setVisibility(View.GONE);
			mapView.put(position, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final FlunetListBaseModel model = list.get(position);
		if (model == null) {
			return convertView;
		}

		String sentenceCn = model.getTitleCn();
		String sentenceEn = model.getTitleEn();

		holder.txt_sentence_cn.setText("" + sentenceCn);
		holder.txt_sentence_en.setText("" + sentenceEn);

		holder.iv_tag.setImageDrawable(resources
				.getDrawable(R.drawable.ls_catt_16));

		String fileName = "";
		String dirId = "";

		dirId = model.getDirId();
		FlunetAudioContentBaseModel audioContent = model.getAudioContent();
		if (audioContent != null) {
			fileName = audioContent.getFileName();// audio fileName
		}

		if (!"".equals(fileName) && fileName != null) {
			try {
				TbFileDownload tbFileDownload = (TbFileDownload) MyDao
						.getDaoMy(TbFileDownload.class).queryBuilder().where()
						.eq("fileName", fileName).queryForFirst();
				if (tbFileDownload != null) {// 下载过了
					int dlStatus = tbFileDownload.getDlStatus();
					Log.d(TAG, "dlStatus:" + dlStatus);
					if (dlStatus == 0) {
						holder.img_add_lesson.setVisibility(View.GONE);
						holder.img_remove_lesson.setVisibility(View.VISIBLE);
					} else {
						holder.img_add_lesson.setVisibility(View.VISIBLE);
						holder.img_remove_lesson.setVisibility(View.GONE);
					}

				} else {// 没下载过
					Log.d(TAG, "没下载过");
					holder.img_add_lesson.setVisibility(View.VISIBLE);
					holder.img_remove_lesson.setVisibility(View.GONE);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		holder.img_add_lesson.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				downLoadAudioFiles(position, model);
				downLoadContentFiles(position, model);
			}
		});

		holder.img_remove_lesson.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String dirId = model.getDirId();
				try {
					TbMyFluentNow tbMyFluentNow = (TbMyFluentNow) MyDao
							.getDaoMy(TbMyFluentNow.class).queryForId(dirId);
					if (tbMyFluentNow != null) {
						tbMyFluentNow.setDownloaded(0);
					}
					MyDao.getDaoMy(TbMyFluentNow.class).createOrUpdate(
							tbMyFluentNow);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

	String dlAudioFileName = "";
	String dlDirId = "";

	@SuppressWarnings("unchecked")
	private void downLoadAudioFiles(final int position,
			final FlunetListBaseModel listBaseModel) {
		Log.d(TAG, "downLoadAudioFiles");
		if (listBaseModel != null) {
			dlDirId = listBaseModel.getDirId();
			FlunetAudioContentBaseModel audioContent = listBaseModel
					.getAudioContent();
			if (audioContent != null) {
				dlAudioFileName = audioContent.getFileName();// audio fileName
			}
		}

		HttpUtils http = new HttpUtils();
		final String filePath = DatabaseHelperMy.CACHE_DIR_DOWNLOAD + "/"
				+ dlAudioFileName;
		final String fileUrl = "http://58.67.154.138:8080/" + dlAudioFileName;
		HttpHandler handler = http.download(fileUrl, filePath, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
				true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
				new RequestCallBack<File>() {
					TbFileDownload tbFileDownload;
					View convertView = mapView.get(position);
					RoundProgressBar progress_download;

					@Override
					public void onStart() {

					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

						Log.d(TAG, "total:" + total + "current:" + current);
						// 在数据库中插入记录
						// 先查询 有了就更新 没有了再new
						try {
							if (tbFileDownload != null) {// 如果数据库存在
								tbFileDownload = (TbFileDownload) MyDao
										.getDaoMy(TbFileDownload.class)
										.queryBuilder().where()
										.eq("fileName", dlAudioFileName)
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
								tbFileDownload.setFileName(dlAudioFileName);
								tbFileDownload.setFilePath(filePath);
								tbFileDownload.setFileURL(fileUrl);
								tbFileDownload.setType(3);
								tbFileDownload.setDlStatus(0);
								MyDao.getDaoMy(TbFileDownload.class)
										.createOrUpdate(tbFileDownload);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						// 把下载的值通过集合传过去
						// modelList.get(position).setCount(
						// tbFileDownload.getFileContentSize());
						// modelList.get(position).setCurrentSize(
						// tbFileDownload.getCurFileContentSize());

						progress_download = (RoundProgressBar) convertView
								.findViewById(R.id.progress_download);
						progress_download.setVisibility(View.VISIBLE);
						progress_download.setMax((float) total);
						progress_download.setProgress((float) current);
						progress_download.setAccurally(1.0f);
						progress_download.isDrawText(false);
						notifyDataSetChanged();
					}

					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						Log.d(TAG, "onSuccess");
						try {
							tbFileDownload.setDlStatus(1);
							MyDao.getDaoMy(TbFileDownload.class)
									.createOrUpdate(tbFileDownload);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						// 下载完后更变state和图片颜色
						// modelList.get(position).setState(FINISH);
						// String imageName = modelList.get(position)
						// .getImageName() + "_hit";
						// modelList.get(position).setImageName(imageName);
						notifyDataSetChanged();
						progress_download.setVisibility(View.GONE);
						// 下载完后解压到音频目录
						new Thread() {
							public void run() {
								FileTools.unZip(filePath,
										DatabaseHelperMy.SOUND_PATH);
							};
						}.start();

						String dirId = listBaseModel.getDirId();
						String title_CN = listBaseModel.getTitleCn();
						String title_EN = listBaseModel.getTitleEn();
						int diffLevel = listBaseModel.getDiffLevel();
						int showOrder = listBaseModel.getShowOrder();

						TbMyFluentNow tbMyFluentNow = new TbMyFluentNow();
						tbMyFluentNow.setDownloaded(1);
						int id = Integer.parseInt(dirId);
						Log.d(TAG, "id:" + id);
						tbMyFluentNow.setFluentID(id);
						tbMyFluentNow.setLevel(diffLevel);
						tbMyFluentNow.setTitle_CN(title_CN);
						tbMyFluentNow.setTitle_EN(title_EN);
						tbMyFluentNow.setShowOrder(showOrder);
						tbMyFluentNow.setDownloaded(1);
						try {
							MyDao.getDaoMy(TbMyFluentNow.class).createOrUpdate(
									tbMyFluentNow);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						notifyDataSetChanged();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// modelList.get(position).setState(0);
						notifyDataSetChanged();
					}
				});

	}

	String dlTextFileName = "";

	@SuppressWarnings("unchecked")
	private void downLoadContentFiles(final int position,
			final FlunetListBaseModel listBaseModel) {
		Log.d(TAG, "downLoadContentFiles");
		if (listBaseModel != null) {
			dlDirId = listBaseModel.getDirId();
			FlunetTextContentBaseModel textContent = listBaseModel
					.getTxtContent();
			if (textContent != null) {
				dlTextFileName = textContent.getFileName();// audio fileName
			}
		}

		HttpUtils http = new HttpUtils();
		// 下载的位置
		final String filePath = DatabaseHelperMy.CACHE_DIR_DOWNLOAD + "/"
				+ dlTextFileName;
		final String fileUrl = "http://58.67.154.138:8080/" + dlTextFileName;
		HttpHandler handler = http.download(fileUrl, filePath, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
				true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
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
										.eq("fileName", dlTextFileName)
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
								tbFileDownload.setFileName(dlTextFileName);
								tbFileDownload.setFilePath(filePath);
								tbFileDownload.setFileURL(fileUrl);
								tbFileDownload.setType(3);
								tbFileDownload.setDlStatus(0);
								MyDao.getDaoMy(TbFileDownload.class)
										.createOrUpdate(tbFileDownload);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}

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
						notifyDataSetChanged();
						// 下载完后解压到音频目录
						new Thread() {
							public void run() {
								FileTools.unZip(filePath,
										DatabaseHelperMy.CONTENT_JSON_PATH);
							};
						}.start();

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// modelList.get(position).setState(0);
					}
				});

	}

}
