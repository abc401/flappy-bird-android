package com.example.flappybird

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.os.SystemClock
import android.view.View
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

val GAME_GRAVITY = Vec2(0F, 0.002F)

@OptIn(DelicateCoroutinesApi::class)
class GameView(context: Context) : View(context) {
    private val dimensions: Vec2
    private val flappy: Flappy
    private var playing = AtomicBoolean()

//    private var frameRate = 30F
    private var previousFrameStartTime = 0L
    private var currentFrameStartTime = SystemClock.elapsedRealtime()
    private var deltaT = 0L


    init {
        playing.set(true)
        val displayMetrics = context.resources.displayMetrics
        dimensions = Vec2(
            displayMetrics.widthPixels.toFloat(),
            displayMetrics.heightPixels.toFloat()
        )
        flappy = Flappy(Vec2(30F, dimensions.y/2))

        updateDeltaT()

        this.setOnClickListener {
            flappy.flap()
        }

        GlobalScope.launch {
            while (playing.get()) {
                update()
                delay(15L)
            }
        }
    }

    private fun updateDeltaT() {
        previousFrameStartTime = currentFrameStartTime
        currentFrameStartTime = SystemClock.elapsedRealtime()
        deltaT = currentFrameStartTime - previousFrameStartTime
    }

    private fun update() {
        updateDeltaT()
        flappy.update(deltaT)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        flappy.draw(canvas)
    }
}