package com.example.flappybird

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.view.View

val GAME_GRAVITY = Vec2(0F, 1F)

class GameView(context: Context) : View(context) {
    private val dimensions: Vec2
    private val flappy: Flappy

    init {
        val displayMetrics = context.resources.displayMetrics
        dimensions = Vec2(
            displayMetrics.widthPixels.toFloat(),
            displayMetrics.heightPixels.toFloat()
        )
        flappy = Flappy(Vec2(30F, dimensions.y/2))

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        flappy.draw(canvas)
    }
}