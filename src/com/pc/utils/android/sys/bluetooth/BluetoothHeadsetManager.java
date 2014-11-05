package com.pc.utils.android.sys.bluetooth;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import com.pc.app.PcBaseApplicationImpl;
import com.pc.utils.log.PcLog;

/**
 * use {@link #registeListener()}; to <br>
 * Afater used,remeber invoke {@link #unregisteListener()} to release;
 * 
 * @author RenYufeng
 * @date 2014-4-17
 */
public class BluetoothHeadsetManager {

	/**
	 * update interface
	 * 
	 * @author chenj
	 * @date 2014-8-8
	 */
	public interface IUpdataBlueToothState {

		public void updataModel();
	}

	private Context mContext;
	private BluetoothHeadset mBlueToothHeadSet;
	private IUpdataBlueToothState mUpdataBlueToothState;

	private BluetoothHeadsetManager() {
		mContext = PcBaseApplicationImpl.getContext();
	}

	private static BluetoothHeadsetManager mBluetoothManager;

	public static synchronized BluetoothHeadsetManager getInstance() {
		if (mBluetoothManager == null) {
			mBluetoothManager = new BluetoothHeadsetManager();
		}
		return mBluetoothManager;
	}

	/**
	 * 蓝牙是否连接
	 *
	 * @return
	 */
	@Deprecated
	public static boolean isConnectedBluetooth() {
		BluetoothHeadsetManager mBluetoothManager = BluetoothHeadsetManager.getInstance();
		if (null == mBluetoothManager) {
			return false;
		}

		return mBluetoothManager.isConnected();
	}

	/**
	 * 设置Update监听 
	 *
	 * @param _updataBlueToothState
	 */
	public void setUpdataBlueToothState(IUpdataBlueToothState _updataBlueToothState) {
		mUpdataBlueToothState = _updataBlueToothState;
	}

	/**
	 * 注册监听
	 * 
	 * registe BoradCastReciver to listen the bluetooth connection status
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void registeListener() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

		intentFilter.addAction(AudioManager.ACTION_SCO_AUDIO_STATE_CHANGED);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			intentFilter.addAction(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED);
		}
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);

		new Handler(mContext.getMainLooper()).postDelayed(new Runnable() {

			@Override
			public void run() {
				registeServiceConntection();
			}
		}, 500);

		// 已经注册过
		if (null != receiver) {
			return;
		}

		receiver = new BlueToothReceiver();
		mContext.registerReceiver(receiver, intentFilter);

	}

	/**
	 * 反注册
	 * 
	 * relese BoradCastReciver
	 */
	public void unregisteListener() {
		if (receiver != null && null != mContext) {
			mContext.unregisterReceiver(receiver);
			receiver = null;
		}

		releasePorxy();
	}

	/** 
	 * try switch the audio to bluetooth SCO
	 *
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public boolean tryRouteSCO() {
		// boolean b = registeServiceConntection();
		// check SCO is available

		return false;
	}

	private boolean isConnected;

	/**
	 * 蓝牙是否连接
	 *
	 * @return the isConnected 
	 */
	@Deprecated
	public boolean isConnected() {
		if (null == receiver) isConnected = false;

		return isConnected;
	}

	/**
	 * get the BluetoothProfile proxy
	 *
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private boolean registeServiceConntection() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return true;
		}

		if (!adapter.isEnabled()) {
			return false;
		}

		// close previous proxy
		releasePorxy();

		// registe a new proxy
		boolean b = adapter.getProfileProxy(mContext, new ServiceListener() {

			@Override
			public void onServiceConnected(int profile, BluetoothProfile proxy) {
				mBlueToothHeadSet = (BluetoothHeadset) proxy;
				List<BluetoothDevice> l = mBlueToothHeadSet.getConnectedDevices();
				if (l.size() > 0) {
					isConnected = true;
					AudioManager manager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
					// check SCO is available
					if (manager.isBluetoothScoAvailableOffCall()) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							manager.setMode(AudioManager.MODE_IN_COMMUNICATION);
						} else {
							manager.setMode(AudioManager.MODE_IN_CALL);
						}
						manager.setBluetoothScoOn(true);
						manager.startBluetoothSco();

					}
				}
			}

			@Override
			public void onServiceDisconnected(int profile) {
			}
		}, BluetoothProfile.HEADSET);

		if (PcLog.isPrint) {
			// DefToast.show(context, b ? "get proxy success!" :
			// "get proxy success!", 0);
		}

		return b;
	}

	/**
	 * close the proxy,otherwise causes a leaked Excepion
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void releasePorxy() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			if (mBlueToothHeadSet != null) {
				adapter.closeProfileProxy(BluetoothProfile.HEADSET, (BluetoothProfile) mBlueToothHeadSet);
			}
		}
	}

	/**
	 * switch the audio to local
	 */
	public void routeLocal() {
		AudioManager manager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		if (manager.isBluetoothScoOn()) {
			manager.setBluetoothScoOn(false);
			manager.stopBluetoothSco();
		}
		manager.setMode(AudioManager.MODE_NORMAL);
		manager.setSpeakerphoneOn(true);
	}

