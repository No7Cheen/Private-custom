/**
 * @(#)ApplicationBm.java 2014-2-12 Copyright 2014 it.kedacom.com, Inc. All
 *                        rights reserved.
 */

package com.pc.app;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.util.Log;

import com.pc.utils.StringUtils;
import com.pc.utils.file.PcAbsPathManager;

/**
 * supper Application
 * 
 * @author chenjian
 * @date 2014-2-12
 */

public class PcBaseApplicationImpl extends Application {

	public static PcBaseApplicationImpl mOurApplication;

	public static Context getContext() {
		return mOurApplication.getApplicationContext();
	}

	/**
	 * @see android.content.ContextWrapper#getApplicationContext()
	 */
	@Override
	public Context getApplicationContext() {
		return super.getApplicationContext();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		getApplicationContext();

		mOurApplication = this;

		new PcAbsPathManager(getRootDirName(), getApprootDirName()) {

			/**
			 * @see com.pc.utils.file.PcAbsPathManager#createRootDir()
			 */
			@Override
			public File createRootDir() {
				return super.createRootDir();
			}

			/**
			 * @see com.pc.utils.file.PcAbsPathManager#createAppRootDir()
			 */
			@Override
			public File createAppRootDir() {
				return super.createAppRootDir();
			}
		};
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * @see android.content.ContextWrapper#getPackageName()
	 */
	@Override
	public String getPackageName() {
		return super.getPackageName();
	}

	/**
	 * 根目录名
	 *
	 * @return <meta-data  android:name="com.kedacom.truetouch.Approot" android:value="PcDir" />
	 */
	public String getRootDirName() {
		try {
			ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			String rootDir = appInfo.metaData.getString("com.privatecustom.publiclibs.RootDir");
			if (StringUtils.isNull(rootDir)) {
				rootDir = "PcDir";
			}

			return rootDir;
		} catch (Exception e) {
			return "PcDir";
		}
	}

	/**
	 * 根目录绝对路径
	 *
	 * @return /sdcard/mRootDir/
	 */
	public String getRootDir4AbsolutePath() {
		return new PcAbsPathManager(getRootDirName(), getApprootDirName()) {
		}.getRootDir();
	}

	/**
	 * App跟目录名
	 *
	 * @return <meta-data  android:name="com.privatecustom.publiclibs.Approot" android:value="PcAppDir" />
	 */
	public String getApprootDirName() {
		try {
			ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			String appRootDir = appInfo.metaData.getString("com.privatecustom.publiclibs.Approot");
			if (StringUtils.isNull(appRootDir)) {
				appRootDir = "PcAppDir";
			}

			return appRootDir;
		} catch (Exception e) {
			return "PcAppDir";
		}
	}

	/**
	 * App根目录绝对路径
	 *
	 * @return /sdcard/mRootDir/mAppRootDir/
	 */
	public String getApprootDir4AbsolutePath() {
		return new PcAbsPathManager(getRootDirName(), getApprootDirName()) {
		}.getAppRootDir();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

		Log.i(PcBaseApplicationImpl.class.getSimpleName(), "onTerminate...");
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();

		Log.e(PcBaseApplicationImpl.class.getSimpleName(), "onLowMemory...");
	}

}
