package com.remotearthsolutions.expensetracker.fragments.helpers

import android.widget.SeekBar


class RepeatDayListener(val callBack: (Int) -> Unit) : SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        callBack(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}