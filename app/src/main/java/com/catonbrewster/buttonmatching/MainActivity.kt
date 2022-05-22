package com.catonbrewster.buttonmatching

import android.app.AlertDialog
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    internal var gameStarted = false

    internal lateinit var gridView: GridView
    internal lateinit var startButton: Button
    internal lateinit var quitButton: Button
    internal lateinit var timerTextView: TextView

    internal val initialTime: Long = 0
    private var seconds = 0

    private lateinit var numbers: ArrayList<Int>
    private lateinit var remainingNums: ArrayList<Int>

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val TIME = "TIME"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called.")

        timerTextView = findViewById(R.id.timerTextView)
        timerTextView.setVisibility(View.GONE)
        timerTextView.text = getString(R.string.timer, seconds)

        gridView = findViewById(R.id.gridView)

        startButton = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            startGame()
        }

        quitButton = findViewById(R.id.quitButton)
        quitButton.setVisibility(View.GONE)
        quitButton.setOnClickListener {
            resetGame()
        }
    }

    fun hideButton(numToHide: Int) {
        remainingNums.remove(numToHide)
    }

    fun getRemainingNums(): ArrayList<Int> {
        return remainingNums
    }

    private fun resetGame() {
        //consistent default state when the game starts
        timerTextView.text = getString(R.string.timer, initialTime)
        gridView.setAdapter(null)

        startButton.setVisibility(View.VISIBLE)
        quitButton.setVisibility(View.GONE)
        timerTextView.setVisibility(View.GONE)

        gameStarted = false
    }

    private fun startGame() {
        //gen random numbers
        numbers = generateSequence {(1..100).random()}
            .distinct()
            .take(24)
            .toCollection(ArrayList())
        remainingNums = ArrayList(numbers)

        //fill grid
        val gridAdapter = GridAdapter(this@MainActivity, numbers)
        gridView.adapter = gridAdapter

        startButton.setVisibility(View.GONE)
        quitButton.setVisibility(View.VISIBLE)
        timerTextView.setVisibility(View.VISIBLE)

        startTimer()
        gameStarted = true
    }

    private fun startTimer() {
        timerTextView.text = getString(R.string.timer, initialTime)
        seconds = 0

        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                if (gameStarted) {
                    timerTextView.text = getString(R.string.timer, seconds)
                    seconds++
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

    fun endGame() {
        val mainView = findViewById<ConstraintLayout>(R.id.mainView)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.gameOverMessage, seconds))
        val dialog: AlertDialog = builder.create()
        dialog.show()
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