package com.util.svgandroid;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Region;
import android.util.Log;

import com.util.svgandroid.HwSVGDrawer.HwPoint;
import com.util.tool.PathView;

public class HwAnim {
	private static final int AffectDistanceInPx = 50;
	protected int mAffectDistance;
	protected Canvas mAnimCanvas = null;
	protected List<HwPoint> mAnimHistoryPoints = new ArrayList<HwPoint>();
	protected int mHwAnimPartIndex = 0;
	protected boolean mHwAnimRunning = false;
	protected OnAnimListener mListener = null;
	protected PathMeasure mMeasure = null;
	protected ValueAnimator mValueAnim = null;
	protected PathView mView;
	public int timeGap;

	public HwAnim(PathView paramHwView, double paramDouble) {
		this.mView = paramHwView;
		this.mAffectDistance = ((int) (50.0D * paramDouble));
		this.mAnimCanvas = new Canvas(mView.mHwBmp);
		this.mAnimCanvas.scale(mView.mRatio, mView.mRatio);
	}

	private void setAnim() {
		if (!this.mHwAnimRunning) {
			return;
		}
		this.mAnimHistoryPoints.clear();
		if (this.mMeasure == null) {
			this.mMeasure = new PathMeasure();
		}
		this.mMeasure.setPath(
				(mView.mPartDirection.get(mHwAnimPartIndex)).path, false);
		float[] arrayOfFloat = new float[2];
		arrayOfFloat[0] = 0.0F;
		arrayOfFloat[1] = this.mMeasure.getLength();
		this.mValueAnim = ValueAnimator.ofFloat(arrayOfFloat);
		this.mValueAnim
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					protected float[] animPos = new float[2];

					public void onAnimationUpdate(
							ValueAnimator paramAnonymousValueAnimator) {
						float f = ((Float) paramAnonymousValueAnimator
								.getAnimatedValue()).floatValue();
						mMeasure.getPosTan(f, this.animPos, null);
						mAnimHistoryPoints.add(new HwPoint(this.animPos[0],
								this.animPos[1]));
						mAnimCanvas.save();
						preDraw(mAnimCanvas);
						mView.invalidate();
						Log.d("onAnimationUpdate", "mView.invalidate()");
					}
				});
		this.mValueAnim.addListener(new Animator.AnimatorListener() {
			public void onAnimationCancel(Animator paramAnonymousAnimator) {
			}

			public void onAnimationEnd(Animator paramAnonymousAnimator) {
				HwAnim localHwAnim = HwAnim.this;
				localHwAnim.mHwAnimPartIndex = (1 + localHwAnim.mHwAnimPartIndex);
				if (mHwAnimPartIndex < mView.mPartPolygon.size()) {
					setAnim();
					mView.postDelayed(new Runnable() {
						public void run() {
							mValueAnim.start();
						}
					}, timeGap);
				}
//				do {
//					// return;
//					mHwAnimRunning = false;
//					mView.invalidate();
//				} while (mListener == null);
				
				mListener.onEnd();
				Log.d("HwAnim", "onAnimationEnd");

			}

			public void onAnimationRepeat(Animator paramAnonymousAnimator) {
			}

			public void onAnimationStart(Animator paramAnonymousAnimator) {
			}
		});
		long l = (long) (this.mMeasure.getLength() / this.mAffectDistance * this.timeGap);
		this.mValueAnim.setDuration(l);
	}

	public void drawAnim(Canvas paramCanvas) {

		Log.d("drawAnim", "mHwAnimPartIndex:" + mHwAnimPartIndex);
		if (this.mHwAnimPartIndex == this.mView.mPartPolygon.size()) {
			paramCanvas.drawBitmap(this.mView.mFgCharBmp, 0.0F, 0.0F, null);
			Log.d("", "paramCanvas.drawBitmap(mFgCharBmp");
		}
		while ((!this.mHwAnimRunning)
				|| (this.mHwAnimPartIndex > this.mView.mPartPolygon.size())) {
			Log.e("drawAnim", "return");
			return;
		}
		paramCanvas.drawBitmap(this.mView.mHwBmp, 0.0F, 0.0F, null);
		Log.d("drawAnim", "paramCanvas.drawBitmap(mHwBmp");
	}

	protected void preDraw(Canvas paramCanvas) {
		int count = mView.mPartPolygon.size();
		Log.e("preDraw", "mHwAnimPartIndex:" + mHwAnimPartIndex);
		Log.e("preDraw", "mPartPolygon.size():" + count);

		if ((this.mHwAnimRunning)
				&& (this.mHwAnimPartIndex < this.mView.mPartPolygon.size())) {
			Path localPath = new Path();
			for (int i = 0; i < this.mAnimHistoryPoints.size(); i++) {
				HwPoint localHwPoint = (HwPoint) this.mAnimHistoryPoints.get(i);
				localPath.addCircle(localHwPoint.x, localHwPoint.y,
						this.mAffectDistance, Path.Direction.CW);// CW
																	// 创建顺时针方向的矩形路径
			}
			paramCanvas.clipPath((Path) this.mView.mPartPolygon
					.get(this.mHwAnimPartIndex));
			paramCanvas.clipPath(localPath, Region.Op.INTERSECT);
			for (int j = 0; j < this.mHwAnimPartIndex; j++) {
				paramCanvas.clipPath((Path) this.mView.mPartPolygon.get(j),
						Region.Op.UNION);
			}
			paramCanvas.drawBitmap(this.mView.mFgCharBmp, 0.0F, 0.0F, null);
			paramCanvas.restore();
			Log.d("HwAnim", "preDraw()");
		}
	}

	public void setAnimListener(OnAnimListener paramOnAnimListener) {
		this.mListener = paramOnAnimListener;
	}

	public void startHwAnim() {
		this.mAnimHistoryPoints.clear();
		this.mHwAnimPartIndex = 0;
		this.mHwAnimRunning = true;
		this.mView.mHwBmp.eraseColor(0);
		setAnim();
		this.mValueAnim.start();
	}

	public void stopHwAnim() {
		this.mHwAnimPartIndex = 0;
		this.mHwAnimRunning = false;
		this.mAnimHistoryPoints.clear();
		if (this.mValueAnim != null) {
			this.mValueAnim.cancel();
		}
	}

	public static abstract interface OnAnimListener {
		public abstract void onEnd();
	}
}
