/**
 *  ClassName: ImageLoader.java
 *  created on 2012-3-10
 *  Copyrights 2011-2012 qjyong All rights reserved.
 *  site: http://blog.csdn.net/qjyong
 *  email: qjyong@gmail.com
 */
package com.util.tool;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.hw.chineseLearn.R;

/**
 */
public class ImageLoader {
    public interface ImageCallback {
        /** 图片下载完成的回调方法 */
        public void imageLoaded(Bitmap bmp, String url);
    }

    private class LoadBitmapRunnable implements Runnable {
        private String url;
        private Handler handler;
        private int width;

        public LoadBitmapRunnable(String url, int width, Handler handler) {
            this.url = url;
            this.handler = handler;
            this.width = width;
        }

        @Override
        public void run() {
            Message msg = this.handler.obtainMessage(HttpStatus.SC_OK);
            Bitmap bmp = null;
            if (this.url == null) {
//                Log.e(TAG, "this.url==null");
                return;
            }

            try {
                bmp = HttpHelper.downloadBitmap(this.url);

                if (null != bmp) {

                    if (this.width > 0) {
                        bmp = ImageHelper.zoom(bmp, this.width);
                    }
                    // // 放入内存缓存
                    msg.obj = bmp;
                    this.handler.sendMessage(msg);

                    // 写磁盘
                    String destFileName = MD5.digest(url);
                    File dest = new File(AppFinal.CACHE_DIR_IMAGE, destFileName);
                    if (!dest.exists()) {
                        ImageHelper.write(bmp, dest);
                    }
                }
            } catch (IOException e) {
                msg.what = HttpStatus.SC_INTERNAL_SERVER_ERROR;
                e.printStackTrace();
            }
        }
    }

    public static final String TAG = "ImageLoader";

    private final static ImageLoader imageLoader = new ImageLoader();

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(6,
            30, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    /**
     * 此列表主要针对用户图像的缓存,保证每一次启动时都清空用户图像缓存 此列表里的数据表示本次启动已清空过的图像缓存
     */
    Map<String, String> lstRemovedBitmap = new HashMap<String, String>();

    public static Bitmap getBitmapFromDisk(String uurl) {
        Bitmap bmp = null;
        String destFileName = MD5.digest(uurl);
        {
            File dest = new File(AppFinal.CACHE_DIR_IMAGE, destFileName);
            if (dest.exists()) {
                try {
                    bmp = ImageHelper.loadFromFile(dest);
//                    Log.d(TAG, "in SD-->" + uurl);
                    System.out.println("getBitmapFromDisk" + uurl);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    // TODO: handle exception
                    System.gc();
                    bmp = null;
                }
            }
        }
        return bmp;
    }

    public static ImageLoader getInstance() {
        return imageLoader;
    }

    private ImageLoader() {
    }

    /**
     * 异步加载指定url处的图片设置到imageView中<br/>
     * 
     * @param url
     * @param imageView
     */
    public void asyncLoadBitmap(final String uurl, final ImageCallback callback) {

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (HttpStatus.SC_OK == msg.what) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    callback.imageLoaded(bitmap, uurl);
                } else {
                    callback.imageLoaded(null, uurl);
                }
            }
        };
        threadPool.execute(new LoadBitmapRunnable(uurl, -1, handler));
    }

    /**
     * 异步加载指定url处的图片设置到imageView中<br/>
     * 注意：以原宽度和目标宽度的缩放比对原图片进行了缩放。
     * 
     * @param url
     * @param width
     * @param imageView
     */
    public void asyncLoadBitmap(final String uurl, final int width,
            final ImageCallback callback) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (HttpStatus.SC_OK == msg.what) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    callback.imageLoaded(bitmap, uurl);
                }
            }
        };
        threadPool.execute(new LoadBitmapRunnable(uurl, width, handler));
    }

    /**
     * @author
     * */
    public void asyncLoadBitmap2(final String uurl, final int width,
            final Context context, final ImageCallback callback) {

        Bitmap bitmaptemp = BitmapFactory.decodeResource(
                context.getResources(), R.drawable.img_nav_one_bg_n);
        callback.imageLoaded(bitmaptemp, uurl);
    }

    /**
     * 下载图片到文件夹pic里
     * 
     * @param uurl
     */
    public void downloadBitmap(final String uurl) {

        String destFileName = MD5.digest(uurl);
        File dest = new File(AppFinal.CACHE_DIR_IMAGE, destFileName);
        if (!dest.exists()) {
            try {
                HttpHelper.download(uurl, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载图片到文件夹pic里
     * 
     * @param uurl
     */
    public void downloadBitmap2(final String uurl) {

        String destFileName = MD5.digest(uurl);
        File dest = new File(AppFinal.CACHE_DIR_UPLOADING_IMG, destFileName);
        if (!dest.exists()) {
            try {
                HttpHelper.download(uurl, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
