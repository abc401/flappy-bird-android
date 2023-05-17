package com.example.flappybird

import android.content.Context
import android.graphics.Canvas
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.abs
import kotlin.math.pow


@OptIn(DelicateCoroutinesApi::class)
class GameView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    companion object {
        val gameGravity = Vec2(0F, 0.002F)
        val fps = 60L
    }

    private val dimensions = run {
        val displayMetrics = context.resources.displayMetrics
        Vec2(
            displayMetrics.widthPixels.toFloat(),
            displayMetrics.heightPixels.toFloat()
        )
    }

    private var playing = AtomicBoolean()
    private var paused = AtomicBoolean()

    private val flappy: Flappy

    private var obstaclesMutex = Mutex()
    private var obstacles: Queue<Obstacle> = LinkedList()

    private var deltaT = 0L
    private var previousFrameStartTime = 0L
    private var currentFrameStartTime = 0L


    init {
        playing.set(true)
        paused.set(false)

        flappy = Flappy(Vec2(30F, dimensions.y/2))
        obstacles.add(Obstacle(dimensions))

        sanitizeDeltaT()

        this.setOnClickListener {
            flappy.flap()
        }

        GlobalScope.launch {
            launch {
                obstacleManagementLoop()
            }
            launch {
                gameLoop()
            }
        }
    }

    private suspend fun removeOutOfBoundsObstacles() {
        obstaclesMutex.lock()
        var obstacle = obstacles.element()
        while (obstacle.shouldReset()) {
            obstacles.remove()
            obstacle = obstacles.element()
        }
        obstaclesMutex.unlock()
    }

    private suspend fun addObstacle() {
        val newObstacle = Obstacle(dimensions)

        obstaclesMutex.lock()
        val lastObstacle = obstacles.last()
        val distBetweenOpenings = abs(
            lastObstacle.openingLocation - newObstacle.openingLocation
        )
        obstaclesMutex.unlock()

        delay(200L + distBetweenOpenings.toLong()*2)

        obstaclesMutex.lock()
        obstacles.add(newObstacle)
        obstaclesMutex.unlock()
    }

    private suspend fun obstacleManagementLoop() {
        while (playing.get()) {
            if (paused.get()) {
                continue
            }
            removeOutOfBoundsObstacles()
            addObstacle()
        }
    }

    private suspend fun gameLoop() {
        while (playing.get()) {
            if (!paused.get()) {
                update()
            }
            delay(1000/fps )
        }
    }

    private fun updateDeltaT() {
        previousFrameStartTime = currentFrameStartTime
        currentFrameStartTime = SystemClock.elapsedRealtime()
        deltaT = currentFrameStartTime - previousFrameStartTime
    }

    private fun sanitizeDeltaT() {
        deltaT = 10.toDouble().pow(9).toLong() / fps
        previousFrameStartTime = SystemClock.elapsedRealtimeNanos()
        currentFrameStartTime = previousFrameStartTime + deltaT
    }

    private suspend fun update() {
        updateDeltaT()
        flappy.update(deltaT)

        obstaclesMutex.lock()
            obstacles.forEach { it.update(deltaT) }
            println("Array Size: ${obstacles.size}")
        obstaclesMutex.unlock()

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