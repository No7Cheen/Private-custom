package com.pc.utils.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.pc.app.dialog.BottomPopupDialogSingle;
import com.pc.app.dialog.IDialogCancelListener;
import com.pc.app.dialog.IDialogOKClickListener;
import com.pc.app.dialog.OnOkListener;
import com.pc.app.dialog.modle.PcEmDialogType;
import com.pc.app.dialog.v4.BottomPopupDialogFragment;
import com.pc.app.dialog.v4.BottomPopupDialogFragmentListView;
import com.pc.app.dialog.v4.InputDialogFragment;
import com.pc.app.dialog.v4.PcDialogFragmentV4;
import com.pc.app.dialog.v4.ProgressInfoDialogFragmentV4;
import com.pc.app.dialog.v4.PromptDialogFragmentV4;
import com.pc.app.dialog.v4.TopPopdownDialogFragment;
import com.pc.utils.StringUtils;
import com.privatecustom.publiclibs.R;

/**
 * @author ChenJian
 * @date 2012-1-15
 */
public class PcDialogFragmentUtilV4 {

	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	// Android系统默认Dialog
	// ///////////////////////////////////////////////////////////////////////////////////////////////////

	public static DialogFragment openMsgDialog(final Context context, FragmentTransaction transaction, String tag, final int icon, final String title, final String message,
			final String okTxt, final String cancelTxt, final DialogInterface.OnClickListener okListener, final DialogInterface.OnClickListener cancelListener) {
		DialogFragment df = new PcDialogFragmentV4() {

			/**
			* @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			*/
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				return new AlertDialog.Builder(context).setIcon(icon).setTitle(title).setMessage(message).setPositiveButton(okTxt, okListener)
						.setNegativeButton(cancelTxt, cancelListener).create();
			}

			/**
			 * @see com.pc.app.dialog.v4.PcDialogFragmentV4#onCancel(android.content.DialogInterface)
			 */
			@Override
			public void onCancel(DialogInterface dialog) {
				super.onCancel(dialog);
			}

		};

