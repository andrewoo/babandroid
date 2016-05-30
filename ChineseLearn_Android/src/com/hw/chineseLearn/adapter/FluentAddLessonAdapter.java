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
import com.hw.chineseLearn.interfaces.AppConstants;
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
    ViewHolder holder = null;

    @SuppressWarnings("unchecked")
    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        mapView.put(position, convertView);
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

        int dlStatue = checkDownLoadState(model);
        Log.d(TAG, "dlStatue:" + dlStatue);

        if (dlStatue == -1) {// 没下载过

            holder.img_add_lesson.setVisibility(View.VISIBLE);
            holder.progress_download.setVisibility(View.GONE);
            holder.img_remove_lesson.setVisibility(View.GONE);

        } else if (dlStatue == 0) {// 正在下载

            holder.img_add_lesson.setVisibility(View.GONE);
            holder.progress_download.setVisibility(View.VISIBLE);
            holder.img_remove_lesson.setVisibility(View.GONE);
            downLoadAudioFiles(position, model);
            downLoadContentFiles(position, model);

        } else if (dlStatue == 1) {// 下载过

            int DownLoadStatue = getDownLoadStatue(model.getId());// 获取标记的状态

            if (DownLoadStatue == 0) {// 标记删除

                holder.img_add_lesson.setVisibility(View.VISIBLE);
                holder.progress_download.setVisibility(View.GONE);
                holder.img_remove_lesson.setVisibility(View.GONE);

            } else {// 未标记删除
                holder.img_add_lesson.setVisibility(View.GONE);
                holder.progress_download.setVisibility(View.GONE);
                holder.img_remove_lesson.setVisibility(View.VISIBLE);
            }
        }

        // boolean isDownLoaded = checkIsDownLoaded(model);// 这一列是否下载过
        // if (isDownLoaded) {// 下载过
        // int DownLoadStatue = getDownLoadStatue(model.getId());// 获取标记的状态
        // if (DownLoadStatue == 0) {// 标记删除
        // holder.img_add_lesson.setVisibility(View.VISIBLE);
        // holder.progress_download.setVisibility(View.GONE);
        // holder.img_remove_lesson.setVisibility(View.GONE);
        // } else {// 未标记删除
        // holder.img_add_lesson.setVisibility(View.GONE);
        // holder.progress_download.setVisibility(View.GONE);
        // holder.img_remove_lesson.setVisibility(View.VISIBLE);
        // }
        //
        // } else {// 没下载过
        // holder.img_add_lesson.setVisibility(View.VISIBLE);
        // holder.progress_download.setVisibility(View.GONE);
        // holder.img_remove_lesson.setVisibility(View.GONE);
        // }

        holder.img_add_lesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                int dlStatue = checkDownLoadState(model);

                if (dlStatue == 1) {// 已下载
                    Log.e(TAG, "已下载过");
                    holder.img_add_lesson.setVisibility(View.GONE);
                    holder.img_remove_lesson.setVisibility(View.VISIBLE);
                    setDownLoaded(1, model.getId());//标记删除

                } else if (dlStatue == -1) {

                    Log.e(TAG, "没下载过");
                    holder.img_add_lesson.setVisibility(View.VISIBLE);
                    holder.progress_download.setVisibility(View.GONE);
                    holder.img_remove_lesson.setVisibility(View.GONE);
                    downLoadAudioFiles(position, model);
                    downLoadContentFiles(position, model);

                }
            }
        });

        holder.img_remove_lesson.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // String dirId = model.getDirId();
                holder.img_add_lesson.setVisibility(View.VISIBLE);
                holder.progress_download.setVisibility(View.VISIBLE);
                holder.img_remove_lesson.setVisibility(View.GONE);
                setDownLoaded(0, model.getId());

            }
        });

        return convertView;
    }

    /**
     * 获取标记的状态
     *
     * @param fluentId
     * @return DownLoadStatue 0标记删除，1
     */
    @SuppressWarnings("unchecked")
    private int getDownLoadStatue(int fluentId) {
        Log.d(TAG, "getDownLoadStatue()--fluentId:" + fluentId);
        int DownLoadStatue = 0;
        try {
            TbMyFluentNow tbMyFluentNow = (TbMyFluentNow) MyDao.getDaoMy(
                    TbMyFluentNow.class).queryForId(fluentId);
            if (tbMyFluentNow == null) {
                Log.e(TAG, "tbMyFluentNow == null,没有查询到数据");
                return DownLoadStatue;
            }
            DownLoadStatue = tbMyFluentNow.getDownloaded();
            Log.d(TAG, "DownLoadStatue:" + DownLoadStatue);
            return DownLoadStatue;

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d(TAG, "DownLoadStatue:" + DownLoadStatue);
        return DownLoadStatue;
    }

    /**
     * 是否标记删除
     *
     * @param downLoadStatue
     */
    @SuppressWarnings("unchecked")
    private void setDownLoaded(int downLoadStatue, int fluentId) {
        try {
            TbMyFluentNow tbMyFluentNow = (TbMyFluentNow) MyDao.getDaoMy(
                    TbMyFluentNow.class).queryForId(fluentId);
            if (tbMyFluentNow != null) {
                tbMyFluentNow.setDownloaded(downLoadStatue);
            } else {
                Log.e(TAG, "tbMyFluentNow == null");
            }
            int T = MyDao.getDaoMy(TbMyFluentNow.class).update(tbMyFluentNow);
            Log.d(TAG, "T:" + T);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d(TAG, "更新异常");
        }
        notifyDataSetChanged();
    }

    /**
     * 检查下载的状态-1未下载，0正在下载，1已下载
     *
     * @param model
     * @return
     */
    private int checkDownLoadState(FlunetListBaseModel model) {
        int downLoadState = -1;
        FlunetAudioContentBaseModel audioContent = model.getAudioContent();
        String fileName = "";
        if (audioContent != null) {
            fileName = audioContent.getFileName();// audio fileName
        }
        try {
            Log.d(TAG, "fileName:" + fileName);
            TbFileDownload tbFileDownload = (TbFileDownload) MyDao
                    .getDaoMy(TbFileDownload.class).queryBuilder().where()
                    .eq("fileName", fileName).queryForFirst();
            if (tbFileDownload != null) {
                downLoadState = tbFileDownload.getDlStatus();
            } else {
                Log.e(TAG,
                        "checkDownLoadState()-tbFileDownload == null,downLoadState=="
                                + downLoadState);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return downLoadState;
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

    TbFileDownload tbAudioFileDownload = null;
    TbFileDownload tbContentFileDownload = null;
    String dlAudioFileName = "";
    int baseModelId;

    @SuppressWarnings("unchecked")
    private void downLoadAudioFiles(final int position,
                                    final FlunetListBaseModel listBaseModel) {
        View convertView = mapView.get(position);
        final RoundProgressBar progress_download = (RoundProgressBar) convertView
                .findViewById(R.id.progress_download);
        final ImageView img_add_lesson = (ImageView) convertView
                .findViewById(R.id.img_add_lesson);

        baseModelId = listBaseModel.getId();
        FlunetAudioContentBaseModel audioContent = listBaseModel
                .getAudioContent();
        if (audioContent != null) {
            dlAudioFileName = audioContent.getFileName();// audio fileName
        }
        Log.d(TAG, "dlAudioFileName:" + dlAudioFileName);
        HttpUtils http = new HttpUtils();
        final String filePath = DatabaseHelperMy.CACHE_DIR_DOWNLOAD + "/"
                + dlAudioFileName;
        final String fileUrl = AppConstants.BASE_URL + "/" + dlAudioFileName;//
        Log.d(TAG, "downLoadAudioFiles()-fileUrl:" + fileUrl);
        HttpHandler handler = http.download(fileUrl, filePath, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将重新下载。
                true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {
                        Log.d(TAG, "onStart");
                        progress_download.setVisibility(View.VISIBLE);
                        img_add_lesson.setVisibility(View.GONE);
                        try {
                            tbAudioFileDownload = new TbFileDownload();
                            tbAudioFileDownload.setId(baseModelId);
                            tbAudioFileDownload.setFileName(dlAudioFileName);
                            tbAudioFileDownload.setDlStatus(0);
                            tbAudioFileDownload.setFilePath(filePath);
                            tbAudioFileDownload.setFileURL(fileUrl);
                            tbAudioFileDownload.setType(3);

                            MyDao.getDaoMy(TbFileDownload.class)
                                    .createOrUpdate(tbAudioFileDownload);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {

                        Log.d(TAG, "total:" + total + "current:" + current);

                        tbAudioFileDownload.setCurFileContentSize(current);
                        tbAudioFileDownload.setFileContentSize(total);//
                        tbAudioFileDownload.setDlStatus(0);// 正在下载

                        progress_download.setVisibility(View.VISIBLE);
                        img_add_lesson.setVisibility(View.GONE);
                        progress_download.setMax((float) total);
                        progress_download.setProgress((float) current);
                        progress_download.setAccurally(1.0f);
                        progress_download.isDrawText(false);
                        progress_download.invalidate();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        try {
                            tbAudioFileDownload.setDlStatus(1);
                            MyDao.getDaoMy(TbFileDownload.class)
                                    .createOrUpdate(tbAudioFileDownload);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        progress_download.setVisibility(View.GONE);
                        // 下载完后解压到音频目录
                        new Thread() {
                            public void run() {
                                FileTools.unZip(filePath,
                                        DatabaseHelperMy.SOUND_PATH);
                            }

                            ;
                        }.start();

                        String title_CN = listBaseModel.getTitleCn();
                        String title_EN = listBaseModel.getTitleEn();
                        int diffLevel = listBaseModel.getDiffLevel();
                        int showOrder = listBaseModel.getShowOrder();
                        String aWS_ID = listBaseModel.getDirId();

                        TbMyFluentNow tbMyFluentNow = new TbMyFluentNow();
                        tbMyFluentNow.setFluentID(baseModelId);
                        tbMyFluentNow.setAWS_ID(Integer.parseInt(aWS_ID));
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

                    }
                });

    }

    String dlTextFileName = "";

    @SuppressWarnings("unchecked")
    private void downLoadContentFiles(final int position,
                                      final FlunetListBaseModel listBaseModel) {
        Log.d(TAG, "downLoadContentFiles");
        if (listBaseModel != null) {
            baseModelId = listBaseModel.getId();
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
        final String fileUrl = AppConstants.BASE_URL + "/" + dlTextFileName;
        Log.d(TAG, "downLoadContentFiles()-fileUrl:" + fileUrl);
        HttpHandler handler = http.download(fileUrl, filePath, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {
                        try {
                            tbContentFileDownload = new TbFileDownload();
                            tbContentFileDownload.setId(baseModelId);
                            tbContentFileDownload.setFileName(dlAudioFileName);
                            tbContentFileDownload.setFilePath(filePath);
                            tbContentFileDownload.setFileURL(fileUrl);
                            tbContentFileDownload.setType(4);
                            tbContentFileDownload.setDlStatus(-1);
                            int T = MyDao.getDaoMy(TbFileDownload.class)
                                    .create(tbContentFileDownload);
                            Log.d(TAG, "T:" + T);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                        tbContentFileDownload.setCurFileContentSize(current);
                        tbContentFileDownload.setFileContentSize(total);//
                        tbContentFileDownload.setDlStatus(0);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        try {
                            tbContentFileDownload.setDlStatus(1);
                            MyDao.getDaoMy(TbFileDownload.class)
                                    .createOrUpdate(tbContentFileDownload);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        // 下载完后解压到音频目录
                        new Thread() {
                            public void run() {
                                FileTools.unZip(filePath,
                                        DatabaseHelperMy.CONTENT_JSON_PATH);
                            }

                            ;
                        }.start();

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        // modelList.get(position).setState(0);
                    }
                });

    }

}
