package com.util.tool;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.util.svgandroid.ArrowCal;
import com.util.svgandroid.HwAnim;
import com.util.svgandroid.HwSVGDrawer;
import com.util.svgandroid.HwSVGDrawer.HwPoint;
import com.util.svgandroid.HwWriting;
//import com.util.svgandroid.HwAnim;

/**
 * PathView is a View that animates paths.
 */
public class PathView extends View {
	/**
	 * Logging tag.
	 */
	public static final String LOG_TAG = "PathView";
	/**
	 * The paint for the path.
	 */
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	public Paint mPaint = new Paint(1);
	/**
	 * Utils to catch the paths from the svg.
	 */
	private final SvgUtils svgUtils = new SvgUtils(paint);
	/**
	 * All the paths provided to the view. Both from Path and Svg.
	 */
	private List<SvgUtils.SvgPath> paths = new ArrayList<SvgUtils.SvgPath>();

	public List<Path> mPartPolygon = new ArrayList<Path>();
	/**
	 * This is a lock before the view is redrawn or resided it must be
	 * synchronized with this object.
	 */
	private final Object mSvgLock = new Object();
	/**
	 * Thread for working with the object above.
	 */
	private Thread mLoader;

	/**
	 * The svg image from the raw directory.
	 */
	private int svgResourceId;
	/**
	 * The progress of the drawing.
	 */
	private float progress = 0f;

	/**
	 * If the used colors are from the svg or from the set color.
	 */
	private boolean naturalColors;
	/**
	 * If the view is filled with its natural colors after path drawing.
	 */
	private boolean fillAfter;
	/**
	 * The view will be filled and showed as default without any animation.
	 */
	private boolean fill;
	/**
	 * The solid color used for filling svg when fill is true
	 */
	private int fillColor;
	/**
	 * The width of the view.
	 */
	private int width;
	/**
	 * The height of the view.
	 */
	private int height;
	/**
	 * Will be used as a temporary surface in each onDraw call for more control
	 * over content are drawing.
	 */
	
	protected HwAnim.OnAnimListener mAnimListener;
	protected HwWriting.OnWritingListener mWritingListener;
	protected final int CHAR_BG_COLOR = -3355444;
	protected final int CHAR_FG_COLOR = -16777216;
	protected final int DIRECTION_COLOR = -65536;

	/**
	 * Default constructor.
	 * 
	 * @param context
	 *            The Context of the application.
	 */
	public PathView(Context context) {
		this(context, null);
	}