		return df;
	}

	public static DialogFragment openLongMsgDialog(final Context context, FragmentTransaction transaction, String tag, final int icon, final String title, final String msg,
			final String okTxt, final String cancelTxt, final DialogInterface.OnClickListener okListener, final DialogInterface.OnClickListener cancelListener) {
		DialogFragment df = new PcDialogFragmentV4() {

			/**
			 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				return PcDialogUtil.openLongMsgDialog(context, icon, title, msg, okTxt, cancelTxt, okListener, cancelListener);
			}
		};

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	public static DialogFragment openListDialog(final Context context, FragmentTransaction transaction, String tag, final int icon, final String title, final String[] items,
			final DialogInterface.OnClickListener listener) {
		DialogFragment df = new PcDialogFragmentV4() {

			/**
			 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				return PcDialogUtil.openListDialog(context, icon, title, items, listener);
			}
		};

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	public static DialogFragment openListDialog(final Context context, FragmentTransaction transaction, String tag, final int icon, final String title, final int itemsId,
			final DialogInterface.OnClickListener listener) {
		DialogFragment df = new PcDialogFragmentV4() {

			/**
			 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				return PcDialogUtil.openListDialog(context, icon, title, itemsId, listener);
			}
		};

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	public static DialogFragment openListDialog(final Context context, FragmentTransaction transaction, String tag, final String title, final int itemsId,
			final DialogInterface.OnClickListener listener) {
		DialogFragment df = new PcDialogFragmentV4() {

			/**
			 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				if (StringUtils.isNull(title)) {
					return new AlertDialog.Builder(context).setItems(itemsId, listener).create();
				} else {
					return new AlertDialog.Builder(context).setTitle(title).setItems(itemsId, listener).create();
				}
			}
		};

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	public static DialogFragment openSingleChoiceDialog(final Context context, FragmentTransaction transaction, String tag, final int icon, final String title,
			final String[] items, final String okTxt, final String cancelTxt, final DialogInterface.OnClickListener listener, final DialogInterface.OnClickListener okListener,
			final DialogInterface.OnClickListener cancelListener) {
		DialogFragment df = new PcDialogFragmentV4() {

			/**
			 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				return new AlertDialog.Builder(context).setIcon(icon).setTitle(title).setSingleChoiceItems(items, 0, listener).setPositiveButton(okTxt, okListener)
						.setNegativeButton(cancelTxt, cancelListener).create();
			}
		};

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	public static DialogFragment openMultChoiceDialog(final Context context, FragmentTransaction transaction, String tag, final int icon, final String title, final String[] items,
			final boolean[] defaultValues, final String okTxt, final String cancelTxt, final DialogInterface.OnMultiChoiceClickListener listener,
			final DialogInterface.OnClickListener okListener, final DialogInterface.OnClickListener cancelListener) {
		DialogFragment df = new PcDialogFragmentV4() {

			/**
			 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {

				return new AlertDialog.Builder(context).setIcon(icon).setTitle(title).setMultiChoiceItems(items, defaultValues, listener).setPositiveButton(okTxt, okListener)
						.setNegativeButton(cancelTxt, cancelListener).create();
			}
		};

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	/**
	 * Progress Dialog
	 *
	 * @param context
	 * @param title
	 * @param message
	 * @param okTxt
	 * @param cancelTxt
	 * @param okListener
	 * @param cancelListener
	 * @return ProgressDialog
	 */
	public static DialogFragment openSpinnerProgressDialog(final Context context, FragmentTransaction transaction, String tag, final String title, final String message,
			final String okTxt, final String cancelTxt, final IDialogOKClickListener okListener, final IDialogCancelListener cancelListener) {
		DialogFragment df = new PcDialogFragmentV4() {

			/**
			 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {

				return PcDialogUtil.openSpinnerProgressDialog(context, title, message, okTxt, cancelTxt, okListener, cancelListener);
			}
		};

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	/**
	 * 进度对话框，未修改
	 *
	 * @param context
	 * @param title
	 * @param message
	 * @return
	 */
	public static DialogFragment openSpinnerProgressDialog(final Context context, FragmentTransaction transaction, String tag, final String title, final String message) {
		DialogFragment df = new PcDialogFragmentV4() {

			/**
			 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {

				return PcDialogUtil.openSpinnerProgressDialog(context, title, message);
			}
		};

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	public static DialogFragment openViewDialog(final Context context, FragmentTransaction transaction, String tag, final int icon, final String title, final View view,
			final String okTxt, final String cancelTxt, final DialogInterface.OnClickListener okListener, final DialogInterface.OnClickListener cancelListener) {

		DialogFragment df = new PcDialogFragmentV4() {

			/**
			 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {

				AlertDialog d = new AlertDialog.Builder(context).setIcon(icon).setTitle(title).setView(view).setPositiveButton(okTxt, okListener)
						.setNegativeButton(cancelTxt, cancelListener).create();
				d.getWindow().setLayout(300, 300);

				return d;
			}
		};

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	public static DialogFragment openDefMsgDialog(final Context context, FragmentTransaction transaction, String tag, final String title, final String message, final String okTxt,
			final String cancelTxt, final DialogInterface.OnClickListener okListener, final DialogInterface.OnClickListener cancelListener) {
		DialogFragment df = new PcDialogFragmentV4() {

			/**
			 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {

				return new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton(okTxt, okListener).setNegativeButton(cancelTxt, cancelListener)
						.create();
			}
		};

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	public static DialogFragment openDefMsgDialog(final Context context, FragmentTransaction transaction, String tag, final String title, final String message, final String okTxt,
			final DialogInterface.OnClickListener okListener) {
		if (context == null) {
			return null;
		}

		DialogFragment df = new PcDialogFragmentV4() {

			/**
			 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {

				return new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton(okTxt, okListener).create();
			}
		};

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	/**
	 * 没有提示标题
	 * 
	 * @param context
	 * @param m_tTitle
	 * @param message
	 * @param okTxt
	 * @param cancelTxt
	 * @param okListener
	 * @param cancelListener
	 * @return
	 */
	public static DialogFragment openDefMsgDialog(final Context context, FragmentTransaction transaction, String tag, final String message, final String okTxt,
			final String cancelTxt, final DialogInterface.OnClickListener okListener, final DialogInterface.OnClickListener cancelListener) {
		DialogFragment df = new PcDialogFragmentV4() {

			/**
			 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {

				return new AlertDialog.Builder(context).setMessage(message).setPositiveButton(okTxt, okListener).setNegativeButton(cancelTxt, cancelListener).create();
			}
		};

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	/**
	 * 弹出框
	 */
	public static DialogFragment loadPopupWindow(final Context context, FragmentTransaction transaction, String tag, final int layoutResID, final boolean cancelable,
			final int width, final int height) {
		DialogFragment df = new PcDialogFragmentV4() {

			/**
			 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {

				final Dialog dialog = new Dialog(context, R.style.My_Dialog_Theme);
				dialog.setContentView(layoutResID);
				dialog.setCancelable(cancelable);
				dialog.getWindow().setLayout(width, height);
				// dialog.show();

				return dialog;
			}
		};

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	// 自定义Progress DialogFragment
	// ///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 消息/加载弹出框
	 * 
	 * @Description
	 * @param context
	 * @param cancelable
	 * @param infoTxtRes
	 * @return
	 */
	public static DialogFragment progressInfoDialog(FragmentTransaction transaction, String tag, boolean cancelable, boolean progressbar, int infoTxtRes) {
		DialogFragment df = new ProgressInfoDialogFragmentV4(false, progressbar, cancelable, infoTxtRes, null);
		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	/**
	 * 消息/加载弹出框
	 * 
	 * @param context
	 * @param cancelable
	 * @param progressbar 显示加载
	 * @param infoTxt 信息
	 * @return
	 */
	public static DialogFragment progressInfoDialog(FragmentTransaction transaction, String tag, boolean cancelable, boolean progressbar, String infoTxt) {
		return progressInfoDialog(transaction, tag, cancelable, progressbar, infoTxt, false);
	}

	/**
	 * 弹出框 上部进度条，下部显示文字
	 * 
	 * <p>
	 * 默认加载框
	 * </p>
	 * @param context
	 * @param message
	 * @return
	 */
	public static DialogFragment loadDialog(FragmentTransaction transaction, String tag, String message) {
		return progressInfoDialog(transaction, tag, false, true, message, false);
	}

	/**
	 * 消息弹出框
	 * 
	 * @Description
	 * @param context
	 * @param cancelable
	 * @param infoTxt
	 * @return
	 */
	public static DialogFragment infoDialog(FragmentTransaction transaction, String tag, boolean cancelable, String infoTxt) {
		return progressInfoDialog(transaction, tag, cancelable, false, infoTxt, false);
	}

	public static DialogFragment progressInfoDialog(FragmentTransaction transaction, String tag, boolean cancelable, boolean progressbar, int infoTxtRes, boolean large) {
		DialogFragment df = new ProgressInfoDialogFragmentV4(large, progressbar, cancelable, infoTxtRes, null);
		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	/**
	 * 消息/加载弹出框
	 * 
	 * <pre>
	 * 弹出框 上部进度条，下部显示文字
	 * </pre>
	 * @param context
	 * @param cancelable
	 * @param progressbar 显示加载
	 * @param infoTxt 信息
	 * @param large 显示大加载
	 * @return
	 */
	public static DialogFragment progressInfoDialog(FragmentTransaction transaction, String tag, boolean cancelable, boolean progressbar, String infoTxt, boolean large) {
		return progressInfoDialog(transaction, tag, cancelable, progressbar, infoTxt, large, null);
	}

	/**
	 * 消息/加载弹出框
	 * 
	 * <pre>
	 * 弹出框 上部进度条，下部显示文字
	 * </pre>
	 * @param context
	 * @param cancelable
	 * @param progressbar 显示加载
	 * @param infoTxt 信息
	 * @param large 显示大加载
	 * @param cancelListener
	 * @return
	 */
	public static DialogFragment progressInfoDialog(FragmentTransaction transaction, String tag, boolean cancelable, boolean progressbar, String infoTxt, boolean large,
			OnCancelListener cancelListener) {
		DialogFragment df = new ProgressInfoDialogFragmentV4(large, progressbar, cancelable, infoTxt, cancelListener);
		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	/**
	 * 可自动消隐的提示框
	 * 
	 * @param context
	 * @param message
	 * @return
	 */
	public static DialogFragment promptDialog(FragmentTransaction transaction, String tag, String message) {
		return promptDialog(transaction, tag, message, null);
	}

	/**
	 * 可自动消隐的提示框
	 *
	 * @param transaction
	 * @param tag
	 * @param message
	 * @param listener
	 * @return
	 */
	public static DialogFragment promptDialog(FragmentTransaction transaction, String tag, String message, OnCancelListener listener) {
		return promptDialog(transaction, tag, message, 1000, listener);
	}

	/**
	 * 可自动消隐的提示框
	 * 
	 * @param context
	 * @param message
	 * @param durationMillis 提示框延迟自动消失的时间
	 * @param listener
	 * @return
	 */
	public static DialogFragment promptDialog(FragmentTransaction transaction, String tag, String message, long durationMillis, final OnCancelListener listener) {
		DialogFragment df = new PromptDialogFragmentV4(message, durationMillis, listener);

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	// 自定义顶部弹出Dialog
	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	public static DialogFragment topPopdownDialog(FragmentTransaction transaction, String tag, final int layoutResId, final OnCancelListener cancelLister) {
		DialogFragment df = new TopPopdownDialogFragment(layoutResId, true, cancelLister);

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	public static DialogFragment topPopdownDialog(FragmentTransaction transaction, String tag, View view, OnCancelListener cancelLister) {
		return topPopdownDialog(transaction, tag, view, true, true, cancelLister);
	}

	public static DialogFragment topPopdownDialog(FragmentTransaction transaction, String tag, View view, final boolean cancelable, final boolean cancelableOnTouchOutside,
			final OnCancelListener cancelLister) {
		DialogFragment df = new TopPopdownDialogFragment(view, cancelableOnTouchOutside, cancelLister);

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	public static DialogFragment topPopdownDialog(FragmentTransaction transaction, String tag, View view, final boolean cancelable, final boolean cancelableOnTouchOutside,
			final OnCancelListener cancelLister, final int height) {
		DialogFragment df = new TopPopdownDialogFragment(view, cancelableOnTouchOutside, cancelLister, -2, height);

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	// 自定义底部弹出Dialog
	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 底部弹出框
	 * @Description
	 * @param context
	 * @param layoutResID
	 * @param cancelable
	 * @param width
	 * @param height
	 * @return
	 */
	public static DialogFragment bottomPopupDialog(FragmentTransaction transaction, String tag, final int layoutResId, final OnCancelListener cancelLister) {
		DialogFragment df = new PcDialogFragmentV4() {

			/**
			 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				return new BottomPopupDialogSingle(getActivity(), layoutResId);
			}

			/**
			 * @see com.pc.app.dialog.v4.PcDialogFragmentV4#onCancel(android.content.DialogInterface)
			 */
			@Override
			public void onCancel(DialogInterface dialog) {
				if (null != cancelLister) {
					cancelLister.onCancel(dialog);
				}

				super.onCancel(dialog);
			}
		};

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	/**
	 * 底部弹出选项框
	 * @param context
	 * @param listeners
	 * @param onCancelListener
	 * @param arrayResId
	 * @param types
	 * @param titleResId
	 * @param d
	 * @return Dialog
	 */
	public static DialogFragment bottomPopupDialog(FragmentTransaction transaction, String tag, View.OnClickListener[] listeners, final OnCancelListener onCancelListener,
			int arrayResId, PcEmDialogType[] types, int titleResId) {
		DialogFragment df = new BottomPopupDialogFragment(arrayResId, types, listeners, true, onCancelListener, titleResId);

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	/**
	 * 底部弹出选项框
	 * @param context
	 * @param listeners
	 * @param onCancelListener
	 * @param arrayResId
	 * @param types
	 * @param title
	 * @param d
	 * @return
	 */
	public static DialogFragment bottomPopupDialog(FragmentTransaction transaction, String tag, View.OnClickListener[] listeners, final OnCancelListener onCancelListener,
			int arrayResId, PcEmDialogType[] types, String title) {
		return bottomPopupDialog(transaction, tag, listeners, true, onCancelListener, arrayResId, types, title);
	}

	/**
	 * 底部弹出选项框
	 * @param context
	 * @param listeners
	 * @param touchOutsideCanceled
	 * @param onCancelListener
	 * @param arrayResId
	 * @param types
	 * @param title
	 * @param d
	 * @return
	 */
	public static DialogFragment bottomPopupDialog(FragmentTransaction transaction, String tag, View.OnClickListener[] listeners, final boolean touchOutsideCanceled,
			final OnCancelListener onCancelListener, int arrayResId, PcEmDialogType[] types, String title) {

		DialogFragment df = new BottomPopupDialogFragment(arrayResId, types, listeners, touchOutsideCanceled, onCancelListener, title);

		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	// 底部弹出框ListView
	// ///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 底部弹出框ListView
	 * 
	 * <p>
	 * Dialog
	 * </p>
	 * @param context
	 * @param listeners 每个Menu对应的Click Listener,与Menu数组一一对应
	 * @param onCancelListener
	 * @param arrayResId Resources Id:代表一个Menu数组
	 * @param drawableResId Resources Id:代表一个Menu数组图标
	 * @param types 每个Menu对应的背景,与Menu数组一一对应
	 * @param title
	 * @return
	 */
	public static DialogFragment bottomPopupDialogExtras(FragmentTransaction transaction, String tag, View.OnClickListener[] listeners, final OnCancelListener onCancelListener,
			int arrayResId, int[] drawableResId, PcEmDialogType[] types, String title, String subTitle) {
		BottomPopupDialogFragmentListView df = new BottomPopupDialogFragmentListView(arrayResId, types, listeners, drawableResId, title, subTitle);
		if (null != transaction) {
			df.show(transaction, tag);
		}

		df.setOnCancelListener(onCancelListener);

		return df;
	}

	/**
	 * 底部输入弹出框
	 *
	 * @param transaction
	 * @param tag
	 * @param onOkListener
	 * @param hint
	 * @param inputMaxCount
	 * @return
	 */
	public static DialogFragment bottomPopupInputDialogFragment(FragmentTransaction transaction, String tag, final OnOkListener onOkListener, String hint, int inputMaxCount) {
		return bottomPopupInputDialogFragment(transaction, tag, onOkListener, hint, inputMaxCount, -1);
	}

	/**
	 * 底部输入弹出框
	 *
	 * @param transaction
	 * @param tag
	 * @param onOkListener
	 * @param hint
	 * @param inputMaxCount
	 * @param inputType
	 * @return
	 */
	public static DialogFragment bottomPopupInputDialogFragment(FragmentTransaction transaction, String tag, final OnOkListener onOkListener, String hint, int inputMaxCount,
			int inputType) {
		InputDialogFragment df = new InputDialogFragment(onOkListener, hint, inputMaxCount, inputType);
		if (null != transaction) {
			df.show(transaction, tag);
		}

		return df;
	}
}
