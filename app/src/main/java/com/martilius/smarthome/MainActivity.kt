package com.martilius.smarthome

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.martilius.smarthome.Service.CustomAdapter
import com.martilius.smarthome.adapters.NewDeviceAdapter
import com.martilius.smarthome.models.*
import com.martilius.smarthome.ui.fragments.PawelsRoomFragment
import com.martilius.smarthome.ui.fragments.SettingsFragment
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adding_devices_dialog.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.room_adding_dialog.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<MainViewModel> { factory }
    var title : String? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    var position:Int = 0

    private val newDeviceAdapter by lazy {
        NewDeviceAdapter {

        }
    }

    val stompClient: StompClient = Stomp.over(
        Stomp.ConnectionProvider.OKHTTP,
        "ws://192.168.2.174:9999/mywebsocket/websocket"
    )

    companion object {
        var internetConnection = false
    }
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreference =  getSharedPreferences("userInfo",Context.MODE_PRIVATE)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val toggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.bbbb, R.string.bbbb)
                drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //initBackStackChangeListener(toggle)

        lateinit var deviceTypes: List<DeviceType>

        viewModel.connection(stompClient, navView, applicationContext)
//        toolbar.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.action_settings -> {
//                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_settings)
//                    true
//                }
//                else -> false
//            }
//        }
        logOutBt.setOnClickListener {
            sharedPreference.edit()
                .putBoolean("admin", false)
                .putBoolean("logged",false)
                .apply()
            PawelsRoomFragment().resetSubscription()
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_nav_pawels_room_to_nav_login)
        }


        fabAddDevice.setOnClickListener { view ->
            var roomsList = mutableListOf<String>()
            var deviceTypeList = mutableListOf<String>()
            deviceTypes.forEach {
                deviceTypeList.add(it.deviceType.toString())
            }
            navView.menu.forEach {
                roomsList.add(it.title.toString())
            }
            roomsList.remove(roomsList.last())
            val roomsAdapter = ArrayAdapter<String>(this, R.layout.dropdown_menu_item, roomsList)
            val deviceTypeAdapter =
                ArrayAdapter<String>(this, R.layout.dropdown_menu_item, deviceTypeList)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.adding_devices_dialog, null)


            dialogView.roomAutoCompleteTextView.setAdapter(roomsAdapter)
            dialogView.deviceTypeAutoCompleteTextView.setAdapter(deviceTypeAdapter)
            dialogView.roomAutoCompleteTextView.setText(fragmentTitle.text.toString(), false)
            dialogView.rvNewDevice.adapter = newDeviceAdapter
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(dialogView)

            with(viewModel){
                deviceIp.observe(this@MainActivity, Observer {
                    if(!newDeviceAdapter.currentList.contains(it.toString())&& !newDeviceAdapter.currentList.isNullOrEmpty()){
                        if(dialog.isShowing){
                            newDeviceAdapter.currentList.add(it)
                        }
                    }else if(newDeviceAdapter.currentList.isNullOrEmpty()){
                        if(dialog.isShowing){
                            newDeviceAdapter.submitList(listOf(it))
                            dialogView.addDeviceProgressBar.visibility = View.GONE
                        }
                    }
                })
            }
            if(!newDeviceAdapter.currentList.isNullOrEmpty()){
                newDeviceAdapter.submitList(null)
                dialogView.addDeviceProgressBar.visibility = View.GONE
            }
            dialog.show()
            //Toast.makeText(applicationContext, dialogView.deviceTypeAutoCompleteTextView.text.toString(), Toast.LENGTH_LONG).show()
            dialogView.btCancelAddDevice.setOnClickListener { dialog.dismiss() }
            dialogView.btAddDevice.setOnClickListener {
                if (newDeviceAdapter.getSelected() != -1) {
                    val selected = newDeviceAdapter.currentList.get(newDeviceAdapter.getSelected())
                    Toast.makeText(applicationContext, dialogView.deviceTypeAutoCompleteTextView.text.toString(), Toast.LENGTH_LONG).show()
                    if(!dialogView.etDeviceName.text.isNullOrEmpty()){
                        if(!dialogView.deviceTypeAutoCompleteTextView.text.toString().isNullOrEmpty()){
                            viewModel.sendNewDeviceViaWebSocket(stompClient,"/device/addDevice/${selected}/${dialogView.etDeviceName.text.toString()}/${dialogView.roomAutoCompleteTextView.text.toString()}/${dialogView.deviceTypeAutoCompleteTextView.text.toString()}")
                            newDeviceAdapter.submitList(null)
                            dialogView.rvNewDevice.adapter = newDeviceAdapter
                            dialog.cancel()
                            viewModel.deviceIp.removeObservers(this)
                        }else{
                            dialogView.textInputDeviceTypeMenuLayout.error = "Choose device type"
                        }
                    }else{
                        dialogView.textInputDeviceAddName.error = "Can't be empty"
                    }
                }

            }
            dialogView.etDeviceName.addTextChangedListener {
                dialogView.textInputDeviceAddName.error = null
            }
            dialogView.deviceTypeAutoCompleteTextView.setOnClickListener {
                dialogView.textInputDeviceTypeMenuLayout.error = null
            }

        }

        with(viewModel) {
            menuList.observe(this@MainActivity, Observer { it ->
                navView.menu.clear()
                var increment: Int = 1
                if(sharedPreference.getBoolean("admin",false)){
                    it.forEach {room->
                        navView.menu.add(R.id.firstGroup, increment,increment*100, room.roomName)
                            .setIcon(getDrawable(room.roomType.res)).isCheckable = true
                        increment++
                    }
                }else{
                    it.forEach {room->
                        if(!room.admin){
                            navView.menu.add(R.id.firstGroup, increment,increment*100, room.roomName)
                                .setIcon(getDrawable(room.roomType.res)).isCheckable = true
                            increment++
                        }
                    }
                }
                navView.menu.setGroupCheckable(R.id.firstGroup, true,true)

                viewModel.changeTitle(navView.menu.children.first().title.toString())
                navView.menu.add(R.id.firstGroup,increment,increment*100,"add").icon = getDrawable(R.drawable.ic_baseline_add_24)
                navView.menu.add(R.id.secondGroup, increment+1, (increment+1)*100,"Settings").icon = getDrawable(R.drawable.ic_baseline_settings_24)
                //navView.menu.setGroupCheckable(R.id.secondGroup, false,false)
                navView.menu.setGroupCheckable(R.id.secondGroup, true, true)
                navView.setNavigationItemSelectedListener(this@MainActivity)
                visibilityNavElements(navController, navView, toggle)
                onNavigationItemSelected(navView.menu.getItem(0))
                //navView.setCheckedItem(navView.menu.getItem(2))
            })
            menuListNull.observe(this@MainActivity, Observer {
                navView.menu.clear()
                navView.menu.add("add").icon = getDrawable(R.drawable.ic_baseline_add_24)
                //viewModel.changeTitle(navView.menu.children.first().title.toString())
                navView.setNavigationItemSelectedListener(this@MainActivity)
                visibilityNavElements(navController, navView, toggle)
                //onNavigationItemSelected(navView.menu.getItem(0))
            })
            roomCountChanged.observe(this@MainActivity, Observer {
                navView.menu.clear()
                var increment: Int = 1
                if(sharedPreference.getBoolean("admin",false)){
                    it.forEach {room->
                        navView.menu.add(R.id.firstGroup, increment,increment*100, room.roomName)
                            .setIcon(getDrawable(room.roomType.res)).isCheckable = true
                        increment++
                    }
                }else{
                    it.forEach {room->
                        if(!room.admin){
                            navView.menu.add(R.id.firstGroup, increment,increment*100, room.roomName)
                                .setIcon(getDrawable(room.roomType.res)).isCheckable = true
                            increment++
                        }
                    }
                }
                navView.menu.setGroupCheckable(R.id.firstGroup, true,true)
                navView.menu.add(R.id.firstGroup,increment, increment*100,"add").icon = getDrawable(R.drawable.ic_baseline_add_24)
                navView.menu.add(R.id.secondGroup, increment+1, (increment+1)*100,"Settings").icon = getDrawable(R.drawable.ic_baseline_settings_24)
                navView.menu.setGroupCheckable(R.id.secondGroup, true, true)
                navView.setCheckedItem(navView.menu.getItem(checkPosition(navView,fragmentTitle.text.toString())))
            })

            deviceTypeResponse.observe(this@MainActivity, Observer {
                deviceTypes = it
            })
            checkResponse.observe(this@MainActivity, Observer {
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            })
        }
        navView.setupWithNavController(navController)
        navController.setGraph(R.navigation.mobile_navigation)
        connectivityChecker(applicationContext)
        viewModel.initDone()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


    }


    @SuppressLint("ResourceAsColor")
    fun visibilityNavElements(navController: NavController, navView: NavigationView, toggle: ActionBarDrawerToggle) {
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.nav_login -> {
                    supportActionBar?.hide()
                    fabAddDevice.visibility = View.GONE
                }
                R.id.nav_pawels_room -> {
                    supportActionBar?.show()
                    logOutBt.visibility = View.VISIBLE
                    fabAddDevice.visibility = View.VISIBLE
                    changePrimaryColor(R.color.brown, R.color.brownDarker)
                    //toolbar.menu.findItem(R.id.action_settings).isVisible = true
                    //toolbar.menu.getItem(R.id.action_settings)
                    //supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    toggle?.isDrawerIndicatorEnabled = true
                    toggle?.toolbarNavigationClickListener = null
                    toggle?.syncState()
                    if(!title.isNullOrEmpty()){
                        fragmentTitle.text = title
                        //Toast.makeText(applicationContext,checkPosition(navView,title.toString()).toString() , Toast.LENGTH_SHORT).show()
                        //val pos:Int = checkPosition(navView,title.toString())
                        if(checkPosition(navView,title.toString())==-1){
                            onNavigationItemSelected(navView.menu.getItem(0))
                            navView.setCheckedItem(navView.menu.getItem(0))
                        }else{
                            navView.setCheckedItem(navView.menu.getItem(checkPosition(navView,title.toString())))
                        }
                    }else{
                        navView.setCheckedItem(navView.menu.getItem(0))
                    }
                }
                R.id.nav_settings->{
                    //supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    logOutBt.visibility = View.GONE
                    toggle.isDrawerIndicatorEnabled = false
                    drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    toggle.setToolbarNavigationClickListener { onBackPressed() }
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    title = fragmentTitle.text.toString()
                    fragmentTitle.text = "Settings"
                    fabAddDevice.visibility = View.GONE
                }
                else -> {
                    supportActionBar?.show()
                    fabAddDevice.visibility = View.VISIBLE
                    //toolbar.menu.findItem(R.id.action_settings).isVisible = false
                }
            }
        }
    }


    fun changePrimaryColor(colorId: Int, colorAccentId: Int) {
        toolbar.setBackgroundColor(ContextCompat.getColor(applicationContext, colorId))
        //menuHeader.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.bla))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, colorAccentId)
        }
        //nav_view.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.navViewBottom))
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        if(internetConnection) {
            if (item.title.equals("add")) {
                val dialogView =
                    LayoutInflater.from(this).inflate(R.layout.room_adding_dialog, null)
                val dialog = Dialog(this)
                dialog.setTitle("Add room")
                dialog.setCancelable(true)
                dialog.setContentView(dialogView)
                dialog.show()

                val adapter: ArrayList<RoomTypes> = ArrayList()

                adapter.add(RoomTypes.LIVING_ROOM)
                adapter.add(RoomTypes.BATHROOM)
                adapter.add(RoomTypes.BEDROOM)
                adapter.add(RoomTypes.GARAGE)
                adapter.add(RoomTypes.KITCHEN)
                adapter.add(RoomTypes.LIBRARY)

                val adapterDone =
                    CustomAdapter(applicationContext, R.layout.dropdown_menu_drawable, adapter)
                // dialogView.addRoomAutoCompleteTextView.setAdapter(adapterDone)
                dialogView.spinner.adapter = adapterDone
                dialogView.btAddRoom.setOnClickListener {
                    if (dialogView.etRoomName.text.isNullOrEmpty()) {
                        dialogView.roomNameTextField.error = "cant be empty"
                    } else {
                        viewModel.sendViaWebSocket(
                            stompClient,
                            RoomModelRespond(
                                dialogView.etRoomName.text.toString(),
                                adapterDone.getItem(dialogView.spinner.selectedItemPosition)
                                    .toString()
                            )
                        )
                        println(
                            adapterDone.getItem(dialogView.spinner.selectedItemPosition).toString()
                        )
                        dialog.dismiss()
                    }
                }
                dialogView.etRoomName.addTextChangedListener {
                    dialogView.roomNameTextField.error = null
                }
                dialogView.btAddRoomCancel.setOnClickListener { dialog.dismiss() }

            } else if (item.title.equals("Settings")) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.nav_settings)
                nav_view.setCheckedItem(nav_view.menu.getItem(checkPosition(nav_view, "Settings")))
                stompClient.disconnect()
            } else {
                viewModel.changeTitle(item.title.toString())
                fragmentTitle.text = item.title
                drawer_layout.close()
                TransitionManager.beginDelayedTransition(drawer_layout, AutoTransition())
                // Toast.makeText(applicationContext,nav_view.checkedItem?.title.toString(),Toast.LENGTH_LONG).show()

            }
        }else{
            //nav_view.setCheckedItem(nav_view.menu.getItem(checkPosition(nav_view,fragmentTitle.text.toString())))
            Toast.makeText(applicationContext, "No internet Connection", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    fun checkPosition(navView: NavigationView, title:String):Int{
        var pos = 0
        navView.menu.forEach {
            if(it.title.equals(title)){
                return pos
            }else{
                pos++
            }
        }
        return -1
    }

    fun connectivityChecker(context: Context){
        val connectivityManager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        var networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                MainActivity.Companion.internetConnection = true
            }

            override fun onUnavailable() {
            }

            override fun onLost(network: Network) {
                MainActivity.Companion.internetConnection = false
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                Toast.makeText(context, "losing", Toast.LENGTH_SHORT).show()
            }
        }
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }


}