	/**
	 * Default constructor.
	 * 
	 * @param context
	 *            The Context of the application.
	 * @param attrs
	 *            attributes provided from the resources.
	 */
	public PathView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}



	//有用的。。。。。。。。。。。。。。。。。
	//整个字的path
	public void setCharPath(final Path path) {
		int width = getLayoutParams().width;
		int height = getLayoutParams().height;
		if(width != height) {
			throw new IllegalArgumentException();
	     }
		mRatio = ((float)width / 800.0f);
		charPath = path;
		
		
		mBgCharBmp = Bitmap.createBitmap(getLayoutParams().width,
				getLayoutParams().height, Bitmap.Config.ARGB_8888);
		mHwBmp = Bitmap.createBitmap(getLayoutParams().width,
				getLayoutParams().height, Bitmap.Config.ARGB_8888);
		
		mTempCanvas = new Canvas(mBgCharBmp);
		mTempCanvas.save();
		mTempCanvas.scale(mRatio, mRatio);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.LTGRAY);
		mTempCanvas.drawPath(charPath, paint);
		invalidate();
		if (this.mWriting == null) {
		      this.mWriting = new HwWriting(this, this.mRatio);
		}else{
			this.mWriting.reset();
		}
	    setOnTouchListener(this.mWriting);
	    this.mWriting.setWritingListener(this.mWritingListener);
		if (this.mAnim == null) {
	      this.mAnim = new HwAnim(this, this.mRatio);
	      this.mAnim.timeGap = this.timeGap;
	    }else{
	    	this.mAnim.reset();
	    }
	    this.mAnim.setAnimListener(this.mAnimListener);
		
	}
	//每个部首的区域的路径
	public void setPartPaths(List<Path> paramList){
		partPaths=paramList;


	}
	//每个笔画走势
	public void setDirectionPaths(List<Path> paramList){
		directionPaths=paramList;
		//获取箭头的路径集合
        for(int i = 0; i < directionPaths.size(); i = i + 1) {
        	Path dpath= directionPaths.get(i);
            Path arrowPath =  ArrowCal.cal(getContext(), dpath, mRatio);
            arrowPaths.add(arrowPath);
        }
        
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
        if(mBgVisible) {
			canvas.drawBitmap(mBgCharBmp, 0, 0, null);
        }
        if(mAnim != null) {
            mAnim.drawAnim(canvas);
        }
        if(mWriting != null) {
            mWriting.drawWriting(canvas);
        }
        boolean isInAction = false;
        if((mAnim != null) && (mAnim.mHwAnimRunning)) {
            isInAction = true;
        }
        if((mWriting != null) && (mWriting.mAllowWriting)) {
            isInAction = true;
        }
        if(!isInAction) {
            drawFg(canvas);
        }   
	}
 
	 protected void drawFg(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        canvas.scale(mRatio, mRatio);
        canvas.drawPath(charPath, mPaint);
	 }


	public boolean mBgVisible = true;
	public HwWriting mWriting = null;
	public int timeGap = 1000;
	public float mRatio = 1.0f;
	public Bitmap mBgCharBmp;
	public Bitmap mHwBmp = null;

	public Path charPath;
	public List<Path> partPaths = new ArrayList<Path>();
	public List<Path> directionPaths = new ArrayList<Path>();
	public List<Path> arrowPaths = new ArrayList<Path>();
	protected PathMeasure mMeasure = null;
	protected HwAnim mAnim = null;
	private Canvas mTempCanvas;
	
	
    public void setBgHanziVisibility(boolean visible) {
        mBgVisible = visible;
        invalidate();
    }
	public void setTimeGap(int paramInt) {
		this.timeGap = paramInt;
		if (this.mAnim != null) {
			this.mAnim.timeGap = paramInt;
		}
	}

	public void setWritingListener(HwWriting.OnWritingListener paramOnWritingListener) {
		this.mWritingListener = paramOnWritingListener;
		if (this.mWriting != null) {
			this.mWriting.setWritingListener(this.mWritingListener);
		}
	}
    
    public void setAnimListener(HwAnim.OnAnimListener l) {
        mAnimListener = l;
        if(mAnim != null) {
            mAnim.setAnimListener(mAnimListener);
        }
    }
//
    public void startHwAnim() {
		stopHwAnim();
        disableHandwriting();
        if(mAnim != null) {
            mAnim.startHwAnim();
        }   
    }
//
	public void stopHwAnim() {
		 if(mAnim != null) {
	            mAnim.stopHwAnim();
	        }
	}

	public void disableHandwriting() {
		if (this.mWriting != null) {
			this.mWriting.disableWriting();
		}
	}

	public void enableHandwriting() {
		stopHwAnim();
		if (this.mWriting == null) {
			HwWriting localHwWriting = new HwWriting(this, this.mRatio);
			this.mWriting = localHwWriting;
			setOnTouchListener(this.mWriting);
			this.mWriting.setWritingListener(this.mWritingListener);
		}
		if (this.mWriting != null) {
			this.mWriting.enableWriting();
		}
	}
    public void destroy() {
        if(mHwBmp != null) {
            mHwBmp.recycle();
            mHwBmp = null;
        }
        if(mAnim != null) {
            mAnim.reset();
        }
        if(mWriting != null) {
            mWriting.reset();
        }
    }
	public static class PartDirectionPath {
		public Path arrowPath;
		public Path path;
	}
}
