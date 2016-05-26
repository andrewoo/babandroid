package com.util.tool;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;

/**
 * 本地图片加载器,采用的是异步解析本地图片，单例模式利用getInstance()获取NativeImageLoader实例
 * 调用loadNativeImage()方法加载本地图片，此类可作为一个加载本地图片的工具类
 */
public class NativeImageLoader {
    private LruCache<String, Bitmap> mMemoryCache;
    private ExecutorService mImageThreadPool = Executors.newFixedThreadPool(10);
    private static NativeImageLoader instance = new NativeImageLoader();
    
    private String cacheDir;
    private int mwidth = 500, mheight = 500;

    /**
     * 通过此方法来获取NativeImageLoader的实例
     *
     * @return
     */
    public static NativeImageLoader getInstance() {
        return instance;
    }
    
    private NativeImageLoader() {
        //获取应用程序的最大内存
    	int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //用最大内存的1/4来存储图片
        final int cacheSize = maxMemory / 4;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            //获取每张图片的大小
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
        cacheDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download/images";
    }
    
    /**
     * setAvatarCache的重载函数
     * @param userName
     * @param length
     * @param callBack
     */
    public void loadImageCache(String imgUrl, int width, int height) {
    	Bitmap bitmap = null;
        if (imgUrl != null && imgUrl.length() != 0) {
			// 获取url中图片的文件名与后缀
			String imageName = imgUrl
					.substring(imgUrl.lastIndexOf("/") + 1);
			if (imageName != null && imageName.length() != 0) {
				// 图片在手机本地的存放路径
				File file = new File(cacheDir, imageName);// 保存文件
				// 可以在这里通过文件名来判断，是否本地有此图片
				if (file.exists() && !file.isDirectory()) {
					
					//先获取内存中的Bitmap
			        bitmap = getBitmapFromMemCache(imgUrl);

			        //若该Bitmap不在内存缓存中，则启用线程去加载本地的图片，并将Bitmap加入到mMemoryCache中
			        if (bitmap == null) {
						//先获取图片的缩略图
			        	bitmap = decodeThumbBitmapForFile(file.getAbsolutePath(), mwidth, mheight);
			        	//将图片加入到内存缓存
						addBitmapToMemoryCache(imgUrl, bitmap);
			        }
				}else{
					bitmap = BitmapLoader.getBitmapFromFile(file.getAbsolutePath(), mwidth, mheight);
					//将图片加入到内存缓存
					addBitmapToMemoryCache(imgUrl, bitmap);
				}
			}
        }
    }

    /**
     * 往内存缓存中添加Bitmap
     *
     * @param key
     * @param bitmap
     */
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && bitmap != null) {
            mMemoryCache.put(key, bitmap);
        }
    }
    
    /**
     * 根据key来获取内存中的图片
     *
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemCache(String key) {
        if (key == null) {
            return null;
        } else {
            return mMemoryCache.get(key);
        }
    }

    public void updateBitmapFromCache(String key, Bitmap bitmap) {
        if (null != bitmap) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public void releaseCache() {
        mMemoryCache.evictAll();
    }

    /**
     * 根据View(主要是ImageView)的宽和高来获取图片的缩略图
     *
     * @param path
     * @param viewWidth
     * @param viewHeight
     * @return
     */
    private Bitmap decodeThumbBitmapForFile(String path, int viewWidth, int viewHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置为true,表示解析Bitmap对象，该对象不占内存
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        //设置缩放比例
        options.inSampleSize = calculateInSampleSize(options, viewWidth, viewHeight);

        //设置为false,解析Bitmap对象加入到内存中
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 计算压缩比例值
     *
     * @param options   解析图片的配置信息
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 保存图片原宽高值
        int height = options.outHeight;
        int width = options.outWidth;

        // 初始化压缩比例为1
        int inSampleSize = 1;

        // 当图片宽高值任何一个大于所需压缩图片宽高值时,进入循环计算系统
        if (height > reqHeight || width > reqWidth) {

            int halfHeight = height / 2;
            int halfWidth = width / 2;

            // 压缩比例值每次循环两倍增加,
            // 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * 加载本地图片的回调接口
     *
     * @author xiaanming
     */
    public interface NativeImageCallBack {
        /**
         * 当子线程加载完了本地的图片，将Bitmap和图片路径回调在此方法中
         *
         * @param bitmap
         * @param path
         */
        public void onImageLoader(Bitmap bitmap, String path);
    }

}
