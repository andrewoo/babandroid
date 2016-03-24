package com.util.tool;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hw.chineseLearn.R;
import com.util.svgandroid.ArrowCal;
import com.util.svgandroid.HwAnim;
import com.util.svgandroid.HwSVGDrawer;
import com.util.svgandroid.HwWriting;

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
	public boolean mBgVisible = true;
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
	public Bitmap mBgCharBmp;
	/**
	 * Will be used as a temporary Canvas for mBgCharBmp for drawing content on
	 * it.
	 */

	private Canvas mTempCanvas;

	private Canvas mFgCharCanvas;

	public Bitmap mFgCharBmp;

	public Bitmap mHwBmp = null;

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
		this(context, attrs, 0);
	}

	/**
	 * Default constructor.
	 * 
	 * @param context
	 *            The Context of the application.
	 * @param attrs
	 *            attributes provided from the resources.
	 * @param defStyle
	 *            Default style.
	 */
	public PathView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		getFromAttributes(context, attrs);
	}

	/**
	 * Get all the fields from the attributes .
	 * 
	 * @param context
	 *            The Context of the application.
	 * @param attrs
	 *            attributes provided from the resources.
	 */
	private void getFromAttributes(Context context, AttributeSet attrs) {
		final TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.PathView);
		try {
			if (a != null) {
				paint.setColor(a.getColor(R.styleable.PathView_pathColor,
						0xff00ff00));
				paint.setStrokeWidth(a.getDimensionPixelSize(
						R.styleable.PathView_pathWidth, 8));
				svgResourceId = a.getResourceId(R.styleable.PathView_svg, 0);
				naturalColors = a.getBoolean(
						R.styleable.PathView_naturalColors, false);
				fill = a.getBoolean(R.styleable.PathView_fill, false);
				fillColor = a.getColor(R.styleable.PathView_fillColor,
						Color.argb(0, 0, 0, 0));
			}
		} finally {
			if (a != null) {
				a.recycle();
			}
			// to draw the svg in first show , if we set fill to true
			invalidate();
		}
	}

	public void setBitMapSize(int width, int height) {

	}

	/**
	 * Set paths to be drawn and animated.
	 * 
	 * @param paths
	 *            - Paths that can be drawn.
	 */
	public void setPaths(final List<Path> paths) {
		for (Path path : paths) {
			this.paths.add(new SvgUtils.SvgPath(path, paint));
		}
		synchronized (mSvgLock) {
			updatePathsPhaseLocked();
		}
	}

	/**
	 * Set path to be drawn and animated.
	 * 
	 * @param path
	 *            - Paths that can be drawn.
	 */
	public void setPath(final Path path) {
		paths.add(new SvgUtils.SvgPath(path, paint));
		mPartPolygon.add(path);
		synchronized (mSvgLock) {
			updatePathsPhaseLocked();
		}
	}

	/**
	 * Animate this property. It is the percentage of the path that is drawn. It
	 * must be [0,1].
	 * 
	 * @param percentage
	 *            float the percentage of the path.
	 */
	public void setPercentage(float percentage) {
		if (percentage < 0.0f || percentage > 1.0f) {
			// throw new IllegalArgumentException(
			// "setPercentage not between 0.0f and 1.0f");
			Log.e(LOG_TAG, "setPercentage not between 0.0f and 1.0f");
		}
		progress = percentage;
		synchronized (mSvgLock) {
			updatePathsPhaseLocked();
		}
		invalidate();
	}

	/**
	 * This refreshes the paths before draw and resize.
	 */
	private void updatePathsPhaseLocked() {
		final int count = paths.size();
		for (int i = 0; i < count; i++) {
			SvgUtils.SvgPath svgPath = paths.get(i);
			svgPath.path.reset();
			svgPath.measure.getSegment(0.0f, svgPath.length * progress,
					svgPath.path, true);
			// Required only for Android 4.4 and earlier
			svgPath.path.rLineTo(0.0f, 0.0f);
		}
	}

	/**
	 * The measure of the path, we can use it later to get segment of it.
	 */
	public PathMeasure measure;

	/**
	 * This refreshes the paths before draw and resize.
	 */
	private void updatePathsPhaseLocked1() {
		int count = mPartPolygon.size();
		for (int i = 0; i < count; i++) {
			Path path = mPartPolygon.get(i);
			path.reset();
			measure = new PathMeasure(path, false);
			measure.getSegment(0.0f, measure.getLength() * progress, path, true);
			// Required only for Android 4.4 and earlier
			path.rLineTo(0.0f, 0.0f);
		}
	}

	public void setAHanzi(String paramString, List<String> paramList1,
			List<String> paramList2) {

		stopHwAnim();
//		HwSVGParser localHwSVGParser = new HwSVGParser();
//		ArrayList localArrayList1 = new ArrayList();
//		localHwSVGParser.resetString(paramString);
//		for (;;) {
//			HwSVGDrawer.HwCmd localHwCmd1 = localHwSVGParser.nextCmd();
//			if (localHwCmd1 == null) {
//				break;
//			}
//			localArrayList1.add(localHwCmd1);
//		}

		if (mBgCharBmp != null) {
			mBgCharBmp.recycle();
			mBgCharBmp = null;
		}
		if (mFgCharBmp != null) {
			mFgCharBmp.recycle();
			mFgCharBmp = null;
		}
		if (mHwBmp != null) {
			mHwBmp.recycle();
			mHwBmp = null;
		}
//		for (;;) {
//			Iterator localIterator1 = localArrayList1.iterator();
//			while (localIterator1.hasNext()) {
//				Iterator localIterator4 = ((HwSVGDrawer.HwCmd) localIterator1
//						.next()).points.iterator();
//				while (localIterator4.hasNext()) {
//					HwSVGDrawer.HwPoint localHwPoint2 = (HwSVGDrawer.HwPoint) localIterator4
//							.next();
//					localHwPoint2.x = ((float) (localHwPoint2.x * this.mRatio));
//					localHwPoint2.y = ((float) (localHwPoint2.y * this.mRatio));
//				}
//			}
//			this.mHwBmp.eraseColor(0);
//			break;
//		}
		mBgCharBmp = Bitmap.createBitmap(getLayoutParams().width,
				getLayoutParams().height, Bitmap.Config.ARGB_8888);

		mFgCharBmp = Bitmap.createBitmap(getLayoutParams().width,
				getLayoutParams().height, Bitmap.Config.ARGB_8888);

		mHwBmp = Bitmap.createBitmap(getLayoutParams().width,
				getLayoutParams().height, Bitmap.Config.ARGB_8888);
		
		mTempCanvas = new Canvas(mBgCharBmp);
		mTempCanvas.save();
		mTempCanvas.scale(mRatio, mRatio);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(CHAR_BG_COLOR);
		final int count = mPartPolygon.size();
		for (int i = 0; i < count; i++) {
			Path path = mPartPolygon.get(i);
			mTempCanvas.drawPath(path, paint);
			Log.d(LOG_TAG, "mTempCanvas.drawPath");
		}

		mFgCharCanvas = new Canvas(mFgCharBmp);
		mFgCharCanvas.save();
		mFgCharCanvas.scale(mRatio, mRatio);
		paint.setColor(CHAR_FG_COLOR);
		paint.setStyle(Paint.Style.FILL);
		for (int i = 0; i < count; i++) {
			Path path = mPartPolygon.get(i);
			mFgCharCanvas.drawPath(path, paint);
			Log.d(LOG_TAG, "mFgCharCanvas.drawPath");
		}

		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(DIRECTION_COLOR);
		paint.setStrokeWidth(1.5f);
		for (int i = 0; i < mPartDirection.size(); i++) {
			Path arrowPath = mPartDirection.get(i).arrowPath;
			Path path = mPartDirection.get(i).path;
			mTempCanvas.drawPath(path, paint);
			mTempCanvas.drawPath(arrowPath, paint);
		}
		
//		Canvas localCanvas1 = new Canvas(this.mBgCharBmp);
//		localCanvas1.scale(mRatio, mRatio);
//		mPaint.setStyle(Paint.Style.FILL);
//		mPaint.setColor(CHAR_BG_COLOR);
//		localCanvas1.drawPath(this.mDrawer.drawPath(localArrayList1),
//				mPaint);
//		
//		Canvas localCanvas2 = new Canvas(this.mFgCharBmp);
//		localCanvas2.scale(mRatio, mRatio);
//		mPaint.setColor(CHAR_FG_COLOR);
//		localCanvas2.drawPath(this.mDrawer.drawPath(localArrayList1),
//				this.mPaint);
//		this.mPartDirection.clear();
//		this.mPartPolygon.clear();
//		HwPolygonParser localHwPolygonParser = new HwPolygonParser(
//				localHwSVGParser, this.mDrawer);
//		for (int k = 0; k < paramList1.size(); k++) {
//			String str = (String) paramList1.get(k);
//			ArrayList localArrayList2 = new ArrayList();
//			localHwSVGParser.resetString(str);
//			for (;;) {
//				HwSVGDrawer.HwCmd localHwCmd2 = localHwSVGParser.nextCmd();
//				if (localHwCmd2 == null) {
//					break;
//				}
//				localArrayList2.add(localHwCmd2);
//			}
//			Iterator localIterator2 = localArrayList2.iterator();
//			while (localIterator2.hasNext()) {
//				Iterator localIterator3 = ((HwSVGDrawer.HwCmd) localIterator2
//						.next()).points.iterator();
//				while (localIterator3.hasNext()) {
//					HwSVGDrawer.HwPoint localHwPoint1 = (HwSVGDrawer.HwPoint) localIterator3
//							.next();
//					localHwPoint1.x = ((float) (localHwPoint1.x * this.mRatio));
//					localHwPoint1.y = ((float) (localHwPoint1.y * this.mRatio));
//				}
//			}
//			PartDirectionPath localPartDirectionPath = new PartDirectionPath();
//			localPartDirectionPath.path = new Path();
//			localPartDirectionPath.path.set(this.mDrawer
//					.drawPath(localArrayList2));
//			ArrowCal.cal(getContext(), localPartDirectionPath, this.mRatio);
//			this.mPartDirection.add(localPartDirectionPath);
//			Path localPath = localHwPolygonParser.parsePolygonStr(
//					(String) paramList2.get(k), this.mRatio);
//			this.mPartPolygon.add(localPath);
//		}
		invalidate();
			
		if (this.mWriting == null) {
			HwWriting localHwWriting = new HwWriting(this, this.mRatio);
			this.mWriting = localHwWriting;
			// setOnTouchListener(this.mWriting);//
			this.mWriting.setWritingListener(this.mWritingListener);
		}

		if (this.mAnim == null) {
			HwAnim localHwAnim = new HwAnim(this, this.mRatio);
			this.mAnim = localHwAnim;
			this.mAnim.timeGap = this.timeGap;
		}
		this.mAnim.setAnimListener(this.mAnimListener);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// fillAfter(mTempCanvas);
		// applySolidColor(mTempBitmap);
		if (mBgVisible) {
			canvas.drawBitmap(mBgCharBmp, 0, 0, null);
		}
		if (this.mAnim != null) {
			this.mAnim.drawAnim(canvas);
		}
	}

	/**
	 * If there is svg , the user called setFillAfter(true) and the progress is
	 * finished.
	 * 
	 * @param canvas
	 *            Draw to this canvas.
	 */
	private void fillAfter(final Canvas canvas) {
		if (svgResourceId != 0 && fillAfter && progress == 1f) {
			svgUtils.drawSvgAfter(canvas, width, height);
		}
	}

	/**
	 * If there is svg , the user called setFill(true).
	 * 
	 * @param canvas
	 *            Draw to this canvas.
	 */
	private void fill(final Canvas canvas) {
		if (svgResourceId != 0 && fill) {
			svgUtils.drawSvgAfter(canvas, width, height);
		}
	}

	/**
	 * If fillColor had value before then we replace untransparent pixels of
	 * bitmap by solid color
	 * 
	 * @param bitmap
	 *            Draw to this canvas.
	 */
	private void applySolidColor(final Bitmap bitmap) {
		if (fill && fillColor != Color.argb(0, 0, 0, 0))
			if (bitmap != null) {
				for (int x = 0; x < bitmap.getWidth(); x++) {
					for (int y = 0; y < bitmap.getHeight(); y++) {
						int argb = bitmap.getPixel(x, y);
						int alpha = Color.alpha(argb);
						if (alpha != 0) {
							int red = Color.red(fillColor);
							int green = Color.green(fillColor);
							int blue = Color.blue(fillColor);
							argb = Color.argb(alpha, red, green, blue);
							bitmap.setPixel(x, y, argb);
						}
					}
				}
			}
	}

	@Override
	protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (mLoader != null) {
			try {
				mLoader.join();
			} catch (InterruptedException e) {
				Log.e(LOG_TAG, "Unexpected error", e);
			}
		}
		if (svgResourceId != 0) {
			mLoader = new Thread(new Runnable() {
				@Override
				public void run() {

					svgUtils.load(getContext(), svgResourceId);

					synchronized (mSvgLock) {
						width = w - getPaddingLeft() - getPaddingRight();
						height = h - getPaddingTop() - getPaddingBottom();
						paths = svgUtils.getPathsForViewport(width, height);

						updatePathsPhaseLocked();
					}
				}
			}, "SVG Loader");
			mLoader.start();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (svgResourceId != 0) {
			int widthSize = MeasureSpec.getSize(widthMeasureSpec);
			int heightSize = MeasureSpec.getSize(heightMeasureSpec);
			setMeasuredDimension(widthSize, heightSize);
			return;
		}

		int desiredWidth = 0;
		int desiredHeight = 0;
		final float strokeWidth = paint.getStrokeWidth() / 2;
		for (SvgUtils.SvgPath path : paths) {
			desiredWidth += path.bounds.left + path.bounds.width()
					+ strokeWidth;
			desiredHeight += path.bounds.top + path.bounds.height()
					+ strokeWidth;
		}
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(widthMeasureSpec);

		int measuredWidth, measuredHeight;

		if (widthMode == MeasureSpec.AT_MOST) {
			measuredWidth = desiredWidth;
		} else {
			measuredWidth = widthSize;
		}

		if (heightMode == MeasureSpec.AT_MOST) {
			measuredHeight = desiredHeight;
		} else {
			measuredHeight = heightSize;
		}

		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	/**
	 * If the real svg need to be drawn after the path animation.
	 * 
	 * @param fillAfter
	 *            - boolean if the view needs to be filled after path animation.
	 */
	public void setFillAfter(final boolean fillAfter) {
		this.fillAfter = fillAfter;
	}

	/**
	 * If the real svg need to be drawn without the path animation.
	 * 
	 * @param fill
	 *            - boolean if the view needs to be filled after path animation.
	 */
	public void setFill(final boolean fill) {
		this.fill = fill;
	}

	/**
	 * The color for drawing svg in that color if the color be not transparent
	 * 
	 * @param color
	 *            - the color for filling in that
	 */
	public void setFillColor(final int color) {
		this.fillColor = color;
	}

	/**
	 * If you want to use the colors from the svg.
	 */
	public void useNaturalColors() {
		naturalColors = true;
	}

	/**
	 * Get the path color.
	 * 
	 * @return The color of the paint.
	 */
	public int getPathColor() {
		return paint.getColor();
	}

	/**
	 * Set the path color.
	 * 
	 * @param color
	 *            -The color to set to the paint.
	 */
	public void setPathColor(final int color) {
		paint.setColor(color);
	}

	/**
	 * Get the path width.
	 * 
	 * @return The width of the paint.
	 */
	public float getPathWidth() {
		return paint.getStrokeWidth();
	}

	/**
	 * Set the path width.
	 * 
	 * @param width
	 *            - The width of the path.
	 */
	public void setPathWidth(final float width) {
		paint.setStrokeWidth(width);
	}

	/**
	 * Get the svg resource id.
	 * 
	 * @return The svg raw resource id.
	 */
	public int getSvgResource() {
		return svgResourceId;
	}

	/**
	 * Set the svg resource id.
	 * 
	 * @param svgResource
	 *            - The resource id of the raw svg.
	 */
	public void setSvgResource(int svgResource) {
		svgResourceId = svgResource;
	}

	public HwWriting mWriting = null;
	public HwSVGDrawer mDrawer = new HwSVGDrawer();
	public HwWriting.OnWritingListener mWritingListener;
	public List<PartDirectionPath> mPartDirection = new ArrayList<PartDirectionPath>();
	public HwAnim mAnim = null;
	public HwAnim.OnAnimListener mAnimListener;
	public int timeGap = 1000;
	public float mRatio = 1.0f;

	public void setDirectionPath(Path path) {
		PartDirectionPath localPartDirectionPath = new PartDirectionPath();
		localPartDirectionPath.path = new Path();
		localPartDirectionPath.path.set(path);
		ArrowCal.cal(getContext(), localPartDirectionPath, this.mRatio);
		this.mPartDirection.add(localPartDirectionPath);
	}

	public void setAnimListener(HwAnim.OnAnimListener paramOnAnimListener) {
		this.mAnimListener = paramOnAnimListener;
		if (this.mAnim != null) {
			this.mAnim.setAnimListener(this.mAnimListener);
		}
	}

	public void setTimeGap(int paramInt) {
		this.timeGap = paramInt;
		if (this.mAnim != null) {
			this.mAnim.timeGap = paramInt;
		}
	}

	public void setWritingListener(
			HwWriting.OnWritingListener paramOnWritingListener) {
		this.mWritingListener = paramOnWritingListener;
		if (this.mWriting != null) {
			this.mWriting.setWritingListener(this.mWritingListener);
		}
	}

	public void startHwAnim() {
		disableHandwriting();
		if (this.mAnim != null) {
			this.mAnim.startHwAnim();
		}
	}

	public void stopHwAnim() {
		if (this.mAnim != null) {
			this.mAnim.stopHwAnim();
		}
	}

	public void disableHandwriting() {
		if (this.mWriting != null) {
			this.mWriting.disableWriting();
		}
	}

	public void enableHandwriting() {
		stopHwAnim();
		if (this.mWriting != null) {
			this.mWriting.enableWriting();
		}
	}

	public static class PartDirectionPath {
		public Path arrowPath;
		public Path path;
	}
}
