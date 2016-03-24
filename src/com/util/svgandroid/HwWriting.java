package com.util.svgandroid;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;

import com.util.svgandroid.HwSVGDrawer.HwPoint;
import com.util.tool.PathView;

public class HwWriting

{
	private static final int AffectDistanceInPx = 50;
	private static final int PartEndGapPx = 40;
	private static final int TouchGapLengthInPx = 60;
	private int mAffectDistance;
	private boolean mAllowWriting = false;
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
		this.mAffectDistance = ((int) (50.0D * paramDouble));
		this.mTouGapLength = ((int) (60.0D * paramDouble));
		this.mPartEndGap = ((int) (40.0D * paramDouble));
		this.mPathMeasure = new PathMeasure();
		this.mWritingPartIndex = 0;
		this.mWritingCanvas = new Canvas(this.mView.mHwBmp);
	}

	protected void beginHandwriting() {
		this.mWritingPartIndex = 0;
		this.mView.mHwBmp.eraseColor(0);
		beginPartHandwriting();
	}

	protected void beginPartHandwriting() {
		this.mHistoryDrawnPoints.clear();
		this.mCurDrawnLength = 0.0F;
		this.mPathMeasure.setPath(
				((PathView.PartDirectionPath) this.mView.mPartDirection
						.get(this.mWritingPartIndex)).path, false);
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
		if (this.mWritingPartIndex == this.mView.mPartPolygon.size()) {
			paramCanvas.drawBitmap(this.mView.mFgCharBmp, 0.0F, 0.0F, null);
		}
		while ((!this.mAllowWriting)
				|| (this.mWritingPartIndex >= this.mView.mPartPolygon.size())) {
			return;
		}
		paramCanvas.drawBitmap(this.mView.mHwBmp, 0.0F, 0.0F, null);
	}

	public void enableWriting() {
		this.mAllowWriting = true;
		beginHandwriting();
	}

	protected void judgeDistance(float paramFloat1, float paramFloat2) {
		double d1 = calDistanceBetweenPoint(paramFloat1, paramFloat2,
				this.mCurDrawnPoint.x, this.mCurDrawnPoint.y);
		if ((d1 < this.mTouGapLength)
				&& (judgeInRect(paramFloat1, paramFloat2, null))) {
			do {
				this.mHistoryDrawnPoints.add(new HwPoint(
						this.mCurDrawnPoint.x, this.mCurDrawnPoint.y));
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
				this.mPathMeasure.getPosTan(this.mCurDrawnLength, this.mPos,
						null);
				this.mCurDrawnPoint.x = this.mPos[0];
				this.mCurDrawnPoint.y = this.mPos[1];
			} while (d1 > 0.0D);
			this.mWritingCanvas.save();
			preDraw(this.mWritingCanvas);
			this.mView.invalidate();
		}
	}

	protected boolean judgeInRect(float paramFloat1, float paramFloat2,
			List<HwPoint> paramList) {
		return true;
	}

	public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
		if (!this.mAllowWriting) {
			return false;
		}
		switch (paramMotionEvent.getAction()) {
		}
		for (;;) {
			// return true;
			// judgeDistance(paramMotionEvent.getX(), paramMotionEvent.getY());
			// continue;
			judgeDistance(paramMotionEvent.getX(), paramMotionEvent.getY());
			// continue;
			if ((this.mCurDrawnLength == this.mPathMeasure.getLength())
					|| (this.mPathMeasure.getLength() - this.mCurDrawnLength < this.mPartEndGap)) {
				this.mWritingPartIndex = (1 + this.mWritingPartIndex);
				if (this.mWritingPartIndex >= this.mView.mPartDirection.size()) {
					if (this.mListener != null) {
						this.mListener.onEnd();
					}
					this.mView.invalidate();
				} else {
					beginPartHandwriting();
				}
			}
		}
	}

	protected void preDraw(Canvas paramCanvas) {
		if ((this.mAllowWriting)
				&& (this.mWritingPartIndex < this.mView.mPartPolygon.size())) {
			this.mView.mHwBmp.eraseColor(0);
			this.mView.mPaint.setStyle(Paint.Style.STROKE);
			Paint localPaint = this.mView.mPaint;
			this.mView.getClass();
			localPaint.setColor(-65536);
			paramCanvas.drawPath(
					((PathView.PartDirectionPath) this.mView.mPartDirection
							.get(this.mWritingPartIndex)).path,
					this.mView.mPaint);
			paramCanvas.drawPath(
					((PathView.PartDirectionPath) this.mView.mPartDirection
							.get(this.mWritingPartIndex)).arrowPath,
					this.mView.mPaint);
			Path localPath = new Path();
			for (int i = 0; i < this.mHistoryDrawnPoints.size(); i++) {
				HwPoint localHwPoint = (HwPoint) this.mHistoryDrawnPoints
						.get(i);
				localPath.addCircle(localHwPoint.x, localHwPoint.y,
						this.mAffectDistance, Path.Direction.CW);
			}
			paramCanvas.clipPath((Path) this.mView.mPartPolygon
					.get(this.mWritingPartIndex));
			paramCanvas.clipPath(localPath, Region.Op.INTERSECT);
			for (int j = 0; j < this.mWritingPartIndex; j++) {
				paramCanvas.clipPath((Path) this.mView.mPartPolygon.get(j),
						Region.Op.UNION);
			}
			paramCanvas.drawBitmap(this.mView.mFgCharBmp, 0.0F, 0.0F, null);
			paramCanvas.restore();
		}
	}

	public void setWritingListener(OnWritingListener paramOnWritingListener) {
		this.mListener = paramOnWritingListener;
	}

	public static abstract interface OnWritingListener {
		public abstract void onEnd();
	}
}
