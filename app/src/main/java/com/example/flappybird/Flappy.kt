package com.example.flappybird

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Flappy(private var pos: Vec2) {
    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.GREEN
        style = Paint.Style.FILL_AND_STROKE
    }
    private var vel = Vec2()
    private val acc = GameView.gameGravity

    fun update(deltaT: Number) {
        vel += acc * deltaT
        pos += vel * deltaT
    }

    fun flap() {
        vel.y = -0.8F
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(pos.x, pos.y, 10F, paint)
    }
}