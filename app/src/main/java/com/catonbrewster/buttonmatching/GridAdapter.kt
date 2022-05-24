package com.catonbrewster.buttonmatching

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import java.lang.Integer.getInteger


class GridAdapter(private val context: Context,
                  val dataSource: ArrayList<Int>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    var remainingNums: ArrayList<Int> = (context as MainActivity).seeRemainingNums()

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(pos: Int): Any {
        return dataSource[pos]
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for tile in grid
        val tileView = inflater.inflate(R.layout.grid_button, parent, false)
        if (parent.height > parent.width) {
            //portrait
            tileView.minimumHeight = parent.height / 6
        } else {
            //landscape
            tileView.minimumHeight = parent.height / 4
        }

        // Get button element
        val buttonView = tileView.findViewById(R.id.gridButton) as Button
        val number = getItem(pos) as Int
        buttonView.text = number.toString()

        //check if button should be visible
        remainingNums = (context as MainActivity).seeRemainingNums()

        if (remainingNums.contains(number)) {
            buttonView.setVisibility(View.VISIBLE)
        } else {
            buttonView.setVisibility(View.INVISIBLE)
        }

        val blinkAnimation = AnimationUtils.loadAnimation(context, R.anim.blink)

        buttonView.setOnClickListener {
            if (remainingNums.size == 1) {
                (context as MainActivity).hideButton(number)
                remainingNums = (context as MainActivity).seeRemainingNums()
                this.notifyDataSetChanged()
                (context as MainActivity).wonGame()
            } else {
                //buttons remain
                if (number == remainingNums.minOrNull()) {
                    //success
                    (context as MainActivity).hideButton(number)
                    remainingNums = (context as MainActivity).seeRemainingNums()
                    this.notifyDataSetChanged()
                }
                else {
                    //wrong button - bounce
                    buttonView.startAnimation(blinkAnimation)
                }
            }
        }
        Log.d("NEW MIN:", remainingNums.minOrNull().toString())
        Log.d("ORIG LIST:", "$dataSource")
        Log.d("NEW LIST:", "$remainingNums")

        return tileView
    }
}
