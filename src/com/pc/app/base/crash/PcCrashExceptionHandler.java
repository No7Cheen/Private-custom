/**
 * @(#)CrashExceptionHandler.java 2014-1-16 Copyright 2014 it.kedacom.com, Inc.
 *                                All rights reserved.
 */

package com.pc.app.base.crash;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import android.content.Context;
import android.util.Log;

import com.pc.utils.StringUtils;
import com.pc.utils.android.sys.AppUtil;
import com.pc.utils.file.FileUtil;
import com.pc.utils.log.PcLog;
import com.pc.utils.time.TimeUtils;

/**
 * 异常捕获帮助类 
 * 
 * @author chenjian
 * @date 2014-1-16
 */

public class PcCrashExceptionHandler implements UncaughtExceptionHandler {

	private final String subject = "App for Android Phone exception report";

	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static PcCrashExceptionHandler INSTANCE = new PcCrashExceptionHandler();

	private Context mContext;

	private Icrash mIcrash;

	private PcCrashExceptionHandler() {
		mMailSubject = subject;
	}

	/**
	 * 获取CrashHandler实例 ,单例模式
	 *
	 * @return
	 */
	public static PcCrashExceptionHandler getInstance() {
		return INSTANCE;
	}

	private String mCrashDir;
	private boolean isSendMail;

	private String eMail;
	private String[] ccEmailArr;
	private String mMailSubject;

	/**
	 * 设置异常信息存储目录
	 *
	 * @param crashDir
	 */
	public void setCrashDir(String crashDir) {
		mCrashDir = crashDir;
	}

	/**
	 * 设置捕获异常是否发送邮件
	 *
	 * @param isSendMail
	 */
	public void setSendMail(boolean isSendMail) {
		this.isSendMail = isSendMail;
	}

	/**
	 * eMail Addr
	 *
	 * @param eMail
	 */
	public void setMailAddr(String eMail) {
		this.eMail = eMail;
	}

	/**
	 * 邮件抄送地址
	 *
	 * @param ccEmailArr
	 */
	public void setccEmailArr(String[] ccEmailArr) {
		this.ccEmailArr = ccEmailArr;
	}

	/**
	 * eMail subject
	 *
	 * @param subject
	 */
	public void setSubject(String subject) {
		mMailSubject = subject;
		if (StringUtils.isNull(mMailSubject)) {
			mMailSubject = subject;
		}
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context, Icrash crash) {
		mIcrash = crash;
		mContext = context;

		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		StringWriter stackTrace = new StringWriter();
		ex.printStackTrace(new PrintWriter(stackTrace));
		// System.err.println(stackTrace);
		StringBuffer sb = new StringBuffer();
		sb.append(TimeUtils.simpleDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
		sb.append("\n");
		sb.append(stackTrace.toString());

		// 退出
		// AppStackManager.Instance().exitApp();
		// AppStackManager.Instance().popAllActivity();

		final String info = sb.toString();

		if (null != mIcrash) {
			mIcrash.crash(stackTrace, info);
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				saveCrashInfo2File(info);
			}
		}).start();

		if (isSendMail) {
			AppUtil.sendeMile(mContext, eMail, ccEmailArr, mMailSubject, info);
		}

		if (PcLog.isPrint) {
			Log.e("CrashExceptionHandler", info);
		}

		// // 退出程序, 不需要调用
		// android.os.Process.killProcess(android.os.Process.myPid());
		// System.exit(1);
	}

	/**
	 * 保存错误信息到文件中
	 */
	private void saveCrashInfo2File(String crashinfo) {
		if (crashinfo == null || crashinfo.length() == 0) return;

		String fileName = new StringBuffer("crashinfo").append(TimeUtils.simpleDateFormat(new Date(), "yyyyMMdd-HHmmss")).append(".txt").toString();
		File file = FileUtil.createFile(mCrashDir, fileName);
		FileWriter fWriter = null;
		try {
			file.createNewFile();
			fWriter = new FileWriter(file, true);
			fWriter.write(crashinfo);
			fWriter.flush();
			fWriter.close();
		} catch (IOException e) {
		} finally {
			try {
				if (null != fWriter) {
					fWriter.close();
				}
			} catch (Exception e2) {
			}
		}
	}

}
