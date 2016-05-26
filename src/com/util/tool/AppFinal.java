package com.util.tool;

import java.io.File;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * 
 * @author YH
 */
public class AppFinal {

    public final static int FAILED = 1;
    public final static int SUCCESS = 1;
    public final static int NET_FAILED = 2;
    public final static int TIME_OUT = 3;
    /**
     * 提交联系方式成功结果码
     */
    public static int POST_CONTACT_SUCCESS = 1;
    /** 本地缓存目录 */
    public static String CACHE_DIR;

    /** 待上传图片缓存目录 */
    public static String CACHE_DIR_UPLOADING_IMG;

    /** 图片缓存目录 */
    public static String CACHE_DIR_IMAGE;

    /** 用于标识请求照相功能的activity结果码 */
    public static final int RESULT_CODE_CAMERA = 1;

    /** 用来标识请求相册gallery的activity结果码 */
    public static final int RESULT_CODE_PHOTO_PICKED = 2;
    /** 用来标识图片剪辑的结果码 */
    public static final int RESULT_CODE_PHOTO_CUT = 3;
    /** 图片类型 */
    public static String IMAGE_UNSPECIFIED = "image/*";
    /** 回复图片的匹配路径 */
    public static String HUIFU = "common/back.gif";

    static {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            CACHE_DIR = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/notice";
        } else {
            CACHE_DIR = Environment.getRootDirectory().getAbsolutePath()
                    + "/notice";
        }

        CACHE_DIR_UPLOADING_IMG = CACHE_DIR + "/uploading_img";
        CACHE_DIR_IMAGE = CACHE_DIR + "/pic";

        File file = new File(CACHE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }

        File uploadImgFile = new File(CACHE_DIR_UPLOADING_IMG);
        if (!uploadImgFile.exists()) {
            uploadImgFile.mkdirs();
        }

        File imgFile = new File(CACHE_DIR_IMAGE);
        if (!imgFile.exists()) {
            imgFile.mkdirs();
        }
    }
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
            String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

}
