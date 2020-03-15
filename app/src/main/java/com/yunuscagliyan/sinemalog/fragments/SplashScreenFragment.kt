package com.yunuscagliyan.sinemalog.fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.databinding.FragmentSplashScreenBinding
import com.yunuscagliyan.sinemalog.ui.SharedPref

/**
 * A simple [Fragment] subclass.
 */
class SplashScreenFragment : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var mPref: SharedPref

    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSplashScreenBinding.inflate(inflater,container,false)




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
        initStatusBar()
        initCountDown()
        mPref= SharedPref(context!!)


    }
    private fun initStatusBar() {
        var sharedPref = SharedPref(context!!)
        var statusColor=sharedPref.loadStatusBarColor()
        var window=activity!!.window
        window.statusBarColor=statusColor
    }

    private fun initCountDown() {
        object : CountDownTimer(4000, 1000) {
            override fun onFinish() {

                if(mPref.loadFirstTime()){
                    var navOptions= NavOptions.Builder()
                        .setPopUpTo(R.id.destination_splash_screen,true)
                        .build()
                    navController.navigate(R.id.next_action,null,navOptions)
                }else{
                    var navOptions=NavOptions.Builder()
                        .setPopUpTo(R.id.destination_splash_screen,true)
                        .build()
                    navController.navigate(R.id.destination_trending,null,navOptions)
                }

            }

            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()
    }


}
