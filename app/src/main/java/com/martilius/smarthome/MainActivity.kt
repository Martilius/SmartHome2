package com.martilius.smarthome

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
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
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_settings->{
                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_settings)
                    true
                }
                else -> false
            }
        }


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
    }


    @SuppressLint("ResourceAsColor")
    fun visibilityNavElements(navController: NavController, navView: NavigationView) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_login-> {
                    supportActionBar?.hide()
                    fab.visibility = View.GONE
                }
                R.id.nav_home -> {
                    supportActionBar?.show()
                    fab.visibility = View.VISIBLE
                    changePrimaryColor(R.color.brown, R.color.brownDarker)
                    toolbar.menu.findItem(R.id.action_settings).isVisible = true
                    //toolbar.menu.getItem(R.id.action_settings)
                }
                R.id.nav_gallery->{
                    supportActionBar?.show()
                    fab.visibility = View.VISIBLE
                    changePrimaryColor(R.color.darkEcru, R.color.darkerEcru)
                }
                R.id.nav_slideshow->{
                    supportActionBar?.show()
                    fab.visibility = View.VISIBLE
                    changePrimaryColor(R.color.grey, R.color.darkGrey)
                }

                else -> {
                    supportActionBar?.show()
                    fab.visibility = View.VISIBLE
                    toolbar.menu.findItem(R.id.action_settings).isVisible = false
                }
            }
        }
    }


    fun changePrimaryColor(colorId: Int, colorAccentId: Int){
        toolbar.setBackgroundColor(ContextCompat.getColor(applicationContext,colorId))
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
}