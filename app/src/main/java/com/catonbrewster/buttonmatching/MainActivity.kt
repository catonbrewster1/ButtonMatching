package com.catonbrewster.buttonmatching

import android.app.AlertDialog
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
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

    internal lateinit var timer: CountDownTimer
    internal val maxTime: Long = 1000000
    internal var seconds = 0

    internal var numbers: ArrayList<Int> = arrayListOf()
    internal var remainingNums: ArrayList<Int> = arrayListOf()

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val TIME = "TIME"
        private const val NUMBERS = "NUMBERS"
        private const val REMAINING_NUMBERS = "REMAINING_NUMBERS"
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
            numbers = generateSequence {(1..100).random()}
                .distinct()
                .take(24)
                .toCollection(ArrayList())
            remainingNums = ArrayList(numbers)
            restoreGame()
        }

        quitButton = findViewById(R.id.quitButton)
        quitButton.setVisibility(View.GONE)
        quitButton.setOnClickListener {
            resetGame()
        }


        if (savedInstanceState != null) {
            Log.d(TAG, "savedInstanceState != null")
            seconds = savedInstanceState.getInt(TIME)
            numbers = savedInstanceState.getIntegerArrayList(NUMBERS) as ArrayList<Int>
            remainingNums = savedInstanceState.getIntegerArrayList(REMAINING_NUMBERS) as ArrayList<Int>
            Log.d(TAG, "seconds in restored onCreate:$seconds")
            Log.d(TAG, "numbers in restored onCreate:$numbers")
            Log.d(TAG, "remainingNums in restored onCreate:$remainingNums")
            restoreGame()
        } else {
            Log.d(TAG, "savedInstanceState == null")
            resetGame()
        }
    }

    fun hideButton(numToHide: Int) {
        remainingNums.remove(numToHide)
    }

    fun seeRemainingNums(): ArrayList<Int> {
        return remainingNums
    }

    private fun resetGame() {
        //consistent default state when the game starts
        timerTextView.text = getString(R.string.timer, 0)

        gridView.setAdapter(null)

        startButton.setVisibility(View.VISIBLE)
        quitButton.setVisibility(View.GONE)
        timerTextView.setVisibility(View.GONE)


        val timeTillMax = maxTime - seconds.toLong()
        timer =  object : CountDownTimer(timeTillMax,1000) {
            override fun onTick(millisUntilFinished: Long) {
                val time = (maxTime - millisUntilFinished) / 1000
                timerTextView.text = getString(R.string.timer, time)
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false
    }

    private fun restoreGame() {
        //restore time
        timerTextView.text = getString(R.string.timer, seconds)

        //fill grid
        Log.d(TAG, "numbers in restoreGame:$numbers")
        Log.d(TAG, "remaining numbers in restoreGame:$remainingNums")
        val gridAdapter = GridAdapter(this@MainActivity, numbers)
        gridView.adapter = gridAdapter

        startButton.setVisibility(View.GONE)
        quitButton.setVisibility(View.VISIBLE)
        timerTextView.setVisibility(View.VISIBLE)

        val timeTillMax = (maxTime - (seconds.toLong() * 1000))
        Log.d(TAG, "timeTillMax in restoreGame:$timeTillMax")
        timer =  object : CountDownTimer(timeTillMax,1000) {
            override fun onTick(millisUntilFinished: Long) {
                val time = (maxTime - millisUntilFinished) / 1000
                Log.d(TAG, "time in onTock:$time")
                timerTextView.text = getString(R.string.timer, time)
                val timeText = timerTextView.text
                Log.d(TAG, "timerTextView.text in onTock:$timeText")
                seconds = time.toInt()
                Log.d(TAG, "seconds in onTock:$seconds")
            }

            override fun onFinish() {
                endGame()
            }
        }

        timer.start()
        gameStarted = true
    }

    fun startGame() {
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

        timer.start()
        gameStarted = true
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
        outState.putIntegerArrayList(NUMBERS, numbers)
        outState.putIntegerArrayList(REMAINING_NUMBERS, remainingNums)
        timer.cancel()

        Log.d(TAG, "onSaveInstance State: State is $outState")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called.")
    }
}

