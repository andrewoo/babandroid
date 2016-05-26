package com.util.svgandroid;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Region;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.util.svgandroid.HwSVGDrawer.HwPoint;
import com.util.tool.PathView;

public class HwWriting implements View.OnTouchListener {

	private static final int AffectDistanceInPx = 80;
	private static final int PartEndGapPx = 40;
	private static final int TouchGapLengthInPx = 60;
	private int mAffectDistance;
	public boolean mAllowWriting = false;
	private float mCurDrawnLength = 0.0F;
	private HwPoint mCurDrawnPoint = new HwPoint();
	private List<HwPoint> mHistoryDrawnPoints = new ArrayList();
	private OnWritingListener mListener;
	private int mPartEndGap;
	private PathMeasure mPathMeasure;
	private float[] mPos = new float[2];
	private int mTouGapLength;
	private PathView mView;
	protected Canvas mWritingCanvas = null;
	private int mWritingPartIndex;

	public HwWriting(PathView pathView, float paramDouble) {
		this.mView = pathView;
		this.mAffectDistance = ((int) (80.0D * paramDouble));
		this.mTouGapLength = ((int) (60.0D * paramDouble));
		this.mPartEndGap = ((int) (40.0D * paramDouble));
		this.mPathMeasure = new PathMeasure();
		this.mWritingPartIndex = 0;
		this.mWritingCanvas = new Canvas(this.mView.mHwBmp);
		this.mWritingCanvas.scale(paramDouble, paramDouble);

	}

	protected void beginHandwriting() {
	    if(mView.mHwBmp == null) {
            return;
        }
		this.mWritingPartIndex = 0;
		this.mView.mHwBmp.eraseColor(0);
		beginPartHandwriting();
	}

	protected void beginPartHandwriting() {
		Log.d("mWritingPartIndex", "mWritingPartIndex:"+this.mWritingPartIndex);
		this.mHistoryDrawnPoints.clear();
		this.mCurDrawnLength = 0.0F;
		this.mPathMeasure.setPath(this.mView.directionPaths.get(this.mWritingPartIndex), false);
		this.mPathMeasure.getPosTan(0.0F, this.mPos, null);
		this.mCurDrawnPoint.set(this.mPos[0], this.mPos[1]);
		this.mWritingCanvas.save();
		preDraw(this.mWritingCanvas);
		this.mView.invalidate();
	}

	protected double calDistanceBetweenPoint(float paramFloat1,
			float paramFloat2, float paramFloat3, float paramFloat4) {
		return Math.sqrt(Math.pow(paramFloat3 - paramFloat1, 2.0D)
				+ Math.pow(paramFloat4 - paramFloat2, 2.0D));
	}

	public void disableWriting() {
		this.mAllowWriting = false;
	}

	public void drawWriting(Canvas paramCanvas) {

        if(mWritingPartIndex == mView.partPaths.size()) {
            return;
        }
        if((mAllowWriting) && (mWritingPartIndex < mView.partPaths.size())) {
    		paramCanvas.drawBitmap(this.mView.mHwBmp, 0.0F, 0.0F, null);
        }
    
	}

	public void enableWriting() {
		this.mAllowWriting = true;
		beginHandwriting();
	}

	protected void judgeDistance(float paramFloat1, float paramFloat2) {
		double d1 = calDistanceBetweenPoint(paramFloat1, paramFloat2,
				this.mCurDrawnPoint.x*this.mView.mRatio, this.mCurDrawnPoint.y*this.mView.mRatio);
		Log.d("judgeDistance", "judgeDistance:"+d1);
		Log.d("mTouGapLength", "mTouGapLength:"+this.mTouGapLength);

		if (d1 < this.mTouGapLength) {
			do {
				this.mHistoryDrawnPoints.add(new HwPoint(this.mCurDrawnPoint.x, this.mCurDrawnPoint.y));
				double d2 = this.mAffectDistance;
				if (d1 < this.mAffectDistance) {
					d2 = d1;
				}
				this.mCurDrawnLength = ((float) (d2 + this.mCurDrawnLength));
				d1 -= d2;
				if (this.mCurDrawnLength > this.mPathMeasure.getLength()) {
					this.mCurDrawnLength = this.mPathMeasure.getLength();
					d1 = 0.0D;
				}
				this.mPathMeasure.getPosTan(this.mCurDrawnLength, this.mPos,null);
				this.mCurDrawnPoint.x = this.mPos[0];
				this.mCurDrawnPoint.y = this.mPos[1];
			} while (d1 > 0.0D);
			this.mWritingCanvas.save();
			preDraw(this.mWritingCanvas);
			this.mView.invalidate();
        }
	}