	private BroadcastReceiver receiver;

	class BlueToothReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// noisy
			if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
				// DefToast.showShortToast(context, "NOISY");
			} else if (AudioManager.ACTION_SCO_AUDIO_STATE_CHANGED.equals(action)
					|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED.equals(action))) {// SCO
				int state = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, 0);
				switch (state) {
					case AudioManager.SCO_AUDIO_STATE_CONNECTED:
						// we cannot route SCO right now, just delay the trying;
						new Handler(context.getMainLooper()).postDelayed(new Runnable() {

							@Override
							public void run() {
								registeServiceConntection();
							}
						}, 500);
						break;
					case AudioManager.SCO_AUDIO_STATE_DISCONNECTED:

						break;

					case AudioManager.SCO_AUDIO_STATE_ERROR:
						releasePorxy();
						break;
					default:
						break;
				}

			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {// ACL_found
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (PcLog.isPrint) {
					// DefToast.showShortToast(context,
					// "BluetoothDevice Found!\n" + device.getName() + "\n" +
					// device.getAddress());
				}
			} else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {// ACL_connected
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (PcLog.isPrint) {
					// DefToast.showShortToast(context,
					// "BluetoothDevice Connected!\n" + device.getName() + "\n"
					// + device.getAddress());
				}
				sendOpenSCOBroadcast();
			} else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {// ACL_disconnected
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (PcLog.isPrint) {
					// DefToast.showShortToast(context,
					// "BluetoothDevice Disconnected!\n" + device.getName() +
					// "\n"
					// + device.getAddress());
				}

				isConnected = false;
				if (mUpdataBlueToothState != null) {
					mUpdataBlueToothState.updataModel();
				} else {
					AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
					if (!audioManager.isWiredHeadsetOn()) {
						int volumeIndex = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);

						audioManager.setSpeakerphoneOn(true);

						// 设置音量
						audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, volumeIndex, AudioManager.STREAM_VOICE_CALL);
						audioManager.setMode(AudioManager.MODE_NORMAL);
					}
				}
			}
		}
	};

	private void sendOpenSCOBroadcast() {
		Intent i = new Intent();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			i.setAction(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED);
		}
		i.setAction(AudioManager.ACTION_SCO_AUDIO_STATE_CHANGED);
		i.putExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, AudioManager.SCO_AUDIO_STATE_CONNECTED);
		mContext.sendBroadcast(i);
	}

	// ///////////////////////////////////////////////////////////////////////
	// test
	// ///////////////////////////////////////////////////////////////////////

	private MusicService mMusicService;// just for test

	// for test
	public void playBackgroundMusic(Context context) {
		if (mMusicService == null) {
			Intent intent = new Intent(context, MusicService.class);
			context.bindService(intent, mConn, Service.BIND_AUTO_CREATE);
		}
	}

	// for test
	public void toogleMusic() {
		if (mMusicService != null) {
			mMusicService.toggleMusic();
		}
	}

	// for test
	public void releaseBackgroundMusic(Context context) {
		if (mMusicService != null) {
			context.unbindService(mConn);
			mMusicService = null;
		}
	}

	// for test
	private ServiceConnection mConn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			if (mMusicService != null) mMusicService.stopMusic();
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mMusicService = ((MusicService.LocalBinder) service).getService();
		}
	};

}
