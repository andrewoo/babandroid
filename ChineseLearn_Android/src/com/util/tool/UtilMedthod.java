package com.util.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
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
	 * 改变图片中有像素部分的颜色
	 * 
	 * @param color
	 *            想要改变的颜色
	 * 
	 * @return mAlphaBitmap 改变颜色后的图片
	 */
	public static Bitmap translateImageColor(Bitmap bitmap, int color) {
		int colorRandom;
		int DKGRAY = 0xFF444444;
		int GRAY = 0xFF888888;
		int LTGRAY = 0xFFCCCCCC;
		int RED = 0xFFFF0000;
		int GREEN = 0xFF00FF00;
		int BLUE = 0xFF0000FF;
		int YELLOW = 0xFFFFFF00;
		int CYAN = 0xFF00FFFF;
		int MAGENTA = 0xFFFF00FF;

		if (color == 0) {
			int[] colors = { DKGRAY, GRAY, LTGRAY, RED, GREEN, BLUE, YELLOW,
					CYAN, MAGENTA };
			colorRandom = colors[(int) (Math.random() * colors.length)]; // 随机取数组某个位置的元素
		} else {
			colorRandom = color;
		}
		Bitmap mAlphaBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas mCanvas = new Canvas(mAlphaBitmap);
		Paint mPaint = new Paint();
		mPaint.setColor(colorRandom);// 填入想要的颜色
		// 从原位图中提取只包含alpha的位图
		Bitmap alphaBitmap = bitmap.extractAlpha();
		// 在画布上（mAlphaBitmap）绘制alpha位图
		mCanvas.drawBitmap(alphaBitmap, 0, 0, mPaint);
		return mAlphaBitmap;
	}

	/**
	 * @param bitmap
	 * @param color
	 * @return
	 */
	public static Bitmap getImageHasAlpha(Bitmap bitmap) {

		// 从原位图中提取只包含alpha的位图
		Bitmap alphaBitmap = bitmap.extractAlpha();
		int w = alphaBitmap.getWidth();
		int h = alphaBitmap.getHeight();
		Bitmap mAlphaBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas mCanvas = new Canvas(mAlphaBitmap);
		Paint mPaint = new Paint();
		mPaint.setColor(0xFFFF0000);
		// 在画布上（mAlphaBitmap）绘制alpha位图
		mCanvas.drawBitmap(alphaBitmap, 0, 0, mPaint);
		return mAlphaBitmap;
	}
	
	
	/**
	 * 裁剪出有像素的部分（矩形）
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getCropBitmap(Bitmap bitmap){
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		List<Point> list=new ArrayList<Point>();
		//一列一列的找 第一个为最左边
		first(bitmap, width, height, list);
		//第二个为最上边
		sencond(bitmap, width, height, list);
		
		//第三个最右边  x固定 y遍历
		third(bitmap, width, height, list);
		//第四个最下边 y固定 x遍历
		forth(bitmap, width, height, list);
		Matrix matrix = new Matrix();
		
		Bitmap createBitmap = Bitmap.createBitmap(bitmap, list.get(0).x, list.get(1).y, list.get(2).x-list.get(0).x, list.get(3).y-list.get(1).y,matrix,true);//最后一个参数抗锯齿好像不管用
		
		return createBitmap;
		
	}
	
	private static void forth(Bitmap bitmap, int width, int height, List<Point> list) {
		for (int i =height-1; i >=0; i--) {
			for (int j = width-1; j >=0 ; j--) {
				if(Color.TRANSPARENT!=bitmap.getPixel(j, i)){
					Point point=new Point();
					point.x=j; point.y=i;
					list.add(point);
					return ;
				}
			}
		}
	}

	private static void third(Bitmap bitmap, int width, int height, List<Point> list) {
		for (int i = width-1; i >=0; i--) {
			for (int j = 0; j < height-1; j++) {
				if(Color.TRANSPARENT!=bitmap.getPixel(i, j)){
					Point point=new Point();
					point.x=i; point.y=j;
					list.add(point);
					return ;
				}
			}
		}
	}

	private static void sencond(Bitmap bitmap, int width, int height, List<Point> list) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if(Color.TRANSPARENT!=bitmap.getPixel(j, i)){
					Point point=new Point();
					point.x=j; point.y=i;
					list.add(point);
					return ;
				}
			}
		}
	}

	private static void first(Bitmap bitmap, int width, int height, List<Point> list) {
		for (int i = 0; i < width-1; i++) {
			for (int j = 0; j < height-1; j++) {
				if(Color.TRANSPARENT!=bitmap.getPixel(i, j)){
					Point point=new Point();
					point.x=i; point.y=j;
					list.add(point);
					return ;
				}
			}
		}
	}

}
