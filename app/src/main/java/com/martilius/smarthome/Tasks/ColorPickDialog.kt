package com.martilius.smarthome.Tasks

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import com.github.dhaval2404.colorpicker.ColorPickerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import com.martilius.smarthome.R
import javax.inject.Inject

public class ColorPickDialog() {


    fun showDialog(context: Context, sharedPreferences: SharedPreferences, sharedprefsID:String, fab:FloatingActionButton, imageView: ImageView) {
        val dialog = Dialog(context)
        var hsv = FloatArray(3)
        var hsl = FloatArray(3)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.color_pick_fragment)
        val okBtn = dialog.findViewById(R.id.btok) as Button
        val cancelBtn = dialog.findViewById(R.id.btcancel) as Button
        val sliderValue = dialog.findViewById(R.id.sliderValueColorPicker) as Slider
        val sliderBrightness = dialog.findViewById(R.id.sliderBrightnessColorPicker) as Slider
        val colorPicker = dialog.findViewById(R.id.colorPicker) as ColorPickerView
        val tv = dialog.findViewById(R.id.tvVal) as TextView
        val tvValue = dialog.findViewById(R.id.tvColorValue) as TextView
        val tvBrightness = dialog.findViewById(R.id.tvColorBrightness) as TextView
        var color = sharedPreferences?.getString(sharedprefsID,Color.WHITE.toString()).toString().toInt()
        colorPicker.setBackgroundColor(color)
        colorPicker.setColor(color)
        Color.colorToHSV(color,hsv)
        ColorUtils.colorToHSL(color,hsl)

        sliderValue.value= (hsv[2] * 100)
        tvValue.text = "Color value: ${(hsv[2]*100).toInt()}%"
        sliderBrightness.value = (hsl[2]*100)
        tvBrightness.text = "Color brightness: ${(hsl[2]*100).toInt()}%"

        okBtn.setOnClickListener {
            fab.backgroundTintList =
                ColorStateList.valueOf(sharedPreferences.getString(sharedprefsID,Color.WHITE.toString()).toString().toInt())
            dialog.dismiss()
        }

        cancelBtn.setOnClickListener{
            fab.backgroundTintList =
                ColorStateList.valueOf(sharedPreferences.getString(sharedprefsID,Color.WHITE.toString()).toString().toInt())
            dialog.dismiss()
        }

        colorPicker.setColorListener { pickedColor, s ->
            colorPicker.setBackgroundColor(pickedColor)
            sharedPreferences.edit().putString(sharedprefsID, pickedColor.toString()).apply()
            Color.colorToHSV(pickedColor,hsv)
            sliderValue.value= (hsv[2] * 100)
            tv.text = hsv[2].toString()
            tvValue.text = "Color value: ${(hsv[2]*100).toInt()}%"
            ColorUtils.colorToHSL(pickedColor,hsl)
            tvBrightness.text = "Color brightness: ${(hsl[2]*100).toInt()}%"
            sliderBrightness.value = hsl[2]*100
            imageView.setBackgroundColor(pickedColor)
            UdpServices().sendWithoutRespond("set;${sharedprefsID};${Color.red(pickedColor)};${Color.green(pickedColor)};${Color.blue(pickedColor)}", context)
        }

        sliderValue.addOnChangeListener { slider, value, fromUser ->
            if(fromUser) {
                tvValue.text = "Color value: ${value.toInt()}%"
                hsv[2]=value/100
                color= Color.HSVToColor(hsv)
                //color=ColorUtils.HSLToColor(hsl)
                colorPicker.setColor(color)
                colorPicker.setBackgroundColor(color)
                ColorUtils.colorToHSL(color,hsl)
                tvBrightness.text = "Color brightness: ${(hsl[2]*100).toInt()}%"
                sliderBrightness.value = hsl[2]*100
                sharedPreferences.edit().putString(sharedprefsID, color.toString()).apply()
                imageView.setBackgroundColor(color)
                UdpServices().sendWithoutRespond("set;${sharedprefsID};${Color.red(color)};${Color.green(color)};${Color.blue(color)}", context)
            }
        }

        sliderBrightness.addOnChangeListener { slider, value, fromUser ->
            if(fromUser) {
                tvBrightness.text = "Color brightness: ${value.toInt()}%"
                hsl[2] = value / 100
                color = ColorUtils.HSLToColor(hsl)
                colorPicker.setColor(color)
                colorPicker.setBackgroundColor(color)
                Color.colorToHSV(color,hsv)
                tvValue.text = "Color value: ${(hsv[2]*100).toInt()}%"
                sliderValue.value = hsv[2]*100
                sharedPreferences.edit().putString(sharedprefsID, color.toString()).apply()
                imageView.setBackgroundColor(color)
                UdpServices().sendWithoutRespond("set;${sharedprefsID};${Color.red(color)};${Color.green(color)};${Color.blue(color)}", context)
            }
        }

        if(dialog.window!=null) dialog.window!!.attributes.windowAnimations = R.style.SlidingDialogAnimation
        dialog.show()
    }
}