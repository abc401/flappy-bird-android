package com.example.flappybird

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect


class Obstacle(private var pos: Float, private val viewDimensions: Vec2) {
//    private var pos = viewDimensions.x

    companion object {
        const val width = 50F
        const val vel = -0.3F
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.RED
        style = Paint.Style.FILL_AND_STROKE
    }

    private val openingLocation = 500F
    private val openingSize = 200F

    private lateinit var topRect: Rect
    private lateinit var bottomRect: Rect

    init {
        updateRects()
    }


    private fun updateRects() {
        val pos = pos.toInt()
        topRect = Rect(
            pos,
            0,
            (pos+width).toInt(),
            openingLocation.toInt()
        )
        bottomRect = Rect(
            pos,
            (openingLocation+openingSize).toInt(),
            (pos+width).toInt(),
            viewDimensions.y.toInt()
        )
    }

    fun update(deltaT: Number) {
        pos += vel * deltaT.toFloat()
        tryReset()
        updateRects()
    }

    private fun tryReset() {
        if (shouldReset()) {
            pos = viewDimensions.x - width
        }
    }

    private fun shouldReset(): Boolean {
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
        canvas.drawRect(topRect, paint)
        dealWithOutOfBoundsRect(topRect, canvas)
        canvas.drawRect(bottomRect, paint)
        dealWithOutOfBoundsRect(bottomRect, canvas)
    }
}