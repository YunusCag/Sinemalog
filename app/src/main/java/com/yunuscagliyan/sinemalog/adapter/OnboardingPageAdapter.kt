package com.yunuscagliyan.sinemalog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.data.model.OnBoardingModel

class OnboardingPageAdapter(
var modelList:ArrayList<OnBoardingModel>,
var context: Context
):PagerAdapter() {
    override fun isViewFromObject(view: View, obj: Any): Boolean {

        return view == obj
    }

    override fun getCount(): Int {
        return modelList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var layoutInflater=LayoutInflater.from(context)
        var view=layoutInflater.inflate(R.layout.item_onboarding,container,false)
        var imageView=view.findViewById<ImageView>(R.id.ivOnBoard)
        var tvTitle=view.findViewById<TextView>(R.id.tvTitle)
        var model=modelList[position]
        Glide.with(context).load(model.image).into(imageView)
        tvTitle.text=""+model.title
        container.addView(view,0)


        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}