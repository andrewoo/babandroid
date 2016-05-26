/**
 *  ClassName: BitmapHelper.java
 *  created on 2012-1-2
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package com.util.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter.ViewBinder;

/**
 * z 图片处理工具类
 * 
 * @author qjyong
 */
public class ImageHelper {

	/**
	 * 图像对象绑定器
	 * 
	 * @author qjyong
	 */
	public static class MyViewBinder implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			if ((view instanceof ImageView) && (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bm = (Bitmap) data;
				iv.setImageBitmap(bm);

				return true;
			}
			return false;
		}
	}

	/**
	 * 根据资源ID获取对应的Bitmap实例
	 * 
	 * @param ctx
	 * @param id
	 * @return
	 */
	public static Bitmap getBitmap(Context ctx, int id) {
		return BitmapFactory.decodeResource(ctx.getResources(), id);
	}

	public static Bitmap getHttpBitmap(String url) {
		URL myurl = null;
		InputStream in = null;
		Bitmap bitmap = null;
		try {
			myurl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
			conn.setConnectTimeout(0);
			conn.setDoInput(true);
			conn.connect();
			in = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	/**
	 * 根据图片名称，从assets中加载对应的Bitmap对象
	 * 
	 * @param ctx
	 * @param fileName
	 * @return
	 */
	public static Bitmap getImageFromAssetsFile(Context ctx, String fileName) {
		Bitmap image = null;
		AssetManager am = ctx.getResources().getAssets();
		InputStream is = null;
		try {
			is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return image;
	}

	public static Bitmap loadFromFile(File name) throws IOException {
		Bitmap bmp = null;
		InputStream is = null;
		try {
			is = new FileInputStream(name);
			bmp = BitmapFactory.decodeStream(is);
			System.out.println("ImageHelper================loadFromFile");
		} catch (IOException e) {
			System.gc();
			bmp = null;
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			System.gc();
			bmp = null;
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					System.gc();
					bmp = null;
				}
			}
		}
		return bmp;
	}

	public static Bitmap loadFromFile(String name) throws IOException {
		Bitmap bmp = null;
		InputStream is = null;
		try {
			is = new FileInputStream(name);
			bmp = BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			throw e;
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
		return bmp;
	}

	public static File getFilePath(String filePath, String fileName) {
		File file = null;
		makeRootDirectory(filePath);
		try {
			file = new File(filePath + fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 把png或jpg(jpeg)格式图片按指定名称写入指定目录下
	 * 
	 * @param bmp
	 * @param path
	 * @param fileName
	 */
	public static void write(Bitmap bmp, File file) {
		FileOutputStream fos = null;

		try {
			File parent = file.getParentFile();

			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}

			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			System.out.println("fos===>" + fos.toString());
			bmp.compress(Bitmap.CompressFormat.PNG, 75, fos);

			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 把png或jpg(jpeg)格式图片按指定名称写入指定目录下
	 * 
	 * @param bmp
	 * @param path
	 * @param fileName
	 */
	public static void write(Bitmap bmp, String fileName) {
		String extension = IOHelper.getExtension(fileName);
		FileOutputStream fos = null;

		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);

			if ("png".equalsIgnoreCase(extension)) {
				bmp.compress(Bitmap.CompressFormat.PNG, 75, fos);
			} else if ("jpg".equalsIgnoreCase(extension)
					|| "jpeg".equalsIgnoreCase(extension)) {
				bmp.compress(Bitmap.CompressFormat.JPEG, 75, fos);
			}

			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 把png或jpg(jpeg)格式图片按指定名称写入指定目录下
	 * 
	 * @param bmp
	 * @param path
	 * @param fileName
	 */
	public static void write(Bitmap bmp, String path, String fileName) {
		String extension = IOHelper.getExtension(fileName);
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(new File(path, fileName));

			if ("png".equalsIgnoreCase(extension)) {
				bmp.compress(Bitmap.CompressFormat.PNG, 75, fos);
			} else if ("jpg".equalsIgnoreCase(extension)
					|| "jpeg".equalsIgnoreCase(extension)) {
				bmp.compress(Bitmap.CompressFormat.JPEG, 75, fos);
			}

			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 图片的缩放，以图片的原宽度和目标宽度作为缩放比
	 * 
	 * @param src
	 *            源图片
	 * @param destWidth
	 *            目标宽度
	 * @return
	 */
	public static Bitmap zoom(Bitmap src, int destWidth) {
		int width = src.getWidth();
		int height = src.getHeight();
		if (width == destWidth) {
			width = width - 1;
			height = height - 1;
		}
		// 计算缩放比例
		float scaleWidth = ((float) destWidth) / width;
		// float scaleHeight = ((float) destHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();

		matrix.postScale(scaleWidth, scaleWidth);

		// 得到新的图片
		Bitmap b = Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
		src.recycle();
		System.gc();
		return b;
	}

	public static Bitmap zoom(Bitmap src, int destWidth, int destHeight) {
		int width = src.getWidth();
		int height = src.getHeight();
		if ((width == destWidth) || (height == destHeight)) {
			width = width - 1;
			height = height - 1;
		}
		// 计算缩放比例
		float scaleWidth = ((float) destWidth) / width;
		// float scaleHeight = ((float) destHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();

		matrix.postScale(scaleWidth, scaleWidth);

		// 得到新的图片
		Bitmap b = Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
		src.recycle();
		System.gc();
		return b;
	}

}