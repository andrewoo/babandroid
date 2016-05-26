package com.util.svgandroid;

import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;

import com.util.svgandroid.HwSVGDrawer.HwPoint;
import com.util.tool.PathView.PartDirectionPath;

public class ArrowCal {

	public static Path cal(Context paramContext,
			Path path, float paramDouble) {
		Object localObject = new PathMeasure();
		((PathMeasure) localObject).setPath(path, false);
		float[] arrayOfFloat = new float[2];
		((PathMeasure) localObject).getPosTan(
				((PathMeasure) localObject).getLength(), arrayOfFloat, null);
		HwPoint localHwPoint = new HwPoint();
		localHwPoint.set(arrayOfFloat[0], arrayOfFloat[1]);
		((PathMeasure) localObject).getPosTan(
				((PathMeasure) localObject).getLength()
						- (int) (20.0D * paramDouble), arrayOfFloat, null);
		localObject = new HwPoint();
		((HwPoint) localObject).set(arrayOfFloat[0], arrayOfFloat[1]);
		Path arrowPath = calArrows(paramContext, path,
				(HwPoint) localObject, localHwPoint);
		return arrowPath;
	}

	public static Path calArrows(Context paramContext,
			Path path,
			HwPoint paramHwPoint1, HwPoint paramHwPoint2) {
		float f1 = paramHwPoint1.x;
		float f2 = paramHwPoint1.y;
		double d1 = f1 - paramHwPoint2.x;
		double d2 = Math.cos(0.5235987755982988D);
		double d3 = f2 - paramHwPoint2.y;
		double d4 = Math.sin(0.5235987755982988D);
		double d5 = paramHwPoint2.x;
		double d6 = f2 - paramHwPoint2.y;
		double d7 = Math.cos(0.5235987755982988D);
		double d8 = f1 - paramHwPoint2.x;
		double d9 = Math.sin(0.5235987755982988D);
		double d10 = paramHwPoint2.y;
		double d11 = f1 - paramHwPoint2.x;
		double d12 = Math.cos(5.759586531581287D);
		double d13 = f2 - paramHwPoint2.y;
		double d14 = Math.sin(5.759586531581287D);
		double d15 = paramHwPoint2.x;
		double d16 = f2 - paramHwPoint2.y;
		double d17 = Math.cos(5.759586531581287D);
		double d18 = f1 - paramHwPoint2.x;
		double d19 = Math.sin(5.759586531581287D);
		double d20 = paramHwPoint2.y;
		Path arrowPath= new Path();
		arrowPath.moveTo(
				(float) (d11 * d12 - d13 * d14 + d15), (float) (d16 * d17 + d18
						* d19 + d20));
		arrowPath.lineTo(paramHwPoint2.x,
				paramHwPoint2.y);
		arrowPath.lineTo(
				(float) (d1 * d2 - d3 * d4 + d5),
				(float) (d6 * d7 + d8 * d9 + d10));
		return arrowPath;
	}
}
