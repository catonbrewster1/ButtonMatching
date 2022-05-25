package com.catonbrewster.buttonmatching

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.core.view.isVisible
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.w3c.dom.Text


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
        systemUnderTest.startNewGame()
        val newNumbers = systemUnderTest.numbers

        //Then
        assertNotEquals(newNumbers, origNumbers)
    }

    @Test
    fun timerAtZero_whenStart() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()
        systemUnderTest.startNewGame()
        val origNumbers = systemUnderTest.numbers

        //When
        systemUnderTest.startNewGame()
        val timerTextView = systemUnderTest.findViewById(R.id.timerTextView) as TextView

        //Then
        assertNotEquals(0.toString(), timerTextView.text)
    }

    @Test
    fun startButtonDisappears_whenStart() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()
        val startButton = systemUnderTest.findViewById(R.id.startButton) as Button

        //When
        startButton.performClick()

        //Then
        assertEquals(false, startButton.isVisible)
    }

    @Test
    fun quitButtonAppears_whenStart() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()
        val quitButton = systemUnderTest.findViewById(R.id.quitButton) as Button

        //When
        systemUnderTest.startNewGame()

        //Then
        assertEquals(true, quitButton.isVisible)
    }

    @Test
    fun timerAppears_whenStart() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()
        val timerTextView = systemUnderTest.findViewById(R.id.timerTextView) as TextView

        //When
        systemUnderTest.startNewGame()

        //Then
        assertEquals(true, timerTextView.isVisible)
    }

    @Test
    fun tileDisappears_whenCorrectNumberPressed() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()
        val gridView = systemUnderTest.findViewById(R.id.gridView) as GridView

        //When
        systemUnderTest.startNewGame()

        val numbers = systemUnderTest.numbers
        val minNum = numbers.minOrNull()
        val minNumPos = numbers.indexOf(minNum)
        val gridAdapter = systemUnderTest.gridAdapter
        val tileBefore = gridAdapter.getView(minNumPos, null, gridView)
        val tileButtonBefore = tileBefore.findViewById(R.id.gridButton) as Button

        tileButtonBefore.performClick()

        val tileAfter = gridAdapter.getView(minNumPos, null, gridView)
        val tileButtonAfter = tileAfter.findViewById(R.id.gridButton) as Button
        val remainingNums = systemUnderTest.remainingNums

        //then
        assertEquals(false, tileButtonAfter.isVisible)
        assertThat(
            remainingNums,
            not(hasItem(minNum))
        )
    }

    @Test
    fun tileDoesNotDisappear_whenIncorrectNumberPressed() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()
        val gridView = systemUnderTest.findViewById(R.id.gridView) as GridView

        //When
        systemUnderTest.startNewGame()

        val numbers = systemUnderTest.numbers
        val maxNum = numbers.maxOrNull()
        val maxNumPos = numbers.indexOf(maxNum)
        val gridAdapter = systemUnderTest.gridAdapter
        val tileBefore = gridAdapter.getView(maxNumPos, null, gridView)
        val tileButtonBefore = tileBefore.findViewById(R.id.gridButton) as Button

        tileButtonBefore.performClick()

        val tileAfter = gridAdapter.getView(maxNumPos, null, gridView)
        val tileButtonAfter = tileAfter.findViewById(R.id.gridButton) as Button
        val remainingNums = systemUnderTest.remainingNums

        //then
        assertEquals(true, tileButtonAfter.isVisible)
        assertThat(
            remainingNums,
            hasItem(maxNum)
        )
    }

    @Test
    fun boardPersists_whenOrientationTurns() {
        //Given
        var controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        var systemUnderTest: MainActivity = controller.get()
        systemUnderTest.startNewGame()
        val numbersBeforeTurn = systemUnderTest.numbers

        //When (rotate device)
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

        //Then
        val numbersAfterTurn = recreatedSystemUnderTest.numbers
        assertEquals(numbersBeforeTurn, numbersAfterTurn)
    }

    @Test
    fun alert_whenGameEnds() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()
        val gridView = systemUnderTest.findViewById(R.id.gridView) as GridView
        systemUnderTest.startNewGame()
        systemUnderTest.numbers = arrayListOf(1)
        systemUnderTest.remainingNums = arrayListOf(1)
        val gridAdapter = systemUnderTest.gridAdapter
        val tile = gridAdapter.getView(0, null, gridView)
        val tileButton = tile.findViewById(R.id.gridButton) as Button

        //When
        tileButton.performClick()

        //then
        assertEquals(true, systemUnderTest.wonGameDialog.isShowing())
    }

    @Test
    fun startButtonAppears_whenGameEnds() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()
        val gridView = systemUnderTest.findViewById(R.id.gridView) as GridView
        systemUnderTest.startNewGame()
        systemUnderTest.numbers = arrayListOf(1)
        systemUnderTest.remainingNums = arrayListOf(1)
        val gridAdapter = systemUnderTest.gridAdapter
        val tile = gridAdapter.getView(0, null, gridView)
        val tileButton = tile.findViewById(R.id.gridButton) as Button

        //When
        tileButton.performClick()

        //then
        val startButton = systemUnderTest.findViewById(R.id.startButton) as Button
        assertEquals(true, startButton.isVisible)
    }

    @Test
    fun quitButtonDisappears_whenGameEnds() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()
        val gridView = systemUnderTest.findViewById(R.id.gridView) as GridView
        systemUnderTest.startNewGame()
        systemUnderTest.numbers = arrayListOf(1)
        systemUnderTest.remainingNums = arrayListOf(1)
        val gridAdapter = systemUnderTest.gridAdapter
        val tile = gridAdapter.getView(0, null, gridView)
        val tileButton = tile.findViewById(R.id.gridButton) as Button

        //When
        tileButton.performClick()

        //then
        val quitButton = systemUnderTest.findViewById(R.id.quitButton) as Button
        assertEquals(false, quitButton.isVisible)
    }

    @Test
    fun timerDisappears_whenGameEnds() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()
        val gridView = systemUnderTest.findViewById(R.id.gridView) as GridView
        systemUnderTest.startNewGame()
        systemUnderTest.numbers = arrayListOf(1)
        systemUnderTest.remainingNums = arrayListOf(1)
        val gridAdapter = systemUnderTest.gridAdapter
        val tile = gridAdapter.getView(0, null, gridView)
        val tileButton = tile.findViewById(R.id.gridButton) as Button

        //When
        tileButton.performClick()

        //then
        val timerTextView = systemUnderTest.findViewById(R.id.timerTextView) as TextView
        assertEquals(false, timerTextView.isVisible)
    }
}


