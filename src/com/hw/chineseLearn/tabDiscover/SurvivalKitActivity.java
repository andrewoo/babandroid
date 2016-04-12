package com.hw.chineseLearn.tabDiscover;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.SurvivalKitAdapter;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbMyCategory;
import com.hw.chineseLearn.dao.bean.category;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 生存模式
 * 
 * @author yh
 */
public class SurvivalKitActivity extends BaseActivity {

	private String TAG = "==SurvivalKitActivity==";
	private Context context;
	View contentView;
	ListView lv_survival;
	SurvivalKitAdapter adapter;
	private List<category> categoryList=new ArrayList<category>();//数据库中所有category
	private List<TbMyCategory> myCategoryList=new ArrayList<TbMyCategory>();//所有TbMyCategory

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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

//		if (CustomApplication.app.favouriteList.size() != 0) {
//			LearnUnitBaseModel modelBase = new LearnUnitBaseModel();
//			modelBase.setIconResSuffix("favorite_img");
//			modelBase.setUnitName("Favourite");
//			listBase.set(0, modelBase);
//		} else {
//			for (int i = 0; i < listBase.size(); i++) {
//				LearnUnitBaseModel modelBase = listBase.get(i);
//
//				if (modelBase == null) {
//					continue;
//				}
//				String unitName = modelBase.getUnitName();
//				if ("Favourite".equals(unitName)) {
//					listBase.remove(i);
//					break;
//				}
//			}
//		}
		if(adapter!=null){
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
		initIcon();
		adapter = new SurvivalKitAdapter(context, categoryList);
		lv_survival.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void initIcon() {
		for (int i = 0; i < categoryList.size(); i++) {
			categoryList.get(i).setComplete_dl(myCategoryList.get(i).getComplete_dl());
		}
	}

	@SuppressWarnings("unchecked")
	private void initDBDatas() {
		//从数据库中拿到测试的数据
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

				CustomApplication.app.finishActivity(SurvivalKitActivity.this);
				break;

			default:
				break;
			}
		}
	};

	OnItemClickListener onItemclickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			//点击后查表 确定是 跳转界面还是下载
			 category category = categoryList.get(arg2);
			
			int cid = category.getId();
			int complete_dl; 
			try {
				TbMyCategory tbMyCategory=(TbMyCategory) MyDao.getDaoMy(TbMyCategory.class).queryForId(cid);
				complete_dl = tbMyCategory.getComplete_dl();
			} catch (SQLException e) {e.printStackTrace();}
			if(cid==0){//如果是0 下载 显示下载动画
				//查询数据库是否为空 如果不为空isUploading=true
				//欠你一个动画 根据数据库中的值 给定动画位置
				download();
				
			}else{//跳转界面
				Intent intent = new Intent(SurvivalKitActivity.this,SurvivalKitDetailActivity.class);
				intent.putExtra("category", category);
				startActivity(intent);
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

	private boolean isUploading;//是否正在下载
	protected void download() {
		HttpUtils http = new HttpUtils();
		HttpHandler handler = http.download("https://d2kox946o1unj2.cloudfront.net/cssc_1.zip",
		    "/sdcard/cssc_1.zip",
		    true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
		    true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
		    new RequestCallBack<File>() {

				@Override
		        public void onStart() {
					isUploading=true;
		        }

		        @Override
		        public void onLoading(long total, long current, boolean isUploading) {
		        	
		        	
		        }

		        @Override
		        public void onSuccess(ResponseInfo<File> responseInfo) {
//		            testTextView.setText("downloaded:" + responseInfo.result.getPath());
		        	isUploading=false;
		        }


		        @Override
		        public void onFailure(HttpException error, String msg) {
		        	isUploading=false;
		        }
		});
		
	}

}
