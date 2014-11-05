package com.pc.imgs.download;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.pc.cache.PcFileSDCache;
import com.pc.utils.StringUtils;
import com.pc.utils.encryption.PcMd5;
import com.pc.utils.file.sdcard.PcSDcardUtil;
import com.pc.utils.imgs.EmImageOperationType;
import com.pc.utils.imgs.ImageUtil;
import com.pc.utils.log.PcLog;

public class PcImageFileUtil {

	// 默认下载图片文件地址
	private static String downPathImageDir = "";

	// MB 单位B
	private static int MB = 1024 * 1024;

	// 剩余空间大于200M才使用缓存
	private static long freeSdSpaceNeededToCache = 100 * MB;

	// 启用文件缓存
	private static boolean fileSDCacheEnable = true;;
	private static PcFileSDCache mPcFileCache;

	/**
	 * 设置下载目录
	 *
	 * @param dir
	 */
	public static void setDownDir(String dir) {
		if (null == dir) {
			dir = "";
		}

		if (null == downPathImageDir) {
			downPathImageDir = "";
		}

		if (StringUtils.equals(downPathImageDir, dir)) {
			return;
		}

		if (StringUtils.isNull(dir)) {
			downPathImageDir = "cache_images";
		} else {
			downPathImageDir = dir;
		}

		initFileCache();
	}

	/**
	 * 下载网络文件到SD卡中,如果SD中存在同名文件将不再下载
	 * 
	 * @param url
	 * @param name
	 * @return
	 */
	public static String downFileToSD(String url, String name) {
		return downFileToSD(url, name, null);
	}

	/**
	 * 下载网络文件到SD卡中,如果SD中存在同名文件将不再下载
	 *
	 * @param url
	 * @param name
	 * @param dir
	 * @return
	 */
	public static String downFileToSD(String url, String name, String dir) {
		InputStream in = null;
		FileOutputStream fileOutputStream = null;
		HttpURLConnection con = null;
		String downFilePath = null;
		File file = null;
		try {
			if (!isCanUseSD()) {
				return null;
			}

			File fileDirectory = null;
			if (StringUtils.isNull(dir)) {
				fileDirectory = getFullImageDownDir();
			} else {
				String sdkPath = PcSDcardUtil.getExternalStorageDirectory();
				if (!dir.startsWith(sdkPath)) {
					fileDirectory = new File(sdkPath + File.separator + dir);
				}
				if (!fileDirectory.isDirectory() || !fileDirectory.exists()) {
					fileDirectory.mkdirs();
				}
			}

			file = new File(fileDirectory, name);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				return file.getPath();
			}

			downFilePath = file.getPath();
			URL mUrl = new URL(url);
			con = (HttpURLConnection) mUrl.openConnection();
			con.connect();
			in = con.getInputStream();
			fileOutputStream = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int temp = 0;
			while ((temp = in.read(b)) != -1) {
				fileOutputStream.write(b, 0, temp);
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
				if (con != null) {
					con.disconnect();
				}

				// 检查文件大小,如果文件为0B说明网络不好没有下载成功，要将建立的空文件删除
				if (file.length() == 0) {
					file.delete();
				}

			} catch (Exception e) {
			}
		}

