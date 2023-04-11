package com.example.flappybird

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class GamePlay : AppCompatActivity() {
//    private lateinit var gameView: GameView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)
//        gameView = findViewById(R.id.game_view)
    }
}