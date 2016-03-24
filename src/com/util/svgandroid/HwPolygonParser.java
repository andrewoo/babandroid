package com.util.svgandroid;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Path;

public class HwPolygonParser {
	protected HwSVGDrawer drawer = null;
	protected HwSVGParser parser = null;

	public HwPolygonParser(HwSVGParser paramHwSVGParser,
			HwSVGDrawer paramHwSVGDrawer) {
		this.parser = paramHwSVGParser;
		this.drawer = paramHwSVGDrawer;
	}

	public Path parsePolygonStr(String paramString, double paramDouble) {
		Path localPath = new Path();
		int i = paramString.charAt(0);
		if ((i >= 48) && (i <= 57)) {
			String[] arrayOfString1 = paramString.split("[ ]+");
			ArrayList localArrayList2 = new ArrayList();
			int j = arrayOfString1.length;
			int k = 0;
			if (k < j) {
				String str = arrayOfString1[k].trim();
				if (str.equals("")) {
				}
				for (;;) {
					k++;
					localArrayList2.add(str);
					break;
				}
			}
			int m = 0;
			if (m < localArrayList2.size()) {
				String[] arrayOfString2 = ((String) localArrayList2.get(m))
						.split(",");
				float f1 = Float.parseFloat(arrayOfString2[0].trim());
				float f2 = Float.parseFloat(arrayOfString2[1].trim());
				float f3 = (float) (paramDouble * f1);
				float f4 = (float) (paramDouble * f2);
				if (m == 0) {
					localPath.moveTo(f3, f4);
				}
				for (;;) {
					m++;
					localPath.lineTo(f3, f4);
					break;
				}
			}
			localPath.close();
			return localPath;
		}
		this.parser.resetString(paramString);
		ArrayList localArrayList1 = new ArrayList();
		for (;;) {
			HwSVGDrawer.HwCmd localHwCmd = this.parser.nextCmd();
			if (localHwCmd == null) {
				break;
			}
			localArrayList1.add(localHwCmd);
		}
		Iterator localIterator1 = localArrayList1.iterator();
		while (localIterator1.hasNext()) {
			Iterator localIterator2 = ((HwSVGDrawer.HwCmd) localIterator1
					.next()).points.iterator();
			while (localIterator2.hasNext()) {
				HwSVGDrawer.HwPoint localHwPoint = (HwSVGDrawer.HwPoint) localIterator2
						.next();
				localHwPoint.x = ((float) (paramDouble * localHwPoint.x));
				localHwPoint.y = ((float) (paramDouble * localHwPoint.y));
			}
		}
		localPath.set(this.drawer.drawPath(localArrayList1));
		return localPath;
	}
}
