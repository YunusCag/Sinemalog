package com.yunuscagliyan.sinemalog.fragments

import android.animation.ArgbEvaluator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.adapter.OnboardingPageAdapter
import com.yunuscagliyan.sinemalog.data.model.OnBoardingModel

import com.yunuscagliyan.sinemalog.databinding.FragmentOnBoardingBinding
import com.yunuscagliyan.sinemalog.ui.SharedPref

/**
 * A simple [Fragment] subclass.
 */
class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding
    private lateinit var navController: NavController
    private lateinit var mPref: SharedPref
    private lateinit var mAdapter:OnboardingPageAdapter
    private lateinit var mList:ArrayList<OnBoardingModel>
    private lateinit var mColors:Array<Int>
    private lateinit var mArgbEvaluator: ArgbEvaluator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentOnBoardingBinding.inflate(inflater,container,false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
        initStatusBar()
        mList=ArrayList<OnBoardingModel>()
        mList.add(
            OnBoardingModel(
            R.drawable.light_theme,resources.getString(R.string.day_light_mode)
        )
        )
        mList.add(OnBoardingModel(
                R.drawable.dark_theme,resources.getString(R.string.dark_mode)
        ))
        mList.add(OnBoardingModel(
            R.drawable.change_theme,resources.getString(R.string.change_theme)
        ))
        mList.add(OnBoardingModel(
            R.drawable.trend,resources.getString(R.string.trend_movie)
        ))
        mAdapter= OnboardingPageAdapter(mList,context!!)
        binding.viewPager.adapter=mAdapter
        binding.viewPager.setPadding(130,0,130,0)

        binding.dotsIndicator.setViewPager(binding.viewPager)


        binding.btnNextPage.setOnClickListener {
            mPref.setFirstTime(false)

            val navOptions= NavOptions.Builder()
                .setPopUpTo(R.id.destination_on_boarding,true)
                .build()
            navController.navigate(R.id.destination_trending,null,navOptions)

        }
    }
    private fun initStatusBar() {
        mPref= SharedPref(context!!)
        val statusColor=mPref.loadStatusBarColor()
        val window=activity!!.window
        window.statusBarColor=statusColor
    }

}
