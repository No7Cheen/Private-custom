package com.pc.utils.android.sys.bluetooth;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.privatecustom.publiclibs.R;


	// for test
	public class MusicService extends Service {

		public MusicService() {
			super();
		}

		@Override
		public IBinder onBind(Intent intent) {
			IBinder binder = new LocalBinder();
			return binder;
		}

		public class LocalBinder extends Binder {

			public MusicService getService() {
				return MusicService.this;
			}
		}

		MediaPlayer mMediaPlayer;

		@Override
		public void onCreate() {
			super.onCreate();
			toggleMusic();
		}

		public void toggleMusic() {
			if (mMediaPlayer != null) {
				if (mMediaPlayer.isPlaying()) {
					mMediaPlayer.pause();
				} else {
					mMediaPlayer.start();
				}
				return;
			}
			try {
				mMediaPlayer = MediaPlayer.create(this, R.raw.qav_incoming);
				mMediaPlayer.setLooping(true);
				mMediaPlayer.start();
			} catch (Exception e) {
			}

		}

		public void stopMusic() {
			if (mMediaPlayer != null) {
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			stopMusic();
		}

}

