package com.example.flappybird

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Flappy(pos: Vec2) {
    companion object {
        val flapVelocity = Vec2(0, -0.8)
        const val radius = 10F
    }
    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.GREEN
        style = Paint.Style.FILL_AND_STROKE
    }

    private var circle: Circle
    val pos: Vec2
        get() = circle.pos
    private var vel = Vec2()
    private val acc = GameView.gameGravity

    init {
        circle = Circle(pos, radius)
    }

    fun update(deltaT: Number) {
        vel += acc * deltaT
        circle.pos += vel * deltaT
    }

    fun flap() {
        vel = flapVelocity
    }

    fun checkOverlap(rect: Rect): Boolean {
        return circle.checkOverlap(rect)
    }

    fun checkAndCorrectOverlap(obstacle: Obstacle): Boolean {
        return checkAndCorrectOverlap(obstacle.topRect)
            || checkAndCorrectOverlap(obstacle.bottomRect)
    }

    private fun checkAndCorrectOverlap(rect: Rect): Boolean {
        val delta = circle.vecToClosestPointOn(rect)
        if (delta.magnitude() > circle.radius) {
            return false
        }
        circle.pos += delta - (delta.normalize() * circle.radius)
        return true
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(pos.x, pos.y, radius, paint)
    }
}