package com.catonbrewster.buttonmatching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    internal lateinit var startButton: Button
    internal lateinit var gridView: GridView

    internal var gameStarted = false

    internal lateinit var timer: Timer
    private var seconds = 0
    internal val initialTime: Long = 0
    internal lateinit var timerTextView: TextView

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val TIME = "TIME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called.")

        startButton = findViewById(R.id.startButton)
        timerTextView = findViewById(R.id.timerTextView)
        timerTextView.text = getString(R.string.timer, seconds)

        startButton.setOnClickListener { view ->
            restoreGame()
        }
    }

    private fun resetGame() {
        //consistent default state when the game starts
        timerTextView.text = getString(R.string.timer, initialTime)
        //reset grid here
        gameStarted = false
    }


    private fun restoreGame() {
        startTimer()
        //reset grid here
        gameStarted = true
    }

    private fun startTimer() {

        // Get the text view.
        timerTextView.text = getString(R.string.timer, initialTime)
        seconds = 0

        // Creates a new Handler
        val handler = Handler()

        // Call the post() method,
        // passing in a new Runnable.
        // The post() method processes
        // code without a delay,
        // so the code in the Runnable
        // will run almost immediately.
        handler.post(object : Runnable {
            override fun run() {
                // Set the text view text.
                timerTextView.text = getString(R.string.timer, seconds)

                // If running is true, increment the
                // seconds variable.
                if (gameStarted) {
                    seconds++
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000)
            }
        })


    }


    private fun startGame() {
        startTimer()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.gameOverMessage, seconds), Toast.LENGTH_LONG).show()
        resetGame()
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(TIME, seconds)
        resetGame()

        Log.d(TAG, "onSaveInstance State: Saving Score: Time is $seconds")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called.")
    }
}