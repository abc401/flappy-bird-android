package com.example.flappybird

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class GameOverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)
        val score = intent.getIntExtra(GamePlay.scoreExtraKey, 0)
        val scoreView = findViewById<TextView>(R.id.score_view)
        scoreView.text = "Score: $score"
    }

    fun confirmGameOver(view: View) {
        finish()
    }
}