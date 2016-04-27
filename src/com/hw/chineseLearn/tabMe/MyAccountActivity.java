package com.hw.chineseLearn.tabMe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.base.BaseActivity;
import com.hw.chineseLearn.base.CustomApplication;
import com.hw.chineseLearn.dao.MyDao;
import com.hw.chineseLearn.dao.bean.TbLessonMaterialStatus;
import com.hw.chineseLearn.interfaces.AppConstants;
import com.hw.chineseLearn.interfaces.HttpInterfaces;
import com.util.thread.ThreadWithDialogTask;
import com.util.tool.AppFinal;
import com.util.tool.ImageLoader;
import com.util.tool.UiUtil;
import com.util.weight.CustomDialog;
import com.util.weight.RoundImageView;

/**
 * 个人中心页面
 * 
 * @author yh
 */
public class MyAccountActivity extends BaseActivity {

	private String TAG = "==MyAccountActivity==";
	private Context context;
	public RoundImageView img_photo;
	private TextView txt_email;
	private RelativeLayout rel_change_psw, rel_upload_progress,
			rel_reset_progress;
	private TextView btn_logout;
	private ThreadWithDialogTask tdt;
	HttpInterfaces interfaces;
	String emailString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		context = this;
		tdt = new ThreadWithDialogTask();
		interfaces = new HttpInterfaces(this);
		initData();
		init();
		CustomApplication.app.addActivity(this);
		super.gestureDetector();
	}

	private void initData() {
		
		emailString = CustomApplication.app.preferencesUtil.getValue(
				AppConstants.LOGIN_USERNAME, "");
	}

	/**
	 * 初始化
	 */
	public void init() {
		setTitle(View.GONE, View.VISIBLE, R.drawable.btn_selector_top_left,
				"Account", View.GONE, View.GONE, 0);

		img_photo = (RoundImageView) findViewById(R.id.img_photo);
		img_photo.setOnClickListener(onClickListener);
		txt_email = (TextView) findViewById(R.id.txt_email);

		emailString = CustomApplication.app.preferencesUtil.getValue(
				AppConstants.LOGIN_USERNAME, "");
		txt_email.setText(emailString);

		rel_change_psw = (RelativeLayout) findViewById(R.id.rel_change_psw);
		rel_upload_progress = (RelativeLayout) findViewById(R.id.rel_upload_progress);
		rel_reset_progress = (RelativeLayout) findViewById(R.id.rel_reset_progress);
		btn_logout = (TextView) findViewById(R.id.btn_logout);

		rel_change_psw.setOnClickListener(onClickListener);
		rel_upload_progress.setOnClickListener(onClickListener);
		rel_reset_progress.setOnClickListener(onClickListener);
		btn_logout.setOnClickListener(onClickListener);
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

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {

			case R.id.iv_title_left:// 返回
				CustomApplication.app.finishActivity(MyAccountActivity.this);
				break;

			case R.id.img_photo:// 设置头像

				showPickPhotoPopupWindow();
				break;
			case R.id.rel_change_psw:
				startActivity(new Intent(MyAccountActivity.this,
						MyChangePswActivity.class));

				break;
			case R.id.rel_upload_progress:

				break;
			case R.id.rel_reset_progress:
				resetProgress();

				break;

			case R.id.btn_logout:
				showLogOutDialog();
				break;

			default:
				break;
			}
		}
		
	};
	
	private void resetProgress() {
		
		try {
			List<TbLessonMaterialStatus> list = MyDao.getDaoMy(
					TbLessonMaterialStatus.class).queryForAll();
			List<Integer> idList = new ArrayList<Integer>();
			if(list.size()>3){
				for (int i = 3; i < list.size(); i++) {
					idList.add(list.get(i).getLessonId());
				}
				MyDao.getDaoMy(TbLessonMaterialStatus.class).deleteIds(idList);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	/**
	 * 退出登录对话框
	 */
	private void showLogOutDialog() {

		final AlertDialog mModifyDialog = new AlertDialog.Builder(
				MyAccountActivity.this).create();
		LayoutInflater inflater = LayoutInflater.from(MyAccountActivity.this);
		View view = inflater.inflate(R.layout.layout_dialog_loginout, null);
		TextView title = (TextView) view.findViewById(R.id.dialog_title);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		Button ok = (Button) view.findViewById(R.id.commit_btn);
		Button cancel = (Button) view.findViewById(R.id.cancel_btn);
		title.setText("Logout");
		content.setText("Are you sure to logout?");
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		ok.setText("Ok");
		cancel.setText("Cancel");
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// tdt.RunWithMsg(MySettingActivity.this, new LoginOut(),
				// "正在退出...");
				//登出
				CustomApplication.app.isLogin = false;
				CustomApplication.app.finishAllActivity();
				CustomApplication.app.preferencesUtil.setValue(AppConstants.LOGIN_PWD, "");
				CustomApplication.app.preferencesUtil.setValue(AppConstants.LOGIN_TOKEN, "");
				startActivity(new Intent(MyAccountActivity.this,MyLoginActivity.class));
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mModifyDialog.dismiss();
			}
		});
		mModifyDialog.show();
		mModifyDialog.setContentView(view);
	}

	// public class LoginOut implements ThreadWithDialogListener {
	//
	// @Override
	// public boolean TaskMain() {
	// simpleModel = interfaces.loginout();
	// return true;
	// }
	//
	// @Override
	// public boolean OnTaskDismissed() {
	// // TODO Auto-generated method stub
	// return true;
	// }
	//
	// @Override
	// public boolean OnTaskDone() {
	//
	// if (simpleModel == null) {
	// UiUtil.showToast(getApplicationContext(), "退出登录失败！");
	// return false;
	// }
	// if ("success".equals(simpleModel.getStatus())) {
	//
	// UiUtil.showToast(getApplicationContext(), "退出登录成功！");
	//
	// CustomApplication.app.loginBaseModel = null;
	// CustomApplication.app.isLogin = false;
	// CustomApplication.app.openId = "";
	// CustomApplication.app.weChatNickName = "";
	// CustomApplication.app.preferencesUtil.setValue(
	// AppConstants.LOGIN_PWD, "");
	//
	// CustomApplication.app.preferencesUtil.getValue(
	// AppConstants.LOGIN_PWD, "");
	//
	// Intent intent = new Intent();
	// intent.setClass(MySettingActivity.this, WXEntryActivity.class);
	// intent.putExtra("logout", false);
	// intent.putExtra("login", -1);
	// startActivity(intent);
	// // MainActivity.mainActivity.selectIndex = 0;
	// NoticeFragment.IsRefresh = true;
	// CustomApplication.app.finishAllActivity();
	// MainActivity.mainActivity.performClickBtn(0);
	//
	// } else {
	//
	// UiUtil.showToast(getApplicationContext(), "退出登录失败！");
	// }
	// return true;
	// }
	// }

	/**
	 * 拍照布局
	 */
	private void showPickPhotoPopupWindow() {
		// 一个自定义的布局，作为显示的内容
		View pview = LayoutInflater.from(this).inflate(
				R.layout.layout_pick_photo_select, null);

		TextView tv_pick_from_take = (TextView) pview
				.findViewById(R.id.tv_pick_from_take);
		TextView tv_pick_from_dicm = (TextView) pview
				.findViewById(R.id.tv_pick_from_dicm);
		TextView tv_show_cancle = (TextView) pview
				.findViewById(R.id.tv_show_cancle);

		final CustomDialog builder = new CustomDialog(this, R.style.my_dialog)
				.create(pview, true, 1f, 0.216f, 1);
		builder.show();

		tv_show_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});

		tv_pick_from_take.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				takePhoto();
				builder.dismiss();
			}
		});
		tv_pick_from_dicm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPic();
				builder.dismiss();
			}
		});
		tv_show_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});

		builder.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
			}
		});

	}

	private String camera_fileName;
	Boolean isBundleSaved = false;

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		bundle.putString("save_camera_fileName", camera_fileName);
		isBundleSaved = true; 
		super.onSaveInstanceState(bundle);
	}

	@Override
	public void onRestoreInstanceState(Bundle bundle) {
		// 屏幕旋转销毁之前的窗体之后，执行上个Activity的OnDestroy(),再执行新窗体的OnCreate()，然后执行这个函数
		if (bundle.containsKey("save_camera_fileName")) {
			camera_fileName = bundle.getString("save_camera_fileName");
		}

		super.onRestoreInstanceState(bundle);
	}

	Uri uritempFile;
	String imgName;

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) { 
		Log.d(TAG, "裁剪图片宽高:" + CustomApplication.app.displayMetrics.widthPixels);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, AppFinal.IMAGE_UNSPECIFIED);
		Log.d("Uri:", "" + uri);
		intent.putExtra("crop", "true");
		// intent.putExtra("noFaceDetection", true);
		// 宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪图片宽高
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 400);
		// intent.putExtra("scale", true);
		// intent.putExtra("return-data", true);
		// this.startActivityForResult(intent, AppFinal.RESULT_CODE_PHOTO_CUT);

		/**
		 * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
		 * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
		 */
		// intent.putExtra("return-data", true);

		// uritempFile为Uri类变量，实例化uritempFile
		imgName = System.currentTimeMillis() + ".jpg";  
		uritempFile = Uri.parse("file://" + "/"
				+ AppFinal.CACHE_DIR_UPLOADING_IMG + "/" + imgName);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

		startActivityForResult(intent, AppFinal.RESULT_CODE_PHOTO_CUT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((resultCode == RESULT_OK)) {
			switch (requestCode) {
			case AppFinal.RESULT_CODE_CAMERA: // 拍照后进行裁剪
				Uri uri = Uri.fromFile(new File(this.camera_fileName));
				this.startPhotoZoom(uri);
				break;
			case AppFinal.RESULT_CODE_PHOTO_PICKED: // 从本地选择图片后
				if (data != null) {
					String filePath = AppFinal.getPath(this, data.getData());
					Uri uri1 = Uri.fromFile(new File(filePath));
					this.startPhotoZoom(uri1);
					// this.startPhotoZoom(data.getData());
				}

				break;
			case AppFinal.RESULT_CODE_PHOTO_CUT: // 裁剪后

				// 将Uri图片转换为Bitmap
				Bitmap bitmap = null;
				try {
					bitmap = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(uritempFile));
					// TODO，将裁剪的bitmap显示在imageview控件上
					Drawable dr = new BitmapDrawable(bitmap);
					// SendUserPhoto(imgName, dr);
					// test
					bitmap = UiUtil.getCroppedRoundBitmap(bitmap, 40);// 设置圆形图片
					img_photo.setImageBitmap(bitmap);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;

			// Bundle extras = data.getExtras();
			// if (extras != null) {
			// Bitmap photo = extras.getParcelable("data");
			// String imgName = System.currentTimeMillis() + ".jpg";
			// String imgPath = AppFinal.CACHE_DIR_UPLOADING_IMG + "/"
			// + imgName;
			// ImageHelper.write(photo, imgPath);
			// Drawable dr = new BitmapDrawable(photo);
			// SendUserPhoto(imgName, dr);
			// }
			// break;
			}
		}
	}

	/**
	 * 拍照的方法
	 */
	public void takePhoto() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File file = new File(AppFinal.CACHE_DIR_IMAGE, "nc_"
				+ System.currentTimeMillis() + ".jpg");

		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		camera_fileName = file.getAbsolutePath();
		startActivityForResult(cameraIntent, AppFinal.RESULT_CODE_CAMERA);
	}

	/**
	 * 选择本地图片
	 */
	public void selectPic() {
		Intent localIntent = new Intent("android.intent.action.GET_CONTENT");
		localIntent.setType(AppFinal.IMAGE_UNSPECIFIED);
		startActivityForResult(Intent.createChooser(localIntent, "选择图片"),
				AppFinal.RESULT_CODE_PHOTO_PICKED);
	}

	// 向服务器提交用户头像
	// public void SendUserPhoto(String imgPath, final Drawable dr) {
	// // 向URL提交的参数用map来存放
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put("imgType", "");
	//
	// HashMap<String, String> mapFile = new HashMap<String, String>();
	// mapFile.put("file2", imgPath);
	//
	// // JSON的解析类
	//
	// UpLoadPhotoParser jsonParser = new UpLoadPhotoParser();
	// RequestVo reqVo = new RequestVo();
	// reqVo.jsonParser = jsonParser;
	// reqVo.requestFileMap = mapFile;
	// reqVo.fullUrl = AppConstants.URL_POST_PHOTO;
	// reqVo.useFullUrl = true;
	// reqVo.showDlg = true;
	// // UploadPhotoModel
	// // 请求数据
	// super.getDataByPost(reqVo,
	// // 收到数据并做JSON解析后的处理函数
	// new DataCallback<UploadPhotoModel>() {
	// @Override
	// public void processData(UploadPhotoModel vo,
	// boolean paramBoolean) {
	//
	// if (vo == null) {
	// UiUtil.showToast(context, "头像上传失败！");
	// return;
	// }
	// if ("success".equals(vo.getStatus())) {
	// UiUtil.showToast(context, "头像上传成功！");
	//
	// String newImgUrl = vo.getData();
	// Log.d(TAG, "newImgUrl:" + newImgUrl);
	// CustomApplication.app.loginBaseModel.getAnnMember()
	// .setHeadimgurl("" + newImgUrl);
	//
	// img_photo.setTag(AppConstants.BASE_URL + newImgUrl);
	// async(img_photo);// 设置用户头像
	// // 更新用户聊天头像 by liyunlong 20150925 start
	// UserInfo userInfo = new UserInfo(
	// CustomApplication.app.loginBaseModel
	// .getAccount(),
	// CustomApplication.app.loginBaseModel
	// .getName(), Uri
	// .parse((String) img_photo.getTag()));
	// RongIM.getInstance().refreshUserInfoCache(userInfo);
	// // 更新用户聊天头像 by liyunlong 20150925 end
	// } else {
	// UiUtil.showToast(context, "头像上传失败！");
	// Log.d(TAG, "头像上传失败！");
	// }
	// }
	// });
	// }

	// 异步加载头像并设置成圆形图片
	private void async(final ImageView imageView) {

		ImageLoader.getInstance().asyncLoadBitmap((String) imageView.getTag(),
				100, new ImageLoader.ImageCallback() {
					@Override
					public void imageLoaded(Bitmap bmp, String url) {
						if (bmp != null) {
							// photo = toRoundCorner(photo,30);//设置成圆角图片
							bmp = UiUtil.getCroppedRoundBitmap(bmp, 40);// 设置圆形图片
							imageView.setImageBitmap(bmp);

						}
					}
				});
	}

}
