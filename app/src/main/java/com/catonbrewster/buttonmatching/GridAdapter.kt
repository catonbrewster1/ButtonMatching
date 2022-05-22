package com.catonbrewster.buttonmatching

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


class GridAdapter(private val context: Context,
                     private val dataSource: IntArray) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

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
        tileView.minimumHeight = parent.height / 6
        val number = getItem(pos)


        // Get button element
        val buttonView = tileView.findViewById(R.id.gridButton) as Button
        buttonView.text = number.toString()
        //buttonView.setBackgroundColor()
        buttonView.setOnClickListener {
            buttonView.text = "clicked"
        }
        return tileView
    }
}
