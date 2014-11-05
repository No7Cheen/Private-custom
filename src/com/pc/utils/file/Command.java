package com.pc.utils.file;

import java.io.DataOutputStream;

public class Command {

	static {
		System.loadLibrary("systemcall");
	}

	public static native void exec(String cmd);

	public static void excuteCmd(String cmd) {
		exec(cmd);
	}

	public static boolean runRootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			// Log.d("runCommand",
			// "Unexpected error - Here is what I know: " + e.getMessage());
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (process != null) process.destroy();
			} catch (Exception e) {
				// nothing
			}
		}
		return true;
	}

	public static boolean runCommand(String command) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
		} catch (Exception e) {
			// Log.d("runCommand",
			// "Unexpected error - Here is what I know: " + e.getMessage());
			return false;
		} finally {
			try {
				if (process != null) process.destroy();
			} catch (Exception e) {
			}
		}
		return true;
	}

}
