package com.catonbrewster.buttonmatching

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Configuration
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

    internal lateinit var gridView: GridView
    internal lateinit var gridAdapter: GridAdapter
    internal lateinit var startButton: Button
    internal lateinit var quitButton: Button
    internal lateinit var timerTextView: TextView

    internal lateinit var timer: CountDownTimer
    internal val maxTime: Long = 600000
    internal var seconds: Long = 0
    internal var timerRunning: Boolean = false

    internal var numbers: ArrayList<Int> = arrayListOf()
    internal var remainingNums: ArrayList<Int> = arrayListOf()

    internal lateinit var wonGameDialog: AlertDialog

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val TIME = "TIME"
        private const val NUMBERS = "NUMBERS"
        private const val REMAINING_NUMBERS = "REMAINING_NUMBERS"
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridView.numColumns =  6
        } else {
            gridView.numColumns =  4
        }
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
            startNewGame()
        }

        quitButton = findViewById(R.id.quitButton)
        quitButton.setVisibility(View.GONE)
        quitButton.setOnClickListener {
            resetGame()
        }

        if (savedInstanceState != null) {
            Log.d(TAG, "Restoring Game from saved instance state")
            seconds = savedInstanceState.getLong(TIME)
            numbers = savedInstanceState.getIntegerArrayList(NUMBERS) as ArrayList<Int>
            remainingNums = savedInstanceState.getIntegerArrayList(REMAINING_NUMBERS) as ArrayList<Int>
            restoreGame()
            Log.d("seconds", "$seconds")
        } else {
            Log.d(TAG, "Resetting New Game")
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
        seconds = 0
        timerTextView.text = getString(R.string.timer, seconds)

        gridView.setAdapter(null)

        startButton.setVisibility(View.VISIBLE)
        quitButton.setVisibility(View.GONE)
        timerTextView.setVisibility(View.GONE)

        if (timerRunning) {
            timer.cancel()
        }

        timer =  object : CountDownTimer(maxTime,1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerRunning = true
                val time = (maxTime - millisUntilFinished) / 1000
                timerTextView.text = getString(R.string.timer, time)
                seconds = time
            }

            override fun onFinish() {
                timerRunning = false
                timesUp()
                timer.cancel()
            }
        }
    }

    private fun restoreGame() {
        //restore time
        timerTextView.text = getString(R.string.timer, seconds)

        //fill grid
        gridAdapter = GridAdapter(this@MainActivity, numbers)
        gridView.adapter = gridAdapter

        startButton.setVisibility(View.GONE)
        quitButton.setVisibility(View.VISIBLE)
        timerTextView.setVisibility(View.VISIBLE)

        if (timerRunning) {
            timer.cancel()
        }

        val timeTillMax = (maxTime - (seconds * 1000))
        timer =  object : CountDownTimer(timeTillMax,1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerRunning = true
                val time = (maxTime - millisUntilFinished) / 1000
                timerTextView.text = getString(R.string.timer, time)
                seconds = time
            }

            override fun onFinish() {
                timerRunning = false
                timesUp()
                timer.cancel()
            }

        }
        timer.start()
    }

    fun startNewGame() {
        //gen random numbers
        numbers = generateSequence {(1..100).random()}
            .distinct()
            .take(24)
            .toCollection(ArrayList())
        remainingNums = ArrayList(numbers)

        seconds = 0
        restoreGame()
    }

    fun wonGame() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.gameOverMessage, seconds))
        wonGameDialog = builder.create()
        wonGameDialog.show()
        resetGame()
    }

    fun timesUp() {
        val mainView = findViewById<ConstraintLayout>(R.id.mainView)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.timesUp))
        val dialog: AlertDialog = builder.create()
        dialog.show()
        resetGame()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putLong(TIME, seconds)
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

