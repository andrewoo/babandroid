package com.util.svgandroid;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HwSVGParser {
	private static final Pattern SVG_PATTERN = Pattern
			.compile("[-+]?[0-9]*\\.?[0-9]+");
	private String charStr;
	private int index = 0;

	private String next() {
		String str;
		if (this.index >= this.charStr.length()) {
			str = null;
		}
		do {
			// return str;
			int i = -1;
			while (this.index < this.charStr.length()) {
				if (((this.charStr.charAt(this.index) >= 'a') && (this.charStr
						.charAt(this.index) <= 'z'))
						|| ((this.charStr.charAt(this.index) >= 'A') && (this.charStr
								.charAt(this.index) <= 'Z'))) {
					if (i != -1) {
						break;
					}
					i = this.index;
				}
				this.index = (1 + this.index);
			}
			if (i == -1) {
				throw new IllegalArgumentException();
			}
			str = this.charStr.substring(i, this.index);

		} while (this.index < this.charStr.length());
		return str;
	}

	public HwSVGDrawer.HwCmd nextCmd() {
		String str = next();
		HwSVGDrawer.HwCmd localHwCmd;
		if (str == null) {
			localHwCmd = null;
		}
		for (;;) {

			localHwCmd = new HwSVGDrawer.HwCmd();
			if (Character.isUpperCase(str.charAt(0))) {
				return localHwCmd;
			}
			ArrayList localArrayList = null;
			for (localHwCmd.absolute = true;; localHwCmd.absolute = false) {
				localHwCmd.cmd = (str.charAt(0) + "").toLowerCase();
				if (str.substring(1).equals("")) {
					break;
				}
				localArrayList = new ArrayList();
				Matcher localMatcher = SVG_PATTERN.matcher(str);
				for (int i = 0; localMatcher.find(i); i = localMatcher.end()) {
					localArrayList.add(Float.valueOf(Float
							.parseFloat(localMatcher.group())));
				}
			}
			if (localHwCmd.cmd.equals("v")) {
				localHwCmd.points.add(new HwSVGDrawer.HwPoint(0.0F,
						((Float) localArrayList.get(0)).floatValue()));
				return localHwCmd;
			}
			if (localHwCmd.cmd.equals("h")) {
				localHwCmd.points.add(new HwSVGDrawer.HwPoint(
						((Float) localArrayList.get(0)).floatValue(), 0.0F));
				return localHwCmd;
			}
			for (int j = 0; j < localArrayList.size(); j += 2) {
				localHwCmd.points.add(new HwSVGDrawer.HwPoint(
						((Float) localArrayList.get(j)).floatValue(),
						((Float) localArrayList.get(j + 1)).floatValue()));
			}
		}
	}

	public void resetString(String paramString) {
		this.charStr = paramString;
		this.index = 0;
	}
}
