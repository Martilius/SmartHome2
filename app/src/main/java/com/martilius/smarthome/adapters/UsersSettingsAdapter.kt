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
import com.martilius.smarthome.models.Configuration
import com.martilius.smarthome.models.Cupboard
import com.martilius.smarthome.models.Rooms
import com.martilius.smarthome.models.UserSettingsRespond
import com.martilius.smarthome.repository.Repository
import com.martilius.smarthome.repository.remote.ConfigurationService
import com.martilius.smarthome.repository.remote.UserService
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

class UsersSettingsAdapter(
    private val listener: (UserSettingsRespond) -> Unit
) : ListAdapter<UserSettingsRespond, UsersSettingsAdapter.LedViewHolder>(DIFF_CALLBACK) {
//    val stompClient: StompClient = Stomp.over(
//        Stomp.ConnectionProvider.OKHTTP,
//        "ws://192.168.2.174:9999/mywebsocket/websocket"
//    )



    val stomp = StompService()

    inner class LedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val button: ToggleButton = itemView.findViewById(R.id.toggleButtonItemOnOff)
        fun bind(item: UserSettingsRespond, listener: (UserSettingsRespond) -> Unit) {
            itemView.apply {
                stomp.initial()
                val sharedPreferences = itemView.context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                if(sharedPreferences.getString("login","").equals(item.login)){
                    //itemView.background =ContextCompat.getDrawable(itemView.context,R.color.lightestGrey)
                    roomSettingsItemTitle.text = item.login+" (current)"
                    roomSettingsDeleteButton.isClickable= false
                    roomSettingsDeleteButton.isFocusable = false
                    roomSettingsDeleteButton.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.lightestGrey))
                }else{
                    roomSettingsItemTitle.text = item.login
                    roomSettingsDeleteButton.setOnClickListener {
                        stomp.sendMessage("/user/deleteUser/${item.login}")
                    }
                }
//                stompClient.connect()

                if(item.admin){
                    roomSettingsAdminToggleButton.check(R.id.roomSettingsAdminButton)
                }else{
                    roomSettingsAdminToggleButton.uncheck(R.id.roomSettingsAdminButton)
                }

                roomSettingsAdminToggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
                        if(isChecked&& roomSettingsAdminButton.isPressed){
                            stomp.sendMessage("/user/changeUser/${item.login}/true")
                        }else if(!isChecked && roomSettingsAdminButton.isPressed){
                            stomp.sendMessage("/user/changeUser/${item.login}/false")


                    }
                }


                setOnClickListener { listener(item) }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LedViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.room_settings_item, parent, false
        ).apply {



        }
        )

    override fun onBindViewHolder(holder: LedViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
        val item = getItem(position)


    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserSettingsRespond>() {
            override fun areItemsTheSame(oldItem: UserSettingsRespond, newItem: UserSettingsRespond) =
                oldItem.login == newItem.login

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: UserSettingsRespond, newItem: UserSettingsRespond) =
                oldItem == newItem
        }
    }


}