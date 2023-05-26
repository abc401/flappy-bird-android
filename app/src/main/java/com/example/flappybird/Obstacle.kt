package com.example.flappybird

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import kotlin.math.abs


class Obstacle(
    private val viewDimensions: Vec2,
) {
    companion object {
        const val width = 50F
        const val vel = -0.3F
        val maxOpeningSize = abs(Flappy.flapVelocity.y)*400 + Flappy.radius*2
        val minOpeningSize = abs(Flappy.flapVelocity.y)*200 + Flappy.radius*2
        const val minPipeLength = 100
    }

    private var pos = viewDimensions.x

    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.RED
        style = Paint.Style.FILL_AND_STROKE
    }

    private var flappyPreviouslyBehindObstacle = true
    private var flappyCurrentlyBehindObstacle = true

    val openingLocation: Float
        get() = _openingLocation

    private var _openingLocation = 0F
    private var openingSize = 0F

    val topRect: Rect
        get() = _topRect

    val bottomRect: Rect
        get() = _bottomRect

    private lateinit var _topRect: Rect
    private lateinit var _bottomRect: Rect

    init {
        randomize()
        updateRects()
    }

    fun updateFlappyData(flappyPosition: Vec2) {
        flappyPreviouslyBehindObstacle = flappyCurrentlyBehindObstacle
        flappyCurrentlyBehindObstacle = flappyPosition.x < pos+width
    }

    fun flappyJustCrossedObstacle(): Boolean {
        return flappyPreviouslyBehindObstacle && !flappyCurrentlyBehindObstacle
    }

    private fun updateRects() {
        val pos = pos.toInt()
        _topRect = Rect(
            pos,
            0,
            (pos+width).toInt(),
            _openingLocation.toInt()
        )
        _bottomRect = Rect(
            pos,
            (_openingLocation+openingSize).toInt(),
            (pos+width).toInt(),
            viewDimensions.y.toInt()
        )
    }

    fun update(deltaT: Number) {
        pos += vel * deltaT.toFloat()
        updateRects()
    }

    private fun randomize() {
        openingSize = lerp(minOpeningSize, maxOpeningSize, Math.random().toFloat())
        _openingLocation = lerp(
            minPipeLength.toFloat(),
            viewDimensions.y-openingSize-minPipeLength,
            Math.random().toFloat()
        )
        println("Opening Location: $_openingLocation")
        println("Opening Size: $openingSize")
    }


    fun isUseless() = pos+width < 0

    fun draw(canvas: Canvas) {
        canvas.drawRect(_topRect, paint)
        canvas.drawRect(_bottomRect, paint)
    }
}