package com.pc.ui.animations;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 旋转动画
 * @author chenjian
 */
public class RotateAnimation extends Animation {

	/**
	 * 沿Y轴正方向看，动画逆时针旋转
	 */
	public static final boolean ROTATE_COUNTER_CLOCKWISE = true;
	/**
	 * 沿Y轴正方向看，动画顺时针旋转
	 */
	public static final boolean ROTATE_CLOCKWISE = false;

	/**
	 * 以x轴旋转
	 */
	public static final int ROTATE_AXIS_X = 0x00000000;
	/**
	 * 以Y轴旋转
	 */
	public static final int ROTATE_AXIS_Y = 0x00000002;

	/**
	 * Z轴上最大深度
	 */
	private final float DEPTH_Z = 310.0f;
	/**
	 * 动画持续时长，默认800
	 */
	public final long DURATION = 800l;

	/** 值为true时可明确查看动画的旋转方向，用于测试 */
	private final boolean DEBUG = false;

	/** 图片翻转类型，逆时针或顺时针方向 */
	private final boolean type;
	private final float centerX;
	private final float centerY;
	private Camera mCamera;
	private int rotateAxis = ROTATE_AXIS_Y;

	/**
	 * @param cX 界面中心的坐标
	 * @param cY 界面中心的坐标
	 * @param type 动画类型：顺时针/逆时针
	 * @param axis 动画轴：X/Y
	 */
	public RotateAnimation(float cX, float cY, boolean type, int axis) {
		centerX = cX;
		centerY = cY;
		this.type = type;
		setDuration(DURATION);// 设置动画持续时长
		if (axis == ROTATE_AXIS_X) {
			rotateAxis = axis;
		} else {
			rotateAxis = ROTATE_AXIS_Y;
		}
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		// 在构造函数之后、applyTransformation()之前调用本方法。
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation transformation) {
		// interpolatedTime:动画进度值，范围为0～1，0.5为正好翻转一半
		if (listener != null) {
			listener.interpolatedTime(interpolatedTime);
		}

		float from = 0.0f, to = 0.0f;

		// 逆时针旋转
		if (type == ROTATE_COUNTER_CLOCKWISE) {
			from = 0.0f;
			to = 180.0f;
		}
		// 顺时针旋转
		else if (type == ROTATE_CLOCKWISE) {
			from = 360.0f;
			to = 180.0f;
		}

		float degree = from + ((to - from) * interpolatedTime);// 旋转的角度
		boolean overHalf = (interpolatedTime > 0.5f);
		if (overHalf) {// 翻转过半的情况下，为保证数字仍为可读的文字而非镜面效果的文字，需翻转180度，否则显示为镜面效果
			degree = degree - 180;
		}

		final float depth = (0.5f - Math.abs(interpolatedTime - 0.5f)) * DEPTH_Z;// 旋转深度
		final Matrix matrix = transformation.getMatrix();
		mCamera.save();
		mCamera.translate(0.0f, 0.0f, depth);// 深度：相当于与屏幕的距离

		// 以x轴旋转
		if (rotateAxis == ROTATE_AXIS_X) {
			mCamera.rotateX(degree);
		}

		// 以y轴旋转
		else {
			mCamera.rotateY(degree);
		}
		mCamera.getMatrix(matrix);
		mCamera.restore();

		if (DEBUG) {
			if (overHalf) {
				matrix.preTranslate(-centerX * 2, -centerY);
				matrix.postTranslate(centerX * 2, centerY);
			}
		} else {
			// 确保图片的翻转过程一直处于组件的中心点位置
			matrix.preTranslate(-centerX, -centerY); // 在setScale前平移
			matrix.postTranslate(centerX, centerY); // 在setScale后平移
		}
	}

	/** 用于监听动画进度，当值过半时需更新的内 */
	private InterpolatedTimeListener listener;

	/**
	 * @Description
	 * @param listener
	 */
	public void setInterpolatedTimeListener(InterpolatedTimeListener listener) {
		this.listener = listener;
	}

}
