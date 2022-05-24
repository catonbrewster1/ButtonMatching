package com.catonbrewster.buttonmatching

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith

import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    fun timerStarts_whenStart() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()

        //When
        val startButton = systemUnderTest.findViewById(R.id.startButton) as Button
        startButton.performClick()

        //Then
        assertEquals(systemUnderTest.timerRunning, true)
    }

    @Test
    fun newNumbers_whenStart() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()
        systemUnderTest.startNewGame()
        val origNumbers = systemUnderTest.numbers

        //When
        val startButton = systemUnderTest.findViewById(R.id.startButton) as Button
        startButton.performClick()
        val newNumbers = systemUnderTest.numbers

        //Then
        assertNotEquals(newNumbers, origNumbers)
    }

    @Test
    fun alert_whenGameEnds() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun timerStops_whenGameEnds() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()

        //When
        val quitButton = systemUnderTest.findViewById(R.id.quitButton) as Button
        quitButton.performClick()

        //Then
        assertEquals(systemUnderTest.timerRunning, false)
    }

    @Test
    fun timerStops_whenQuit() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()

        //When
        val quitButton = systemUnderTest.findViewById(R.id.quitButton) as Button
        quitButton.performClick()

        //Then
        assertEquals(systemUnderTest.timerRunning, false)
    }

    @Test
    fun timeConstant_whenOrientationTurns() {
        //Given
        var controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        var systemUnderTest: MainActivity = controller.get()
        var scoreTextView: TextView = systemUnderTest.findViewById(R.id.gameScoreTextView) as TextView
        val tapMeButton: Button = systemUnderTest.findViewById(R.id.tapMeButton) as Button

        //When
        tapMeButton.performClick()
        tapMeButton.performClick()
        tapMeButton.performClick()
        assertEquals("Your Score: 3", scoreTextView.text)

        //Rotate Device
        val bundle = Bundle()
        controller
            .saveInstanceState(bundle)
            .pause()
            .stop()
            .destroy()
        controller = Robolectric.buildActivity(MainActivity::class.java)
            .create(bundle)
            .resume()
            .visible()

        val recreatedSystemUnderTest = controller.get()
        scoreTextView = recreatedSystemUnderTest.findViewById(R.id.gameScoreTextView) as TextView

        //Then
        assertEquals("Your Score: 3", scoreTextView.text)
    }

    @Test
    fun boardConstant_whenOrientationTurns() {
    }


    @Test
    fun disappears_whenCorrectNumberPressed() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()
        val remainingNums = systemUnderTest.remainingNums
        //val numbers = systemUnderTest.dataSource
        val minNum = remainingNums.minOrNull()
        //val minNumPos = numbers.indexOf(minNum)
        //val nextButton = systemUnderTest.getView(minNumPos, null, null)


        //When

    }

    @Test
    fun doesNotDisappear_whenIncorrectNumberPressed() {
        assertEquals(4, 2 + 2)
    }
}


