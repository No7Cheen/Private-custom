/**
 * @ TextViewPlus.java 2013-8-15
 */

package com.pc.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.privatecustom.publiclibs.R;

/**
 * 可以设置drawable大小
 * 
 * @author ryf
 * @date 2013-8-15
 */
public class TextViewPlus extends TextView {

	// 需要从xml中读取的各个方向图片的宽和高
	private int leftHeight = -1;
	private int leftWidth = -1;
	private int rightHeight = -1;
	private int rightWidth = -1;
	private int topHeight = -1;
	private int topWidth = -1;
	private int bottomHeight = -1;
	private int bottomWidth = -1;
	private int mDuration = 3000;// 持续显示时间

	// private final String NAME_SPACE = "com.ui.plus.TextViewPlus";

	public TextViewPlus(Context context) {
		super(context);
	}

	public TextViewPlus(Context context, AttributeSet attrs) {
		super(context, attrs);

		// super一定要在我们的代码之前配置文件
		init(context, attrs, 0);
	}

	public TextViewPlus(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// super一定要在我们的代码之前配置文件
		init(context, attrs, defStyle);
	}

	/**
	 * 初始化读取参数
	 */
	private void init(Context context, AttributeSet attrs, int defStyle) {
		// String durationStr = attrs.getAttributeValue(NAME_SPACE, "duration");
		// duration = StringUtils.str2Int(durationStr, 0);
		// TypeArray中含有我们需要使用的参数
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewPlus, defStyle, 0);
		if (null == a) {
			return;
		}

		int index = 0;
		int count = a.getIndexCount();

		// 遍历参数，先将index从TypedArray中读出来，
		// 得到的这个index对应于attrs.xml中设置的参数名称在R中编译得到的数
		// 这里会得到各个方向的宽和高
		for (int i = 0; i < count; i++) {
			index = a.getIndex(i);
			switch (index) {
				case R.styleable.TextViewPlus_bottom_height:
					bottomHeight = a.getDimensionPixelSize(index, -1);
					break;

				case R.styleable.TextViewPlus_bottom_width:
					bottomWidth = a.getDimensionPixelSize(index, -1);
					break;

				case R.styleable.TextViewPlus_left_height:
					leftHeight = a.getDimensionPixelSize(index, -1);
					break;

				case R.styleable.TextViewPlus_left_width:
					leftWidth = a.getDimensionPixelSize(index, -1);
					break;

				case R.styleable.TextViewPlus_right_height:
					rightHeight = a.getDimensionPixelSize(index, -1);
					break;

				case R.styleable.TextViewPlus_right_width:
					rightWidth = a.getDimensionPixelSize(index, -1);
					break;

				case R.styleable.TextViewPlus_top_height:
					topHeight = a.getDimensionPixelSize(index, -1);
					break;

				case R.styleable.TextViewPlus_top_width:
					topWidth = a.getDimensionPixelSize(index, -1);
					break;

				case R.styleable.TextViewPlus_duration:
					mDuration = a.getInt(index, mDuration);
					break;
			}
		}

		// 获取各个方向的图片，按照：左-上-右-下 的顺序存于数组中
		Drawable[] drawables = getCompoundDrawables();
		int dir = 0;
		// 0-left; 1-top; 2-right; 3-bottom;
		for (Drawable drawable : drawables) {
			// 设定图片大小
			setImageSize(drawable, dir++);
		}
		// 将图片放回到TextView中
		setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);

		a.recycle();
	}

	/**
	 * 设定图片的大小
	 */
	private void setImageSize(Drawable d, int dir) {
		if (d == null) {
			return;
		}

		int width = -1;
		int height = -1;

		// 根据方向给宽和高赋值
		switch (dir) {
		// left
			case 0:
				height = leftHeight;
				width = leftWidth;
				break;

			// top
			case 1:
				height = topHeight;
				width = topWidth;
				break;

			// right
			case 2:
				height = rightHeight;
				width = rightWidth;
				break;

			// bottom
			case 3:
				height = bottomHeight;
				width = bottomWidth;
				break;
		}

		// 如果有某个方向的宽或者高没有设定值，则不去设定图片大小
		if (width != -1 && height != -1) {
			d.setBounds(0, 0, width, height);
		}
	}

	private Runnable mCallBack = new Runnable() {

		@Override
		public void run() {
			setVisibility(GONE);
		}
	};

	@Override
	public void setVisibility(int visibility) {
		if (visibility == VISIBLE && mDuration > 0) {
			if (getVisibility() == VISIBLE) {
				clearAnimation();
			} else {
				setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fast_fade_in));
			}
			removeCallbacks(mCallBack);
			postDelayed(mCallBack, mDuration);
		} else {
			setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fast_fade_out));
		}
		super.setVisibility(visibility);
	}

}
