package com.pc.utils.log;

/**
 * Log
 * @author chenjian
 */
public class PcLog {

	public static boolean isPrint = true; // isPrint

	private PcLog() {
	}

	public static int v(String tag, String msg) {
		if (!isPrint) {
			return -1;
		}

		if (msg == null) {
			msg = "";
		}

		return android.util.Log.v(tag, msg);
	}

	public static int v(String tag, String msg, Throwable tr) {
		if (!isPrint) {
			return -1;
		}

		if (msg == null) {
			msg = "";
		}

		return android.util.Log.v(tag, msg, tr);
	}

	public static int d(String tag, String msg) {
		if (!isPrint) {
			return -1;
		}

		if (msg == null) {
			msg = "";
		}

		return android.util.Log.d(tag, msg);
	}

	public static int d(String tag, String msg, Throwable tr) {
		if (!isPrint || msg == null) {
			return -1;
		}

		return android.util.Log.d(tag, msg, tr);
	}

	public static int i(String tag, String msg) {
		if (!isPrint) {
			return -1;
		}

		if (msg == null) {
			msg = "";
		}

		return android.util.Log.i(tag, msg);
	}

	public static int i(String tag, String msg, Throwable tr) {
		if (!isPrint) {
			return -1;
		}

		if (msg == null) {
			msg = "";
		}

		return android.util.Log.i(tag, msg, tr);
	}

	public static int w(String tag, String msg) {
		if (!isPrint) {
			return -1;
		}

		if (msg == null) {
			msg = "";
		}

		return android.util.Log.w(tag, msg);
	}

	public static int w(String tag, String msg, Throwable tr) {
		if (!isPrint) {
			return -1;
		}

		if (msg == null) {
			msg = "";
		}

		return android.util.Log.w(tag, msg, tr);
	}

	public static int w(String tag, Throwable tr) {
		if (!isPrint) {
			return -1;
		}

		return android.util.Log.w(tag, tr);
	}

	public static int e(String tag, String msg) {
		if (!isPrint) {
			return -1;
		}

		if (msg == null) {
			msg = "";
		}

		return android.util.Log.e(tag, msg);
	}

	public static int e(String tag, String msg, Throwable tr) {
		if (!isPrint) {
			return -1;
		}

		if (msg == null) {
			msg = "";
		}

		return android.util.Log.e(tag, msg, tr);
	}

	public static int println(int priority, String tag, String msg) {
		if (!isPrint) {
			return -1;
		}

		if (msg == null) {
			msg = "";
		}

		return android.util.Log.println(priority, tag, msg);
	}

}
