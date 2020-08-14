package com.martilius.smarthome.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.martilius.smarthome.R
import com.martilius.smarthome.Service.StompService
import com.martilius.smarthome.Tasks.ColorPickDialog
import com.martilius.smarthome.Tasks.UdpServices
import com.martilius.smarthome.di.modules.RepositoryModule_ProvideRepositoryFactory
import com.martilius.smarthome.models.*
import com.martilius.smarthome.repository.Repository
import com.martilius.smarthome.repository.remote.ConfigurationService
import com.martilius.smarthome.repository.remote.UserService
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.device_settings_item.view.*
import kotlinx.android.synthetic.main.item_led_rgb.view.*
import kotlinx.android.synthetic.main.item_on_off.view.*
import kotlinx.android.synthetic.main.room_settings_item.view.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import java.lang.Exception
import javax.inject.Inject

class DeviceSettingsAdapter(
    private val listener: (DeviceSettings) -> Unit
) : ListAdapter<DeviceSettings, DeviceSettingsAdapter.LedViewHolder>(DIFF_CALLBACK) {
//    val stompClient: StompClient = Stomp.over(
//        Stomp.ConnectionProvider.OKHTTP,
//        "ws://192.168.2.174:9999/mywebsocket/websocket"
//    )



    val stomp = StompService()

    inner class LedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val button: ToggleButton = itemView.findViewById(R.id.toggleButtonItemOnOff)
        fun bind(item: DeviceSettings, listener: (DeviceSettings) -> Unit) {
            itemView.apply {
                stomp.initial()

//                stompClient.connect()
                    deviceSettingsItemTitle.text = item.name


//                roomSettingsAdminToggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
//                        if(isChecked&& roomSettingsAdminButton.isPressed){
//                            stomp.sendMessage("/user/changeUser/${item.login}/true")
//                        }else if(!isChecked && roomSettingsAdminButton.isPressed){
//                            stomp.sendMessage("/user/changeUser/${item.login}/false")
//
//
//                    }
//                }


                setOnClickListener { listener(item) }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LedViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.device_settings_item, parent, false
        ).apply {



        }
        )

    override fun onBindViewHolder(holder: LedViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
        val item = getItem(position)


    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DeviceSettings>() {
            override fun areItemsTheSame(oldItem: DeviceSettings, newItem: DeviceSettings) =
                oldItem.id == newItem.id

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: DeviceSettings, newItem: DeviceSettings) =
                oldItem == newItem
        }
    }


}