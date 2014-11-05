package com.pc.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import android.util.Log;

import com.pc.utils.StringUtils;

/**
 * 文件工具类
 * @author chenjian
 */
public final class FileUtil {

	/**
	 * 创建文件夹
	 * @param dir
	 */
	public static File createFolder(String dir) {
		if (StringUtils.isNull(dir)) {
			return null;
		}

		try {
			File file = new File(dir);
			if (!file.exists()) {
				file.mkdirs();
			}

			return file;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 创建文件夹
	 * @param dir
	 * @param folder
	 * @return
	 */
	public static File createFolder(String dir, String folder) {
		if (StringUtils.isNull(dir) && StringUtils.isNull(folder)) {
			return null;
		}

		if (StringUtils.isNull(dir)) {
			return createFolder(folder);
		}

		if (StringUtils.isNull(folder)) {
			return createFolder(dir);
		}

		File file = new File(dir, folder);
		if (!file.exists()) {
			file.mkdirs();
		}

		return file;
	}

	/**
	 * 创建文件夹
	 *
	 * @param dir
	 * @param folder
	 * @return
	 */
	public static File createFolder(File dir, String folder) {
		if (null == dir && StringUtils.isNull(folder)) {
			return null;
		}

		if (null == dir) {
			return createFolder(folder);
		}

		File file = new File(dir, folder);
		if (!file.exists()) {
			file.mkdirs();
		}

		return file;
	}

	/**
	 * 在dir/创建文件
	 * @param name dir/name
	 */
	public static File createFile(String dir, String name) {
		if (StringUtils.isNull(dir) || StringUtils.isNull(name)) {
			return null;
		}

		File dirF = new File(dir);
		if (!dirF.exists()) dirF.mkdirs();

		File f = new File(dirF, name);
		if (!f.exists() || !f.isFile()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
			}
		}

		return f;
	}

	/**
	 * 在dir/创建文件
	 * @param dir
	 * @param name
	 * @return
	 */
	public static File createFile(File dir, String name) {
		if (null == dir || StringUtils.isNull(name)) {
			return null;
		}

		if (!dir.exists()) dir.mkdirs();

		File f = new File(dir, name);
		if (!f.exists() || !f.isFile()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
			}
		}

		return f;
	}

	public static boolean createDirByShell(String path) {
		if (StringUtils.isNull(path)) return false;
		File dd = new File(path);
		if (dd.exists() && !dd.isDirectory()) {
			return dd.mkdirs();
		}

		if (dd.exists() && dd.canWrite()) return true;

		if (dd.exists() && !dd.canWrite()) {
			String command = "chmod 777" + path;

			return Command.runCommand(command);
		}
		String command = "mkdir " + path;

		return Command.runCommand(command);
	}

	/**
	 * 是否存在文件or文件夹
	 * @param uri
	 * @return
	 */
	public static boolean isExist(String uri) {
		boolean b = false;
		if (StringUtils.isNull(uri)) return b;
		try {
			return (new File(uri)).exists();
		} catch (Exception e) {
		}
		return b;
	}

	/**
	 * 文件是否存在
	 * @param path
	 * @return
	 */
	public static boolean existsFile(String path) {
		if (StringUtils.isNull(path)) return false;

		boolean flag = false;
		File tmpF = new File(path);
		if (tmpF.exists() && tmpF.isFile()) {
			flag = true;
		} else {
			flag = false;
		}

		return flag;
	}

	/**
	 * 文件是否存在
	 * @param dir
	 * @param path
	 * @return
	 */
	public static boolean existsFile(String dir, String path) {
		if (StringUtils.isNull(dir) || StringUtils.isNull(path)) {
			return false;
		}

		boolean flag = false;
		File tmpF = new File(new File(dir), path);
		if (tmpF.exists() && tmpF.isFile()) {
			flag = true;
		} else {
			flag = false;
		}

		return flag;
	}

	/**
	 * 文件是否存在
	 * @param dir
	 * @param path
	 * @return
	 */
	public static boolean existsFile(File dir, String path) {
		if (StringUtils.isNull(path)) return false;

		File tmpF = new File(dir, path);
		if (tmpF.exists() && tmpF.isFile()) {
			return true;
		}

		return false;
	}

	/**
	 * 文件夹
	 * @param path
	 * @return
	 */
	public static boolean existsFolder(String name) {
		if (StringUtils.isNull(name)) {
			return false;
		}

		boolean flag = false;
		File tmpF = new File(name);
		if (tmpF.exists() && tmpF.isDirectory()) {
			flag = true;
		} else {
			flag = false;
		}

		return flag;
	}

