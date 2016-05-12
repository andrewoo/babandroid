package com.util.svgandroid;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Region;
import android.util.Log;

import com.util.svgandroid.HwSVGDrawer.HwPoint;
import com.util.tool.PathView;

public class HwAnim {
	private static final int AffectDistanceInPx = 80;
	protected int mAffectDistance;
	protected Canvas mAnimCanvas = null;
	protected List<HwPoint> mAnimHistoryPoints = new ArrayList<HwPoint>();
	protected int mHwAnimPartIndex = 0;
	public boolean mHwAnimRunning = false;
	protected OnAnimListener mListener = null;
	protected PathMeasure mMeasure = null;
	protected ValueAnimator mValueAnim = null;
	protected PathView mView;
	public int timeGap;

	public HwAnim(PathView paramHwView, double paramDouble) {
		this.mView = paramHwView;
		this.mAffectDistance = ((int) (80.0D * paramDouble));
		this.mAnimCanvas = new Canvas(mView.mHwBmp);
		this.mAnimCanvas.scale(mView.mRatio, mView.mRatio);
	}
    public void reset() {
        mAnimHistoryPoints.clear();
        mHwAnimPartIndex = 0x0;
        mHwAnimRunning = false;
        if(mValueAnim != null) {
            mValueAnim.cancel();
        }
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
				(mView.directionPaths.get(mHwAnimPartIndex)), false);
		float[] arrayOfFloat = new float[2];
		arrayOfFloat[0] = 0.0F;
		arrayOfFloat[1] = this.mMeasure.getLength();
		mValueAnim = ValueAnimator.ofFloat(arrayOfFloat); 
		mValueAnim.setDuration(1500);        
		//值动画每走一次更新在mAnimHistoryPoints中添加最新的两个点作为线的连接点
		mValueAnim.addUpdateListener(new AnimatorUpdateListener() {
			protected float[] animPos = new float[2];
			public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator) {
				float f = ((Float) paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
				mMeasure.getPosTan(f, this.animPos, null);
				mAnimHistoryPoints.add(new HwPoint(this.animPos[0],this.animPos[1]));
				mAnimCanvas.save();
				preDraw(mAnimCanvas);
				mView.invalidate();
			}
		});	
		mValueAnim.addListener(new Animator.AnimatorListener() {
			public void onAnimationCancel(Animator paramAnonymousAnimator) {
			}
			public void onAnimationEnd(Animator paramAnonymousAnimator) {
				Log.d("onAnimationEnd", "onAnimationEnd");

//				mAnimPartIndex=mAnimPartIndex+1;
				Log.d("------------------", mHwAnimPartIndex+"");
				Log.d("partPaths.size()", mView.partPaths.size()+"");
				mHwAnimPartIndex=mHwAnimPartIndex+1;
				
				   if(mHwAnimPartIndex < mView.partPaths.size()) {
						//继续画下一个笔画
	                    mView.postDelayed(new Runnable() {
	                        public void run() {
	                        	setAnim();
	                        }
	                    }, (long)timeGap);
	                    return;
	                }
					//动画全部做完；操作
				    mHwAnimRunning = false;
	                mView.invalidate();
	                if(mListener != null) {
	                    mListener.onEnd();
	                }
			}
			public void onAnimationRepeat(Animator paramAnonymousAnimator) {
			}
			public void onAnimationStart(Animator paramAnonymousAnimator) {
			}
			
		});
		mValueAnim.start();   
	}

	public void drawAnim(Canvas paramCanvas) {
		 if(mHwAnimPartIndex == mView.partPaths.size()) {
	            return;
	        }
	        if((mHwAnimRunning) && (mHwAnimPartIndex < mView.partPaths.size())) {
	        	paramCanvas.drawBitmap(mView.mHwBmp, 0.0f, 0.0f, null);
	        }
	}
	
	protected void preDraw(Canvas paramCanvas) {
		int count = mView.partPaths.size();
		Log.e("preDraw", "mHwAnimPartIndex:" + mHwAnimPartIndex);
		Log.e("preDraw", "mPartPolygon.size():" + count);

		if ((this.mHwAnimRunning) && (this.mHwAnimPartIndex < this.mView.partPaths.size())) {
			Path localPath = new Path();
			for (int i = 0; i < this.mAnimHistoryPoints.size(); i++) {
				HwPoint localHwPoint = (HwPoint) this.mAnimHistoryPoints.get(i);
				localPath.addCircle(localHwPoint.x, localHwPoint.y, 
						this.mAffectDistance, Path.Direction.CW);
			}
			Path part1=this.mView.partPaths.get(mHwAnimPartIndex);
			//当前写的这个笔画的路径区域 结合 当前已经绘制的部分的交集；
			paramCanvas.clipPath(part1);
			paramCanvas.clipPath(localPath, Region.Op.INTERSECT);
	        for(int i = 0x0; i < mHwAnimPartIndex; i = i + 0x1) {
	        	paramCanvas.clipPath(mView.partPaths.get(i), Region.Op.UNION);
	        }
			
			Paint dpaint = new Paint(1);;
			dpaint.setStyle(Paint.Style.FILL_AND_STROKE);
			dpaint.setColor(Color.BLACK);
			paramCanvas.drawPath(mView.charPath, dpaint);				
	        paramCanvas.restore();
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
