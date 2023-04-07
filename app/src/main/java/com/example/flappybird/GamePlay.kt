package com.example.flappybird

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Choreographer
import android.view.View
import android.widget.ImageView

class GamePlay : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GameView(this))
    }
    fun onClick(view: View) {
        println("FrameLayout Clicked!")
    }
}