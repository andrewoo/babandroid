package com.util.svgandroid;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Path;
import android.util.Log;

public class HwSVGDrawer {
	protected HwPoint mCurrentPoint = new HwPoint();
	protected Path mPath = new Path();

	public Path drawPath(List<HwCmd> paramList) {
		return drawPath(paramList, null);
	}

	public Path drawPath(List<HwSVGDrawer.HwCmd> cmds,
			List<HwSVGDrawer.HwPoint> points) {
		mPath.reset();
		mCurrentPoint.set(0.0f, 0.0f);
		int endPos = cmds.size();
		HwSVGDrawer.HwCmd prev = null;
		for (int i = 0; i < endPos; i = i + 1) {
			HwSVGDrawer.HwCmd c = (HwSVGDrawer.HwCmd) cmds.get(i);
			if (c.cmd.equals("m")) {
				performMoveCmd(c);
			} else if (c.cmd.equals("z")) {
				mPath.close();
				prev = c;
			} else if (c.cmd.equals("c")) {
				performCurveCmd(c);
				prev = c;
			} else if (c.cmd.equals("s")) {
				performSmoothCurveCmd(c, prev);
				prev = c;
			} else if (c.cmd.equals("v")) {
				performVerticalLineCmd(c);
				prev = c;
			} else if (c.cmd.equals("h")) {
				performHorizontalLineCmd(c);
				prev = c;
			} else if (c.cmd.equals("l")) {
				performLineCmd(c);
				prev = c;
			} else {
				Log.i("unknown cmds", c.cmd);
				prev = c;
				break;
			}
			prev = c;
			if (points != null) {
				points.add(new HwSVGDrawer.HwPoint(mCurrentPoint.x,
						mCurrentPoint.y));
			}
		}
		return mPath;
	}

	public HwPoint getCurrentPoint() {
		return this.mCurrentPoint;
	}

	protected void performCurveCmd(HwCmd paramHwCmd) {
		if (paramHwCmd.absolute) {
			this.mPath.cubicTo(((HwPoint) paramHwCmd.points.get(0)).x,
					((HwPoint) paramHwCmd.points.get(0)).y,
					((HwPoint) paramHwCmd.points.get(1)).x,
					((HwPoint) paramHwCmd.points.get(1)).y,
					((HwPoint) paramHwCmd.points.get(2)).x,
					((HwPoint) paramHwCmd.points.get(2)).y);
			this.mCurrentPoint.set(((HwPoint) paramHwCmd.points.get(2)).x,
					((HwPoint) paramHwCmd.points.get(2)).y);
			return;
		}
		this.mPath.cubicTo(((HwPoint) paramHwCmd.points.get(0)).x
				+ this.mCurrentPoint.x, ((HwPoint) paramHwCmd.points.get(0)).y
				+ this.mCurrentPoint.y, ((HwPoint) paramHwCmd.points.get(1)).x
				+ this.mCurrentPoint.x, ((HwPoint) paramHwCmd.points.get(1)).y
				+ this.mCurrentPoint.y, ((HwPoint) paramHwCmd.points.get(2)).x
				+ this.mCurrentPoint.x, ((HwPoint) paramHwCmd.points.get(2)).y
				+ this.mCurrentPoint.y);
		this.mCurrentPoint.set(this.mCurrentPoint.x
				+ ((HwPoint) paramHwCmd.points.get(2)).x, this.mCurrentPoint.y
				+ ((HwPoint) paramHwCmd.points.get(2)).y);
	}

	protected void performHorizontalLineCmd(HwCmd paramHwCmd) {
		if (paramHwCmd.absolute) {
			this.mPath.lineTo(((HwPoint) paramHwCmd.points.get(0)).x,
					this.mCurrentPoint.y);
			this.mCurrentPoint.setX(((HwPoint) paramHwCmd.points.get(0)).x);
			return;
		}
		this.mPath.lineTo(((HwPoint) paramHwCmd.points.get(0)).x
				+ this.mCurrentPoint.x, this.mCurrentPoint.y);
		this.mCurrentPoint.setX(((HwPoint) paramHwCmd.points.get(0)).x
				+ this.mCurrentPoint.x);
	}

	protected void performLineCmd(HwSVGDrawer.HwCmd c) {
		if (c.absolute) {
			mCurrentPoint.set(c.points.get(0).x, c.points.get(0).y);
		} else {
			mCurrentPoint.set((mCurrentPoint.x + c.points.get(0).x),
					(c.points.get(0).y + mCurrentPoint.y));
		}
		mPath.lineTo(mCurrentPoint.x, mCurrentPoint.y);
	}

