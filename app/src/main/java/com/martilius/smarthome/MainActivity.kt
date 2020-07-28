package com.martilius.smarthome

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.martilius.smarthome.adapters.NewDeviceAdapter
import com.martilius.smarthome.models.DeviceType
import com.martilius.smarthome.models.NewDevice
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adding_devices_dialog.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.room_adding_dialog.view.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<MainViewModel> { factory }

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val newDeviceAdapter by lazy {
        NewDeviceAdapter{

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.bbbb,R.string.bbbb)
        lateinit var  deviceTypes: List<DeviceType>
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_settings -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_settings)
                    true
                }
                else -> false
            }
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
            val roomsAdapter = ArrayAdapter<String>(this, R.layout.dropdown_menu_item,roomsList)
            val deviceTypeAdapter = ArrayAdapter<String>(this, R.layout.dropdown_menu_item,deviceTypeList)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.adding_devices_dialog, null)

            dialogView.roomAutoCompleteTextView.setAdapter(roomsAdapter)
            dialogView.deviceTypeAutoCompleteTextView.setAdapter(deviceTypeAdapter)
            dialogView.roomAutoCompleteTextView.setText(supportActionBar?.title,false)
            dialogView.rvNewDevice.adapter = newDeviceAdapter
//            val decoration = DividerItemDecoration(applicationContext,DividerItemDecoration.VERTICAL)
//            decoration.setDrawable(ShapeDrawable().apply {
//                intrinsicHeight = resources.getDimensionPixelOffset(R.dimen.activity_horizontal_margin)
//            })
//            dialogView.rvNewDevice.addItemDecoration(decoration)
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(dialogView)
            dialog.show()
            dialogView.btCancelAddDevice.setOnClickListener { dialog.dismiss() }
            dialogView.btAddDevice.setOnClickListener { dialog.dismiss()
                if(newDeviceAdapter.getSelected()!=-1){
                    val selected =  newDeviceAdapter.currentList.get(newDeviceAdapter.getSelected())
                    Toast.makeText(applicationContext,selected.ip,Toast.LENGTH_LONG).show()
                }

            //Toast.makeText(applicationContext, newDeviceAdapter.getSelected().toString(),Toast.LENGTH_LONG).show()
            }
            newDeviceAdapter.submitList(listOf(NewDevice(id = 1,ip = "192.168.1.1"),NewDevice(id = 2,ip = "192.168.2.2")))
        }

        with(viewModel) {
            menuList.observe(this@MainActivity, Observer {
            navView.menu.clear()
            it.forEach {
                navView.menu.add(it.roomName).isCheckable = true
            }
                //Toast.makeText(applicationContext,navView.menu.children.first().title.toString(),Toast.LENGTH_LONG).show()
                viewModel.changeTitle(navView.menu.children.first().title.toString())
                navView.menu.add("add").icon =  getDrawable(R.drawable.ic_baseline_add_24)
                //navView.menu.getItem(0).isEnabled = true
                navView.setNavigationItemSelectedListener(this@MainActivity)
                visibilityNavElements(navController, navView)
                onNavigationItemSelected(navView.menu.getItem(0))
                //Toast.makeText(applicationContext,navView.checkedItem.toString(),Toast.LENGTH_LONG).show()
            })
            roomAdded.observe(this@MainActivity, Observer {
                navView.menu.children.last().isVisible = false
                navView.menu.add(it).isCheckable = true
                navView.menu.add("add").icon = getDrawable(R.drawable.ic_baseline_add_24)

            })

            deviceTypeResponse.observe(this@MainActivity, Observer {
                deviceTypes = it
            })
        }
        navView.setupWithNavController(navController)
        navController.setGraph(R.navigation.mobile_navigation)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


    }


    @SuppressLint("ResourceAsColor")
    fun visibilityNavElements(navController: NavController, navView: NavigationView) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_login -> {
                    supportActionBar?.hide()
                    fabAddDevice.visibility = View.GONE
                }
                R.id.nav_pawels_room -> {
                    supportActionBar?.show()
                    fabAddDevice.visibility = View.VISIBLE
                    changePrimaryColor(R.color.brown, R.color.brownDarker)
                    toolbar.menu.findItem(R.id.action_settings).isVisible = true
                    //toolbar.menu.getItem(R.id.action_settings)
                }
                else -> {
                    supportActionBar?.show()
                    fabAddDevice.visibility = View.VISIBLE
                    toolbar.menu.findItem(R.id.action_settings).isVisible = false
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        if(item.title.equals("add")){
            val dialogView = LayoutInflater.from(this).inflate(R.layout.room_adding_dialog, null)
            val dialog = Dialog(this)
            dialog.setTitle("Add room")
            dialog.setCancelable(true)
            dialog.setContentView(dialogView)
            dialog.show()

            dialogView.btAddRoom.setOnClickListener {
                if(dialogView.etRoomName.text.isNullOrEmpty()){
                    dialogView.roomNameTextField.error="cant be empty"
                }else{
                    viewModel.AddRoom(dialogView.etRoomName.text.toString())
                    dialog.dismiss()
                }
            }
            dialogView.etRoomName.addTextChangedListener {
                dialogView.roomNameTextField.error = null
            }
            dialogView.btAddRoomCancel.setOnClickListener { dialog.dismiss() }

        }else{
            viewModel.changeTitle(item.title.toString())
            //Toast.makeText(applicationContext, item.title.toString(), Toast.LENGTH_SHORT).show()
            supportActionBar?.title =item.title
            drawer_layout.close()
            TransitionManager.beginDelayedTransition(drawer_layout,AutoTransition())
            //nav_view.setCheckedItem(item)
            //Toast.makeText(applicationContext,nav_view.checkedItem.toString(),Toast.LENGTH_LONG).show()
        }

        return true
    }
}