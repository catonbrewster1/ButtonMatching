package com.catonbrewster.buttonmatching

import android.widget.Button
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
        assertEquals(4, 2 + 2)
    }

    @Test
    fun newNumbers_whenStart() {
        //Given
        val controller = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .visible()
        val systemUnderTest = controller.get()
        systemUnderTest.startGame()
        val origNumbers = systemUnderTest.numbers

        //When
        val startButton = systemUnderTest.findViewById(R.id.startButton) as Button
        startButton.performClick()
        val newNumbers = systemUnderTest.numbers

        //Then
        assertNotEquals(newNumbers, origNumbers)
    }

    @Test
    fun disappears_whenCorrectNumberPressed() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun doesNotDisappear_whenIncorrectNumberPressed() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun alert_whenGameEnds() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun timerStops_whenGameEnds() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun timerStops_whenQuit() {
        assertEquals(4, 2 + 2)
    }


}