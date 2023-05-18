package com.example.flappybird

import android.os.SystemClock

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

    private fun update() {
        previousFrameStartTime = currentFrameStartTime
        currentFrameStartTime = SystemClock.elapsedRealtime()
        deltaT = currentFrameStartTime - previousFrameStartTime
    }

    suspend fun delay() {
        delayWithoutUpdate()
        update()
    }

    suspend fun delayWithoutUpdate() {
        kotlinx.coroutines.delay(1000/fps)
    }
}