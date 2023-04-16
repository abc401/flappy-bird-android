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


@OptIn(DelicateCoroutinesApi::class)
class GameView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    companion object {
        val gameGravity = Vec2(0F, 0.002F)
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

    private var obstaclesMutex = Mutex()
    private var obstacles: Queue<Obstacle> = LinkedList()

    private var previousFrameStartTime = 0L
    private var currentFrameStartTime = SystemClock.elapsedRealtime()
    private var deltaT = 0L

    init {
        playing.set(true)
        flappy = Flappy(Vec2(30F, dimensions.y/2))
        obstacles.add(Obstacle(dimensions))

        updateDeltaT()

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
        obstaclesMutex.unlock()

        while (obstacle.shouldReset()) {
            obstaclesMutex.lock()
            obstacles.remove()
            obstacle = obstacles.element()
            obstaclesMutex.unlock()
        }
    }

    private suspend fun addObstacle() {
        val newObstacle = Obstacle(dimensions)

        obstaclesMutex.lock()
        val lastObstacle = obstacles.last()
        obstaclesMutex.unlock()

        val distBetweenOpenings = abs(
            lastObstacle.openingLocation - newObstacle.openingLocation
        )
        delay(200L + distBetweenOpenings.toLong()*2)

        obstaclesMutex.lock()
        obstacles.add(newObstacle)
        obstaclesMutex.unlock()
    }

    private suspend fun obstacleManagementLoop() {
        while (playing.get()) {
            removeOutOfBoundsObstacles()
            addObstacle()
        }
    }

    private suspend fun gameLoop() {
        while (playing.get()) {
            update()
            delay(15L)
        }
    }

    private fun updateDeltaT() {
        previousFrameStartTime = currentFrameStartTime
        currentFrameStartTime = SystemClock.elapsedRealtime()
        deltaT = currentFrameStartTime - previousFrameStartTime
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