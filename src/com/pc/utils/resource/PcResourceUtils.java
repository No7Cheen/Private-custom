package com.pc.utils.resource;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.pc.utils.StringUtils;
import com.pc.utils.log.PcLog;
import com.privatecustom.publiclibs.R;

/**
 * 资源工具
 * 
 * @author chenj
 * @date 2014-7-30
 */
public final class PcResourceUtils {

	/**
	 * 根据资源的名字获取其id值
	 *
	 * @param context
	 * @param className 类名
	 * @param name 资源名
	 * @return int eg: getIdByName(getApplication(), "id", "button1") or getIdByName(getApplication(), "layout", "activity_main")
	 */
	@SuppressWarnings("rawtypes")
	public static int getIdByName(Context context, String className, String name) {
		if (null == context) {
			return 0;
		}

		int id = 0;
		Class rCls = null;
		String packageName = context.getPackageName();
		try {
			rCls = Class.forName(packageName + ".R");
			Class[] classes = rCls.getClasses();
			Class desireClass = null;
			for (int i = 0; i < classes.length; ++i) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}

			if (desireClass != null) {
				id = desireClass.getField(name).getInt(desireClass);
			}
		} catch (ClassNotFoundException e) {
			if (PcLog.isPrint) {
				e.printStackTrace();
			}
		} catch (IllegalArgumentException e) {
			if (PcLog.isPrint) {
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			if (PcLog.isPrint) {
				e.printStackTrace();
			}
		} catch (IllegalAccessException e) {
			if (PcLog.isPrint) {
				e.printStackTrace();
			}
		} catch (NoSuchFieldException e) {
			if (PcLog.isPrint) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			if (PcLog.isPrint) {
				e.printStackTrace();
			}
		}

		return id;
	}

	/**
	 * 获取资源id
	 *
	 * @param rCls  R Class, 如: R.string.class or R.layout.class
	 * @param name 资源名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public final static int getResourceId(Class rCls, String name) {
		if (StringUtils.isNull(name)) return 0;

		try {
			Field f = rCls.getField(name);
			if (f == null) return 0;

			return f.getInt(null);
		} catch (Exception e) {
		}

		return 0;
	}

	/**
	 * 获取 Layout ResourceId
	 *
	 * @param layoutRcls R.layout.class
	 * @param name 资源名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public final static int getLayoutResourceId(Class layoutRcls, String name) {
		return getResourceId(layoutRcls, name);
	}

	/**
	 * 获取 Layout ResourceId
	 *
	 * @param name 资源名
	 * @return
	 */
	@Deprecated
	public final static int getLayoutResourceId(String name) {
		return getLayoutResourceId(R.layout.class, name);
	}

	/**
	 *  获取 String ResourceId
	 *
	 * @param stringRcls R.string.class
	 * @param name 资源名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public final static int getStringResourceId(Class stringRcls, String name) {
		int id = -1;
		if (StringUtils.isNull(name)) return id;

		try {
			Field f = stringRcls.getField(name);
			if (f == null) return id;

			return f.getInt(null);
		} catch (Exception e) {
		}

		return id;
	}

	/**
	 *  获取 String ResourceId
	 *
	 * @param name 资源名
	 * @return
	 */
	@Deprecated
	public final static int getStringResourceId(String name) {
		int id = -1;
		if (StringUtils.isNull(name)) return id;

		try {
			Field f = R.string.class.getField(name);
			if (f == null) return id;

			return f.getInt(null);
		} catch (Exception e) {
		}

		return id;
	}

	/**
	 * 获取 Drawable资源id
	 *
	 * @param drawableRcls R.drawable.class
	 * @param name 资源名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public final static int getDrawableResourceId(Class drawableRcls, String name) {
		return getResourceId(drawableRcls, name);
	}

	/**
	 * 获取 Drawable资源id
	 *
	 * @param name 资源名
	 * @return
	 */
	@Deprecated
	public final static int getDrawableResourceId(String name) {
		return getDrawableResourceId(R.drawable.class, name);
	}

	/**
	 * 获取 Raw资源id
	 *
	 * @param rCls R.drawable.class
	 * @param name 资源名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int getRawIdByKey(Class rawRcls, String name) {
		return getResourceId(rawRcls, name);
	}

	/**
	 * 获取 Raw资源id
	 *
	 * @param name 资源名
	 * @return
	 */
	@Deprecated
	public static int getRawIdByKey(String name) {
		return getResourceId(R.raw.class, name);
	}

	/**
	 * 获取字符对象
	 * 
	 * @param context
	 * @param name 资源名
	 * @param defaultValue  默认值
	 * @return
	 */
	@Deprecated
	public static String getStringByKey(Context context, String name, String defaultValue) {
		return getStringByKey(context, R.string.class, name, defaultValue);
	}

	/**
	 * 获取字符对象
	 *
	 * @param context
	 * @param name 资源名
	 * @param defaultId  默认值资源id
	 * @return
	 */
	@Deprecated
	public static String getStringByKey(Context context, String name, int defaultId) {
		return getStringByKey(context, R.string.class, name, defaultId);
	}

	/**
	 * 获取字符对象
	 *
	 * @param context
	 * @param stringRcls R.string.class
	 * @param name 资源名
	 * @param defaultValue 默认值
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getStringByKey(Context context, Class stringRcls, String name, String defaultValue) {
		try {
			Field f = stringRcls.getField(name);
			if (f == null) {
				return defaultValue;
			}

			int id = f.getInt(null);
			return context.getResources().getString(id);
		} catch (Exception e) {
		}

		return defaultValue;
	}

	/**
	 * 获取字符对象
	 *
	 * @param context
	 * @param stringRcls R.string.class
	 * @param name 资源名,形如R.string.name
	 * @param defaultId  默认值资源id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getStringByKey(Context context, Class stringRcls, String name, int defaultId) {
		String s = null != context ? context.getString(defaultId) : "";
		try {
			Field f = stringRcls.getField(name);
			if (f == null) return s;

			int id = f.getInt(null);
			return context.getResources().getString(id);
		} catch (Exception e) {
		}

		return s;
	}

	/**
	 * 获取整型对象
	 *
	 * @param context
	 * @param integerRcls R.integer.class
	 * @param name 资源名
	 * @param defaultValue 默认值
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Integer getIntegerByKey(Context context, Class integerRcls, String name, Integer defaultValue) {
		Integer v = defaultValue;
		try {
			Field f = integerRcls.getField(name);
			if (f == null) return v;
			int id = f.getInt(null);
			return context.getResources().getInteger(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return v;
	}

	/**
	 * 获取整型对象
	 *  
	 * @param context
	 * @param name 资源名
	 * @param defaultValue 默认值
	 * @return
	 */
	@Deprecated
	public static Integer getIntegerByKey(Context context, String name, Integer defaultValue) {
		return getIntegerByKey(context, R.integer.class, name, defaultValue);
	}

	/**
	 * 获取图片
	 *
	 * @param context
	 * @param drawableRcls R.drawable.class
	 * @param name 资源名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public final static Drawable getDrawable(Context context, Class drawableRcls, String name) {
		Drawable s = null;
		try {
			Field f = drawableRcls.getField(name);
			if (f == null) return s;
			int id = f.getInt(null);

			return context.getResources().getDrawable(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return s;
	}

	/**
	 * 获取图片
	 *
	 * @param context
	 * @param name 资源名
	 * @return
	 */
	@Deprecated
	public final static Drawable getDrawable(Context context, String name) {
		return getDrawable(context, R.drawable.class, name);
	}

}
