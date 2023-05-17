package com.example.flappybird

import android.os.SystemClock
import java.time.Duration
import kotlin.math.pow

class FrameRateManager(private val fps: Long) {
    var deltaT = 0L
    private var previousFrameStartTime = 0L
    private var currentFrameStartTime = 0L
    
    init {
        sanitize()
    }

    fun sanitize() {
        deltaT = 1000 / fps
        previousFrameStartTime = SystemClock.elapsedRealtime()
        currentFrameStartTime = previousFrameStartTime + deltaT
    }

    private fun updateDeltaT() {
        previousFrameStartTime = currentFrameStartTime
        currentFrameStartTime = SystemClock.elapsedRealtime()
        deltaT = currentFrameStartTime - previousFrameStartTime
    }

    suspend fun delay() {
        kotlinx.coroutines.delay(deltaT)
        updateDeltaT()
    }
}