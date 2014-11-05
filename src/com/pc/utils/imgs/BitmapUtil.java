package com.pc.utils.imgs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.net.Uri;

import com.pc.utils.StringUtils;
import com.pc.utils.android.sys.TerminalUtils;

/**
 * 加载大图片工具类：解决android加载大图片时报OOM异常 解决原理：先设置缩放选项，再读取缩放的图片数据到内存，规避了内存引起的OOM
 * @author jiang
 * @date 2012-3-5
 */
public class BitmapUtil {

	public static final int UNCONSTRAINED = -1;

	/*
	 * 获得设置信息
	 */
	public static Options getOptionsByPath(String path) {
		if (StringUtils.isNull(path)) {
			return null;
		}

		Options options = new Options();
		options.inJustDecodeBounds = true;// 只描边，不读取数据
		BitmapFactory.decodeFile(path, options);

		return options;
	}

	/**
	 * 通过uri获取options
	 * @param context
	 * @param uri
	 * @return options
	 */
	public static Options getOptionsByUri(Context context, Uri uri) {
		if (uri == null) {
			return null;
		}
		ContentResolver cr = context.getContentResolver();

		InputStream in = null;
		Options options = null;
		try {
			in = cr.openInputStream(uri);
			options = new Options();
			options.inJustDecodeBounds = true;// 只描边，不读取数据
			BitmapFactory.decodeStream(in, null, options);
		} catch (FileNotFoundException e) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}

		}

		return options;
	}

	/**
	 * 获得图像
	 * @param path 文件的绝对路径
	 * @param options
	 * @param a 当前的Activity
	 * @return bitmap
	 * @throws FileNotFoundException
	 */
	public static Bitmap getBitmapByPath(Context context, String path, Options options) {
		if (null == context || StringUtils.isNull(path) || options == null) {
			return null;
		}
		File file = new File(path);
		if (!file.exists()) {
			try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e) {
			}
		}
		FileInputStream in = null;
		Bitmap b = null;
		try {
			in = new FileInputStream(file);
			if (options != null) {
				int wh[] = TerminalUtils.terminalWH(context);
				int w = wh[0];
				int h = wh[1];
				int maxSize = w > h ? w : h;
				int inSimpleSize = computeSampleSize(options, maxSize, w * h);
				options.inSampleSize = inSimpleSize; // 设置缩放比例
				options.inJustDecodeBounds = false;
			}
			b = BitmapFactory.decodeStream(in, null, options);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
		return b;
	}

	/**
	 * 根据所需要的图片分辨率 获取压缩比例
	 * @param options
	 * @param reqWidth 要求的宽度
	 * @param reqHeight 要求的高度
	 * @return 图片的缩放比
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	public static Bitmap getBitmapByPath(String path, Options options, int width, int height) {
		if (StringUtils.isNull(path)) {
			return null;
		}

		File file = new File(path);
		if (!file.exists()) {
			try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e) {
			}
		}
		FileInputStream in = null;
		Bitmap b = null;
		try {
			in = new FileInputStream(file);
			if (options != null) {
				int maxSize = width > height ? width : height;
				int inSimpleSize = computeSampleSize(options, maxSize, width * height);
				options.inSampleSize = inSimpleSize; // 设置缩放比例
				options.inJustDecodeBounds = false;
			}
			b = BitmapFactory.decodeStream(in, null, options);
		} catch (FileNotFoundException e1) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	/**
	 * 根据uri获取对应图片的bitmap
	 * @param context
	 * @param uri
	 * @param options
	 * @param a
	 * @return
	 */
	public static Bitmap getBitmapByUri(Context context, Uri uri, Options options) {
		if (uri == null || context == null) {
			return null;
		}
		ContentResolver cr = context.getContentResolver();

		InputStream in = null;
		Bitmap b = null;
		try {
			in = cr.openInputStream(uri);
			if (options != null) {
				int wh[] = TerminalUtils.terminalWH(context);
				int w = wh[0];
				int h = wh[1];
				int maxSize = w > h ? w : h;
				int inSimpleSize = computeSampleSize(options, maxSize, w * h);
				options.inSampleSize = inSimpleSize; // 设置缩放比例
				options.inJustDecodeBounds = false;
				b = BitmapFactory.decodeStream(in, null, options);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}

		return b;
	}

	private static Rect getScreenRegion(int width, int height) {
		return new Rect(0, 0, width, height);
	}

	/**
	 * 获取需要进行缩放的比例，即options.inSampleSize
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		// ceil 功 能： 返回大于或者等于指定表达式的最小整数
		// min返回给定参数表中的最小值
		// floor功 能： 返回小于或者等于指定表达式的最大整数
		int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
				Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == UNCONSTRAINED) && (minSideLength == UNCONSTRAINED)) {
			return 1;
		} else if (minSideLength == UNCONSTRAINED) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 保存之后刷新一下 系统相册
	 */
	public static void allScan(Context context) {
		if (null == context) {
			return;
		}

		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
				+ android.os.Environment.getExternalStorageDirectory())));
	}

	/**
	 * 获取缩放比
	 * @param w
	 * @param h
	 * @return
	 */
	public static int getSize(int w, int h, Activity a) {
		Rect r = getScreenRegion(TerminalUtils.terminalW(a), TerminalUtils.terminalH(a));
		int screenW = r.width();
		int screenH = 2048;
		// int screenW =
		// KTruetouchAndroidApplication.mOurApplication.getWidthPixels();
		// int screenH = 2048;

		float size = 1f;
		if (w > h) {
			if (w > screenW) {
				size = (float) w / screenW;
			}
		} else {
			if (h > screenH) {
				size = (float) h / screenH;
			}
		}
		return Math.round(size);
	}

}