	/**
	 * 以String形式返回文件内容
	 * @param aFile
	 * @return
	 */
	public static String getFileString(File aFile) {
		if (aFile == null) return null;

		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(aFile));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 以String形式返回文件内容
	 * @param aFile
	 * @return
	 */
	public static String getFileString(String path) {
		if (path == null) return null;

		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * @param aFile
	 * @return
	 */
	public static boolean deleteFile(File aFile) {
		if (aFile == null) return false;

		boolean bResult = false;
		bResult = aFile.delete();

		return bResult;
	}

	/**
	 * 删除所有文件
	 */
	public static boolean removeAllFile(String path) {
		try {
			File fileDirectory = new File(path);
			File[] files = fileDirectory.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static boolean del(String sPath) {
		if (StringUtils.isNull(sPath)) {
			return false;
		}

		boolean flag = deleteDir(sPath);
		if (!flag) {
			flag = deleteFile(sPath);
		}

		return flag;
	}

	/**
	 * delete文件
	 * @param sPath
	 * @return
	 */
	public static boolean deleteFile(String sPath) {
		if (StringUtils.isNull(sPath)) {
			return false;
		}

		boolean flag = false;
		File file = new File(sPath);
		if (file.exists() && file.isFile()) {
			file.delete();
			flag = true;
		} else {
			flag = false;
		}

		return flag;
	}

	/**
	 * delete一级目录
	 * @param sPath
	 * @return
	 */
	public static boolean deleteDir(String sPath) {
		if (StringUtils.isNull(sPath)) return false;

		boolean flag = false;
		File file = new File(sPath);
		if (file.exists() && file.isDirectory()) {
			File[] tmpF = file.listFiles();
			for (File file2 : tmpF) {
				file2.delete();
			}
			file.delete();
			flag = true;
		} else {
			flag = false;
		}

		return flag;
	}

	/**
	 * copyFile
	 * @param srcFile
	 * @param destPath
	 * @return
	 */
	public static boolean copyFile(String srcFile, String destPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(srcFile);
			if (!oldfile.exists()) {// 文件不存在时
				Log.i("mkdirs", "source file not exits : " + srcFile);
				return false;
			}

			File dir = new File(destPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			InputStream inStream = new FileInputStream(srcFile); // 读入原文件
			FileOutputStream fs = new FileOutputStream(destPath + File.separator + oldfile.getName());
			byte[] buffer = new byte[1444];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread; // 字节数 文件大小
				// System.out.println(bytesum);
				fs.write(buffer, 0, byteread);
				fs.flush();
			}
			inStream.close();
			fs.close();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * 复制文件保存
	 * @param srcFile 源文件路径---绝对路径
	 * @param fileName 保存的文件名称
	 * @param destPath 目的路径
	 * @return
	 */
	public static boolean copyFileByName(String srcFile, String fileName, String destPath) {
		File oldfile = new File(srcFile);
		if (!oldfile.exists()) {// 文件不存在时
			Log.i("mkdirs", "source file not exits : " + srcFile);
			return false;
		}

		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			fileInputStream = new FileInputStream(srcFile);
			fileOutputStream = new FileOutputStream(destPath + File.separator + fileName);
			// 一次读取1024字节
			byte[] bytes = new byte[1024];
			while (fileInputStream.read(bytes) != -1) {
				fileOutputStream.write(bytes);
				fileOutputStream.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}

	/**
	 * movie file
	 * @param srcFile
	 * @param destPath
	 */
	public static boolean moveFile(String srcFile, String destPath) {
		if (StringUtils.isNull(srcFile)) return false;

		File file = new File(srcFile);
		if (!file.exists()) return false;

		File dir = new File(destPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// Move file to new directory
		return file.renameTo(new File(dir, file.getName()));
	}

	/**
	 * 用新的内容替换原有File内容
	 * @param aFile
	 * @param newString
	 * @return
	 */
	public static boolean replaceFileContent(String filePath, String newString) {
		if (StringUtils.isNull(filePath)) return false;

		boolean bResult = true;
		try {
			File aFile = new File(filePath);
			if (aFile.exists()) {
				aFile.delete();
			}

			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(aFile), "utf-8");
			// /Writer writer = new FileWriter(aFile);
			writer.write(newString);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			bResult = false;
		}

		return bResult;
	}

	public static boolean replaceFileContent(String filePath, byte[] newString) {
		if (StringUtils.isNull(filePath)) return false;
		boolean bResult = true;
		try {
			File aFile = new File(filePath);
			if (aFile.exists()) {
				aFile.delete();
			}
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(aFile), "utf-8");
			// /Writer writer = new FileWriter(aFile);
			writer.write(new String(newString, "utf-8"));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			bResult = false;
		}
		return bResult;
	}

}
