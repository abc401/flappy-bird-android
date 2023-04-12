package com.example.flappybird

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Flappy(pos: Vec2) {
    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.GREEN
        style = Paint.Style.FILL_AND_STROKE
    }

    private var circle: Circle
    private var vel = Vec2()
    private val acc = GameView.gameGravity

    init {
        circle = Circle(pos, 10F)
    }

    fun update(deltaT: Number) {
        vel += acc * deltaT
        circle.pos += vel * deltaT
    }

    fun flap() {
        vel.y = -0.8F
    }

    fun overlaps(obstacle: Obstacle): Boolean {
        return circle.overlaps(obstacle.topRect) || circle.overlaps(obstacle.bottomRect)
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(circle.pos.x, circle.pos.y, circle.radius, paint)
    }
}