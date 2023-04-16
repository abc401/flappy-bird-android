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
    }

    private var pos = viewDimensions.x

    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.RED
        style = Paint.Style.FILL_AND_STROKE
    }

    var leftOpeningLocation = 0F

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
//        tryReset()
        updateRects()
    }

    private fun randomize() {
        openingSize = lerp(minOpeningSize, maxOpeningSize, Math.random().toFloat())
        _openingLocation = lerp(
            0F,
            viewDimensions.y-openingSize,
            Math.random().toFloat()
        )
        println("Opening Location: $_openingLocation")
        println("Opening Size: $openingSize")
    }


    private fun tryReset() {
        if (!shouldReset()) {
            return
        }
        randomize()
        val openingLocationDifference = abs(openingLocation - leftOpeningLocation)
        pos = viewDimensions.x - width + openingLocationDifference * 0.5F
    }

    fun shouldReset(): Boolean {
        if (pos+width < 0) {
            return true
        }
        return false
    }

    private fun dealWithOutOfBoundsRect(rect: Rect, canvas: Canvas) {
        if (rect.left < 0) {
            rect.left = (viewDimensions.x + rect.left).toInt()
            rect.right = (viewDimensions.x + rect.right).toInt()
            canvas.drawRect(rect, paint)
        }
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(_topRect, paint)
//        dealWithOutOfBoundsRect(_topRect, canvas)
        canvas.drawRect(_bottomRect, paint)
//        dealWithOutOfBoundsRect(_bottomRect, canvas)
    }
}