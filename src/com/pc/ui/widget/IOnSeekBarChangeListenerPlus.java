/**
 * @(#)IOnSeekBarChangeListenerPlus.java   2014-6-17
 * Copyright 2014  it.kedacom.com, Inc. All rights reserved.
 */

package com.pc.ui.widget;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * @author chenj
 * @date 2014-6-17
 */

public interface IOnSeekBarChangeListenerPlus extends OnSeekBarChangeListener {

	void onSeekbarSub(SeekBar seekBar, int progress);

	void onSeekbarAdd(SeekBar seekBar, int progress);

	void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
}
