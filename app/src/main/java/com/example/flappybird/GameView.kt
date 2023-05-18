package com.example.flappybird

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean


@OptIn(DelicateCoroutinesApi::class)
class GameView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    companion object {
        val gameGravity = Vec2(0F, 0.002F)
        const val fps = 120L
    }


    private val dimensions = run {
        val displayMetrics = context.resources.displayMetrics
        Vec2(
            displayMetrics.widthPixels.toFloat(),
            displayMetrics.heightPixels.toFloat()
        )
    }

    private val screenRect = Rect(0, 0, dimensions.x.toInt(), dimensions.y.toInt())

    private val frameRateManager = FrameRateManager(fps)
    private val obstacleManager = ObstacleManager(dimensions)

    private var playing = AtomicBoolean()
    private var paused = AtomicBoolean()

    private val flappy: Flappy

    init {
        playing.set(true)
        paused.set(false)

        flappy = Flappy(Vec2(30F, dimensions.y/2))
        this.setOnClickListener {
            flappy.flap()
        }

        GlobalScope.launch {
            gameLoop()
        }
    }

    private suspend fun gameLoop() {
        while (playing.get()) {
            if (!paused.get()) {
                update()
            }
            frameRateManager.delay()
        }
    }


    private fun update() {
        val deltaT = frameRateManager.deltaT

        flappy.update(deltaT)
        obstacleManager.update(deltaT)

        if (isGameOver()) {
            onGameOver()
        }

        invalidate()
    }

    private fun isGameOver(): Boolean {
        obstacleManager.forEachObstacle {
            if (flappy.checkAndCorrectOverlap(it)) {
                return true
            }
        }

        return !flappy.checkOverlap(screenRect)
    }

    private fun onGameOver() {
        playing.set(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        flappy.draw(canvas)
        obstacleManager.forEachObstacle {
            it.draw(canvas)
        }
    }
}