	protected boolean judgeInRect(float paramFloat1, float paramFloat2, List<HwPoint> paramList) {
		return true;
	}

	public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
		if (!this.mAllowWriting) {
			return false;
		}
		switch(paramMotionEvent.getAction()){
			// 按下
			case MotionEvent.ACTION_DOWN:
	
				judgeDistance(paramMotionEvent.getX(), paramMotionEvent.getY());

				break;
			case MotionEvent.ACTION_MOVE:
				judgeDistance(paramMotionEvent.getX(), paramMotionEvent.getY());
				break;

		  }
		
		if ((this.mCurDrawnLength == this.mPathMeasure.getLength())
				|| (this.mPathMeasure.getLength() - this.mCurDrawnLength < this.mPartEndGap)) {
			Log.d("mWritingPartIndex", "mWritingPartIndex:"+this.mWritingPartIndex);
			Log.d(" this.mView.directionPaths.size()", " this.mView.directionPaths.size():"+ this.mView.directionPaths.size());

			this.mWritingPartIndex = (1 + this.mWritingPartIndex);
			if (this.mWritingPartIndex >= this.mView.directionPaths.size()) {
				if (this.mListener != null) {
					this.mListener.onEnd();
					this.mAllowWriting=false;
				}
				this.mView.invalidate();
			} else {
				beginPartHandwriting();
			}
		}
		return true;
	}

	protected void preDraw(Canvas paramCanvas) {
		if ((this.mAllowWriting)
				&& (this.mWritingPartIndex < this.mView.directionPaths.size())) {
			this.mView.mHwBmp.eraseColor(0);
			this.mView.mPaint.setStyle(Paint.Style.STROKE);
			this.mView.mPaint.setStrokeWidth(5.0f);
            mView.getClass();
            mView.mPaint.setColor(Color.RED);
        	paramCanvas.drawPath(this.mView.directionPaths.get(this.mWritingPartIndex),this.mView.mPaint);
			paramCanvas.drawPath(this.mView.arrowPaths.get(this.mWritingPartIndex),this.mView.mPaint);
			
			Path localPath = new Path();
			for (int i = 0; i < this.mHistoryDrawnPoints.size(); i++) {
				HwPoint localHwPoint = (HwPoint) this.mHistoryDrawnPoints.get(i);
				localPath.addCircle(localHwPoint.x, localHwPoint.y, (float)mAffectDistance, Path.Direction.CW);
			}
			Path part1=this.mView.partPaths.get(this.mWritingPartIndex);
			//当前写的这个笔画的路径区域 结合 当前已经绘制的部分的交集；
			paramCanvas.clipPath(part1);
			paramCanvas.clipPath(localPath, Region.Op.INTERSECT);
			
			Paint dpaint = new Paint(1);;
			dpaint.setStyle(Paint.Style.FILL_AND_STROKE);
			for(int i = 0; i < mWritingPartIndex; i = i + 1) {
				//已经画完的所有笔画的合集
				paramCanvas.clipPath((Path)mView.partPaths.get(i), Region.Op.UNION);
            }
			paramCanvas.drawPath(this.mView.charPath, dpaint);				
			paramCanvas.restore();

		}
	}

	public void setWritingListener(OnWritingListener paramOnWritingListener) {
		this.mListener = paramOnWritingListener;
	}
	   public void reset() {
	        mWritingPartIndex = 0x0;
	        mHistoryDrawnPoints.clear();
	        mCurDrawnLength = 0.0f;
	        mAllowWriting = false;
	    }
	    

	public static abstract interface OnWritingListener {
		public abstract void onEnd();
	}
}
