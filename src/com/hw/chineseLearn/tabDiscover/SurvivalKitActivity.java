package com.hw.chineseLearn.tabDiscover;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.SurvivalKitAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.base.MainActivity;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbFileDownload;
import com.hw.chineseLearn.dao.bean.TbMyCategory;
import com.hw.chineseLearn.dao.bean.category;
import com.hw.chineseLearn.db.DatabaseHelperMy;
import com.hw.chineseLearn.interfaces.AppConstants;
import com.hw.chineseLearn.model.SurvivalKitModel;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.util.tool.FileTools;

/**
 * 生存模式
 * 
 * @author yh
 */
public class SurvivalKitActivity extends BaseActivity {

	public static final int LOADING = 2;// 正在下载
	public static final int FINISH = 1;// 完成
	public static final int NODOWN = 0;// 未下载
	private String TAG = "==SurvivalKitActivity==";
	private Context context;
	View contentView;
	ListView lv_survival;
	SurvivalKitAdapter adapter;
	private List<category> categoryList = new ArrayList<category>();// 数据库中所有category
	private List<TbMyCategory> myCategoryList = new ArrayList<TbMyCategory>();// 所有TbMyCategory
	private List<SurvivalKitModel> modelList = new ArrayList<SurvivalKitModel>();// adaptermodel集合

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activity_survival_kit, null);
		setContentView(contentView);
		CustomApplication.app.addActivity(this);
		context = this;
		init();
	}
	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//销毁前更新数据库下载的状态
		try {
			List<TbFileDownload> download2List = MyDao.getDaoMy(TbFileDownload.class).queryBuilder().where().eq("dlStatus", 2).query();
			List<TbMyCategory> tbMyCategoryList=MyDao.getDaoMy(TbMyCategory.class).queryBuilder().where().eq("complete_dl", 2).query();
			for (int i = 0; i < download2List.size(); i++) {
				TbFileDownload tbFileDownload = download2List.get(i);
				tbFileDownload.setDlStatus(NODOWN);
				MyDao.getDaoMy(TbFileDownload.class).update(tbFileDownload);
			}
			
			for (int i = 0; i < tbMyCategoryList.size(); i++) {
				TbMyCategory tbMyCategory = tbMyCategoryList.get(i);
				tbMyCategory.setComplete_dl(NODOWN);
				MyDao.getDaoMy(TbMyCategory.class).update(tbMyCategory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("onDestroy--onDestroy");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// if (CustomApplication.app.favouriteList.size() != 0) {
		// LearnUnitBaseModel modelBase = new LearnUnitBaseModel();
		// modelBase.setIconResSuffix("favorite_img");
		// modelBase.setUnitName("Favourite");
		// listBase.set(0, modelBase);
		// } else {
		// for (int i = 0; i < listBase.size(); i++) {
		// LearnUnitBaseModel modelBase = listBase.get(i);
		//
		// if (modelBase == null) {
		// continue;
		// }
		// String unitName = modelBase.getUnitName();
		// if ("Favourite".equals(unitName)) {
		// listBase.remove(i);
		// break;
		// }
		// }
		// }
//		initDBDatas();// categoryList ,myCategoryList
//		initModel();//把对应的model加入到modelList

		// 重新从数据库拿处数据 查表 如果对应是2 则写下载的方法 整个集合都执行下载
//		try {
//			List<TbFileDownload> download2List = MyDao
//					.getDaoMy(TbFileDownload.class).queryBuilder().where()
//					.eq("dlStatus", 2).query();
//			if (download2List != null) {
//				for (int i = 0; i < download2List.size(); i++) {
//					TbFileDownload tbFileDownload = download2List.get(i);
//					// if(tbFileDownload.getDlStatus()==LOADING){
//					// dealState(tbFileDownload.getCwsId()-1);
//					// }
//					modelList.get(tbFileDownload.getCwsId() - 1)
//							.setPositionTag(tbFileDownload.getCwsId() - 1);
//					adapter.notifyDataSetChanged();
//					download(tbFileDownload.getCwsId() - 1);
//				}
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 初始化
	 */
	public void init() {
		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Survival Kit", View.GONE, View.GONE, 0);
		lv_survival = (ListView) contentView.findViewById(R.id.lv_survival);
		lv_survival.setOnItemClickListener(onItemclickListener);
		initDBDatas();
		initModel();
		adapter = new SurvivalKitAdapter(context, modelList);
		lv_survival.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void initModel() {
		modelList.clear();
		for (int i = 0; i < categoryList.size(); i++) {
			SurvivalKitModel model = new SurvivalKitModel();
			model.setItemName(categoryList.get(i).getEng_name());
			String imageName = "";
			if (myCategoryList.get(i).getComplete_dl() == NODOWN) {
				imageName = "survival_" + myCategoryList.get(i).getId();
				model.setState(NODOWN);// 0 从未下载
				// if(isUploading){
				// holder.iv_arrow
				// .setBackgroundResource(R.drawable.item_show_arrow);//暂时测试用
				// 下载中的图片
				// }else{
				// holder.iv_arrow .setBackgroundResource(R.drawable.ls_dl_btn);
				// }
				// try {
				// TbFileDownload tbFileDownload=(TbFileDownload)
				// MyDao.getDaoMy(TbFileDownload.class).queryBuilder().where().eq("cwsId",
				// i+1).queryForFirst();
				// if(tbFileDownload==null){
				// model.setState(NODOWN);//0 从未下载
				// }else{
				// model.setState(LOADING);
				// }
				// } catch (SQLException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

			} else if (myCategoryList.get(i).getComplete_dl() == FINISH) {
				imageName = "survival_" + myCategoryList.get(i).getId()
						+ "_hit";
				model.setState(1);// 1 已下载
			} else {
				imageName = "survival_" + myCategoryList.get(i).getId();
				model.setState(2);
			}
			model.setImageName(imageName);

			// categoryList.get(i).setComplete_dl(myCategoryList.get(i).getComplete_dl());
			modelList.add(model);
		}
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	@SuppressWarnings("unchecked")
	private void initDBDatas() {
		// 从数据库中拿到测试的数据
		try {
			categoryList = MyDao.getDao(category.class).queryForAll();
			myCategoryList = MyDao.getDaoMy((TbMyCategory.class)).queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回

				// CustomApplication.app.finishActivity(SurvivalKitActivity.this);
//				SurvivalKitActivity.this.onBackPressed();
				// moveTaskToBack(false);
				startActivity(new Intent(SurvivalKitActivity.this,MainActivity.class));
				overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
				break;

			default:
				break;
			}
		}
	};
	
	public void onBackPressed() {
		startActivity(new Intent(SurvivalKitActivity.this,MainActivity.class));
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
	};

	OnItemClickListener onItemclickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// 点击后查表 确定是 跳转界面还是下载
			// 点击后 确定 跳转新界面 还是下载动画 还是

			// int cid = category.getId();
			// int complete_dl;
			// try {
			// TbMyCategory tbMyCategory=(TbMyCategory)
			// MyDao.getDaoMy(TbMyCategory.class).queryForId(cid);
			// complete_dl = tbMyCategory.getComplete_dl();
			// } catch (SQLException e) {e.printStackTrace();}
			dealState(position);// 根据不同下载状态 做对应处理
		}

	};

	private void dealState(int position) {
		category category = categoryList.get(position);
		int state = modelList.get(position).getState();

		if (state == NODOWN) {// 如果是0 下载 显示下载动画

			modelList.get(position).setState(LOADING);
			modelList.get(position).setPositionTag(position);
			adapter.notifyDataSetChanged();

			download(position);

		} else if (state == LOADING) {//正在下载就不理他
//			modelList.get(position).setPositionTag(position);
//			adapter.notifyDataSetChanged();
//			download(position);
		} else if (state == FINISH) {// 跳转界面
			Intent intent = new Intent(SurvivalKitActivity.this,SurvivalKitDetailActivity.class);
			intent.putExtra("category", category);
			startActivity(intent);
		}
	}

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

	private boolean isUploading;// 是否正在下载
	private HttpUtils http;

	protected void download(final int position) {
		if (http == null) {
			http = new HttpUtils();
		}
		final String fileName = "cssc_" + (position + 1) + ".zip";
		final String filePath = DatabaseHelperMy.CACHE_DIR_DOWNLOAD + "/cssc_"
				+ (position + 1) + ".zip";// https://d2kox946o1unj2.cloudfront.net
		HttpHandler handler = http.download(
				AppConstants.BASE_URL+"/babbel-api-app/resource/cssc_"
						+ (position + 1) + ".zip", filePath, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
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
							// tbFileDownload = (TbFileDownload) MyDao
							// .getDaoMy(TbFileDownload.class)
							// .queryBuilder()
							// .where()
							// .eq("fileName","cssc_" + (position + 1)+ ".zip")
							// .queryForFirst();
							// if (tbFileDownload != null) {
								tbFileDownload.setCurFileContentSize(current);
								tbFileDownload.setFileContentSize(total);
								MyDao.getDaoMy(TbFileDownload.class)
										.createOrUpdate(tbFileDownload);
								// }

							} else {// 如果数据库不存在 就插入
								tbFileDownload = new TbFileDownload();
								tbFileDownload.setId(position + 1);
								tbFileDownload.setCwsId(position + 1);
								tbFileDownload.setCurFileContentSize(current);
								tbFileDownload.setFileContentSize(total);// cssc_"+(position+1)+".zip
								tbFileDownload.setFileName("cssc_"
										+ (position + 1) + ".zip");
								tbFileDownload.setFilePath(filePath);
								tbFileDownload
										.setFileURL(AppConstants.BASE_URL+"/babbel-api-app/resource/cssc_"
												+ (position + 1) + ".zip");
								tbFileDownload.setType(1);
								tbFileDownload.setDlStatus(LOADING);
								MyDao.getDaoMy(TbFileDownload.class)
										.createOrUpdate(tbFileDownload);
								// 更新另一张表
								TbMyCategory category = new TbMyCategory();
								category.setId(position + 1);
								category.setComplete_dl(LOADING);
								MyDao.getDaoMy(TbMyCategory.class)
										.createOrUpdate(category);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						// 把下载的值通过集合传过去
						//进度乱跳的修复测试
						synchronized (SurvivalKitActivity.this) {
							modelList.get(position).setCount(tbFileDownload.getFileContentSize());
							modelList.get(position).setCurrentSize(tbFileDownload.getCurFileContentSize());
							adapter.notifyDataSetChanged();
						}
					}

					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						// testTextView.setText("downloaded:" +
						// responseInfo.result.getPath());
						if (tbFileDownload != null) {
						try {
							tbFileDownload.setDlStatus(FINISH);
							MyDao.getDaoMy(TbFileDownload.class).createOrUpdate(tbFileDownload);
							TbMyCategory category = new TbMyCategory();
							category.setId(tbFileDownload.getCwsId());
							category.setComplete_dl(FINISH);
							MyDao.getDaoMy(TbMyCategory.class).createOrUpdate(category);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						// 下载完后更变state和图片颜色
						modelList.get(position).setState(FINISH);
						String imageName = modelList.get(position).getImageName() + "_hit";
						modelList.get(position).setImageName(imageName);
						adapter.notifyDataSetChanged();
						// 下载完后解压到音频目录
						new Thread() {
							public void run() {
								FileTools.unZip(filePath,DatabaseHelperMy.SOUND_PATH);
							};
						}.start();
					}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						
						if (tbFileDownload != null) {
						//若出现异常 下载失败 则重新开始下载
						try {
							tbFileDownload.setDlStatus(NODOWN);
							MyDao.getDaoMy(TbFileDownload.class).createOrUpdate(tbFileDownload);
							TbMyCategory category = new TbMyCategory();
							category.setId(tbFileDownload.getCwsId());
							category.setComplete_dl(NODOWN);
							MyDao.getDaoMy(TbMyCategory.class).createOrUpdate(category);
						} catch (SQLException e) {
						
						}
						adapter.notifyDataSetChanged();
						//在主线程中弹出吐死提示
						SurvivalKitActivity.this.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(SurvivalKitActivity.this, "Download failure", 0).show();
							}
						});
					}
					}});

	}

}
