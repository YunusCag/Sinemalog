package com.yunuscagliyan.sinemalog.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.adapter.SettingsThemeAdapter
import com.yunuscagliyan.sinemalog.data.model.BackColor
import com.yunuscagliyan.sinemalog.databinding.FragmentSettingsBinding
import com.yunuscagliyan.sinemalog.eventbus.ChangeThemeMessage
import com.yunuscagliyan.sinemalog.ui.SharedPref
import com.yunuscagliyan.sinemalog.ui.Utility
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlin.system.exitProcess

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var mPref: SharedPref
    private lateinit var navController: NavController
    private lateinit var mBackColorList:ArrayList<BackColor>
    private var listPosition=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSettingsBinding.inflate(inflater,container,false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
        mPref= SharedPref(activity!!.applicationContext)
        initSwitchSwap()
        initToolbar()
        initRecycler()
        initAdMob()


    }

    private fun initAdMob() {
        val adRequest = AdRequest.Builder()
            .build()
        adRequest.isTestDevice(context)
        binding.adView.loadAd(adRequest)
    }

    private fun initRecycler() {
        initBackColorList()
        var adapter=SettingsThemeAdapter(mBackColorList,context!!)
        val columnCount= Utility.calculateNoOfColumns(context!!,100f);
        binding.recyclerView.layoutManager=GridLayoutManager(
            context,columnCount)
        binding.recyclerView.adapter=adapter

    }

    private fun initToolbar() {
        //NavigationUI.setupWithNavController(binding.toolbar,navController)
        (activity as MainActivity).setUpToolbar(binding.toolbar)
        changeToolbarColor()
    }

    @Subscribe(sticky = true)
    fun changeTheme(changeThemeMessage: ChangeThemeMessage){
        mPref.setToolBarColor(changeThemeMessage.toolbarColor)
        mPref.setStatusBarColor(changeThemeMessage.statusColor)
        mPref.setThemePosition(changeThemeMessage.position)
        changeToolbarColor()

    }
    private fun changeToolbarColor() {
        var themeColor = mPref.loadToolBarColor()
        binding.toolbar.setBackgroundColor(themeColor)
        var statusColor=mPref.loadStatusBarColor()
        var window=activity!!.window
        window.statusBarColor=statusColor
    }

    private fun initSwitchSwap() {
        binding.switchCompat.isChecked = mPref.loadNightModeState()
        binding.switchCompat.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mPref.setNightModeState(true)
                var intent = Intent(context!!, MainActivity::class.java)
                startActivity(intent)
                exitProcess(0)
            } else {
                mPref.setNightModeState(false)
                var intent = Intent(context!!, MainActivity::class.java)
                startActivity(intent)
                exitProcess(0);
            }
        }
    }
    private fun initBackColorList(){
        mBackColorList=ArrayList<BackColor>()
        //red
        mBackColorList.add(
            BackColor(
                Color.parseColor("#d32f2f"),
                Color.parseColor("#c62828"),
                Color.parseColor("#b71c1c")
            )
        )
        //pink
        mBackColorList.add(
            BackColor(
                Color.parseColor("#C2185B"),
                Color.parseColor("#AD1457"),
                Color.parseColor("#880E4F")
            )
        )
        //purple
        mBackColorList.add(
            BackColor(
                Color.parseColor("#9C27B0"),
                Color.parseColor("#7B1FA2"),
                Color.parseColor("#4A148C")
            )
        )
        //indigo
        mBackColorList.add(
            BackColor(
                Color.parseColor("#303F9F"),
                Color.parseColor("#283593"),
                Color.parseColor("#1A237E")
            )
        )
        //blue
        mBackColorList.add(
            BackColor(
                Color.parseColor("#1976D2"),
                Color.parseColor("#1565C0"),
                Color.parseColor("#0D47A1")
            )
        )
        //green
        mBackColorList.add(
            BackColor(
                Color.parseColor("#388E3C"),
                Color.parseColor("#2E7D32"),
                Color.parseColor("#1B5E20")
            )
        )
        //yellow
        mBackColorList.add(
            BackColor(
                Color.parseColor("#FBC02D"),
                Color.parseColor("#F9A825"),
                Color.parseColor("#F57F17")
            )
        )
        //orange
        mBackColorList.add(
            BackColor(
                Color.parseColor("#F57C00"),
                Color.parseColor("#EF6C00"),
                Color.parseColor("#E65100")
            )
        )
        //deepOrange
        mBackColorList.add(
            BackColor(
                Color.parseColor("#E64A19"),
                Color.parseColor("#D84315"),
                Color.parseColor("#BF360C")
            )
        )
        //brown
        mBackColorList.add(
            BackColor(
                Color.parseColor("#795548"),
                Color.parseColor("#5D4037"),
                Color.parseColor("#3E2723")
            )
        )
        //Grey
        mBackColorList.add(
            BackColor(
                Color.parseColor("#616161"),
                Color.parseColor("#424242"),
                Color.parseColor("#212121")
            )
        )
        //Dark Blue
        mBackColorList.add(
            BackColor(
                Color.parseColor("#455A64"),
                Color.parseColor("#37474F"),
                Color.parseColor("#263238")
            )
        )
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

}
