package com.pc.utils;

import android.graphics.Rect;

public class ShapUtils {

	public static boolean isCross(Rect r1, Rect r2) {
		boolean b = false;
		if (r1 == null || r2 == null) return b;
		int minx = Math.max(r1.left, r2.left);
		int miny = Math.max(r1.top, r2.top);
		int maxx = Math.min(r1.right, r2.right);
		int maxy = Math.min(r1.bottom, r2.bottom);
		if (minx > maxx || miny > maxy) {
			return false;
		}

		return true;
	}

}
