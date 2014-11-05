/**
 * @(#)PcEditTextPlus.java   2014-8-8
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.text;

import java.io.File;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
  * 可监听粘贴的EditText
  * 
  * @author chenj
  * @date 2014-8-8
  */

public class PcEditTextPastePlus extends EditText {

	private IOnPasteLitener mOnPasteLitener;// 粘贴监听
	private String picUri;// 图片uri

	public PcEditTextPastePlus(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 粘贴监听
	 * 
	 * @author chenj
	 * @date 2014-8-8
	 */
	public interface IOnPasteLitener {

		void onPastePic(File file);

		File comparePic2Clipboard();
	}

	public IOnPasteLitener getOnPasteLitener() {
		return mOnPasteLitener;
	}

	public void setOnPasteLitener(IOnPasteLitener mOnPasteLitener) {
		this.mOnPasteLitener = mOnPasteLitener;
	}

	public String getPicUri() {
		return picUri;
	}

	public void setPicUri(String picUri) {
		this.picUri = picUri;
	}

	/**
	 * 监听复制/粘贴行为
	 * 
	 * @see android.widget.TextView#onTextContextMenuItem(int)
	 */
	@Override
	public boolean onTextContextMenuItem(int id) {
		switch (id) {
			case android.R.id.selectAll:
				break;

			case android.R.id.cut:
				break;

			case android.R.id.copy:
				break;

			case android.R.id.paste:// 粘贴，为图片时回调
				if (mOnPasteLitener != null) {
					File file = mOnPasteLitener.comparePic2Clipboard();
					if (file != null) {
						mOnPasteLitener.onPastePic(file);
						return true;
					}
				}
				break;
			default:
				break;
		}

		return super.onTextContextMenuItem(id);
	}

}
