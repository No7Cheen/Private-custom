package com.pc.imgs.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.widget.ImageView;

import com.pc.utils.StringUtils;
import com.pc.utils.imgs.EmImageOperationType;
import com.pc.utils.imgs.ImageUtil;

/**
 * 下载图片并显示的工具类
 */
public class PcImageDownloader {

	private Context mContext;

	// 显示的图片的宽、高
	private int width, height;

	// 图片存储目录相对SD的路径
	private String dir;

	// 图片的处理类型(剪切或者缩放到指定大小)
	private EmImageOperationType type;

	// 显示为下载中的View
	private View mLoadingView;

	// 图片未找到的图片
	private Drawable mNoImage;

	// 显示下载失败的图片
	private Drawable mErrorImage;

	// 显示为下载中的图片
	private Drawable mLoadingImage;

	// 动画控制
	private boolean isAnimation = true;

	// 线程池
	private PcImageDownloadPool mAbImageDownloadPool;

	public PcImageDownloader(Context context) {
		this.mContext = context;
		this.type = EmImageOperationType.ORIGINALIMG;
		this.mAbImageDownloadPool = PcImageDownloadPool.getInstance();
	}

	/**
	 * 设置下载中的图片
	 * 
	 * @param resID
	 * @throws
	 */
	public void setLoadingImage(int resId) {
		if (null == mContext) {
			return;
		}

		this.mLoadingImage = mContext.getResources().getDrawable(resId);
	}

	/**
	 * 设置下载中的View，优先级高于setLoadingImage
	 * 
	 * @param view 放在ImageView的上边或者下边的View
	 * @throws
	 */
	public void setLoadingView(View view) {
		this.mLoadingView = view;
	}

	/**
	 * 设置下载失败的图片，URL不为空，下载失败显示图片资源
	 * 
	 * @param resID
	 * @throws
	 */
	public void setErrorImage(int resId) {
		if (null == mContext) {
			return;
		}

		this.mErrorImage = mContext.getResources().getDrawable(resId);
	}

	/**
	 * 设置未找到的图片，当URL为空时显示图片资源
	 * 
	 * @param resID
	 * @throws
	 */
	public void setNoImage(int resId) {
		if (null == mContext) {
			return;
		}

		this.mNoImage = mContext.getResources().getDrawable(resId);
	}

	public int getWidth() {
		return width;
	}

	/**
	 * 设置图片的宽度
	 * 
	 * @param height
	 * @throws
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * 设置图片的高度
	 * 
	 * @param height
	 * @throws
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/** 
	 * @return the dir
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * 设置图片存储目录(相对路径)
	 * 
	 * @param dir the dir to set
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

	public EmImageOperationType getType() {
		return type;
	}

	/**
	 * 图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类）
	 * 
	 * @param type
	 * @throws
	 */
	public void setType(EmImageOperationType type) {
		this.type = type;
	}

	/**
	 * 是否开启动画.
	 * @param animation
	 * @throws
	 */
	public void setAnimation(boolean animation) {
		this.isAnimation = animation;
	}

	/**
	 * 显示这个图片
	 * 
	 * @param imageView
	 * @return url
	 */
	public void display(final ImageView imageView, String url) {
		if (StringUtils.isNull(url)) {
			if (mNoImage != null) {
				if (mLoadingView != null) {
					mLoadingView.setVisibility(View.INVISIBLE);
					imageView.setVisibility(View.VISIBLE);
				}

				imageView.setImageDrawable(mNoImage);
			}

			return;
		}

		// 设置下载项
		PcImageDownloadItem item = new PcImageDownloadItem();
		item.dir = dir;
		item.type = type;
		item.width = width;
		item.height = height;
		item.imageUrl = url;
		String cacheKey = PcImageDownloadCache.getCacheKey(item.imageUrl, item.width, item.height, item.type);
		item.bitmap = PcImageDownloadCache.getBitmapFromCache(cacheKey);

		// 缓存中存在图片
		if (null != item.bitmap) {
			if (mLoadingView != null) {
				mLoadingView.setVisibility(View.INVISIBLE);
				imageView.setVisibility(View.VISIBLE);
			}

			imageView.setImageBitmap(item.bitmap);

			return;
		}

		// 先显示加载中
		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.INVISIBLE);
		} else if (mLoadingImage != null) {
			if (isAnimation) {
				TransitionDrawable td = ImageUtil.drawableToTransitionDrawable(mLoadingImage);
				imageView.setImageDrawable(td);
				td.startTransition(200);
			} else {
				imageView.setImageDrawable(mLoadingImage);
			}
		}

		// 下载完成后更新界面
		item.listener = new PcImageDownloadListener() {

			@Override
			public void update(Bitmap bitmap, String imageUrl) {
				// 没有设置加载中的图片，并且设置了隐藏的View
				if (mLoadingView != null) {
					mLoadingView.setVisibility(View.INVISIBLE);
					imageView.setVisibility(View.VISIBLE);
				}

				if (bitmap != null) {
					if (isAnimation) {
						TransitionDrawable transitionDrawable = ImageUtil.bitmapToTransitionDrawable(bitmap);
						imageView.setImageDrawable(transitionDrawable);
						transitionDrawable.startTransition(200);
					} else {
						imageView.setImageBitmap(bitmap);
					}
				} else {
					if (mErrorImage != null) {
						if (isAnimation) {
							TransitionDrawable transitionDrawable = ImageUtil.drawableToTransitionDrawable(mErrorImage);
							imageView.setImageDrawable(transitionDrawable);
							transitionDrawable.startTransition(200);
						} else {
							imageView.setImageDrawable(mErrorImage);
						}
					}

				}
			}
		};

		// 下载
		mAbImageDownloadPool.download(item);
	}

}
