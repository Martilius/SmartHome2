package com.martilius.smarthome.Service

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.renderscript.Script
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.martilius.smarthome.R
import com.martilius.smarthome.models.RoomTypes
import kotlinx.android.synthetic.main.dropdown_menu_drawable.view.*

class CustomAdapter(val context: Context, resource: Int, private val  list: ArrayList<RoomTypes>) : BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }
    val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view:View =
            inflater.inflate(R.layout.dropdown_menu_drawable, parent, false)
        val customItem = list.get(position)
        view.ivItem.setImageResource(customItem.res)
        view.ivItem.setColorFilter(Color.BLACK)
        //view.tvItem.text = customItem.title
        return view
    }

    override fun getItem(p0: Int): Any {
    return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position,convertView,parent)
    }
}