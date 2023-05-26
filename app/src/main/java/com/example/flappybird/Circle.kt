package com.example.flappybird

import android.graphics.Rect

class Circle(var pos: Vec2, var radius: Float) {
    fun checkOverlap(rect: Rect): Boolean {
        return vecToClosestPointOn(rect).magnitude() < radius
    }

    fun vecToClosestPointOn(rect: Rect): Vec2 {
        val closestPoint: Vec2
        try {
            closestPoint = Vec2(
                pos.x.coerceIn(
                    rect.left.toFloat(),
                    rect.right.toFloat()
                ),
                pos.y.coerceIn(
                    rect.top.toFloat(),
                    rect.bottom.toFloat()
                )
            )
        } catch (e: IllegalArgumentException) {
            println("Exception in Circle.vecToClosestPointOn")
            return Vec2(Float.MAX_VALUE)
        }
        return closestPoint - pos
    }
}