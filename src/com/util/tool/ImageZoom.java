package com.util.tool;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageZoom {
	public static void main(String[] args) {
		ImageZoom iz = new ImageZoom();
		iz.zoomImage(50, 50, 480, 854);
		System.out.println("width: " + iz.getWidth() + ", height: "
				+ iz.getHeight());

	}
	// 变量声明
	private boolean isZoom; // 是否缩放
	private int srcWidth; // 原始宽
	private int srcHeight; // 原始高
	private int maxWidth; // 限制宽
	private int maxHeight; // 限制高
	private int newWidth; // 新宽

	private int newHeight; // 新高

	private void calc() {
		if (this.isZoom) { // 如果高宽正常，开始计算
			if ((this.srcWidth / this.srcHeight) >= (this.maxWidth
					/ this.maxHeight)) {
				// 比较高宽比例，确定以宽或者是高为基准进行计算。
				if (this.srcWidth > this.maxWidth) { // 以宽为基准开始计算，
					// 当宽度大于限定宽度，开始缩放
					this.newWidth = this.maxWidth;
					this.newHeight = (this.srcHeight * this.maxWidth)
							/ this.srcWidth;
				} else { // 当宽度小于限定宽度，直接返回原始数值。
					this.newWidth = this.srcWidth;
					this.newHeight = this.srcHeight;
				}
			} else {
				if (this.srcHeight > this.maxHeight) { // 以高为基准，进行计算
					// 当高度大于限定高度，开始缩放。
					this.newHeight = this.maxHeight;
					this.newWidth = (this.srcWidth * this.maxHeight)
							/ this.srcHeight;
				} else {
					// 当高度小于限定高度，直接返回原始数值。
					this.newWidth = this.srcWidth;
					this.newHeight = this.srcHeight;
				}
			}
		} else { // 不正常，返回0
			this.newWidth = 0;
			this.newHeight = 0;
		}
	}

	public int getHeight() {// 返回处理后的高度
		return this.newHeight;
	}

	public int getWidth() { // 返回处理后的宽度
		return this.newWidth;
	}

	public void zoomImage(int srcWidth, int srcHeight, int maxWidth,
			int maxHeight) {
		this.srcWidth = srcWidth; // 获得原始宽度
		this.srcHeight = srcHeight; // 获得原始高度
		this.maxWidth = maxWidth; // 获得限定宽度
		this.maxHeight = maxHeight; // 获得限定高度
		if ((this.srcWidth > 0) && (this.srcWidth > 0)) { // 检查图片高度是否正常
			this.isZoom = true; // 高宽正常，执行缩放处理
		} else {
			this.isZoom = false; // 不正常，返回0
		}// if
		this.calc(); // 执行缩放算法
	}
	/**
     * 图片的缩放方法
     *
     * @param src    ：源图片资源
     * @param scaleX ：横向缩放比例
     * @param scaleY ：纵向缩放比例
     */
    public static Bitmap scale(Bitmap src, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(),
                matrix, true);
    }
	
}
