package com.example.flappybird

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import java.util.concurrent.atomic.AtomicBoolean


class GameView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    companion object {
        val gameGravity = Vec2(0, 0.002)
        const val fps = 120L
    }

    private val scorePaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLUE
        style = Paint.Style.FILL_AND_STROKE
        textSize = 50F
        textAlign = Paint.Align.LEFT

    }

    private val dimensions = run {
        val displayMetrics = context.resources.displayMetrics
        Vec2(
            displayMetrics.widthPixels,
            displayMetrics.heightPixels
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

    }

//    suspend fun gameLoop(scoreValueView: TextView) {
    suspend fun gameLoop(): Int {
        while (playing.get()) {
            if (!paused.get()) {
//                update(scoreValueView)
                update()
            }
            frameRateManager.delay()
        }
        return obstacleManager.score
    }


//    private fun update(scoreValueView: TextView) {
    private fun update() {
        val deltaT = frameRateManager.deltaT

        flappy.update(deltaT)
        obstacleManager.update(flappy.pos, deltaT)
//        scoreValueView.text = obstacleManager.score.toString()

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

    fun pause() {
        paused.set(true)
    }

    fun resume() {
        paused.set(false)
        frameRateManager.sanitize()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) { return }
        flappy.draw(canvas)
        obstacleManager.forEachObstacle { it.draw(canvas) }
        canvas.drawText(
            "Score: ${obstacleManager.score}",
            0F, 100F,
            scorePaint
        )
    }
}