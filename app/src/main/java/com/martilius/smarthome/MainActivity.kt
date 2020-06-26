package com.martilius.smarthome

import android.annotation.SuppressLint
import android.app.Application
import android.app.StatusBarManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.service.notification.StatusBarNotification
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.navOptions
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.setGraph(R.navigation.mobile_navigation)
        visibilityNavElements(navController,navView)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home->{
                    //val b = navController.popBackStack(R.id.nav_home,false)
                    //if(!b){
                        navController.navigate(R.id.nav_home)
                    //}
                    true
                }
                R.id.nav_gallery->{
                    //navController.
                    val c = navController.popBackStack(R.id.nav_gallery,false)
                    if(!c){

                        navController.navigate(R.id.nav_gallery)
                    }
                    true
                }
                else -> false
            }
        }
    }


    @SuppressLint("ResourceAsColor")
    fun visibilityNavElements(navController: NavController, navView: NavigationView) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment-> {
                    //navView?.visibility = View.GONE
                    supportActionBar?.hide()
                    fab.visibility = View.GONE
                }
                R.id.nav_home -> {
                    supportActionBar?.show()
                    fab.visibility = View.VISIBLE
                    changePrimaryColor(R.color.brown)
                    //toolbar.setBackgroundColor(resources.getColor(R.color.brown))
                    //menuHeader.setBackgroundColor(resources.getColor(R.color.brown))
                    //window.statusBarColor = getColor(R.color.brown)
                }
                R.id.nav_gallery->{
                    supportActionBar?.show()
                    fab.visibility = View.VISIBLE
                    changePrimaryColor(R.color.ecru)
                }
                R.id.nav_slideshow->{
                    supportActionBar?.show()
                    fab.visibility = View.VISIBLE
                    changePrimaryColor(R.color.green)
                }

                else -> {
                    supportActionBar?.show()
                    fab.visibility = View.VISIBLE
                }
            }
        }
    }

    fun changePrimaryColor(colorId: Int){
        toolbar.setBackgroundColor(ContextCompat.getColor(applicationContext,colorId))
        menuHeader.setBackgroundColor(ContextCompat.getColor(applicationContext,colorId))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, colorId)
        }
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
}