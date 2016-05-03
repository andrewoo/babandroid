package com.util.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.hw.chineseLearn.R;

/**
 * @author naiyu(http://snailws.com)
 * @version 1.0
 */
public class TasksCompletedView extends View {

	// 画实心圆的画笔
	private Paint mCirclePaint;
	// 画圆环的画笔
	private Paint mRingPaint;
//	// 画字体的画笔
//	private Paint mTextPaint;
	// 圆形颜色
	private int mCircleColor;
	// 圆环颜色
	private int mRingColor;
	// 半径
	private float mRadius;
	// 圆环半径
	private float mRingRadius;
	// 圆环宽度
	private float mStrokeWidth;
	// 圆心x坐标
	private int mXCenter;
	// 圆心y坐标
	private int mYCenter;
//	// 字的长度
//	private float mTxtWidth;
//	// 字的高度
//	private float mTxtHeight;
	// 总进度
	private int mTotalProgress = 1;
	// 当前进度
	private float mProgress;

	public TasksCompletedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获取自定义的属性
		initAttrs(context, attrs);
		initVariable();
	}
	
	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.TasksCompletedView, 0, 0);
		mRadius = typeArray.getDimension(R.styleable.TasksCompletedView_radius, 80);
		mStrokeWidth = typeArray.getDimension(R.styleable.TasksCompletedView_strokeWidth, 10);
		mCircleColor = typeArray.getColor(R.styleable.TasksCompletedView_circleColor, 0xFFFFFFFF);
		mRingColor = typeArray.getColor(R.styleable.TasksCompletedView_ringColor, 0xFFFFFFFF);
		
		mRingRadius = mRadius + mStrokeWidth ;
	}

	private void initVariable() {
		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(mCircleColor);//mRingColor
		mCirclePaint.setStyle(Paint.Style.STROKE);
		mCirclePaint.setStrokeWidth(mStrokeWidth);
		
		mRingPaint = new Paint();
		mRingPaint.setAntiAlias(true);
		mRingPaint.setColor(mCircleColor);
		mRingPaint.setStyle(Paint.Style.FILL);
//		mRingPaint.setStrokeWidth(mStrokeWidth);
		
//		mTextPaint = new Paint();
//		mTextPaint.setAntiAlias(true);
//		mTextPaint.setStyle(Paint.Style.FILL);
//		mTextPaint.setARGB(255, 255, 255, 255);
//		mTextPaint.setTextSize(mRadius / 2);
//		
//		FontMetrics fm = mTextPaint.getFontMetrics();
//		mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {

		mXCenter = getWidth() / 2;
		mYCenter = getHeight() / 2;
		
//		canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);
		canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth , mCirclePaint);
		if (mProgress > 0 ) {
			RectF oval = new RectF();
			oval.left = (mXCenter - mRingRadius);
			oval.top = (mYCenter - mRingRadius);
			oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
			oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
//			canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth , mCirclePaint);
			canvas.drawArc(oval, -90, ((float)mProgress / mTotalProgress) * 360, true, mRingPaint); //动态画
//			String txt = mProgress + "%";
//			mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
//			canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);
		}
	}
	
	public void setProgress(float progress) {
		mProgress = progress;
//		invalidate();
		postInvalidate();
	}

}
