package com.yunuscagliyan.sinemalog

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.ads.MobileAds
import com.yunuscagliyan.sinemalog.databinding.ActivityMainBinding
import com.yunuscagliyan.sinemalog.eventbus.SelectFirstMessage
import com.yunuscagliyan.sinemalog.ui.SharedPref
import com.yunuscagliyan.sinemalog.ui.ThemeHelper
import kotlinx.android.synthetic.main.drawer_header.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController:NavController
    private lateinit var mPref: SharedPref



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController= Navigation.findNavController(this,R.id.nav_host_fragment)
        initTheme()
        setUpSideNavMenu(navController)
        setUpDestinationChangeListener(navController)
        initAdMob()


    }

    private fun initAdMob() {
        MobileAds.initialize(this) {}

    }

    private fun initTheme() {
        mPref= SharedPref(context = applicationContext)
        if(mPref.loadNightModeState()){
            ThemeHelper.applyTheme(ThemeHelper.DARK_MODE)
        }else{
            ThemeHelper.applyTheme(ThemeHelper.LIGHT_MODE)
        }

    }
    private fun setUpDestinationChangeListener(navController: NavController) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id==R.id.destination_trending){
                binding.navigationLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }else{
                binding.navigationLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }

    private fun setUpSideNavMenu(navController: NavController) {
        //navigaton Controller SetUp
        binding.navigationDrawer.let {
            NavigationUI.setupWithNavController(it,navController)
        }
        //Side Design SetUp
        var actionBarDrawerToggle=object:ActionBarDrawerToggle(this,
            binding.navigationLayout,null,
            R.string.app_name,R.string.app_name){

            private var scaleFactor:Float=6f
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                super.onDrawerSlide(drawerView, slideOffset)
                var slideX:Float=drawerView.width*slideOffset
                binding.contentLayout.translationX=slideX
                binding.contentLayout.scaleX=(1-(slideOffset/scaleFactor))
                binding.contentLayout.scaleY=(1-(slideOffset/scaleFactor))
            }

        }
        binding.navigationLayout.setScrimColor(Color.TRANSPARENT)
        binding.navigationLayout.drawerElevation=0f
        binding.navigationLayout.addDrawerListener(actionBarDrawerToggle)
    }


    fun setUpToolbar(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration.Builder(setOf<Int>(
            R.id.destination_trending))
            .setDrawerLayout(binding.navigationLayout).build()
        setSupportActionBar(toolbar)
        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,
            binding.navigationLayout)||super.onSupportNavigateUp()
    }

}
