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
    fun timeNonZero_whenOrientationTurns() {
        //Given
        var controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        var systemUnderTest: MainActivity = controller.get()
        val startButton = systemUnderTest.findViewById(R.id.startButton) as Button

        //When
        startButton.performClick()

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
        val newTimerTextView = recreatedSystemUnderTest.findViewById(R.id.timerTextView) as TextView

        //Then
        assertNotEquals("0", newTimerTextView.text)
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


