package com.pc.cache;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import android.os.Environment;

import com.pc.utils.StringUtils;
import com.pc.utils.file.FileLastModifSort;
import com.pc.utils.file.sdcard.PcSDcardUtil;

/**
 * 文件SD本地缓存管理
 * 
 * PcFileSDCache只缓存最近使用的部分File,而非全部
 * PcFileSDCache缓存中没有File,并不代表本地目录就没有对应的文件,或许是PcFileSDCache已达到缓存最大值,导致缓存中的File被释放。
 * 所以PcFileSDCache只是管理最近频繁使用的File
 * 
 * @author chenj
 * @date 2014-7-17
 */
public class PcFileSDCache {

	public int maxCacheSize = 10 * 1024 * 1024;

	// 当前缓存大小
	public int cacheSize = 0;

	// 文件缓存(文件名，文件)
	private final HashMap<String, File> fileCache;

	// 锁对象
	public static final ReentrantLock lock = new ReentrantLock();

	// 缓存相对路径
	private String cacheDir;

	public PcFileSDCache(int cacheSize, String cacheDir) {
		this.cacheDir = cacheDir;
		setMaxCacheSize(cacheSize);

		fileCache = new HashMap<String, File>();
		if (StringUtils.isNull(cacheDir)) {
			this.cacheDir = "cache_images";
		}
	}

	/**
	 * 初始化缓存
	 */
	public boolean initFileCache() {
		try {
			cacheSize = 0;
			if (!PcSDcardUtil.isCanUseSD()) {
				return false;
			}

			File[] files = getCacheDirFile4AbsolutePath().listFiles();
			if (files == null) {
				return true;
			}

			for (int i = 0; i < files.length; i++) {
				addFileToCache(files[i]);
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * 设置缓存空间大小
	 * @param cacheSize
	 */
	public void setMaxCacheSize(int cacheSize) {
		if (cacheSize <= 0) {
			maxCacheSize = 10 * 1024 * 1024;
			return;
		}

		maxCacheSize = cacheSize;
	}

	/**
	 * 缓存文件夹的大小
	 * 
	 * @return
	 * @throws
	 */
	public int getCacheSize() {
		return cacheSize;
	}

	/** 
	 * 缓存文件存储的相对路径
	 * 
	 * @return the cacheDir
	 */
	public String getCacheDir() {
		return cacheDir;
	}

	/**
	 * 缓存文件存储的绝对路径
	 *
	 * @return
	 */
	public String getCacheDir4AbsolutePath() {
		File path = Environment.getExternalStorageDirectory();
		File fileDirectory = new File(path.getAbsolutePath() + File.separator + cacheDir);
		if (!fileDirectory.isDirectory() || !fileDirectory.exists()) {
			fileDirectory.mkdirs();
		}

		return fileDirectory.getAbsolutePath();
	}

	/**
	 * 缓存文件存储的绝对路径
	 *
	 * @return
	 */
	public File getCacheDirFile4AbsolutePath() {
		File path = Environment.getExternalStorageDirectory();
		File fileDirectory = new File(path.getAbsolutePath() + File.separator + cacheDir);
		if (!fileDirectory.isDirectory() || !fileDirectory.exists()) {
			fileDirectory.mkdirs();
		}

		return fileDirectory;
	}

	/**
	 * 从缓存中获取这个File
	 * 
	 * @param name 文件名
	 * @return the file from mem cache
	 */
	public File getFileFromCache(String name) {
		return fileCache.get(name);
	}

	/**
	 * 增加一个File到缓存
	 *
	 * @param file
	 */
	public void addFileToCache(File file) {
		if (null == file) {
			return;
		}

		addFileToCache(file.getName(), file);
	}

	/**
	 * 增加一个File到缓存
	 * 
	 * @param name 文件名
	 * @param file
	 */
	public void addFileToCache(String name, File file) {
		if (StringUtils.isNull(name) || null == file) {
			return;
		}

		try {
			lock.lock();

			if (getFileFromCache(name) == null && file != null) {
				fileCache.put(name, file);
				cacheSize += file.length();
			}

			// 当前大小大于预定缓存空间, 释放部分文件
			if (cacheSize > maxCacheSize) {
				freeCacheFiles();
			}
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 从缓存删除
	 * 
	 * @param name 文件名
	 */
	public void removeFileFromCache(String name) {
		try {
			lock.lock();
			if (getFileFromCache(name) != null) {
				fileCache.remove(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 释放部分文件， 当文件总大小大于规定的Cache
	 * 
	 * maxCacheSize或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定 那么删除40%最近没有被使用的文件
	 */
	public boolean freeCacheFiles() {
		return freeCacheFiles(false);
	}

	/**
	 * 释放所有缓存
	 */
	public void freeAllCacheFiles() {
		try {
			lock.lock();
			if (null != fileCache) {
				fileCache.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 释放部分文件
	 *
	 * @param removeFromSDCache 从目录中删除文件
	 * @return
	 */
	public boolean freeCacheFiles(boolean removeFromSDCache) {
		try {
			if (!PcSDcardUtil.isCanUseSD()) {
				return false;
			}

			File[] files = getCacheDirFile4AbsolutePath().listFiles();
			if (files == null) return true;

			int removeFactor = (int) ((0.4 * files.length) + 1);

			Arrays.sort(files, new FileLastModifSort());

			for (int i = 0; i < removeFactor; i++) {
				cacheSize -= files[i].length();
				// 从目录中删除文件
				if (removeFromSDCache) {
					files[i].delete();
				}

				removeFileFromCache(files[i].getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 从目录清空缓存文件
	 */
	public void removeAllFileFromSDCache() {
		removeAllFileSDCache();

		if (null != fileCache) {
			fileCache.clear();
		}
	}

	/**
	 * 从目录删除所有文件
	 */
	private boolean removeAllFileSDCache() {
		try {
			if (!PcSDcardUtil.isCanUseSD()) {
				return false;
			}

			File[] files = getCacheDirFile4AbsolutePath().listFiles();
			if (files == null) return true;

			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
