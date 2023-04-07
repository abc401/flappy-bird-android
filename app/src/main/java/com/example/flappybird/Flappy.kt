package com.example.flappybird

import android.graphics.Canvas
import android.graphics.Paint

class Flappy(private var pos: Vec2) {
    private var vel = Vec2()
    private val acc = GAME_GRAVITY

    fun update() {
        vel += acc
        pos += vel
    }

    fun flap() {
        vel = Vec2(y = -20F)
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(pos.x, pos.y, 10F, Paint())
    }
}