		return downFilePath;
	}

	/**
	 * 通过文件的网络地址从SD卡中读取图片，如果SD中没有则自动下载并保存
	 * 
	 * @param url 文件的网络地址
	 * @param type 图片的处理类型, 如果设置为原图，则后边参数无效,得到原图
	 * @param width 图片宽
	 * @param height 图片高
	 * @return Bitmap 图片
	 */
	public static Bitmap getBitmapFromSDCache(String url, EmImageOperationType type, int width, int height) {
		return getBitmapFromSDCache(url, type, width, height, "", false);
	}

	/**
	 * 通过文件的网络地址从SD卡中读取图片，如果SD中没有则自动下载并保存
	 * 
	 * @param url 文件的网络地址
	 * @param type 图片的处理类型, 如果设置为原图，则后边参数无效,得到原图
	 * @param width 图片宽
	 * @param height 图片高
	 * @return Bitmap 图片
	  * @param isFileSDCacheEnable 是否启用文件缓存
	 */
	public static Bitmap getBitmapFromSDCache(String url, EmImageOperationType type, int width, int height, boolean isFileSDCacheEnable) {
		return getBitmapFromSDCache(url, type, width, height, "", isFileSDCacheEnable);
	}

	/**
	 * 通过文件的网络地址从SD卡中读取图片，如果SD中没有则自动下载并保存
	 *
	 * @param url
	 * @param type
	 * @param width
	 * @param height
	 * @param dir 保存图片相对路径
	 * @param isFileSDCacheEnable 是否启用文件缓存.启用文件缓存之后会缓存对应目录下的文件
	 * @return
	 */
	public static Bitmap getBitmapFromSDCache(String url, EmImageOperationType type, int width, int height, String dir) {
		return getBitmapFromSDCache(url, type, width, height, dir, false);
	}

	/**
	 * 通过文件的网络地址从SD卡中读取图片，如果SD中没有则自动下载并保存
	 *
	 * @param url
	 * @param type
	 * @param width
	 * @param height
	 * @param dir 保存图片相对路径
	 * @param isFileSDCacheEnable 是否启用文件缓存.启用文件缓存之后会缓存对应目录下的文件
	 * @return
	 */
	public static Bitmap getBitmapFromSDCache(String url, EmImageOperationType type, int width, int height, String dir, boolean isFileSDCacheEnable) {
		Bitmap bitmap = null;
		try {
			if (StringUtils.isNull(url)) {
				return null;
			}

			if (width <= 0 || height <= 0) {
				type = EmImageOperationType.ORIGINALIMG;
			}

			// SD卡不存在 或者剩余空间不足，不缓存到SD卡
			if (!isCanUseSD() || freeSdSpaceNeededToCache < PcSDcardUtil.freeSpaceOnSD()) {
				return getBitmapFormURL(url, type, width, height);
			}

			fileSDCacheEnable = isFileSDCacheEnable;
			setDownDir(dir);

			// 缓存的key,也是文件名
			String key = PcImageDownloadCache.getCacheKey(url, width, height, type);

			// 获取后缀
			String suffix = getSuffixFromNetUrl(url);

			// 缓存的图片文件名
			String fileName = !StringUtils.isNull(suffix) ? key + suffix : key;
			File file = new File(getFullImageDownDir(), fileName);

			// 检查文件缓存中是否存在文件
			if (null != mPcFileCache && null != mPcFileCache.getFileFromCache(fileName)) {
				bitmap = getBitmapFromSD(file, type, width, height);
				if (PcLog.isPrint) {
					PcLog.d(PcImageFileUtil.class.getSimpleName(), "从文件缓存中得到图片:" + key);
				}

				return bitmap;
			}

			// 检查本地目录中是否存在文件
			if (file.exists()) {
				bitmap = getBitmapFromSD(file, type, width, height);
				if (null != bitmap) {
					if (fileSDCacheEnable && null != mPcFileCache) {
						mPcFileCache.addFileToCache(file);
					}

					if (PcLog.isPrint) {
						PcLog.d(PcImageFileUtil.class.getSimpleName(), "从SD中得到图片:" + key);
					}

					return bitmap;
				}
			}

			// 下载文件到SD
			String downFilePath = downFileToSD(url, file.getName(), dir);
			if (downFilePath != null) {
				// 下载成功后存入缓存
				if (fileSDCacheEnable && null != mPcFileCache) {
					mPcFileCache.addFileToCache(file);
				}

				return getBitmapFromSD(file, type, width, height);
			} else {
				return null;
			}
		} catch (Exception e) {
		}

		return bitmap;

	}

	/**
	 * 通过文件的网络地址从SD卡中读取图片
	 * 
	 * @param url 文件的网络地址
	 * @param type 图片的处理类型
	 * @param width 新图片的宽
	 * @param height 新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap getBitmapFromSD(String url, EmImageOperationType type, int width, int height) {
		Bitmap bit = null;
		try {
			// SD卡是否存在
			if (!isCanUseSD()) {
				return null;
			}

			if ((width <= 0 || height <= 0)) {
				type = EmImageOperationType.ORIGINALIMG;
			}

			// 缓存的key，也是文件名
			String key = PcImageDownloadCache.getCacheKey(url, width, height, type);
			// 获取后缀
			String suffix = getSuffixFromNetUrl(url);
			// 缓存的图片文件名
			String fileName = !StringUtils.isNull(suffix) ? key + suffix : key;
			File file = new File(getFullImageDownDir(), fileName);
			if (!file.exists()) {
				return null;
			} else {
				return getBitmapFromSD(file, type, width, height);
			}
		} catch (Exception e) {
		}

		return bit;

	}

	/**
	 * 通过文件的本地地址从SD卡读取图片
	 * 
	 * @param file the file
	 * @param type 图片的处理类型
	 * @param newWidth 新图片的宽
	 * @param newHeight 新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap getBitmapFromSD(File file, EmImageOperationType type, int newWidth, int newHeight) {
		Bitmap bit = null;
		try {
			// SD卡是否存在
			if (!isCanUseSD()) {
				return null;
			}

			if ((newWidth <= 0 || newHeight <= 0)) {
				type = EmImageOperationType.ORIGINALIMG;
			}

			// 文件不存在
			if (!file.exists()) {
				return null;
			}

			// 文件存在
			switch (type) {
				case CUTIMG:
					bit = ImageUtil.cutImg(file, newWidth, newHeight);
					break;

				case SCALEIMG:
					bit = ImageUtil.scaleImg(file, newWidth, newHeight);
					break;

				case ORIGINALIMG:
				default:
					bit = ImageUtil.decodeFile(file);
					break;
			}
		} catch (Exception e) {
		}

		return bit;
	}

	/**
	 * 通过文件的本地地址从SD卡读取图片
	 * 
	 * @param file
	 * @return Bitmap 图片
	 */
	public static Bitmap getBitmapFromSD(File file) {
		try {
			// SD卡是否存在
			if (!isCanUseSD()) {
				return null;
			}

			// 文件不存在
			if (!file.exists()) {
				return null;
			}

			// 文件存在
			return ImageUtil.decodeFile(file);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将图片的byte[]写入本地文件
	 * 
	 * @param imgByte 图片的byte[]
	 * @param fileName 文件名称，需要包含后缀，如.jpg
	 * @param type 图片的处理类型
	 * @param newWidth 新图片的宽
	 * @param newHeight 新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap getBitmapFormByte(byte[] imgByte, String fileName, EmImageOperationType type, int newWidth, int newHeight) {
		FileOutputStream fos = null;
		DataInputStream dis = null;
		ByteArrayInputStream bis = null;
		Bitmap b = null;
		File file = null;
		try {
			if (imgByte != null) {
				String path = getFullImageDownDir().getAbsolutePath();
				file = new File(path + fileName);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if (!file.exists()) {
					file.createNewFile();
				}
				fos = new FileOutputStream(file);
				int readLength = 0;
				bis = new ByteArrayInputStream(imgByte);
				dis = new DataInputStream(bis);
				byte[] buffer = new byte[1024];

				while ((readLength = dis.read(buffer)) != -1) {
					fos.write(buffer, 0, readLength);
					try {
						Thread.sleep(500);
					} catch (Exception e) {
					}
				}
				fos.flush();

				b = getBitmapFromSD(file, type, newWidth, newHeight);
			}
		} catch (Exception e) {
		} finally {
			try {
				if (dis != null) {
					dis.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
			}
		}

		return b;
	}

	/**
	 * 根据URL从互连网获取图片
	 * 
	 * @param url 要下载文件的网络地址
	 * @param type 图片的处理类型
	 * @param newWidth 新图片的宽
	 * @param newHeight 新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap getBitmapFormURL(String url, EmImageOperationType type, int newWidth, int newHeight) {
		Bitmap bit = null;
		try {
			if (newWidth <= 0 || newHeight <= 0) {
				type = EmImageOperationType.ORIGINALIMG;
			}

			switch (type) {
				case CUTIMG:
					bit = ImageUtil.getBitmapFormURL(url, true, false, newWidth, newHeight);
					break;

				case SCALEIMG:
					bit = ImageUtil.getBitmapFormURL(url, false, true, newWidth, newHeight);
					break;

				case ORIGINALIMG:
				default:
					bit = ImageUtil.getBitmapFormURL(url, false, false, newWidth, newHeight);
					break;
			}
		} catch (Exception e) {
			if (PcLog.isPrint) {
				PcLog.d(PcImageFileUtil.class.getSimpleName(), "下载图片异常：" + e.getMessage());
			}
		}

		return bit;
	}

	/**
	 * 获取src中的图片资源.
	 * @param src 图片的src路径，如（“image/arrow.png”）
	 * @return Bitmap 图片
	 */
	public static Bitmap getBitmapFormSrc(String src) {
		Bitmap bit = null;
		try {
			bit = BitmapFactory.decodeStream(PcImageFileUtil.class.getResourceAsStream(src));
		} catch (Exception e) {
			if (PcLog.isPrint) {
				PcLog.d(PcImageFileUtil.class.getSimpleName(), "获取图片异常：" + e.getMessage());
			}
		}

		return bit;
	}

	/**
	 * 获取网络文件的大小
	 * 
	 * @param Url 图片的网络路径
	 * @return int 网络文件的大小
	 */
	public static int getContentLengthFormUrl(String Url) {
		int mContentLength = 0;
		try {
			URL url = new URL(Url);
			HttpURLConnection mHttpURLConnection = (HttpURLConnection) url.openConnection();
			mHttpURLConnection.setConnectTimeout(5 * 1000);
			mHttpURLConnection.setRequestMethod("GET");
			mHttpURLConnection
					.setRequestProperty(
							"Accept",
							"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
			mHttpURLConnection.setRequestProperty("Referer", Url);
			mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
			mHttpURLConnection
					.setRequestProperty("User-Agent",
							"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			mHttpURLConnection.connect();
			if (mHttpURLConnection.getResponseCode() == 200) {
				// 根据响应获取文件大小
				mContentLength = mHttpURLConnection.getContentLength();
			}
		} catch (Exception e) {
			if (PcLog.isPrint) {
				PcLog.d(PcImageFileUtil.class.getSimpleName(), "获取长度异常：" + e.getMessage());
			}
		}

		return mContentLength;
	}

	/**
	 * 获取文件名，通过网络获取
	 * 
	 * @param url 文件地址
	 * @return 文件名
	 */
	public static String getRealFileNameFromUrl(String url) {
		String name = null;
		try {
			if (StringUtils.isNull(url)) {
				return name;
			}

			URL mUrl = new URL(url);
			HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
			mHttpURLConnection.setConnectTimeout(5 * 1000);
			mHttpURLConnection.setRequestMethod("GET");
			mHttpURLConnection
					.setRequestProperty(
							"Accept",
							"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
			mHttpURLConnection.setRequestProperty("Referer", url);
			mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
			mHttpURLConnection
					.setRequestProperty("User-Agent",
							"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			mHttpURLConnection.connect();
			if (mHttpURLConnection.getResponseCode() == 200) {
				for (int i = 0;; i++) {
					String mine = mHttpURLConnection.getHeaderField(i);
					if (mine == null) {
						break;
					}

					String header = mHttpURLConnection.getHeaderFieldKey(i);
					if (header != null && "content-disposition".equals(header.toLowerCase())) {
						Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
						if (m.find()) return m.group(1).replace("\"", "");
					}
				}
			}
		} catch (Exception e) {
			if (PcLog.isPrint) {
				PcLog.d(PcImageFileUtil.class.getSimpleName(), " 获取文件名：" + e.getMessage());
			} else {
				e.printStackTrace();
			}
		}
		return name;
	}

	/**
	 * 获取文件名，外链模式和通过网络获取
	 * 
	 * @param url 文件地址
	 * @return 文件名
	 */
	public static String getFileNameFromUrl(String url, HttpResponse response) {
		if (StringUtils.isNull(url)) {
			return "";
		}

		String name = "";
		try {
			String suffix = "";
			// 获取后缀
			if (url.lastIndexOf(".") != -1) {
				suffix = url.substring(url.lastIndexOf("."));
				if (suffix.indexOf("/") != -1 || suffix.indexOf("?") != -1 || suffix.indexOf("&") != -1) {
					suffix = "";
				}
			}

			if (StringUtils.isNull(suffix)) {
				// 获取文件名
				String fileName = "unknow.tmp";
				Header[] headers = response.getHeaders("content-disposition");
				for (int i = 0; i < headers.length; i++) {
					Matcher m = Pattern.compile(".*filename=(.*)").matcher(headers[i].getValue());
					if (m.find()) {
						fileName = m.group(1).replace("\"", "");
					}
				}
				if (fileName != null && fileName.lastIndexOf(".") != -1) {
					suffix = fileName.substring(fileName.lastIndexOf("."));
				}
			}
			name = PcMd5.MD5(url) + suffix;
		} catch (Exception e) {
		}

		return name;
	}

	/**
	 * 获取文件名，外链模式和通过网络获取
	 * 
	 * @param url 文件地址
	 * @return 文件名
	 */
	public static String getFileNameFromUrl(String url) {
		if (StringUtils.isNull(url)) {
			return null;
		}
		String name = null;
		try {
			String suffix = null;
			// 获取后缀
			if (url.lastIndexOf(".") != -1) {
				suffix = url.substring(url.lastIndexOf("."));
				if (suffix.indexOf("/") != -1) {
					suffix = null;
				}
			}
			if (suffix == null) {
				// 获取后缀
				suffix = getSuffixFromNetUrl(url);
			}
			name = PcMd5.MD5(url) + suffix;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 获取文件后缀
	 * 
	 * @param url 文件地址
	 * @return 文件后缀
	 */
	public static String getSuffixFromNetUrl(String url) {
		if (StringUtils.isNull(url)) {
			return "";
		}

		String suffix = ".tmp";
		try {
			suffix = parseSuffix(url);
			if (!StringUtils.isNull(suffix)) {
				return new StringBuffer().append(".").append(suffix).toString();
			}

			suffix = parseSuffix2(url);
			if (!StringUtils.isNull(suffix)) {
				return new StringBuffer().append(".").append(suffix).toString();
			}

			// 获取后缀
			if (url.lastIndexOf(".") != -1) {
				suffix = url.substring(url.lastIndexOf("."));
				if (suffix.indexOf("/") != -1 || suffix.indexOf("?") != -1 || suffix.indexOf("&") != -1) {
					suffix = "";
				}
			}

			if (StringUtils.isNull(suffix)) {
				// 获取文件名
				String fileName = getRealFileNameFromUrl(url);
				if (fileName != null && fileName.lastIndexOf(".") != -1) {
					suffix = fileName.substring(fileName.lastIndexOf("."));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return suffix;
	}

	/** 
	 * 获取链接的后缀名 
	 * 
	 * @return 
	 */
	public static String parseSuffix(String url) {
		Pattern pattern = Pattern.compile("\\S*[?]\\S*");
		Matcher matcher = pattern.matcher(url);

		String[] spUrl = url.toString().split("/");
		int len = spUrl.length;
		String endUrl = spUrl[len - 1];

		try {
			if (matcher.find()) {
				String[] spEndUrl = endUrl.split("\\?");
				return spEndUrl[0].split("\\.")[1];
			}

			return endUrl.split("\\.")[1];
		} catch (Exception e) {
			return "";
		}
	}

	/** 
	 * 获取链接的后缀名 
	 * 
	 * @return 
	 */
	public static String parseSuffix2(String strUrl) {
		BufferedInputStream bis = null;
		HttpURLConnection urlConnection = null;
		URL url = null;
		String suffix = "";
		try {
			url = new URL(strUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.connect();

			bis = new BufferedInputStream(urlConnection.getInputStream());
			suffix = HttpURLConnection.guessContentTypeFromStream(bis);
			return suffix.split("\\/")[1];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return suffix;
	}

	/**
	 * 从sd卡中的文件读取到byte[]
	 * 
	 * @param path sd卡中文件路径
	 * @return byte[]
	 */
	public static byte[] getByteArrayFromSD(String path) {
		byte[] bytes = null;
		ByteArrayOutputStream out = null;
		try {
			File file = new File(path);
			// SD卡是否存在
			if (!isCanUseSD()) {
				return null;
			}
			// 文件是否存在
			if (!file.exists()) {
				return null;
			}

			long fileSize = file.length();
			if (fileSize > Integer.MAX_VALUE) {
				return null;
			}

			FileInputStream in = new FileInputStream(path);
			out = new ByteArrayOutputStream(1024);
			byte[] buffer = new byte[1024];
			int size = 0;
			while ((size = in.read(buffer)) != -1) {
				out.write(buffer, 0, size);
			}
			in.close();
			bytes = out.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
			}
		}

		return bytes;
	}

	/**
	 * 将byte数组写入文件
	 * 
	 * @param path the path
	 * @param content the content
	 * @param create the create
	 */
	public static void writeByteArrayToSD(String path, byte[] content, boolean create) {

		FileOutputStream fos = null;
		try {
			File file = new File(path);
			if (!isCanUseSD()) {
				return;
			}

			// 文件是否存在
			if (!file.exists()) {
				if (create) {
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
						file.createNewFile();
					}
				} else {
					return;
				}
			}
			fos = new FileOutputStream(path);
			fos.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * SD卡是否能用
	 * 
	 * @return true 可用,false不可用
	 */
	public static boolean isCanUseSD() {
		return PcSDcardUtil.isCanUseSD();
	}

	/**
	 * 获得当前下载的地址.
	 * @return 下载的地址（默认SD卡download）
	 */
	public static String getDownPathImageDir() {
		return downPathImageDir;
	}

	/**
	 * 设置图片文件的下载保存路径（默认SD卡download/cache_images）.
	 * @param downPathImageDir 图片文件的下载保存路径
	 */
	public static void setDownPathImageDir(String downPathImageDir) {
		PcImageFileUtil.downPathImageDir = downPathImageDir;
	}

	/**
	 * 获取默认的图片保存全路径.
	 * @return 完成的存储目录
	 */
	public static String getFullImageDownPathDir() {
		String pathDir = null;
		try {
			if (!isCanUseSD()) {
				return null;
			}

			// 初始化图片保存路径
			pathDir = getFullImageDownDir().getPath();
		} catch (Exception e) {
		}

		return pathDir;
	}

	/**
	 * 下载绝对路径
	 *
	 * @return
	 */
	public static File getFullImageDownDir() {
		File path = Environment.getExternalStorageDirectory();
		File fileDirectory = new File(path.getAbsolutePath() + File.separator + downPathImageDir);
		if (!fileDirectory.isDirectory() || !fileDirectory.exists()) {
			fileDirectory.mkdirs();
		}

		return fileDirectory;
	}

	public static boolean isInitFileCache() {
		if (null == mPcFileCache) {
			return false;
		}

		if (!fileSDCacheEnable) {
			return false;
		}

		return mPcFileCache.initFileCache();
	}

	public static void initFileCache() {
		if (mPcFileCache != null) {
			mPcFileCache.freeAllCacheFiles();
		}

		mPcFileCache = new PcFileSDCache(0, downPathImageDir);
		if (fileSDCacheEnable) {
			mPcFileCache.initFileCache();
		}
	}

	public static void initFileCache(String downDir) {
		if (StringUtils.equals(downDir, downPathImageDir)) {
			return;
		}

		if (mPcFileCache != null) {
			mPcFileCache.freeAllCacheFiles();
		}

		mPcFileCache = new PcFileSDCache(0, downPathImageDir);
		if (fileSDCacheEnable) {
			mPcFileCache.initFileCache();
		}
	}

	public static void freeAllCacheFiles() {
		if (null == mPcFileCache) {
			return;
		}

		mPcFileCache.freeAllCacheFiles();
	}

	public static boolean isFreeCacheFiles() {
		if (null == mPcFileCache) {
			return false;
		}

		return mPcFileCache.freeCacheFiles();
	}

	/**
	 * 剩余空间大于多少B才使用缓存
	 * 
	 * @return
	 * @throws
	 */
	public static long getFreeSdSpaceNeededToCache() {
		return freeSdSpaceNeededToCache;
	}

	/**
	 * 剩余空间大于多少B才使用缓存
	 * 
	 * @param freeSdSpaceNeededToCache
	 * @throws
	 */
	public static void setFreeSdSpaceNeededToCache(int freeSdSpaceNeededToCache) {
		PcImageFileUtil.freeSdSpaceNeededToCache = freeSdSpaceNeededToCache;
	}

	/**
	 * 删除所有缓存文件
	 */
	public static boolean removeAllFileCache() {
		try {
			if (!isCanUseSD()) {
				return false;
			}

			File fileDirectory = getFullImageDownDir();
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

	/**
	 * 读取Assets目录的文件内容
	 * @param context
	 * @param name
	 * @return
	 * @throws
	 */
	public static String readAssetsByName(Context context, String name, String encoding) {
		String text = null;
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		try {
			inputReader = new InputStreamReader(context.getAssets().open(name));
			bufReader = new BufferedReader(inputReader);
			String line = null;
			StringBuffer buffer = new StringBuffer();
			while ((line = bufReader.readLine()) != null) {
				buffer.append(line);
			}
			text = new String(buffer.toString().getBytes(), encoding);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufReader != null) {
					bufReader.close();
				}
				if (inputReader != null) {
					inputReader.close();
				}
			} catch (Exception e) {
			}
		}
		return text;
	}

	/**
	 * 读取Raw目录的文件内容
	 * @param context
	 * @param id
	 * @return
	 * @throws
	 */
	public static String readRawByName(Context context, int id, String encoding) {
		String text = null;
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		try {
			inputReader = new InputStreamReader(context.getResources().openRawResource(id));
			bufReader = new BufferedReader(inputReader);
			String line = null;
			StringBuffer buffer = new StringBuffer();
			while ((line = bufReader.readLine()) != null) {
				buffer.append(line);
			}
			text = new String(buffer.toString().getBytes(), encoding);
		} catch (Exception e) {
		} finally {
			try {
				if (bufReader != null) {
					bufReader.close();
				}
				if (inputReader != null) {
					inputReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return text;
	}

}
