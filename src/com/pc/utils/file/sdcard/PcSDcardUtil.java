/**
 * @(#)SDcardUtil.java 2014-5-16 Copyright 2014 it.kedacom.com, Inc. All rights
 *                     reserved.
 */

package com.pc.utils.file.sdcard;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

import com.pc.utils.StringUtils;
import com.pc.utils.file.FileUtil;

/**
 * SDcard Util
 * 
 * @author chenj
 * @date 2014-5-16
 */

public class PcSDcardUtil {

	/**
	 * 获取外部存储目录
	 * 
	 * @return
	 */
	public static String getExternalStorageDirectory() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 获取外部存储目录
	 *
	 * @return
	 */
	public static File getExternalStorageDir() {
		return Environment.getExternalStorageDirectory();
	}

	/**
	 * 外部存储目录是否存在且可写
	 * 
	 * @return
	 */
	public static boolean existExternalStorageDirectory() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && Environment.getExternalStorageDirectory().canWrite()) {

			return true;
		}

		return false;
	}

	/**
	 * SD卡是否能用
	 * 
	 * @return true 可用
	 */
	public static boolean isCanUseSD() {
		try {
			return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 计算sdcard上的剩余空间 MB
	 * 
	 */
	public static int freeSpaceOnSDMB() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocksLong() * (double) stat.getBlockSizeLong()) / 1024 * 1024;

		return (int) sdFreeMB;
	}

	/**
	 * 计算sdcard上的剩余空间
	 *
	 * @return
	 */
	public static long freeSpaceOnSD() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());

		return stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
	}

	/**
	 * 创建文件夹
	 *
	 * @param dir 相对sd路径
	 * @return /sdcard/dir/
	 */
	public static File createDir(String dir) {
		try {
			if (!isCanUseSD()) {
				return null;
			}

			return FileUtil.createFolder(getExternalStorageDirectory() + File.separator + dir);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 创建文件夹
	 *
	 * @param dir 相对sd路径
	 * @param subDir
	 * @return /sdcard/dir/path
	 */
	public static File createDir(String dir, String subDir) {
		if (StringUtils.isNull(subDir)) {
			return null;
		}

		try {
			if (!isCanUseSD()) {
				return null;
			}

			return FileUtil.createFolder(new File(getExternalStorageDirectory() + File.separator + dir), subDir);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 创建文件 
	 *
	 * @param dir 相对sd路径
	 * @param filename
	 * @return /sdcard/dir/filename
	 */
	public static File createFile(String dir, String filename) {
		if (StringUtils.isNull(filename)) {
			return null;
		}

		try {
			if (!isCanUseSD()) {
				return null;
			}

			return FileUtil.createFile(getExternalStorageDirectory() + File.separator + dir, filename);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * name文件是否存在于ExternalStorageDirectory
	 * 
	 * @param file
	 * @return
	 */
	public static boolean existsFromExternalStorageDirectory(String file) {
		if (StringUtils.isNull(file)) {
			return false;
		}

		try {
			if (!isCanUseSD()) {
				return false;
			}

			return new File(getExternalStorageDirectory() + File.separator + file).exists();
		} catch (Exception e) {
			return false;
		}
	}

}
