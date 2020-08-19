package com.martilius.smarthome.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.martilius.smarthome.R
import com.martilius.smarthome.Service.StompService
import com.martilius.smarthome.models.Configuration
import com.martilius.smarthome.models.Rooms
import com.martilius.smarthome.repository.remote.ConfigurationService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_headlight.view.*
import kotlinx.android.synthetic.main.item_led_rgb.view.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent


class HeadLightAdapter(
    private val listener: (Configuration) -> Unit
) : ListAdapter<Configuration, HeadLightAdapter.HeadLightViewHolder>(DIFF_CALLBACK) {


    val stomp = StompService()

    inner class HeadLightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Configuration, listener: (Configuration) -> Unit) {
            itemView.apply {
                stomp.initial()
//                val stompClient: StompClient = Stomp.over(
//                    Stomp.ConnectionProvider.OKHTTP,
//                    "ws://192.168.2.174:9999/mywebsocket/websocket"
//                )
                tvHeadLightTitle.text = item.name
                btHeadLightSwitch.isChecked = item.state.equals("on")
                if(item.state.equals("on")){
                    ivHeadLightCardView.setImageResource(R.drawable.lampv2on)
                }else{
                    ivHeadLightCardView.setImageResource(R.drawable.lampv2off)
                }
//                stompClient.connect()
//                stompClient.lifecycle()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(Schedulers.computation())
//                    .subscribe({
//                        when (it.type) {
//                            LifecycleEvent.Type.OPENED -> {
//                                // viewModel.receive(it.type.name)
//                            }
//                            LifecycleEvent.Type.ERROR -> {
//                                //check(it.message.toString())
//                                //  viewModel.receive(it.type.name)
//                            }
//                            LifecycleEvent.Type.CLOSED -> {
//                                //   viewModel.receive(it.type.name)
//                            }
//                        }
//                    }, { t: Throwable ->
//                        // viewModel.receive(t.toString())
//                    })
                btHeadLightSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
                    if (isChecked && (btHeadLightSwitch.isPressed || headerHeadLight.isPressed)) {
                        ivHeadLightCardView.setImageResource(R.drawable.lampv2on)
                        stomp.sendMessage("/device/${item.ip}/${item.red}/${item.green}/${item.blue}/on")
//                            .unsubscribeOn(Schedulers.newThread())
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe()
                    } else if (!isChecked && (btHeadLightSwitch.isPressed || headerHeadLight.isPressed)) {
                        ivHeadLightCardView.setImageResource(R.drawable.lampv2off)
                        stomp.sendMessage("/device/${item.ip}/${item.red}/${item.green}/${item.blue}/off")
//                            .unsubscribeOn(Schedulers.newThread())
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe()
                    }

                }
                //setOnClickListener { listener(item) }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HeadLightViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_headlight, parent, false
        ).apply {

            cvHeadLight.setOnClickListener {
                btHeadLightSwitch.isChecked = btHeadLightSwitch.isChecked != true
            }
        }
        )

    override fun onBindViewHolder(holder: HeadLightViewHolder, position: Int) {
        holder.bind(getItem(position), listener)


    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Configuration>() {
            override fun areItemsTheSame(oldItem: Configuration, newItem: Configuration) =
                oldItem.name == newItem.name

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Configuration, newItem: Configuration) =
                oldItem == newItem
        }
    }



}