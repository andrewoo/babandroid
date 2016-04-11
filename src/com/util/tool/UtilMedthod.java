package com.util.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hw.chineseLearn.base.CustomApplication;

public class UtilMedthod {

	public static boolean isMobileNo(String mobiles) {
		Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{4,8}$"); // ^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	// 计算出该TextView中文字的长度(像素)
	public static float getTextViewSingleWordLength(TextView textView,
			String text) {
		TextPaint paint = textView.getPaint();
		// 得到使用该paint写上text的时候,像素为多少
		float textLength = paint.measureText(text);
		return textLength;
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	public static Bitmap decodeFile(File file) {
		int mwidth = 500;
		int mhight = 500;

		Bitmap bitmap = null;
		// Decode image size
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		try {
			FileInputStream fis = new FileInputStream(file);
			BitmapFactory.decodeStream(fis, null, opt);
			fis.close();

			int scale = 1;
			while (opt.outWidth / scale / 2 >= mwidth
					&& opt.outHeight / scale / 2 >= mhight)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options new_opt = new BitmapFactory.Options();
			new_opt.inSampleSize = scale;
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis, null, new_opt);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 根据原图和变长绘制圆形图片
	 * 
	 * @param colorStr
	 * @return
	 */
	public static Bitmap createCircleImage(String colorStr) {
		Bitmap source = Bitmap.createBitmap(100, 40, Bitmap.Config.ARGB_8888);
		int color = Color.parseColor(colorStr);
		source.eraseColor(color);

		int min = 10;
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
		/**
		 * 产生一个同样大小的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 首先绘制圆形
		 */
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		/**
		 * 使用SRC_IN
		 */
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); // PorterDuff.Mode.SRC_IN
		/**
		 * 绘制图片
		 */
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	public static Drawable setBackgroundRounded(Context context, int w, int h,
			int corner, int color) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		double dH = (metrics.heightPixels / 100) * 1.5;
		int iHeight = (int) dH;

		iHeight = corner;

		Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmp);

		Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		paint.setAntiAlias(true);
		paint.setColor(color);
		// paint.setColor(mContext.getResources().getColor(R.color.blue));
		RectF rec = new RectF(0, 0, w, h);
		c.drawRoundRect(rec, iHeight, iHeight, paint);

		Drawable d = new BitmapDrawable(context.getResources(), bmp);
		return d;
	}

	/**
	 * @param context
	 * @param w
	 * @param h
	 * @param corner
	 * @param color
	 * @return
	 */
	public static Drawable translateImageColor(Context context, Bitmap bmp,
			int corner, int color) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		double dH = (metrics.heightPixels / 100) * 1.5;
		int iHeight = (int) dH;
		iHeight = corner;

		Bitmap bm = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
				Bitmap.Config.ARGB_8888);

		int width = bmp.getWidth();
		int height = bmp.getHeight();

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				int pixel = bmp.getPixel(col, row);// ARGB
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

				// bm.setPixel(col, row, Color.argb(alpha, gray, gray, gray));
				bm.setPixel(col, row, color);
			}
		}
		Canvas c = new Canvas(bm);
		Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		paint.setAntiAlias(true);
		// paint.setColor(color);
		RectF rec = new RectF(0, 0, width, height);
		c.drawRoundRect(rec, iHeight, iHeight, paint);

		Drawable d = new BitmapDrawable(context.getResources(), bm);
		return d;
	}

	/**
	 * 改变图片中有像素部分的颜色
	 * 
	 * @param 改变颜色的图片    想要改变的颜色 参考如下
	 *            
	 * @return 改变颜色后的图片
	 */
	public static Bitmap getAlphaBitmap(Bitmap bitmap,int color) {
//		public static final int BLACK       = 0xFF000000;
//	    public static final int DKGRAY      = 0xFF444444;
//	    public static final int GRAY        = 0xFF888888;
//	    public static final int LTGRAY      = 0xFFCCCCCC;
//	    public static final int WHITE       = 0xFFFFFFFF;
//	    public static final int RED         = 0xFFFF0000;
//	    public static final int GREEN       = 0xFF00FF00;
//	    public static final int BLUE        = 0xFF0000FF;
//	    public static final int YELLOW      = 0xFFFFFF00;
//	    public static final int CYAN        = 0xFF00FFFF;
//	    public static final int MAGENTA     = 0xFFFF00FF;
		
		Bitmap mAlphaBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas mCanvas = new Canvas(mAlphaBitmap);
		Paint mPaint = new Paint();
		mPaint.setColor(color);// 填入想要的颜色
		// 从原位图中提取只包含alpha的位图
		Bitmap alphaBitmap = bitmap.extractAlpha();
		// 在画布上（mAlphaBitmap）绘制alpha位图
		mCanvas.drawBitmap(alphaBitmap, 0, 0, mPaint);
		return mAlphaBitmap;
	}

}
