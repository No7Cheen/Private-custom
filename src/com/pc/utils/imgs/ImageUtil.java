/**
 * @(#)ImageUtil.java 2014-1-16 Copyright 2014 it.kedacom.com, Inc. All rights
 *                    reserved.
 */

package com.pc.utils.imgs;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;

import com.pc.utils.StringUtils;
import com.pc.utils.android.sys.EmTerminalConstants;
import com.pc.utils.file.sdcard.PcSDcardUtil;

/**
 * @author chenjian
 * @date 2014-1-16
 */

public class ImageUtil {

	public final static int MAX_WIDTH = 512;
	public final static int MAX_HEIGHT = 384;
	public final static int SDK_4_POINT_0 = 14;
	public final static int OPTIONS_INTEMPSTORAGE_3 = 1024 * 1024 * 3;
	public final static int OPTIONS_INTEMPSTORAGE_5 = 1024 * 1024 * 5;

	/**
	 * drawable To Bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable == null) return null;

		if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) return null;

		try {
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
					: Bitmap.Config.RGB_565);

			Canvas canvas = new Canvas(bitmap);
			// canvas.setBitmap(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			drawable.draw(canvas);

			return bitmap;
		} catch (OutOfMemoryError e) {
			// e.printStackTrace();
			Log.e("Bitmap", "drawableToBitmap", e);
			return null;
		}
	}

	/**
	 * Bitmap to TransitionDrawable
	 * @param bitmap 要转化的Bitmap对象 imageView.setImageDrawable(td);
	 *            td.startTransition(200);
	 * @return Drawable 转化完成的Drawable对象
	 */
	public static TransitionDrawable bitmapToTransitionDrawable(Bitmap bitmap) {
		TransitionDrawable mBitmapDrawable = null;
		try {
			if (bitmap == null) {
				return null;
			}
			mBitmapDrawable = new TransitionDrawable(new Drawable[] {
					new ColorDrawable(android.R.color.transparent), new BitmapDrawable(bitmap)
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mBitmapDrawable;
	}

	/**
	 * Drawable to TransitionDrawable
	 * @param drawable 要转化的Drawable对象 imageView.setImageDrawable(td);
	 *            td.startTransition(200);
	 * @return Drawable 转化完成的Drawable对象
	 */
	public static TransitionDrawable drawableToTransitionDrawable(Drawable drawable) {
		TransitionDrawable mBitmapDrawable = null;
		try {
			if (drawable == null) {
				return null;
			}
			mBitmapDrawable = new TransitionDrawable(new Drawable[] {
					new ColorDrawable(android.R.color.transparent), drawable
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mBitmapDrawable;
	}

	/**
	 * 放大缩小图片
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		if (bitmap == null) {
			return bitmap;
		}
		Bitmap newbmp = null;
		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidht = ((float) w / width);
			float scaleHeight = ((float) h / height);
			matrix.postScale(scaleWidht, scaleHeight);
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		} catch (Exception e) {
		} finally {
			if (newbmp != bitmap) {
				bitmap.recycle();
			}
		}

		return newbmp;
	}

	/**
	 * 描述：缩放图片.压缩
	 * @param file File对象
	 * @param newWidth 新图片的宽
	 * @param newHeight 新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap scaleImg(File file, int newWidth, int newHeight) {
		Bitmap resizeBmp = null;
		if (newWidth <= 0 || newHeight <= 0) {
			throw new IllegalArgumentException("缩放图片的宽高设置不能小于0");
		}
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true; // 设置为true,decodeFile先不创建内存
										// 只获取一些解码边界信息即图片大小信息
		BitmapFactory.decodeFile(file.getPath(), opts);
		// inSampleSize=2表示图片宽高都为原来的二分之一，即图片为原来的四分之一
		// 缩放可以将像素点打薄
		// 获取图片的原始宽度高度
		int srcWidth = opts.outWidth;
		int srcHeight = opts.outHeight;

		int destWidth = srcWidth;
		int destHeight = srcHeight;

		// 缩放的比例
		float scale = 0;
		// 计算缩放比例
		float scaleWidth = (float) newWidth / srcWidth;
		float scaleHeight = (float) newHeight / srcHeight;
		if (scaleWidth > scaleHeight) {
			scale = scaleWidth;
		} else {
			scale = scaleHeight;
		}
		if (scale != 0) {
			destWidth = (int) (destWidth / scale);
			destHeight = (int) (destHeight / scale);
		}

		// 默认为ARGB_8888.
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		// 以下两个字段需一起使用：
		// 产生的位图将得到像素空间，如果系统gc，那么将被清空。当像素再次被访问，如果Bitmap已经decode，那么将被自动重新解码
		opts.inPurgeable = true;
		// 位图可以共享一个参考输入数据(inputstream、阵列等)
		opts.inInputShareable = true;

		// 缩放的比例，缩放是很难按准备的比例进行缩放的，通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
		if (scale > 1) {
			// 缩小
			opts.inSampleSize = (int) scale;
		} else {
			// 放大
			opts.inSampleSize = 1;
		}

		// 设置大小
		opts.outHeight = destHeight;
		opts.outWidth = destWidth;
		// 创建内存
		opts.inJustDecodeBounds = false;
		// 使图片不抖动
		opts.inDither = false;
		// if(D) Log.d(TAG, "将缩放图片:"+file.getPath());
		resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
		// 缩小或者放大
		if (resizeBmp != null && scale != 1) {
			resizeBmp = zoomBitmapByScale(resizeBmp, scale);
		}
		// if(D) Log.d(TAG, "缩放图片结果:"+resizeBmp);
		return resizeBmp;
	}

	/**
	 * 缩放图片,不压缩的缩放
	 * @param bitmap the bitmap
	 * @param newWidth 新图片的宽
	 * @param newHeight 新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap scaleImg(Bitmap bitmap, int newWidth, int newHeight) {
		Bitmap resizeBmp = null;
		if (bitmap == null) {
			return null;
		}

		if (newWidth <= 0 || newHeight <= 0) {
			throw new IllegalArgumentException("缩放图片的宽高设置不能小于0");
		}

		// 获得图片的宽高
		int srcWidth = bitmap.getWidth();
		int srcHeight = bitmap.getHeight();

		if (srcWidth <= 0 || srcHeight <= 0) {
			return null;
		}
		// 缩放的比例
		float scale = 0;
		// 计算缩放比例
		float scaleWidth = (float) newWidth / srcWidth;
		float scaleHeight = (float) newHeight / srcHeight;
		if (scaleWidth > scaleHeight) {
			scale = scaleWidth;
		} else {
			scale = scaleHeight;
		}
		// 缩小或者放大
		if (bitmap != null && scale != 1) {
			resizeBmp = zoomBitmapByScale(bitmap, scale);
		}
		return resizeBmp;
	}

	/**
	 * 放大缩小图片，以宽度为准，高度等比例放大缩小
	 * @param bitmap
	 * @param w
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w) {
		if (bitmap == null) {
			return bitmap;
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		float scale = ((float) w / width);

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

		return newbmp;
	}

	/**
	 * 放大缩小图片，以高度为准，宽度等比例放大缩小
	 * @Description
	 * @param bitmap
	 * @param h
	 * @return
	 */
	public static Bitmap zoomBitmapByH(Bitmap bitmap, int h) {
		if (bitmap == null) {
			return bitmap;
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scale = ((float) h / height);
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

		return newbmp;
	}

	/**
	 * 放大缩小图片
	 * @Description
	 * @param bitmap
	 * @param scale 比例
	 * @return
	 */
	public static Bitmap zoomBitmapByScale(Bitmap bitmap, float scale) {
		if (bitmap == null) {
			return bitmap;
		}

		Bitmap newbmp = null;
		try {
			if (scale < 0) {
				scale = 1;
			}

			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix(); // android.graphics.Matrix
			matrix.postScale(scale, scale);
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		} catch (Exception e) {
		} finally {
			if (newbmp != bitmap) {
				bitmap.recycle();
			}
		}

		return newbmp;
	}

	/**
	 * 创建一个圆形图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createCircleBimap(Bitmap bitmap) {
		if (bitmap == null) {
			return bitmap;
		}

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		float radius = 0.0f;
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);

		if (h < w) {
			radius = (float) h / 2;
		} else {
			radius = (float) w / 2;
		}

		BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		paint.setAntiAlias(true);
		paint.setShader(shader);
		canvas.drawRoundRect(rectF, radius, radius, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		if (!bitmap.isRecycled()) {
			bitmap.recycle();
		}

		return output;
	}

	/**
	 * 创建一个圆形图片
	 * @param id
	 * @return
	 */
	public static Bitmap createCircleBimap(Context context, int id) {
		if (null == context) return null;

		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
		if (bitmap == null) {
			return bitmap;
		}

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		float radius = 0.0f;
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);

		if (h < w) {
			radius = (float) h / 2;
		} else {
			radius = (float) w / 2;
		}

		BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		paint.setAntiAlias(true);
		paint.setShader(shader);
		canvas.drawRoundRect(rectF, radius, radius, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		if (!bitmap.isRecycled()) {
			bitmap.recycle();
		}

		return output;
	}

	/**
	 * 获得圆角图片
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap createRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		if (roundPx <= 0 || bitmap == null) {
			return bitmap;
		}

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		paint.setAntiAlias(true);
		paint.setShader(shader);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		if (!bitmap.isRecycled()) {
			bitmap.recycle();
		}

		return output;
	}

	/**
	 * Bitmap灰色处理
	 * @param image
	 * @return
	 */
	public static Bitmap grayBitmap(final Bitmap image) {
		return grayBitmap(image, -1);
	}

	/**
	 * Bitmap灰色圆角处理
	 * @param image
	 * @param roundPx
	 * @return
	 */
	public static Bitmap grayBitmap(final Bitmap image, float roundPx) {
		if (null == image) return image;

		// 创建新的Bitmap用来保存最终结果
		Bitmap grayscalBitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Config.RGB_565);

		Canvas canvas = new Canvas(grayscalBitmap);
		Paint paint = new Paint();
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.setSaturation(0);// 饱和度
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cMatrix);
		paint.setColorFilter(filter);
		canvas.drawBitmap(image, 0, 0, paint);

		if (roundPx > 0) {
			grayscalBitmap = createRoundedCornerBitmap(grayscalBitmap, roundPx);
		}

		return grayscalBitmap;
	}

	/**
	 * 获得带倒影的图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * get reflection bitmap of the original bitmap.
	 * @param srcBitmap
	 * @return
	 */
	public static Bitmap makeReflectionBitmap(Bitmap srcBitmap) {
		int bmpWidth = srcBitmap.getWidth();
		int bmpHeight = srcBitmap.getHeight();
		int[] pixels = new int[bmpWidth * bmpHeight * 4];
		srcBitmap.getPixels(pixels, 0, bmpWidth, 0, 0, bmpWidth, bmpHeight);

		// get reversed bitmap
		Bitmap reverseBitmap = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
		for (int y = 0; y < bmpHeight; y++) {
			reverseBitmap.setPixels(pixels, y * bmpWidth, bmpWidth, 0, bmpHeight - y - 1, bmpWidth, 1);
		}

		// get reflection bitmap based on the reversed one
		reverseBitmap.getPixels(pixels, 0, bmpWidth, 0, 0, bmpWidth, bmpHeight);
		Bitmap reflectionBitmap = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
		int alpha = 0x00000000;
		for (int y = 0; y < bmpHeight; y++) {
			for (int x = 0; x < bmpWidth; x++) {
				int index = y * bmpWidth + x;
				int r = (pixels[index] >> 16) & 0xff;
				int g = (pixels[index] >> 8) & 0xff;
				int b = pixels[index] & 0xff;

				pixels[index] = alpha | (r << 16) | (g << 8) | b;

				reflectionBitmap.setPixel(x, y, pixels[index]);
			}
			alpha = alpha + 0x01000000;
		}

		return reflectionBitmap;
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap Bytes2Bimap(byte[] b) {
		return Bytes2Bimap(b, false);
	}

	public static Bitmap Bytes2Bimap(byte[] b, boolean bUseNative) {
		if (b.length == 0) {
			return null;
		}
		if (bUseNative) {
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			createNativeAllocOptions(newOpts);
			return BitmapFactory.decodeByteArray(b, 0, b.length, newOpts);
		} else {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
	}

	public static Bitmap decodeFile_new(String imageFile, int maxNumOfPixels) {
		return decodeFile_new(imageFile, maxNumOfPixels, false);
	}

	public static Bitmap decodeFile_new(String imageFile, int maxNumOfPixels, boolean bUseNative) {
		if (StringUtils.isNull(imageFile)) return null;
		Bitmap bitmap = null;
		try {
			// FileDescriptor fd = new FileInputStream(imageFile).getFD();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
			options.inDensity = options.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;

			// BitmapFactory.decodeFileDescriptor(fd, null, options);

			BitmapFactory.decodeFile(imageFile, options);
			if (options.mCancel || options.outWidth == -1 || options.outHeight == -1) {
				return null;
			}
			final int temp = options.outWidth * options.outHeight;
			if (temp < maxNumOfPixels) maxNumOfPixels = temp;

			options.inSampleSize = computeSampleSize(options, -1, maxNumOfPixels);
			options.inJustDecodeBounds = false;

			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			if (bUseNative) createNativeAllocOptions(options);

			// bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);

			bitmap = BitmapFactory.decodeFile(imageFile, options);

		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		} catch (Exception err) {
			err.printStackTrace();
		}
		return bitmap;
	}

	public static Bitmap decodeFile(String imageFile, int maxNumOfPixels) {
		return decodeFile(imageFile, maxNumOfPixels, false);
	}

	public static Bitmap decodeFile(String imageFile, int maxNumOfPixels, boolean bUseNative) {
		return decodeFile(imageFile, maxNumOfPixels, bUseNative, false, false);
	}

	public static Bitmap decodeFile(String imageFile, int maxNumOfPixels, boolean bUseNative, boolean bShare) {
		return decodeFile(imageFile, maxNumOfPixels, bUseNative, bShare, false);
	}

	public static Bitmap decodeFile(String imageFile, int maxNumOfPixels, boolean bUseNative, boolean bShare, boolean bDefaultDensity) {
		if (StringUtils.isNull(imageFile)) return null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// opts.inNativeAlloc = true;
		opts.inJustDecodeBounds = true;
		// DisplayUtil.getDisplayMetrics(act)
		if (bDefaultDensity) opts.inDensity = opts.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
		BitmapFactory.decodeFile(imageFile, opts);
		final int temp = opts.outWidth * opts.outHeight;
		if (temp > 0 && temp < maxNumOfPixels || maxNumOfPixels == 0) maxNumOfPixels = temp;
		// opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
		opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
		opts.inJustDecodeBounds = false;

		/**
		 * todo
		 */
		if (bUseNative) {
			createNativeAllocOptions(opts);
			if (bShare) {
				opts.inPurgeable = true;
				opts.inInputShareable = true;
			}
		} else {
			opts.inPurgeable = true;
			opts.inInputShareable = true;
		}

		try {
			return BitmapFactory.decodeFile(imageFile, opts);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		return null;
	}

	public static Bitmap readBitmap(Context context, int resId) {
		return readBitmap(context, resId, false);
	}

	public static Bitmap readBitmap(Context context, int resId, boolean bUseNative) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		if (bUseNative) ImageUtil.createNativeAllocOptions(opt);
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 获取图片的宽高
	 * @param imageFile
	 * @return
	 */
	public static int[] computeWH(String imageFile) {
		int[] wh = {
				0, 0
		};
		if (StringUtils.isNull(imageFile)) {
			return wh;
		}

		try {
			FileDescriptor fd = new FileInputStream(imageFile).getFD();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFileDescriptor(fd, null, options);
			if (options.mCancel || options.outWidth == -1 || options.outHeight == -1) {
				return wh;
			}
			wh[0] = options.outWidth;
			wh[1] = options.outHeight;
		} catch (Exception e) {
		}
		return wh;
	}

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

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * @Description
	 * @param minSideLength
	 * @param outW Bitmap的W
	 * @param outH Bitmap的H
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSizeByOutWH(int minSideLength, int outW, int outH, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSizeByOutWH(minSideLength, outW, outH, maxNumOfPixels);

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

	/**
	 * @Description
	 * @param minSideLength
	 * @param outW Bitmap的W
	 * @param outH Bitmap的H
	 * @param maxNumOfPixels
	 * @return
	 */
	private static int computeInitialSampleSizeByOutWH(int minSideLength, int outW, int outH, int maxNumOfPixels) {
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(outW * outH / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(outW / minSideLength), Math.floor(outH / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static Bitmap extractMiniThumb(Bitmap source, int width, int height, boolean recycle) throws Exception {
		if (source == null) {
			return null;
		}
		float scale;
		if (source.getWidth() > width && source.getHeight() > height) {
			if (source.getWidth() < source.getHeight()) {
				scale = width / (float) source.getWidth();
			} else {
				scale = height / (float) source.getHeight();
			}
		} else {
			scale = 1;
		}
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		Bitmap miniThumbnail = transform(matrix, source, width, height, true, recycle);
		return miniThumbnail;
	}

	public static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight, boolean scaleUp, boolean recycle) throws Exception {
		if (source == null || source.isRecycled()) return null;
		int deltaX = source.getWidth() - targetWidth;
		int deltaY = source.getHeight() - targetHeight;
		if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/*
			 * In this case the bitmap is smaller, at least in one dimension,
			 * than the target. Transform it by placing as much of the image as
			 * possible into the target and leaving the top/bottom or left/right
			 * (or both) black.
			 */
			Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b2);

			// c.drawColor(Color.WHITE);

			int deltaXHalf = Math.max(0, deltaX / 2);
			int deltaYHalf = Math.max(0, deltaY / 2);
			Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf + Math.min(targetWidth, source.getWidth()), deltaYHalf + Math.min(targetHeight, source.getHeight()));
			int dstX = (targetWidth - src.width()) / 2;
			int dstY = (targetHeight - src.height()) / 2;
			Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight - dstY);
			c.drawBitmap(source, src, dst, null);
			if (recycle) {
				source.recycle();
			}
			return b2;
		}
		float bitmapWidthF = source.getWidth();
		float bitmapHeightF = source.getHeight();
		float bitmapAspect = bitmapWidthF / bitmapHeightF;
		float viewAspect = (float) targetWidth / targetHeight;
		if (bitmapAspect > viewAspect) {
			float scale = targetHeight / bitmapHeightF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		} else {
			float scale = targetWidth / bitmapWidthF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		}
		Bitmap b1;
		if (scaler != null) {
			// this is used for minithumb and crop, so we want to filter here.
			b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler, true);
		} else {
			b1 = source;
		}
		if (recycle && b1 != source) {
			source.recycle();
		}
		int dx1 = scaleUp ? 0 : Math.max(0, b1.getWidth() - targetWidth);
		int dy1 = scaleUp ? 0 : Math.max(0, b1.getHeight() - targetHeight);
		Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth, targetHeight);
		if (b2 != b1) {
			if (recycle || b1 != source) {
				b1.recycle();
			}
		}
		return b2;
	}

	public final static int BITMAP_MAX_WIDTH = 512;
	public final static int BITMAP_MAX_HEIGHT = 512;

	public static Bitmap createBitmap(int w, int h, Bitmap.Config config) {
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(w, h, config);
		} catch (IllegalArgumentException e) {
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 通过文件获取Bitmap
	 * @param imageFile
	 * @return
	 */
	public static BitmapFactory.Options bitmapOptions4decodeFile(String imageFile) {
		if (StringUtils.isNull(imageFile)) return null;
		BitmapFactory.Options opts = new BitmapFactory.Options();

		opts.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeFile(imageFile, opts);
			return opts;
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		return null;
	}

	public static Bitmap decodeFile(String pathName) {
		Bitmap resizeBmp = null;
		try {
			resizeBmp = BitmapFactory.decodeFile(pathName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resizeBmp;
	}

	public static Bitmap decodeFile(File file) {
		Bitmap resizeBmp = null;
		try {
			resizeBmp = BitmapFactory.decodeFile(file.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resizeBmp;
	}

	public static Bitmap ShrinkBitmap(String file, int width, int height) {
		return ShrinkBitmap(file, width, height, false, false);
	}

	public static Bitmap ShrinkBitmap(String file, int width, int height, boolean bUseNative) {
		return ShrinkBitmap(file, width, height, bUseNative, false);
	}

	public static Bitmap ShrinkBitmap(String filePath, int width, int height, boolean bUseNative, boolean bShare) {
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		bmpFactoryOptions.inDensity = bmpFactoryOptions.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmpFactoryOptions);

		int heightRatio = width > 0 ? (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height) : 1;
		int widthRatio = height > 0 ? (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width) : 1;

		if (heightRatio > 1 || widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}
		if (bUseNative) {
			createNativeAllocOptions(bmpFactoryOptions);
		}
		if (bShare) {
			bmpFactoryOptions.inPurgeable = true;
			bmpFactoryOptions.inInputShareable = true;
		}
		bmpFactoryOptions.inJustDecodeBounds = false;
		// bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

		try {
			return BitmapFactory.decodeFile(filePath, bmpFactoryOptions);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		return bitmap;
	}

	public static void createNativeAllocOptions(BitmapFactory.Options options) {
		if (options == null) {
			return;
		}
		// options.inNativeAlloc = true;
		try {
			BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(options, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据uri获取图片的路径
	 * @param uri
	 * @param context
	 * @return
	 */
	public static String getPathByUri(Context context, Uri uri) {
		if (uri == null || context == null) {
			return null;
		}

		String filePath = "";
		ContentResolver cr = context.getContentResolver();
		String[] projection = {
			MediaStore.Images.Media.DATA
		};
		// CursorLoader cl = new CursorLoader(context);
		// Cursor cursor = cl.loadInBackground();
		Cursor cursor = cr.query(uri, projection, null, null, null);

		if (cursor != null && !cursor.isClosed()) {
			try {
				int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				filePath = cursor.getString(columnIndex);
				// 4.0以上的版本会自动关闭 (4.0--14; 4.0.3--15)
				if (Build.VERSION.SDK_INT < SDK_4_POINT_0) {
					cursor.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return filePath;
	}

	/**
	 * 直接获取互联网上的图片
	 * 
	 * @param imageUrl
	 * @param cutimg 裁剪
	 * @param scaleimg 缩放
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap getBitmapFormURL(String imageUrl, boolean cutimg, boolean scaleimg, int newWidth, int newHeight) {
		Bitmap bm = null;
		URLConnection con = null;
		InputStream is = null;
		try {
			URL url = new URL(imageUrl);
			con = url.openConnection();
			con.setDoInput(true);
			con.connect();
			is = con.getInputStream();
			// 获取资源图片
			Bitmap wholeBm = BitmapFactory.decodeStream(is, null, null);
			if (!cutimg && !scaleimg) {
				bm = wholeBm;
			} else if (newWidth <= 0 || newHeight <= 0) {
				bm = wholeBm;
			} else {
				if (cutimg) {
					bm = cutImg(wholeBm, newWidth, newHeight);
				}

				if (scaleimg) {
					bm = scaleImg(wholeBm, newWidth, newHeight);
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
			}
		}

		return bm;
	}

	public static boolean saveImage(Bitmap bm, String fileName, String dir, int quality) {
		if (quality <= 0 || quality >= 100) {
			quality = 100;
		}

		if (bm == null || StringUtils.isNull(fileName) || StringUtils.isNull(dir)) {
			return false;
		}

		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		} else {
			if (!dirFile.isDirectory()) {
				dirFile.mkdirs();
			}
		}

		File myCaptureFile = new File(dir, fileName);
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(myCaptureFile);
			bos = new BufferedOutputStream(fos);
			bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (bos != null) {
				try {
					bos.close();
					if (bos != null) {
						bos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 保存Bitmap至本地(png)
	 * @param bm
	 * @param fileName
	 * @param dir
	 */
	public static boolean saveImage(Bitmap bm, String fileName, String dir) {
		return saveImage(bm, fileName, dir, 100);
	}

	/**
	 * 保存Bitmap(带圆角)至本地(png)
	 * @param bm
	 * @param fileName
	 * @param dir
	 */
	public static void saveImage4Rounded(Bitmap bm, String fileName, String dir, float roundPx) {
		if (bm == null || StringUtils.isNull(fileName) || StringUtils.isNull(dir)) {
			return;
		}

		Bitmap bmp = bm;
		if (roundPx > 0) {
			bmp = createRoundedCornerBitmap(bm, roundPx);
		}
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		} else {
			if (!dirFile.isDirectory()) {
				dirFile.mkdirs();
			}
		}

		File myCaptureFile = new File(dir, fileName);
		// if (myCaptureFile.exists() && myCaptureFile.isFile()) {
		// myCaptureFile.delete();
		// }
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(myCaptureFile);
			bos = new BufferedOutputStream(fos);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			if (bm != null && bm.isRecycled()) {
				bm.recycle();
				bm = null;
			}
			if (bmp != null && bmp.isRecycled()) {
				bmp.recycle();
				bmp = null;
			}
			if (bos != null) {
				try {
					bos.close();
					if (bos != null) {
						bos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	/**
	 * @date 2013-8-26
	 * @author jsl
	 * @param dir 文件目录
	 * @param fileName 文件名
	 * @param inputStream 输入流
	 * @return
	 */
	public static boolean saveImageFile(String dir, String fileName, InputStream inputStream) {

		if (StringUtils.isNull(dir) || StringUtils.isNull(fileName) || inputStream == null) {
			return false;
		}
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		FileOutputStream outStream = null;
		try {
			byte[] data = readInputStream(inputStream);
			File imageFile = new File(dir + fileName);
			// 创建输出流
			outStream = new FileOutputStream(imageFile);
			// 写入数据
			outStream.write(data);
		} catch (Exception e) {
			Log.e("Exception", "ImageUilt saveImageFile", e);
			return false;
		} finally {
			// 关闭输出流
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();

				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * @date 2013-8-26
	 * @author jsl
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		// inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}

	public static void saveImage4Rounded2AbsPath(Bitmap bm, String filePath, float roundPx, int quality) {
		if (bm == null || StringUtils.isNull(filePath)) {
			return;
		}

		if (quality <= 0 || quality >= 100) {
			quality = 100;
		}

		Bitmap bmp = bm;
		if (roundPx > 0) {
			bmp = createRoundedCornerBitmap(bm, roundPx);
		}

		File myCaptureFile = new File(filePath);
		if (myCaptureFile.exists() && myCaptureFile.isFile()) {
			myCaptureFile.delete();
		}
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(myCaptureFile);
			bos = new BufferedOutputStream(fos);
			bmp.compress(Bitmap.CompressFormat.JPEG, quality, bos);
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			if (bm != null && bm.isRecycled()) {
				bm.recycle();
				bm = null;
			}
			if (bmp != null && bmp.isRecycled()) {
				bmp.recycle();
				bmp = null;
			}
			if (bos != null) {
				try {
					bos.close();
					if (bos != null) {
						bos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	public static void saveImage4Rounded2AbsPath(Bitmap bm, String fileName, float roundPx) {
		saveImage4Rounded2AbsPath(bm, fileName, roundPx, 100);
	}

	/**
	 * 根据资源id取得bitmap
	 * @param resourceId
	 * @param context
	 * @return
	 */
	public static Bitmap getBitmapByResouceId(Context context, int resourceId) {
		Bitmap bitmap = null;
		Drawable d = context.getResources().getDrawable(resourceId);
		if (d != null) {
			bitmap = ImageUtil.drawableToBitmap(d);
		}

		return bitmap;
	}

	public static Bitmap getBitmapByResouceId(Context context, int id, int w, int h) {
		if (null == context || -1 == id) {
			return null;
		}

		try {
			Drawable d = context.getResources().getDrawable(id);
			Bitmap bt = null;
			if (w == 0 || h == 0) {
				bt = ImageUtil.drawableToBitmap(d);
			} else {
				bt = zoomBitmap(ImageUtil.drawableToBitmap(d), w, h);
			}
			return bt;
		} catch (Exception e) {
			return null;
		}
	}

	private static HashMap<String, SoftReference<Bitmap>> imageCache2File;

	@Deprecated
	private static Bitmap decodeFile4Cach(String file) {
		Bitmap bmp = null;
		if (StringUtils.isNull(file)) {
			return bmp;
		}

		if (imageCache2File != null && imageCache2File.containsKey(file)) {
			SoftReference<Bitmap> softReference = imageCache2File.get(file);
			if (softReference != null && softReference.get() != null && !softReference.get().isRecycled()) {
				return softReference.get();
			}
		}

		if (imageCache2File == null) {
			imageCache2File = new HashMap<String, SoftReference<Bitmap>>();
		}

		bmp = BitmapFactory.decodeFile(file);

		SoftReference<Bitmap> s = new SoftReference<Bitmap>(bmp);
		imageCache2File.put(file, s);

		return bmp;
	}

	/**
	 * 回收imageCache2File中的所有Bitmap对象
	 * @Description
	 */
	@SuppressWarnings("rawtypes")
	private static void recycleDecodeFile4Cach() {
		if (imageCache2File == null || imageCache2File.isEmpty()) {
			return;
		}
		Set set = imageCache2File.keySet();
		if (set == null || set.isEmpty()) {
			return;
		}
		Iterator it = set.iterator();
		if (it == null) {
			return;
		}
		while (it.hasNext()) {
			SoftReference<Bitmap> softReference = imageCache2File.get(it.next());
			if (softReference != null && softReference.get() != null && !softReference.get().isRecycled()) {
				softReference.get().recycle();
			}
		}
	}

	private static HashMap<String, SoftReference<Bitmap>> imageCache2File2;

	@Deprecated
	private static Bitmap decodeFile4Cach2(String file, BitmapFactory.Options options) {
		Bitmap bmp = null;
		if (StringUtils.isNull(file)) {
			return bmp;
		}

		if (imageCache2File2 != null && imageCache2File2.containsKey(file)) {
			SoftReference<Bitmap> softReference = imageCache2File2.get(file);
			if (softReference != null && softReference.get() != null && !softReference.get().isRecycled()) {
				return softReference.get();
			}
		}

		if (imageCache2File2 == null) {
			imageCache2File2 = new HashMap<String, SoftReference<Bitmap>>();
		}

		bmp = BitmapFactory.decodeFile(file, options);

		SoftReference<Bitmap> s = new SoftReference<Bitmap>(bmp);
		imageCache2File2.put(file, s);

		return bmp;
	}

	/**
	 * 回收imageCache2File2中的所有Bitmap对象
	 * @Description
	 */
	@SuppressWarnings("rawtypes")
	private static void recycledecodeFile4Cach2() {
		if (imageCache2File2 == null || imageCache2File2.isEmpty()) {
			return;
		}
		Set set = imageCache2File2.entrySet();
		if (set == null || set.isEmpty()) {
			return;
		}
		Iterator it = set.iterator();
		if (it == null) {
			return;
		}
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (entry == null) {
				continue;
			}
			SoftReference<Bitmap> softReference = (SoftReference<Bitmap>) entry.getValue();
			if (softReference != null && softReference.get() != null && !softReference.get().isRecycled()) {
				softReference.get().recycle();
			}
		}
	}

	private static HashMap<String, SoftReference<Bitmap>> imageCache;

	@Deprecated
	private static Bitmap getBitmap4Cach(Context context, int id) {
		if (imageCache != null && imageCache.containsKey(id + "")) {
			SoftReference<Bitmap> softReference = imageCache.get(id + "");
			if (softReference != null && softReference.get() != null && !softReference.get().isRecycled()) {
				return softReference.get();
			}
		}
		if (context != null) {
			if (imageCache == null) {
				imageCache = new HashMap<String, SoftReference<Bitmap>>();
			}
			Drawable d = context.getResources().getDrawable(id);
			Bitmap bt = ImageUtil.drawableToBitmap(d);
			SoftReference<Bitmap> s = new SoftReference<Bitmap>(bt);
			imageCache.put(id + "", s);
			return bt;
		}
		return null;
	}

	@Deprecated
	private static Bitmap getBitmap4Cach(int id, Context context, int w, int h) {
		if (imageCache != null && imageCache.containsKey(id + "")) {
			SoftReference<Bitmap> softReference = imageCache.get(id + "");
			if (softReference != null && softReference.get() != null && !softReference.get().isRecycled()) {
				return softReference.get();
			}
		}
		if (context != null) {
			if (imageCache == null) {
				imageCache = new HashMap<String, SoftReference<Bitmap>>();
			}
			Drawable d = context.getResources().getDrawable(id);
			Bitmap bt = null;
			if (w == 0 || h == 0) {
				bt = ImageUtil.drawableToBitmap(d);
			} else {
				bt = zoomBitmap(ImageUtil.drawableToBitmap(d), w, h);
			}
			SoftReference<Bitmap> s = new SoftReference<Bitmap>(bt);
			imageCache.put(id + "", s);
			return bt;
		}
		return null;
	}

	public static Bitmap revitionImageSize(String path, int size) throws IOException {
		// 取得图片
		InputStream temp = new FileInputStream(path);
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
		options.inJustDecodeBounds = true;
		// 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
		BitmapFactory.decodeStream(temp, null, options);
		// 关闭流
		temp.close();

		// 生成压缩的图片
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			// 这一步是根据要设置的大小，使宽和高都能满足
			if ((options.outWidth >> i <= size) && (options.outHeight >> i <= size)) {
				// 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
				temp = new FileInputStream(path);
				// 这个参数表示 新生成的图片为原始图片的几分之一。
				options.inSampleSize = (int) Math.pow(2.0D, i);
				// 这里之前设置为了true，所以要改为false，否则就创建不出图片
				options.inJustDecodeBounds = false;

				bitmap = BitmapFactory.decodeStream(temp, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	private static void save(String srcPath, String destPath, int width, int height) {
		// 对图片进行压缩
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		// 获取这个图片的宽和高
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options);// 此时返回bm为空
		options.inJustDecodeBounds = false;
		// 计算缩放比
		int be = (int) (options.outWidth / (float) width);
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = 1;
		// options.inTempStorage = new byte[10 * 1024 * 1024];//分配10M的空间
		// 重新读入图片，注意这次要把options.inJustDecodeBounds设为false
		bitmap = BitmapFactory.decodeFile(srcPath, options);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		// Log.e("Test", " bitmap w:h = " + w + "," + h);

		// 保存入sdCard
		File destFile = new File(destPath);
		if (destFile.exists()) {
			destFile.delete();
		}
		try {

			FileOutputStream out = new FileOutputStream(destFile);
			Bitmap bmp = Bitmap.createScaledBitmap(bitmap, width, height, true);
			// Bitmap bmp = resizeImage(bitmap, width, height);
			if (bmp.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 根据uri获取文件的路径
	 * @param uri
	 * @param context
	 * @return
	 */
	public static String getPathByUriFromFile(Context context, Uri uri) {
		String head = "file://";
		String mnt = "/mnt";
		String result = "";

		if (!StringUtils.isNull(uri.toString()) && uri.toString().contains(head)) {
			result = uri.getPath();
			String sdcardPath = PcSDcardUtil.getExternalStorageDirectory();

			if (!StringUtils.isNull(sdcardPath) && sdcardPath.contains(mnt) && !StringUtils.isNull(result) && !result.startsWith(mnt)) {
				result = mnt + result;
			}
		} else {
			result = ImageUtil.getPathByUri(context, uri);
		}
		return result;
	}

	/**
	 * 根据path获取图片的偏转角度
	 * @param path ：图片的绝对路径
	 * @return 图片的旋转角度orientation
	 */
	public static String getOrientationByPath(String path) {
		String orientation = "0";
		if (StringUtils.isNull(path)) {
			return orientation;
		}

		// 图片旋转
		ExifInterface exifInterface = null;
		try {
			exifInterface = new ExifInterface(path);
		} catch (IOException e) {
			return orientation;
		}
		// 获取图片的旋转角度
		int tag = -1;
		if (exifInterface != null) {
			tag = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
		}

		if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
			orientation = "90";
		} else if (tag == ExifInterface.ORIENTATION_ROTATE_180) {
			orientation = "180";
		} else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
			orientation = "270";
		}
		return orientation;
	}

	/**
	 * 根据角度旋转图片
	 * @param orientation :图片的旋转角度
	 * @param bitmap
	 */
	public static Bitmap rotateImage(String orientation, Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		Bitmap newBitmap = null;
		int angle = 0;
		if (orientation != null && !"".equals(orientation)) {
			angle = StringUtils.str2Int(orientation, 0);
		}
		if (angle != 0) {
			// 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
			Matrix m = new Matrix();
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			m.setRotate(angle); // 旋转angle度
			newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);// 从新生成图片
			return newBitmap;
		} else {
			return bitmap;
		}
	}

	/**
	 * 按照规则获取图片
	 * @param srcPath //图片绝对路径
	 * @param isWifi //是否是wifi
	 * @param MAXSIZE //最大宽高限制
	 * @param MINLENTH //小于该长度，则不处理
	 * @return
	 */
	public static Bitmap getImageByRule(String srcPath, int MAXSIZE, int MINLENTH) {
		if (StringUtils.isNull(srcPath)) {
			return null;
		}
		File file = new File(srcPath);
		if (!file.exists()) {
			return null;
		}
		Options options = BitmapUtil.getOptionsByPath(srcPath);// 图片设置
		int height = options.outHeight;// 图片高度
		int width = options.outWidth;// 图片宽度
		options.inJustDecodeBounds = false;
		if (file.length() > MINLENTH) {
			if (height > width && height > MAXSIZE) {// 最长高大于2048
				width *= MAXSIZE * 1f / height;
				height = MAXSIZE;
			} else if (width > height && width > MAXSIZE) {// 最长宽大于2048
				height *= MAXSIZE * 1f / width;
				width = MAXSIZE;
			}
		}
		Bitmap bitmap = BitmapUtil.getBitmapByPath(srcPath, options, width, height);// 按宽高比例获取图片
		return bitmap;
	}

	/**
	 * 创建绘制带文字的bitmap
	 * @param txt 需要绘制的文字
	 * @param id 背景id
	 * @param context
	 * @return
	 */
	public static Bitmap createBmpAndDrawTxt(Context context, String txt, int id) {
		if (context == null) {
			return null;
		}
		float scale = context.getResources().getDisplayMetrics().density;
		String model = android.os.Build.MODEL;
		if (EmTerminalConstants.isTerminal(EmTerminalConstants.BUILD_MODEL_GTI9502.getValue())) {
			scale *= 1.8;
		}

		int templeW = (int) (37 * scale);
		int templeH = (int) (32 * scale);
		int textSize = (int) (17 * scale);
		Bitmap src = getBitmapByResouceId(context, id);
		Bitmap temple = Bitmap.createScaledBitmap(src, templeW, templeH, true);
		Canvas canvas = new Canvas(temple);
		TextPaint txtPaint = new TextPaint();
		txtPaint.setTextSize(textSize);
		txtPaint.setTypeface(Typeface.DEFAULT_BOLD);
		txtPaint.setColor(Color.WHITE);
		txtPaint.setAntiAlias(true);
		float txtWidth = txtPaint.measureText(txt);
		float x = (templeW - txtWidth) / 2 + txtWidth / 6;
		float y = templeH / 2 + textSize / 3;
		canvas.drawText(txt, x, y, txtPaint);
		return temple;
	}

	/**
	 * 实现点击图片背景颜色变化功能
	 * @param resources
	 * @param bitmap
	 * @return
	 */
	public static StateListDrawable getStateListDrawable(Resources resources, Bitmap bitmap) {
		Drawable normal = new BitmapDrawable(bitmap);
		StateListDrawable listDrawable = new StateListDrawable();
		Bitmap srcBitmap = ((BitmapDrawable) normal).getBitmap();
		Bitmap bmp = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Config.ARGB_8888);
		int brightness = 60 - 127;// 改变亮度
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.set(new float[] {
				1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0
		});
		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
		Canvas canvas = new Canvas(bmp);
		// 在Canvas上绘制一个Bitmap
		canvas.drawBitmap(srcBitmap, 0, 0, paint);

		Drawable pressed = new BitmapDrawable(resources, bmp);

		listDrawable.addState(new int[] {
			android.R.attr.state_pressed
		}, pressed);
		listDrawable.addState(new int[] {
			android.R.attr.state_selected
		}, pressed);
		listDrawable.addState(new int[] {
			android.R.attr.state_enabled
		}, normal);

		return listDrawable;
	}

	/**
	 * Invoked when pressing button for showing result of the "Crop" decoding
	 * method
	 */
	public static Bitmap getScaledBitmap(String path, int dstWidth, int dstHeight) {
		final long startTime = SystemClock.uptimeMillis();

		// Part 1: Decode image
		Bitmap unscaledBitmap = ImageUtil.decodeResource(path, dstWidth, dstHeight, ImageUtil.ScalingLogic.CROP);

		if (unscaledBitmap == null) {
			return null;
		}

		// Part 2: Scale image
		Bitmap scaledBitmap = ImageUtil.createScaledBitmap(unscaledBitmap, dstWidth, dstHeight, ImageUtil.ScalingLogic.FIT);
		unscaledBitmap.recycle();

		// Calculate memory usage and performance statistics
		final int memUsageKb = (unscaledBitmap.getRowBytes() * unscaledBitmap.getHeight()) / 1024;
		final long stopTime = SystemClock.uptimeMillis();

		// Publish results
		// mResultView.setText("Time taken: " + (stopTime - startTime)
		// + " ms. Memory used for scaling: " + memUsageKb + " kb.");
		return scaledBitmap;
	}

	/**
	 * Utility function for decoding an image resource. The decoded bitmap will
	 * be optimized for further scaling to the requested destination dimensions
	 * and scaling logic.
	 * @param path The resource bitmap dir.
	 * @param dstWidth Width of destination area
	 * @param dstHeight Height of destination area
	 * @param scalingLogic Logic to use to avoid image stretching
	 * @return Decoded bitmap
	 */
	public static Bitmap decodeResource(String path, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, scalingLogic);
		Bitmap unscaledBitmap = BitmapFactory.decodeFile(path, options);

		return unscaledBitmap;
	}

	/**
	 * Utility function for decoding an image resource. The decoded bitmap will
	 * be optimized for further scaling to the requested destination dimensions
	 * and scaling logic.
	 * @param res The resources object containing the image data
	 * @param resId The resource id of the image data
	 * @param dstWidth Width of destination area
	 * @param dstHeight Height of destination area
	 * @param scalingLogic Logic to use to avoid image stretching
	 * @return Decoded bitmap
	 */
	public static Bitmap decodeResource(Resources res, int resId, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, scalingLogic);
		Bitmap unscaledBitmap = BitmapFactory.decodeResource(res, resId, options);

		return unscaledBitmap;
	}

	/**
	 * Utility function for creating a scaled version of an existing bitmap
	 * @param unscaledBitmap Bitmap to scale
	 * @param dstWidth Wanted width of destination bitmap
	 * @param dstHeight Wanted height of destination bitmap
	 * @param scalingLogic Logic to use to avoid image stretching
	 * @return New scaled bitmap object
	 */
	public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
		Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
		Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Config.ARGB_8888);
		Canvas canvas = new Canvas(scaledBitmap);
		canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));

		return scaledBitmap;
	}

	/**
	 * ScalingLogic defines how scaling should be carried out if source and
	 * destination image has different aspect ratio. CROP: Scales the image the
	 * minimum amount while making sure that at least one of the two dimensions
	 * fit inside the requested destination area. Parts of the source image will
	 * be cropped to realize this. FIT: Scales the image the minimum amount
	 * while making sure both dimensions fit inside the requested destination
	 * area. The resulting destination dimensions might be adjusted to a smaller
	 * size than requested.
	 */
	public static enum ScalingLogic {
		CROP, FIT
	}

	/**
	 * Calculate optimal down-sampling factor given the dimensions of a source
	 * image, the dimensions of a destination area and a scaling logic.
	 * @param srcWidth Width of source image
	 * @param srcHeight Height of source image
	 * @param dstWidth Width of destination area
	 * @param dstHeight Height of destination area
	 * @param scalingLogic Logic to use to avoid image stretching
	 * @return Optimal down scaling sample size for decoding
	 */
	public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		if (scalingLogic == ScalingLogic.FIT) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				return srcWidth / dstWidth;
			} else {
				return srcHeight / dstHeight;
			}
		} else {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				return srcHeight / dstHeight;
			} else {
				return srcWidth / dstWidth;
			}
		}
	}

	/**
	 * Calculates source rectangle for scaling bitmap
	 * @param srcWidth Width of source image
	 * @param srcHeight Height of source image
	 * @param dstWidth Width of destination area
	 * @param dstHeight Height of destination area
	 * @param scalingLogic Logic to use to avoid image stretching
	 * @return Optimal source rectangle
	 */
	public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		if (scalingLogic == ScalingLogic.CROP) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				final int srcRectWidth = (int) (srcHeight * dstAspect);
				final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
				return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
			} else {
				final int srcRectHeight = (int) (srcWidth / dstAspect);
				final int scrRectTop = (int) (srcHeight - srcRectHeight) / 2;
				return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
			}
		} else {
			return new Rect(0, 0, srcWidth, srcHeight);
		}
	}

	/**
	 * Calculates destination rectangle for scaling bitmap
	 * @param srcWidth Width of source image
	 * @param srcHeight Height of source image
	 * @param dstWidth Width of destination area
	 * @param dstHeight Height of destination area
	 * @param scalingLogic Logic to use to avoid image stretching
	 * @return Optimal destination rectangle
	 */
	public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		if (scalingLogic == ScalingLogic.FIT) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
			} else {
				return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
			}
		} else {
			return new Rect(0, 0, dstWidth, dstHeight);
		}
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 裁剪图片
	 * @param file File对象
	 * @param newWidth 新图片的宽
	 * @param newHeight 新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap cutImg(File file, int newWidth, int newHeight) {
		Bitmap resizeBmp = null;
		if (newWidth <= 0 || newHeight <= 0) {
			throw new IllegalArgumentException("裁剪图片的宽高设置不能小于0");
		}

		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为true,decodeFile先不创建内存 只获取一些解码边界信息即图片大小信息
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getPath(), opts);
		// inSampleSize=2表示图片宽高都为原来的二分之一，即图片为原来的四分之一
		// 缩放可以将像素点打薄,裁剪前将图片缩放到目标图2倍大小
		int srcWidth = opts.outWidth; // 获取图片的原始宽度
		int srcHeight = opts.outHeight;// 获取图片原始高度
		int destWidth = 0;
		int destHeight = 0;

		int cutSrcWidth = newWidth * 2;
		int cutSrcHeight = newHeight * 2;

		// 缩放的比例,为了大图的缩小到2倍被裁剪的大小在裁剪
		double ratio = 0.0;
		// 任意一个不够长就不缩放
		if (srcWidth < cutSrcWidth || srcHeight < cutSrcHeight) {
			ratio = 0.0;
			destWidth = srcWidth;
			destHeight = srcHeight;
		} else if (srcWidth > cutSrcWidth) {
			ratio = (double) srcWidth / cutSrcWidth;
			destWidth = cutSrcWidth;
			destHeight = (int) (srcHeight / ratio);
		} else if (srcHeight > cutSrcHeight) {
			ratio = (double) srcHeight / cutSrcHeight;
			destHeight = cutSrcHeight;
			destWidth = (int) (srcWidth / ratio);
		}

		// 默认为ARGB_8888.
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		// 以下两个字段需一起使用：
		// 产生的位图将得到像素空间，如果系统gc，那么将被清空。当像素再次被访问，如果Bitmap已经decode，那么将被自动重新解码
		opts.inPurgeable = true;
		// 位图可以共享一个参考输入数据(inputstream、阵列等)
		opts.inInputShareable = true;
		// 缩放的比例，缩放是很难按准备的比例进行缩放的，通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
		if (ratio > 1) {
			opts.inSampleSize = (int) ratio;
		} else {
			opts.inSampleSize = 1;
		}
		// 设置大小
		opts.outHeight = destHeight;
		opts.outWidth = destWidth;
		// 创建内存
		opts.inJustDecodeBounds = false;
		// 使图片不抖动
		opts.inDither = false;
		Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), opts);
		if (bitmap != null) {
			resizeBmp = cutImg(bitmap, newWidth, newHeight);
		}
		return resizeBmp;
	}

	/**
	 * 裁剪图片.
	 * @param bitmap the bitmap
	 * @param newWidth 新图片的宽
	 * @param newHeight 新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap cutImg(Bitmap bitmap, int newWidth, int newHeight) {
		if (bitmap == null) {
			return null;
		}

		if (newWidth <= 0 || newHeight <= 0) {
			throw new IllegalArgumentException("裁剪图片的宽高设置不能小于0");
		}

		Bitmap resizeBmp = null;

		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();

			if (width <= 0 || height <= 0) {
				return null;
			}
			int offsetX = 0;
			int offsetY = 0;

			if (width > newWidth) {
				offsetX = (width - newWidth) / 2;
			} else {
				newWidth = width;
			}

			if (height > newHeight) {
				offsetY = (height - newHeight) / 2;
			} else {
				newHeight = height;
			}

			resizeBmp = Bitmap.createBitmap(bitmap, offsetX, offsetY, newWidth, newHeight);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resizeBmp != bitmap) {
				bitmap.recycle();
			}
		}
		return resizeBmp;
	}

}
