package com.zenora.elemento.common.helper

import android.os.SystemClock
import android.view.View
import java.util.*
import kotlin.math.abs

/**
 * A Debounced OnClickListener
 * Rejects clicks that are too close together in time.
 * This class is safe to use as an OnClickListener for multiple views, and will debounce each one separately.
 */
abstract class DebouncedOnClickListener protected constructor() : View.OnClickListener {
    private val lastClickMap: MutableMap<View, Long>

    /**
     * Implement this in your subclass instead of onClick
     */
    protected abstract fun onDebouncedClick(v: View?)
    override fun onClick(clickedView: View) {
        val previousClickTimestamp = lastClickMap[clickedView]
        val currentTimestamp = SystemClock.uptimeMillis()
        lastClickMap[clickedView] = currentTimestamp
        val minimumIntervalMillis: Long = 600
        if (previousClickTimestamp == null || abs(currentTimestamp - previousClickTimestamp) > minimumIntervalMillis) {
            onDebouncedClick(clickedView)
        }
    }

    /**
     * The one and only constructor
     */
    init {
        lastClickMap = WeakHashMap()
    }
}