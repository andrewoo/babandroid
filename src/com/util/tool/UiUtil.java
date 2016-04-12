package com.util.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * @author YH
 */
public class UiUtil {

	private static String TAG = "UiUtil";

	/**
	 * 显示toast信息
	 * 
	 * @param context
	 * @param message
	 */
	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @throws 抖动并提示
	 */
	// public static void shake(Context context, View view) {
	//
	// // 动画---闪动效果
	// Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
	// view.startAnimation(shake);
	// }

	/**
	 * 回调接口
	 * 
	 * @author YF
	 * @date 2014-7-23
	 */
	public interface OnSetTitleListener {

		/**
		 * 点击事件
		 * 
		 * @param view
		 *            ： 返回的控件
		 */
		public void onClick(View view);

	}

	public static boolean isValidEmail(String paramString) {

		String regex = "[a-zA-Z0-9_\\.]{1,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
		if (paramString.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkEmail(String emailString) {
		String regex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(emailString);
		return matcher.matches();
	}

	public static boolean checkTelephone(String telString) {
		String regex = "[0-9]{11}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(telString);
		return matcher.matches();
	}

	/**
	 * 检验电话是否合法
	 * 
	 * @param Str
	 * @param checkType
	 * @return
	 */
	public static boolean checkPhone(String Str) {
		Pattern p = null;
		p = Pattern
				.compile("(^[0-9]{3,4}[0-9]{7,8}$)|(^[0-9]{3,4}-[0-9]{7,8}$)|^(13[0-9]|14[5|7]|15[0-9]|18[0-9])\\d{8}$");
		Matcher m = p.matcher(Str);
		if (!m.matches()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断是不是正确的电话号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	public static boolean isValidMobiNumber(String paramString) {
		String regex = "^1\\d{10}$";
		if (paramString.matches(regex)) {
			return true;
		}
		return false;
	}

	public static boolean isValidName(String paramString) {

		String regex = "[a-zA-Z0-9_]{1,}";
		if (paramString.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取裁剪后的圆形图片
	 * 
	 * @param radius
	 *            半径
	 */
	public static Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
		Bitmap scaledSrcBmp;
		int diameter = radius * 2;

		// 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		int squareWidth = 0, squareHeight = 0;
		int x = 0, y = 0;
		Bitmap squareBitmap;
		if (bmpHeight > bmpWidth) {// 高大于宽
			squareWidth = squareHeight = bmpWidth;
			x = 0;
			y = (bmpHeight - bmpWidth) / 2;
			// 截取正方形图片
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		} else if (bmpHeight < bmpWidth) {// 宽大于高
			squareWidth = squareHeight = bmpHeight;
			x = (bmpWidth - bmpHeight) / 2;
			y = 0;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		} else {
			squareBitmap = bmp;
		}

		if (squareBitmap.getWidth() != diameter
				|| squareBitmap.getHeight() != diameter) {
			scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
					diameter, true);

		} else {
			scaledSrcBmp = squareBitmap;
		}
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
				scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
				paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
		// bitmap回收(recycle导致在布局文件XML看不到效果)
		// bmp.recycle();
		// squareBitmap.recycle();
		// scaledSrcBmp.recycle();
		bmp = null;
		squareBitmap = null;
		scaledSrcBmp = null;
		return output;
	}

	/**
	 * 设置圆角图片
	 * 
	 * @param bitmap
	 * @param pixels
	 *            30
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels * bitmap.getWidth() / 160;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 为图片添加相框
	 * 
	 * @param frame
	 * @param src
	 * @param x
	 * @param y
	 * @return
	 */
	public static Bitmap montageBitmap(Bitmap frame, Bitmap src, int x, int y) {
		int w = src.getWidth();
		int h = src.getHeight();
		Bitmap sizeFrame = Bitmap.createScaledBitmap(frame, w, h, true);

		Bitmap newBM = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(newBM);
		canvas.drawBitmap(src, x, y, null);
		canvas.drawBitmap(sizeFrame, 0, 0, null);
		return newBM;
	}

	/**
	 * 叠加边框图片有用部分
	 * 
	 * @param bmp
	 * @return
	 */
	private static Bitmap alphaLayer(Bitmap bmp, Context context, int drawable) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		// 边框图片
		Bitmap overlay = BitmapFactory.decodeResource(context.getResources(),
				drawable);
		int w = overlay.getWidth();
		int h = overlay.getHeight();
		float scaleX = width * 1F / w;
		float scaleY = height * 1F / h;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);

		Bitmap overlayCopy = Bitmap.createBitmap(overlay, 0, 0, w, h, matrix,
				true);

		int pixColor = 0;
		int layColor = 0;
		int newColor = 0;

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixA = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;
		int newA = 0;

		int layR = 0;
		int layG = 0;
		int layB = 0;
		int layA = 0;

		float alpha = 0.3F;
		float alphaR = 0F;
		float alphaG = 0F;
		float alphaB = 0F;
		for (int i = 0; i < width; i++) {
			for (int k = 0; k < height; k++) {
				pixColor = bmp.getPixel(i, k);
				layColor = overlayCopy.getPixel(i, k);

				// 获取原图片的RGBA值
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				pixA = Color.alpha(pixColor);

				// 获取边框图片的RGBA值
				layR = Color.red(layColor);
				layG = Color.green(layColor);
				layB = Color.blue(layColor);
				layA = Color.alpha(layColor);

				// 颜色与纯黑色相近的点
				if (layR < 20 && layG < 20 && layB < 20) {
					alpha = 1F;
				} else {
					alpha = 0.3F;
				}

				alphaR = alpha;
				alphaG = alpha;
				alphaB = alpha;

				// 两种颜色叠加
				newR = (int) (pixR * alphaR + layR * (1 - alphaR));
				newG = (int) (pixG * alphaG + layG * (1 - alphaG));
				newB = (int) (pixB * alphaB + layB * (1 - alphaB));
				layA = (int) (pixA * alpha + layA * (1 - alpha));

				// 值在0~255之间
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				newA = Math.min(255, Math.max(0, layA));

				newColor = Color.argb(newA, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);
			}
		}

		return bitmap;
	}

	/**
	 * 
	 * 异步加载头像并设置图片样式
	 * 
	 * @param imageView
	 * @param setCorner设置成圆角
	 * @param pixels
	 *            30
	 * @param setCircle设置成圆形
	 * @param radius
	 *            半径
	 * @param isMontage是否加盖蒙板
	 * @param drawable
	 *            资源图片
	 */
	public static void async(final Context context, final ImageView imageView,
			final boolean setCorner, final int pixels, final boolean setCircle,
			final int radius, final boolean isMontage, final int drawable) {

		ImageLoader.getInstance().asyncLoadBitmap((String) imageView.getTag(),
				100, new ImageLoader.ImageCallback() {
					@Override
					public void imageLoaded(Bitmap bmp, String url) {
						if (bmp != null) {

							if (setCorner) {
								bmp = toRoundCorner(bmp, pixels);// 设置成圆角图片
							}
							if (setCircle) {
								bmp = UiUtil.getCroppedRoundBitmap(bmp, radius);// 设置圆形图片
							}
							if (isMontage) {
								// Bitmap frame = BitmapFactory.decodeResource(
								// context.getResources(),
								// R.drawable.dicm_bg);
								// bmp = montageBitmap(frame, bmp, 200, 200);
								// alphaLayer(bmp, context, drawable);
							}

							imageView.setImageBitmap(bmp);

						}
					}
				});
	}

	/**
	 * 把微信头像写入到指定的文件夹
	 * 
	 * @param headImgUrl
	 */
	public static void downLoadWxHeadImg(final String headImgUrl) {

		ImageLoader.getInstance().asyncLoadBitmap(headImgUrl, 200,
				new ImageLoader.ImageCallback() {
					@Override
					public void imageLoaded(Bitmap bmp, String url) {

						if (bmp != null) {

							String imgName = MD5.digest(headImgUrl) + ".jpg";

							String imgPath = AppFinal.CACHE_DIR_UPLOADING_IMG
									+ "/" + imgName;
							ImageHelper.write(bmp, imgPath);
							Log.d("Uiutil-downLoadWxHeadImg", "下载微信头像成功！");
						}

					}
				});
	}

	/**
	 * 实现文本复制功能
	 * 
	 * @param content
	 */
	@SuppressWarnings("deprecation")
	public static void copy(String content, Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
	}

	/**
	 * 实现粘贴功能
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String paste(Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		return cmb.getText().toString().trim();
	}

	/**
	 * 检查文字是否为空
	 * 
	 * @param text
	 * @return
	 */
	public static boolean IsValueEmpty(String text) {
		if (text != null && text.trim().length() > 0
				&& !text.toLowerCase().equals("null")) {
			return false;
		}
		return true;
	}

	public static String numToString(int num) {
		String numString = "";
		switch (num) {
		case 0:
			numString = "零";
			break;
		case 1:
			numString = "一";
			break;
		case 2:
			numString = "二";
			break;
		case 3:
			numString = "三";
			break;
		case 4:
			numString = "四";
			break;
		case 5:
			numString = "五";
			break;
		case 6:
			numString = "六";
			break;
		case 7:
			numString = "七";
			break;
		case 8:
			numString = "八";
			break;
		case 9:
			numString = "九";
			break;
		case 10:
			numString = "十";
			break;
		default:
			break;
		}
		return numString;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 清楚掉所有特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static String StringFilter(String str) {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		if (str != null) {
			str = str.replace(" ", "");
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(str);
			return m.replaceAll("").trim().toLowerCase();
		} else {
			return "";
		}
	}

	/**
	 * 传入一个字符串，返回一个字符串数组
	 * 
	 * @param str
	 * @return
	 */
	public static String[] getListFormString(String str) {
		// String strEnd = str.substring(str.length() - 1, str.length());
		if (str == null) {
			return null;
		}
		if (str.endsWith(";")) {
			str = str.substring(0, str.length() - 1);// 去掉最后一个字符
			Log.d(TAG, "str:" + str);
		}

		return str.split(";");
	}

	/**
	 * @param bitmap
	 * @return
	 */
	public static Bitmap transparentImage(Bitmap bitmap) {

		Bitmap bm = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				bitmap.getConfig());
		int width = bm.getWidth();
		int height = bm.getHeight();
		// int m_BmpPixel[] = new int[width * height];
		// bitmap.getPixels(m_BmpPixel, 0, width, 0, 0, width, height);

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				int pixel = bitmap.getPixel(col, row);// ARGB
				if (pixel == 0) {
					continue;
				}
				int red = Color.red(pixel); // same as (pixel >> 16) &0xff
				int green = Color.green(pixel); // same as (pixel >> 8) &0xff
				int blue = Color.blue(pixel); // same as (pixel & 0xff)
				int alpha = Color.alpha(pixel); // same as (pixel >>> 24)
				// int gray = (int) (0.3 * red + 0.59 * green + 0.11 * blue);
				int gray = (Math.max(blue, Math.max(red, green)) + Math.min(
						blue, Math.min(red, green))) / 2;
				// int gray = (red + green + blue) / 3;

				bm.setPixel(col, row, Color.argb(alpha, gray, gray, gray));
			}
		}

		return bm;

	}
}
