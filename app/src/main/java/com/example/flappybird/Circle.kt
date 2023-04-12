package com.example.flappybird

import android.graphics.Rect

class Circle(var pos: Vec2, var radius: Float) {
    fun overlaps(rect: Rect): Boolean {
        val closestPoint = Vec2(
            pos.x.coerceIn(
                rect.left.toFloat(),
                rect.right.toFloat()
            ),
            pos.y.coerceIn(
                rect.top.toFloat(),
                rect.bottom.toFloat()
            )
        )
        val distance = closestPoint - pos
        return distance.magnitude() < radius
    }
}