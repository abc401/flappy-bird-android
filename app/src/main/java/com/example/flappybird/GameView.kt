package com.example.flappybird

import android.content.Context
import android.graphics.Canvas
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicBoolean



@OptIn(DelicateCoroutinesApi::class)
class GameView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    companion object {
        val gameGravity = Vec2(0F, 0.002F)
        const val minObstacleGap = 100
    }

    private val dimensions = run {
        val displayMetrics = context.resources.displayMetrics
        Vec2(
            displayMetrics.widthPixels.toFloat(),
            displayMetrics.heightPixels.toFloat()
        )
    }

    private var playing = AtomicBoolean()

    private val flappy: Flappy

    private var obstacles: Array<Obstacle>

    private var previousFrameStartTime = 0L
    private var currentFrameStartTime = SystemClock.elapsedRealtime()
    private var deltaT = 0L


    init {
        playing.set(true)
        flappy = Flappy(Vec2(30F, dimensions.y/2))

        updateDeltaT()

        this.setOnClickListener {
            flappy.flap()
        }

        val obstacleCount = (dimensions.x / (Obstacle.width + minObstacleGap)).toInt()
        val totalEmptySpace = dimensions.x - (obstacleCount * Obstacle.width)
        val actualObstacleGap = totalEmptySpace / obstacleCount

        var currentObstaclePosition = dimensions.x
        obstacles = Array(obstacleCount) {
            val obstacle = Obstacle(currentObstaclePosition, dimensions)
            currentObstaclePosition += Obstacle.width + actualObstacleGap
            obstacle
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
        obstacles.forEach {
            it.update(deltaT)
        }
        if (isGameOver()) {
            onGameOver()
        }
        invalidate()
    }

    private fun isGameOver(): Boolean {
        obstacles.forEach {
            if (flappy.overlaps(it)) {
                return true
            }
        }
        return false
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
        obstacles.forEach {
            it.draw(canvas)
        }
    }
}