/**
 * @(#)PathManager.java 2014-1-15 Copyright 2014 it.kedacom.com, Inc. All rights
 *                      reserved.
 */

package com.pc.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import android.content.Context;

import com.pc.utils.StringUtils;
import com.pc.utils.file.sdcard.PcSDcardUtil;

/**
 * @author chenjian
 * @date 2014-1-15
 */

public abstract class PcAbsPathManager {

	// 根目录
	protected final String mRootDir;

	// app根目录,位于根目录下（根目录/app根目录）
	protected final String mAppRootDir;

	/**
	 * @param rootDir 根目录
	 * @param appRootDir app根目录，根目录的下级
	 */
	public PcAbsPathManager(String rootDir, String appRootDir) {
		mRootDir = rootDir;
		mAppRootDir = appRootDir;

		if (PcSDcardUtil.existExternalStorageDirectory()) {
			File rDir = FileUtil.createFolder(PcSDcardUtil.getExternalStorageDirectory(), rootDir);
			if (rDir == null || StringUtils.isNull(rDir.getAbsolutePath())) {
				FileUtil.createFolder(PcSDcardUtil.getExternalStorageDirectory(), mAppRootDir);
			} else {
				FileUtil.createFolder(rDir.getAbsolutePath(), mAppRootDir);
			}
		}
	}

	/**
	 * 创建项Root目录 
	 * 
	 * @return /sdcard/mRootDir
	 */
	public File createRootDir() {
		return PcSDcardUtil.createDir(mRootDir);
	}

	/**
	 * 创建项目目录
	 * 
	 * @return  /sdcard/mRootDir/mAppRootDir
	 */
	public File createAppRootDir() {
		return PcSDcardUtil.createDir(mRootDir, mAppRootDir);
	}

	/**
	 * 在AppRootDir目录下创建文件夹
	 * 
	 * @param pathname dir
	 * @param name file name
	 * @return /sdcard/mRootDir/mAppRootDir/fileDir
	 */
	public File createFolderOnAppDir(String fileDir) {
		File appRootDir = createAppRootDir();
		if (null == appRootDir) {
			return null;
		}

		return FileUtil.createFolder(appRootDir, fileDir);
	}

	/**
	 * 在AppRootDir目录下创建文件
	 * 
	 * @param pathname dir
	 * @param name file name
	 * @return /sdcard/mRootDir/mAppRootDir/fileDir/file
	 */
	public File createFileOnAppDir(String fileDir, String file) {
		File appRootDir = createAppRootDir();
		if (null == appRootDir) {
			return null;
		}

		return FileUtil.createFile(new File(appRootDir, fileDir), file);
	}

	/**
	 * 在AppRootDir目录下创建文件
	 * 
	 * @param file
	 * @return /sdcard/mRootDir/mAppRootDir/file
	 */
	public File createFile(String file) {
		File appRootDir = createAppRootDir();
		if (null == appRootDir) {
			return null;
		}

		return FileUtil.createFile(appRootDir, file);
	}

	/**
	 * 根目录绝对路径
	 * 
	 * @return /sdcard/mRootDir/
	 */
	public String getRootDir() {
		if (StringUtils.isNull(mRootDir)) {
			return PcSDcardUtil.getExternalStorageDirectory();
		}

		return createRootDir().getAbsolutePath() + File.separator;
	}

	/**
	 * App根目录绝对路径
	 * 
	 * @return /sdcard/mRootDir/mAppRootDir/
	 */
	public String getAppRootDir() {
		return createAppRootDir().getAbsolutePath() + File.separator;
	}

	/**
	 * App data files
	 *
	 * @param context
	 * @return data/data/application package/files
	 */
	public static String getAppFilesDir(Context context) {
		return context.getFilesDir().getAbsolutePath() + File.separator;
	}

	/**
	 * 读取存有List的File，并转换为一个List
	 * 
	 * @param fileName /data/data/<package name>/files/fileName
	 * @param context
	 * @return
	 */
	public static Object readObject4File(String fileName, Context context) {
		if (StringUtils.isNull(fileName) || context == null) {
			return null;
		}

		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(fileName);
			ois = new ObjectInputStream(fis);
			return ois.readObject();
		} catch (FileNotFoundException e1) {
		} catch (StreamCorruptedException e1) {
		} catch (OptionalDataException e1) {
		} catch (ClassNotFoundException e1) {
		} catch (IOException e1) {
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
			}
		}

		return null;
	}

	/**
	 * 保存List数据
	 * 
	 * @param fileName /data/data/<package name>/files/fileName
	 * @param al
	 * @param context
	 */
	public static void writeObject2File(String fileName, Object obj, Context context) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
		} catch (Exception e) {
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
			}
		}
	}

}
