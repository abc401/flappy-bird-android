package com.example.flappybird

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GamePlay : AppCompatActivity() {
    private lateinit var gameView: GameView
    companion object {
        const val scoreExtraKey = "score"
    }
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)
        gameView = findViewById(R.id.game_view)


        GlobalScope.launch {
            val score = gameView.gameLoop()
            val intent = Intent(applicationContext, GameOverActivity::class.java)
            intent.putExtra(scoreExtraKey, score)
            startActivity(intent)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }
}