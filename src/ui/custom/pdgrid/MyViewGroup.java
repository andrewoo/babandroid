package ui.custom.pdgrid;

import java.util.ArrayList;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class MyViewGroup extends ViewGroup implements View.OnTouchListener,
		View.OnClickListener, View.OnLongClickListener {
	private final static String TAG = "MyViewGroup";
	/**
	 * 水平方向padding
	 */
	private static final int PADDING_HOR = 10;// 水平方向padding
	/**
	 * 垂直方向padding
	 */
	private static final int PADDING_VERTICAL = 5;// 垂直方向padding
	/**
	 * 左右间距
	 */
	private static final int SIDE_MARGIN = 10;// 左右间距
	private static final int TEXT_MARGIN = 10;

	// layout vars
	public static float childRatio = .9f;
	protected int colCount, childSize, padding, dpi, scroll = 0;
	protected float lastDelta = 0;
	protected Handler handler = new Handler();
	// dragging vars
	protected int dragged = -1, lastX = -1, lastY = -1, lastTarget = -1;
	protected boolean enabled = true, touching = false;
	// anim vars
	public static int animT = 150;
	protected ArrayList<Integer> newPositions = new ArrayList<Integer>();
	// listeners
	protected OnRearrangeListener onRearrangeListener;
	protected OnClickListener secondaryOnClickListener;
	private OnItemClickListener onItemClickListener;

	// private ViewDragHelper mDragger;
	public MyViewGroup(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		handler.removeCallbacks(updateTask);
		handler.postAtTime(updateTask, SystemClock.uptimeMillis() + 500);
		setChildrenDrawingOrderEnabled(true);

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public MyViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void setListeners() {
		setOnTouchListener(this);
		super.setOnClickListener(this);
		setOnLongClickListener(this);
	}

	protected Runnable updateTask = new Runnable() {
		@SuppressLint("WrongCall")
		public void run() {
			if (dragged != -1) {
				if (lastY < padding * 3 && scroll > 0)
					scroll -= 20;
				else if (lastY > getBottom() - getTop() - (padding * 3)
						&& scroll < getMaxScroll())
					scroll += 20;
			} else if (lastDelta != 0 && !touching) {
				scroll += lastDelta;
				lastDelta *= .9;
				if (Math.abs(lastDelta) < .25)
					lastDelta = 0;
			}
			clampScroll();
			onLayout(true, getLeft(), getTop(), getRight(), getBottom());
			handler.postDelayed(this, 25);
			invalidate();// yh
		}
	};

	// OVERRIDES
	@Override
	public void addView(View child) {
		super.addView(child);
		newPositions.add(-1);
	};

	@Override
	public void removeViewAt(int index) {
		super.removeViewAt(index);
		newPositions.remove(index);
	};

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		int autualWidth = r - l;
		int x = SIDE_MARGIN;// 横坐标开始
		int y = 0;// 纵坐标开始
		int rows = 1;
		for (int i = 0; i < childCount; i++) {
			View view = getChildAt(i);
			view.setBackgroundColor(Color.GREEN);
			int width = view.getMeasuredWidth();
			int height = view.getMeasuredHeight();
			x += width + TEXT_MARGIN;
			if (x > autualWidth) {
				x = width + SIDE_MARGIN;
				rows++;
			}
			y = rows * (height + TEXT_MARGIN);
			if (i == 0) {
				view.layout(x - width - TEXT_MARGIN, y - height, x
						- TEXT_MARGIN, y);
			} else {
				view.layout(x - width, y - height, x, y);
			}
		}
	};

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int x = 0;// 横坐标
		int y = 0;// 纵坐标
		int rows = 1;// 总行数
		int specWidth = MeasureSpec.getSize(widthMeasureSpec);
		int actualWidth = specWidth - SIDE_MARGIN * 2;// 实际宽度
		int childCount = getChildCount();
		for (int index = 0; index < childCount; index++) {
			View child = getChildAt(index);
			child.setPadding(PADDING_HOR, PADDING_VERTICAL, PADDING_HOR,
					PADDING_VERTICAL);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			x += width + TEXT_MARGIN;
			if (x > actualWidth) {// 换行
				x = width;
				rows++;
			}
			y = rows * (height + TEXT_MARGIN);
		}
		setMeasuredDimension(actualWidth, y);
	}

	@Override
	public boolean onLongClick(View view) {
		// TODO Auto-generated method stub
		if (!enabled)
			return false;
		int index = getLastIndex();
		if (index != -1) {
			dragged = index;
			animateDragged();
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (enabled) {
			if (secondaryOnClickListener != null)
				secondaryOnClickListener.onClick(view);
			if (onItemClickListener != null && getLastIndex() != -1)
				onItemClickListener.onItemClick(null,
						getChildAt(getLastIndex()), getLastIndex(),
						getLastIndex() / colCount);
		}
	}

	@SuppressLint("WrongCall")
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			enabled = true;
			lastX = (int) event.getX();
			lastY = (int) event.getY();
			touching = true;
			break;
		case MotionEvent.ACTION_MOVE:
			int delta = lastY - (int) event.getY();
			if (dragged != -1) {
				// change draw location of dragged visual
				int x = (int) event.getX(), y = (int) event.getY();// 手指移动的位置坐标
				int l = x - (3 * childSize / 4), t = y - (3 * childSize / 4);
				getChildAt(dragged).layout(l, t, l + (childSize * 3 / 2),
						t + (childSize * 3 / 2));

				// check for new target hover
				int target = getTargetFromCoor(x, y);
				if (lastTarget != target) {
					if (target != -1) {
						animateGap(target);
						lastTarget = target;
					}
				}
			} else {
				scroll += delta;
				clampScroll();
				if (Math.abs(delta) > 2)
					enabled = false;
				onLayout(true, getLeft(), getTop(), getRight(), getBottom());
			}
			lastX = (int) event.getX();
			lastY = (int) event.getY();
			lastDelta = delta;
			break;
		case MotionEvent.ACTION_UP:
			if (dragged != -1) {
				View v = getChildAt(dragged);
				if (lastTarget != -1)
					reorderChildren();
				else {
					Point xy = getCoorFromIndex(dragged);
					v.layout(xy.x, xy.y, xy.x + childSize, xy.y + childSize / 2);
				}
				v.clearAnimation();
				if (v instanceof ImageView)
					((ImageView) v).setAlpha(255);
				lastTarget = -1;
				dragged = -1;
			}
			touching = false;
			break;
		}
		if (dragged != -1)
			return true;
		return false;
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if (dragged == -1)
			return i;
		else if (i == childCount - 1)
			return dragged;
		else if (i >= dragged)
			return i + 1;
		return i;
	}

	/**
	 * 
	 */
	protected void animateDragged() {
		View v = getChildAt(dragged);
		int x = getCoorFromIndex(dragged).x + childSize / 2, y = getCoorFromIndex(dragged).y
				+ childSize / 2;
		int l = x - (3 * childSize / 4), t = y - (3 * childSize / 4);
		v.layout(l, t, l + (childSize * 3 / 2), t + (childSize * 3 / 4));// yh
		AnimationSet animSet = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(.667f, 1, .667f, 1,
				childSize * 3 / 4, childSize * 3 / 4);
		scale.setDuration(animT);
		AlphaAnimation alpha = new AlphaAnimation(1, .5f);
		alpha.setDuration(animT);

		animSet.addAnimation(scale);
		animSet.addAnimation(alpha);
		animSet.setFillEnabled(true);
		animSet.setFillAfter(true);

		v.clearAnimation();
		v.startAnimation(animSet);
	}

	/**
	 * 根据坐标获取位置
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	protected int getTargetFromCoor(int x, int y) {
		if (getColOrRowFromCoor(y + scroll) == -1) // touch is between rows
			return -1;
		// if (getIndexFromCoor(x, y) != -1) //touch on top of another visual
		// return -1;

		int leftPos = getIndexFromCoor(x - (childSize / 4), y);
		int rightPos = getIndexFromCoor(x + (childSize / 4), y);
		if (leftPos == -1 && rightPos == -1) // touch is in the middle of
												// nowhere
			return -1;
		if (leftPos == rightPos) // touch is in the middle of a visual
			return -1;

		int target = -1;
		if (rightPos > -1)
			target = rightPos;
		else if (leftPos > -1)
			target = leftPos + 1;
		if (dragged < target)
			return target - 1;

		// Toast.makeText(getContext(), "Target: " + target + ".",
		// Toast.LENGTH_SHORT).show();
		return target;
	}

	protected Point getCoorFromIndex(int index) {
		int col = index % colCount;
		int row = index / colCount;
		return new Point(padding + (childSize + padding) * col, padding
				+ (childSize + padding) * row - scroll);
	}

	public int getIndexOf(View child) {
		for (int i = 0; i < getChildCount(); i++)
			if (getChildAt(i) == child)
				return i;
		return -1;
	}

	protected void animateGap(int target) {
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			if (i == dragged)
				continue;
			int newPos = i;
			if (dragged < target && i >= dragged + 1 && i <= target)
				newPos--;
			else if (target < dragged && i >= target && i < dragged)
				newPos++;

			// animate
			int oldPos = i;
			if (newPositions.get(i) != -1)
				oldPos = newPositions.get(i);
			if (oldPos == newPos)
				continue;

			Point oldXY = getCoorFromIndex(oldPos);
			Point newXY = getCoorFromIndex(newPos);
			Point oldOffset = new Point(oldXY.x - v.getLeft(), oldXY.y
					- v.getTop());
			Point newOffset = new Point(newXY.x - v.getLeft(), newXY.y
					- v.getTop());

			TranslateAnimation translate = new TranslateAnimation(
					Animation.ABSOLUTE, oldOffset.x, Animation.ABSOLUTE,
					newOffset.x, Animation.ABSOLUTE, oldOffset.y,
					Animation.ABSOLUTE, newOffset.y);
			translate.setDuration(animT);
			translate.setFillEnabled(true);
			translate.setFillAfter(true);
			v.clearAnimation();
			v.startAnimation(translate);

			newPositions.set(i, newPos);
		}
	}

	@SuppressLint("WrongCall")
	protected void reorderChildren() {
		// FIGURE OUT HOW TO REORDER CHILDREN WITHOUT REMOVING THEM ALL AND
		// RECONSTRUCTING THE LIST!!!
		if (onRearrangeListener != null)
			onRearrangeListener.onRearrange(dragged, lastTarget);
		ArrayList<View> children = new ArrayList<View>();
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).clearAnimation();
			children.add(getChildAt(i));
		}
		removeAllViews();
		while (dragged != lastTarget)
			if (lastTarget == children.size()) // dragged and dropped to the
												// right of the last element
			{
				children.add(children.remove(dragged));
				dragged = lastTarget;
			} else if (dragged < lastTarget) // shift to the right
			{
				Collections.swap(children, dragged, dragged + 1);
				dragged++;
			} else if (dragged > lastTarget) // shift to the left
			{
				Collections.swap(children, dragged, dragged - 1);
				dragged--;
			}
		for (int i = 0; i < children.size(); i++) {
			newPositions.set(i, -1);
			addView(children.get(i));
		}
		onLayout(true, getLeft(), getTop(), getRight(), getBottom());
	}

	public void scrollToTop() {
		scroll = 0;
	}

	public void scrollToBottom() {
		scroll = Integer.MAX_VALUE;
		clampScroll();
	}

	public int getIndexFromCoor(int x, int y) {
		int col = getColOrRowFromCoor(x), row = getColOrRowFromCoor(y + scroll);
		if (col == -1 || row == -1) // touch is between columns or rows
			return -1;
		int index = row * colCount + col;
		if (index >= getChildCount())
			return -1;
		return index;
	}

	protected int getColOrRowFromCoor(int coor) {
		coor -= padding;
		for (int i = 0; coor > 0; i++) {
			if (coor < childSize)
				return i;
			coor -= (childSize + padding);
		}
		return -1;
	}

	protected void clampScroll() {
		int stretch = 3, overreach = getHeight() / 2;
		int max = getMaxScroll();
		max = Math.max(max, 0);

		if (scroll < -overreach) {
			scroll = -overreach;
			lastDelta = 0;
		} else if (scroll > max + overreach) {
			scroll = max + overreach;
			lastDelta = 0;
		} else if (scroll < 0) {
			if (scroll >= -stretch)
				scroll = 0;
			else if (!touching)
				scroll -= scroll / stretch;
		} else if (scroll > max) {
			if (scroll <= max + stretch)
				scroll = max;
			else if (!touching)
				scroll += (max - scroll) / stretch;
		}
	}

	protected int getMaxScroll() {
		int rowCount = (int) Math.ceil((double) getChildCount() / colCount), max = rowCount
				* childSize + (rowCount + 1) * padding - getHeight();
		return max;
	}

	public int getLastIndex() {
		return getIndexFromCoor(lastX, lastY);
	}

	// OTHER METHODS
	public void setOnRearrangeListener(OnRearrangeListener l) {
		this.onRearrangeListener = l;
	}

	public void setOnItemClickListener(OnItemClickListener l) {
		this.onItemClickListener = l;
	}

}