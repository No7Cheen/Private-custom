package com.pc.utils.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pc.app.dialog.BottomPopupDialog;
import com.pc.app.dialog.BottomPopupDialogListView;
import com.pc.app.dialog.BottomPopupDialogSingle;
import com.pc.app.dialog.IDialogCancelListener;
import com.pc.app.dialog.IDialogOKClickListener;
import com.pc.app.dialog.modle.PcEmDialogType;
import com.pc.utils.StringUtils;
import com.pc.utils.android.sys.TerminalUtils;
import com.privatecustom.publiclibs.R;

/**
 * @author ChenJian
 * @date 2012-1-15
 */
public class PcDialogUtil {

	/**
	 * 设置Window attributes
	 *
	 * @param context
	 * @param d
	 */
	public static void setAttributes4PupfromBottom(Context context, Dialog d) {
		if (null == d) return;

		Window window = d.getWindow();
		WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
		int[] wh = TerminalUtils.terminalWH(context);
		windowParams.x = 0;
		windowParams.y = wh[1];
		windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(windowParams);
		window.setBackgroundDrawableResource(R.drawable.alert_dialog_background);
	}

	/**
	 * update Window attributes
	 * 
	 * windowParams.width = WindowManager.LayoutParams.MATCH_PARENT
	 * windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
	 *
	 * @param context
	 * @param d
	 */
	public static void updatewWindowParamsWH4PupfromBottom(Context context, Dialog d) {
		if (null == d) return;

		WindowManager.LayoutParams windowParams = d.getWindow().getAttributes();
		if (null == windowParams) return;

		windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		d.getWindow().setAttributes(windowParams);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	// Android系统默认Dialog
	// ///////////////////////////////////////////////////////////////////////////////////////////////////

	public static Dialog openMsgDialog(Context context, int icon, String title, String message, String okTxt, String cancelTxt, DialogInterface.OnClickListener okListener,
			DialogInterface.OnClickListener cancelListener) {
		return new AlertDialog.Builder(context).setIcon(icon).setTitle(title).setMessage(message).setPositiveButton(okTxt, okListener).setNegativeButton(cancelTxt, cancelListener)
				.create();
	}

	public static Dialog openLongMsgDialog(Context context, int icon, String title, String msg, String okTxt, String cancelTxt, DialogInterface.OnClickListener okListener,
			DialogInterface.OnClickListener cancelListener) {
		return new AlertDialog.Builder(context).setIcon(icon).setTitle(title).setMessage(msg).setPositiveButton(okTxt, okListener).setNegativeButton(cancelTxt, cancelListener)
				.create();
	}

	public static Dialog openListDialog(Context context, int icon, String title, String[] items, DialogInterface.OnClickListener listener) {
		return new AlertDialog.Builder(context).setIcon(icon).setTitle(title).setItems(items, listener).create();
	}

	public static Dialog openListDialog(Context context, int icon, String title, int itemsId, DialogInterface.OnClickListener listener) {
		return new AlertDialog.Builder(context).setIcon(icon).setTitle(title).setItems(itemsId, listener).create();
	}

	public static Dialog openListDialog(Context context, String title, int itemsId, DialogInterface.OnClickListener listener) {
		if (StringUtils.isNull(title)) {
			return new AlertDialog.Builder(context).setItems(itemsId, listener).create();
		} else {
			return new AlertDialog.Builder(context).setTitle(title).setItems(itemsId, listener).create();
		}
	}

	public static Dialog openSingleChoiceDialog(Context context, int icon, String title, String[] items, String okTxt, String cancelTxt, DialogInterface.OnClickListener listener,
			DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
		return new AlertDialog.Builder(context).setIcon(icon).setTitle(title).setSingleChoiceItems(items, 0, listener).setPositiveButton(okTxt, okListener)
				.setNegativeButton(cancelTxt, cancelListener).create();
	}

	public static Dialog openMultChoiceDialog(Context context, int icon, String title, String[] items, boolean[] defaultValues, String okTxt, String cancelTxt,
			DialogInterface.OnMultiChoiceClickListener listener, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
		return new AlertDialog.Builder(context).setIcon(icon).setTitle(title).setMultiChoiceItems(items, defaultValues, listener).setPositiveButton(okTxt, okListener)
				.setNegativeButton(cancelTxt, cancelListener).create();
	}

	public static ProgressDialog openSpinnerProgressDialog(Context context, String title, String message, String okTxt, String cancelTxt, final IDialogOKClickListener okListener,
			final IDialogCancelListener cancelListener) {
		ProgressDialog mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setTitle(title);
		mProgressDialog.setMessage(message);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, okTxt, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				if (okListener != null) {
					okListener.onClick();
				} else {
					dialog.dismiss();
				}
			}
		});

		mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancelTxt, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				if (cancelListener != null) {
					cancelListener.onCancel();
				} else {
					dialog.dismiss();
				}
			}
		});

		return mProgressDialog;
	}

	// 进度对话框，未修改
	public static ProgressDialog openSpinnerProgressDialog(Context context, String title, String message) {
		ProgressDialog mProgressDialog = new ProgressDialog(context);
		if (!StringUtils.isNull(title)) {
			mProgressDialog.setTitle(title);
		}
		if (!StringUtils.isNull(message)) {
			mProgressDialog.setMessage(message);
		}

		mProgressDialog.setProgressStyle(R.style.Loading_Dialog_Theme);

		return mProgressDialog;
	}

	public static Dialog openViewDialog(Context context, int icon, String title, View view, String okTxt, String cancelTxt, DialogInterface.OnClickListener okListener,
			DialogInterface.OnClickListener cancelListener) {

		AlertDialog d = new AlertDialog.Builder(context).setIcon(icon).setTitle(title).setView(view).setPositiveButton(okTxt, okListener)
				.setNegativeButton(cancelTxt, cancelListener).create();
		d.getWindow().setLayout(300, 300);

		return d;
	}

	public static Dialog openDefMsgDialog(Context context, String title, String message, String okTxt, String cancelTxt, DialogInterface.OnClickListener okListener,
			DialogInterface.OnClickListener cancelListener) {
		return new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton(okTxt, okListener).setNegativeButton(cancelTxt, cancelListener).create();
	}

	public static Dialog openDefMsgDialog(Context context, String title, String message, String okTxt, DialogInterface.OnClickListener okListener) {
		if (context == null) {
			return null;
		}
		return new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton(okTxt, okListener).create();
	}

	/**
	 * 没有提示标题
	 * @param context
	 * @param m_tTitle
	 * @param message
	 * @param okTxt
	 * @param cancelTxt
	 * @param okListener
	 * @param cancelListener
	 * @return
	 */
	public static Dialog openDefMsgDialog(Context context, String message, String okTxt, String cancelTxt, DialogInterface.OnClickListener okListener,
			DialogInterface.OnClickListener cancelListener) {
		return new AlertDialog.Builder(context).setMessage(message).setPositiveButton(okTxt, okListener).setNegativeButton(cancelTxt, cancelListener).create();
	}

	/**
	 * 弹出框
	 */
	public static Dialog loadPopupWindow(Context context, int layoutResID, boolean cancelable, int width, int height) {
		final Dialog dialog = new Dialog(context, R.style.My_Dialog_Theme);
		dialog.setContentView(layoutResID);
		dialog.setCancelable(cancelable);
		dialog.getWindow().setLayout(width, height);
		// dialog.show();

		return dialog;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	// 自定义Progress Dialog
	// ///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 消息/加载弹出框
	 * @Description
	 * @param context
	 * @param cancelable
	 * @param infoTxtRes
	 * @return
	 */
	public static Dialog progressInfoDialog(Context context, boolean cancelable, boolean progressbar, int infoTxtRes) {
		String infoTxt = context.getString(infoTxtRes);
		return progressInfoDialog(context, cancelable, progressbar, infoTxt);
	}

	/**
	 * 消息/加载弹出框
	 * @param context
	 * @param cancelable
	 * @param progressbar 显示加载
	 * @param infoTxt 信息
	 * @return
	 */
	public static Dialog progressInfoDialog(Context context, boolean cancelable, boolean progressbar, String infoTxt) {
		return progressInfoDialog(context, cancelable, progressbar, infoTxt, false);
	}

	/**
	 * 弹出框 上部进度条，下部显示文字
	 * <p>
	 * 默认加载框
	 * </p>
	 * @param context
	 * @param message
	 * @return
	 */
	public static Dialog loadDialog(Context context, String message) {
		return progressInfoDialog(context, false, true, message, false);
	}

	/**
	 * 消息弹出框
	 * @Description
	 * @param context
	 * @param cancelable
	 * @param infoTxt
	 * @return
	 */
	public static Dialog infoDialog(Context context, boolean cancelable, String infoTxt) {
		return progressInfoDialog(context, cancelable, false, infoTxt, false);
	}

	public static Dialog progressInfoDialog(Context context, boolean cancelable, boolean progressbar, int infoTxtRes, boolean large) {
		String infoTxt = context.getString(infoTxtRes);
		return progressInfoDialog(context, cancelable, progressbar, infoTxt, false);
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
	public static Dialog progressInfoDialog(Context context, boolean cancelable, boolean progressbar, String infoTxt, boolean large) {

		return progressInfoDialog(context, cancelable, progressbar, infoTxt, large, null);
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
	public static Dialog progressInfoDialog(Context context, boolean cancelable, boolean progressbar, String infoTxt, boolean large, OnCancelListener cancelListener) {
		View view = null;
		if (large) {
			view = LayoutInflater.from(context).inflate(R.layout.progress_dialog_txt_large, null);
		} else {
			view = LayoutInflater.from(context).inflate(R.layout.progress_dialog_txt, null);
		}
		TextView messageTextView = (TextView) view.findViewById(R.id.loading_info_txt);
		if (messageTextView != null) {
			if (StringUtils.isNull(infoTxt)) {
				messageTextView.setVisibility(View.GONE);
			} else {
				messageTextView.setText(infoTxt);
				messageTextView.setVisibility(View.VISIBLE);
			}
		}

		if (!progressbar) {
			view.findViewById(R.id.loading_progressbar).setVisibility(View.GONE);
		}

		final int WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT;
		Dialog dialog = new Dialog(context, R.style.Loading_Dialog_Theme);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;

		if (dialog != null) {
			dialog.addContentView(view, lp);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(cancelable);
			if (cancelable && cancelListener != null) {
				dialog.setOnCancelListener(cancelListener);
			}
		}

		return dialog;
	}

	/**
	 * 提示框
	 * @param context
	 * @param message
	 * @return
	 */
	public static Dialog promptDialog(Context context, String message) {
		return promptDialog(context, message, null);
	}

	public static Dialog promptDialog(Context context, String message, final OnCancelListener listener) {
		return promptDialog(context, message, 1000, listener);
	}

	/**
	 * 提示框
	 * @param context
	 * @param message
	 * @param durationMillis 提示框延迟自动消失的时间
	 * @param listener
	 * @return
	 */
	public static Dialog promptDialog(Context context, String message, final long durationMillis, final OnCancelListener listener) {
		View view = LayoutInflater.from(context).inflate(R.layout.progress_dialog_txt, null);
		final int wrap_content = android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(wrap_content, wrap_content);
		lp.gravity = Gravity.CENTER;
		final Dialog dialog = new Dialog(context, R.style.Loading_Dialog_Theme);
		view.findViewById(R.id.loading_progressbar).setVisibility(View.GONE);
		TextView messageTextView = (TextView) view.findViewById(R.id.loading_info_txt);
		if (messageTextView != null) {
			messageTextView.setText(message);
		}

		try {
			//
			final Handler handler = new Handler() {

				@Override
				public void handleMessage(Message message) {
					if (dialog != null) {
						dialog.cancel();
					}
				}
			};

			if (dialog != null) {
				if (null != listener) {
					dialog.setOnCancelListener(listener);
				}
				dialog.addContentView(view, lp);
				dialog.setCanceledOnTouchOutside(true);
				dialog.setCancelable(true);
				dialog.show();

				if (durationMillis > 0) {
					Message hanMessage = handler.obtainMessage();
					handler.sendMessageDelayed(hanMessage, durationMillis);
				}
			}
		} catch (Exception e) {
		}

		return dialog;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	// 自定义顶部弹出Dialog
	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	public static Dialog topPopdownDialog(Context context, int layoutResID, OnCancelListener cancelLister) {
		final Dialog dialog = new Dialog(context, R.style.Top_Dialog_Theme);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		int[] wh = TerminalUtils.terminalWH(context);
		params.x = 0;
		params.y = -wh[1];
		window.setAttributes(params);
		// window.setGravity(Gravity.TOP);
		dialog.setContentView(layoutResID);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		if (cancelLister != null) {
			dialog.setOnCancelListener(cancelLister);
		}

		dialog.show();

		return dialog;
	}

	public static Dialog topPopdownDialog(Context context, View view, OnCancelListener cancelLister) {

		return topPopdownDialog(context, view, true, true, cancelLister);
	}

	public static Dialog topPopdownDialog(Context context, View view, boolean cancelable, boolean cancelableOnTouchOutside, OnCancelListener cancelLister) {
		final Dialog dialog = new Dialog(context, R.style.Top_Dialog_Theme);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		int[] wh = TerminalUtils.terminalWH(context);
		params.x = 0;
		params.y = -wh[1];
		window.setAttributes(params);
		// window.setGravity(Gravity.TOP);
		dialog.setContentView(view);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(cancelableOnTouchOutside);
		if (cancelLister != null) {
			dialog.setOnCancelListener(cancelLister);
		}

		dialog.show();

		return dialog;
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
	public static Dialog bottomPopupDialog(Context context, int layoutResId, OnCancelListener cancelLister) {
		final Dialog dialog = new BottomPopupDialogSingle(context, layoutResId);

		if (null != dialog) {
			if (cancelLister != null) {
				dialog.setOnCancelListener(cancelLister);
			}

			dialog.show();
		}

		return dialog;
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
	public static Dialog bottomPopupDialog(Context context, View.OnClickListener[] listeners, final OnCancelListener onCancelListener, int arrayResId, PcEmDialogType[] types,
			int titleResId) {
		String title = context.getString(titleResId);

		return bottomPopupDialog(context, listeners, onCancelListener, arrayResId, types, title);
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
	public static Dialog bottomPopupDialog(Context context, View.OnClickListener[] listeners, final OnCancelListener onCancelListener, int arrayResId, PcEmDialogType[] types,
			String title) {
		return bottomPopupDialog(context, listeners, true, onCancelListener, arrayResId, types, title);
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
	public static Dialog bottomPopupDialog(Context context, View.OnClickListener[] listeners, final boolean touchOutsideCanceled, final OnCancelListener onCancelListener,
			int arrayResId, PcEmDialogType[] types, String title) {
		return new BottomPopupDialog(context, arrayResId, types, listeners, title);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	// 底部弹出框ListView
	// ///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 底部弹出框ListView
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
	public static Dialog bottomPopupDialogExtras(Context context, View.OnClickListener[] listeners, final OnCancelListener onCancelListener, int arrayResId, int[] drawableResId,
			PcEmDialogType[] types, String title, String subTitle) {

		BottomPopupDialogListView listDialog = new BottomPopupDialogListView(context, arrayResId, types, listeners, drawableResId, title, subTitle);
		if (null != listDialog && null != onCancelListener) {
			listDialog.setOnCancelListener(onCancelListener);
		}

		return listDialog;
	}
}