	protected void performMoveCmd(HwSVGDrawer.HwCmd c) {
		if (c.absolute) {
			mCurrentPoint.set(c.points.get(0).x, c.points.get(0).y);
		} else {
			mCurrentPoint.set((mCurrentPoint.x + c.points.get(0).x),
					(c.points.get(0).y + mCurrentPoint.y));
		}
		mPath.moveTo(mCurrentPoint.x, mCurrentPoint.y);
	}

	protected void performSmoothCurveCmd(HwSVGDrawer.HwCmd c,
			HwSVGDrawer.HwCmd prev) {
		float firstControlPointX = mCurrentPoint.x;
		float firstControlPointY = mCurrentPoint.y;
		if (prev != null) {
			if (prev.cmd.equals("c")) {
				if (prev.absolute) {
					firstControlPointX = (prev.points.get(1).x * -1.0f)
							+ (mCurrentPoint.x * 2.0f);
					firstControlPointY = (prev.points.get(1).y * -1.0f)
							+ (mCurrentPoint.y * 2.0f);
				} else {
					float oldX = mCurrentPoint.x - prev.points.get(2).x;
					float oldY = mCurrentPoint.y - prev.points.get(2).y;
					firstControlPointX = ((prev.points.get(1).x + oldX) * -1.0f)
							+ (mCurrentPoint.x * 2.0f);
					firstControlPointY = ((prev.points.get(1).y + oldY) * -1.0f)
							+ (mCurrentPoint.y * 2.0f);
				}
			} else if (prev.cmd.equals("s")) {
				if (prev.absolute) {
					firstControlPointX = (prev.points.get(0).x * -1.0f)
							+ (mCurrentPoint.x * 2.0f);
					firstControlPointY = (prev.points.get(0).y * -1.0f)
							+ (mCurrentPoint.y * 2.0f);
				} else {
					float oldX = mCurrentPoint.x - prev.points.get(1).x;
					float oldY = mCurrentPoint.y - prev.points.get(1).y;
					firstControlPointX = ((prev.points.get(0).x + oldX) * -1.0f)
							+ (mCurrentPoint.x * 2.0f);
					firstControlPointY = ((prev.points.get(0).y + oldY) * -1.0f)
							+ (mCurrentPoint.y * 2.0f);
				}
			}
		}
		if (c.absolute) {
			mPath.cubicTo(firstControlPointX, firstControlPointY,
					c.points.get(0).x, c.points.get(0).y, c.points.get(1).x,
					c.points.get(1).y);
			mCurrentPoint.set(c.points.get(1).x, c.points.get(1).y);
			return;
		}
		mPath.cubicTo(firstControlPointX, firstControlPointY,
				c.points.get(0).y, -1.0f, 2.0f, 0.0f);
		mCurrentPoint.y = c.points.get(0).y;
		c.points.get(1).y += mCurrentPoint.y;
		mCurrentPoint.set((mCurrentPoint.x + c.points.get(1).x),
				(c.points.get(1).y + mCurrentPoint.y));
	}

	protected void performVerticalLineCmd(HwCmd paramHwCmd) {
		if (paramHwCmd.absolute) {
			this.mPath.lineTo(this.mCurrentPoint.x,
					((HwPoint) paramHwCmd.points.get(0)).y);
			this.mCurrentPoint.setY(((HwPoint) paramHwCmd.points.get(0)).y);
			return;
		}
		this.mPath.lineTo(this.mCurrentPoint.x,
				((HwPoint) paramHwCmd.points.get(0)).y + this.mCurrentPoint.y);
		this.mCurrentPoint.setY(((HwPoint) paramHwCmd.points.get(0)).y
				+ this.mCurrentPoint.y);
	}

	public static class HwCmd {
		public boolean absolute;
		public String cmd;
		public List<HwSVGDrawer.HwPoint> points = new ArrayList();
	}

	public static class HwPoint {
		public float x;
		public float y;

		public HwPoint() {
		}

		public HwPoint(float paramFloat1, float paramFloat2) {
			this.x = paramFloat1;
			this.y = paramFloat2;
		}

		public void set(float paramFloat1, float paramFloat2) {
			this.x = paramFloat1;
			this.y = paramFloat2;
		}

		public void setX(float paramFloat) {
			this.x = paramFloat;
		}

		public void setY(float paramFloat) {
			this.y = paramFloat;
		}
	}
}
