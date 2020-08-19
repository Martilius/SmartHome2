package com.martilius.smarthome.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.martilius.smarthome.MainActivity
import com.martilius.smarthome.R
import com.martilius.smarthome.Service.StompService
import com.martilius.smarthome.models.Configuration
import kotlinx.android.synthetic.main.item_led_rgb.view.*

class LedAdapter(
    private val listener: (Configuration) -> Unit
) : ListAdapter<Configuration, LedAdapter.LedViewHolder>(DIFF_CALLBACK) {
//    val stompClient: StompClient = Stomp.over(
//        Stomp.ConnectionProvider.OKHTTP,
//        "ws://192.168.2.174:9999/mywebsocket/websocket"
//    )

    val stomp = StompService()

    inner class LedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val button: ToggleButton = itemView.findViewById(R.id.toggleButtonItemOnOff)
        fun bind(item: Configuration, listener: (Configuration) -> Unit) {
            itemView.apply {
                stomp.initial()
//                stompClient.connect()
                tvLedRGBTitle.text = item.name
                btAdditionalLightSwitch.isChecked = item.state.equals("on")

                btAdditionalLightSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
                    if (MainActivity.internetConnection) {
                        if (isChecked && btAdditionalLightSwitch.isPressed) {
                            stomp.sendMessage("/device/${item.ip}/${item.red}/${item.green}/${item.blue}/on")
                        } else if (!isChecked && btAdditionalLightSwitch.isPressed) {
                            stomp.sendMessage("/device/${item.ip}/${item.red}/${item.green}/${item.blue}/off")
                        }
                    } else {
                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                    }


                }

                setOnClickListener { listener(item) }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LedViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_led_rgb, parent, false
        ).apply {

            headerAdditionalLight.setOnClickListener {
                btAdditionalLightCardViewExpander.isChecked =
                    btAdditionalLightCardViewExpander.isChecked != true
            }
            btAdditionalLightCardViewExpander.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    TransitionManager.beginDelayedTransition(parent, AutoTransition())
                    contentAdditionalLightCardView.visibility = View.VISIBLE
                } else {
                    TransitionManager.beginDelayedTransition(parent, AutoTransition())
                    contentAdditionalLightCardView.visibility = View.GONE

                }
            }

//            btAdditionalLightSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
//                if (isChecked && btAdditionalLightSwitch.isPressed){
//                    GlobalScope.launch(Dispatchers.IO) {
//                        repository.changeState("4444","on")
//                    }
//                } else if(!isChecked && btAdditionalLightSwitch.isPressed){
//                    GlobalScope.launch(Dispatchers.IO) {
//                        repository.changeState("4444","off")
//                    }
//                }
//            }


        }
        )

    override fun onBindViewHolder(holder: LedViewHolder, position: Int) {
        holder.bind(getItem(position), listener)

        val item = getItem(position)
        lateinit var state: String
        if (holder.itemView.btAdditionalLightSwitch.isChecked) {
            state = "on"
        } else {
            state = "off"
        }
        holder.itemView.additionalLightWhiteButton.setOnClickListener {
            stomp.sendMessage("/device/${item.ip}/255/255/255/${state}")
            holder.itemView.ivAdditionalLightCardView.setBackgroundColor(Color.WHITE)
        }

        holder.itemView.additionalLightRedButton.setOnClickListener {
            stomp.sendMessage("/device/${item.ip}/255/0/0/${state}")
            holder.itemView.ivAdditionalLightCardView.setBackgroundColor(Color.WHITE)
        }

        holder.itemView.additionalLightGreenButton.setOnClickListener {
            stomp.sendMessage("/device/${item.ip}/0/255/0/${state}")
            holder.itemView.ivAdditionalLightCardView.setBackgroundColor(Color.GREEN)
        }

        holder.itemView.additionalLightBlueButton.setOnClickListener {
            stomp.sendMessage("/device/${item.ip}/0/0/255/${state}")
            holder.itemView.ivAdditionalLightCardView.setBackgroundColor(Color.BLUE)
        }

        holder.itemView.btPickCustomColor.setOnClickListener {
            // ColorPickDialog().showDialog(context,sharedPreferences,sendingAlIDN,additionalLightCustomButton, ivAdditionalLightCardView)
        }

        holder.itemView.additionalLightCustomButton.backgroundTintList =
            ColorStateList.valueOf(Color.rgb(item.red, item.green, item.blue))
        holder.itemView.ivAdditionalLightCardView.setBackgroundColor(
            Color.rgb(
                item.red,
                item.green,
                item.blue
            )
        )

    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Configuration>() {
            override fun areItemsTheSame(oldItem: Configuration, newItem: Configuration) =
                oldItem.id == newItem.id

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Configuration, newItem: Configuration) =
                oldItem == newItem
        }
    }


}