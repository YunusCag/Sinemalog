package com.yunuscagliyan.sinemalog.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yunuscagliyan.sinemalog.data.model.BackColor
import com.yunuscagliyan.sinemalog.databinding.ItemThemeBinding
import com.yunuscagliyan.sinemalog.eventbus.ChangeThemeMessage
import com.yunuscagliyan.sinemalog.ui.SharedPref
import org.greenrobot.eventbus.EventBus

class SettingsThemeAdapter(
    val backColorList:ArrayList<BackColor>,
    val context:Context
) :RecyclerView.Adapter<SettingsThemeAdapter.SettingsViewHolder>(){

    private var selectedPosition=0
    private lateinit var mSharedPref: SharedPref
    init {
        mSharedPref=SharedPref(context)
        selectedPosition=mSharedPref.loadThemePosition()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {

        var binding=ItemThemeBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)

        return SettingsViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return backColorList.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {

        holder.binding.root.setOnClickListener {
            selectedPosition=position

            EventBus.getDefault().postSticky(ChangeThemeMessage(
                backColorList[position].startColor,backColorList[position].endColor,selectedPosition))
            notifyDataSetChanged()
        }
        if(selectedPosition==position){
            holder.setSelectedData(backColorList[position])
        }else{
            holder.setData(backColorList[position])
        }

    }
    inner class SettingsViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

         var binding: ItemThemeBinding
        init {
            binding=ItemThemeBinding.bind(itemView)
        }
        fun setData(backColor: BackColor){

            binding.root.setBackgroundColor(backColor.startColor)
            binding.lSelected.visibility=View.GONE

        }
        fun setSelectedData(backColor: BackColor){
            binding.root.setBackgroundColor(backColor.startColor)
            binding.lSelected.visibility=View.VISIBLE

        }

    }
    /*
    var gd=GradientDrawable(
                GradientDrawable.Orientation.TR_BL,
                intArrayOf(backColor.startColor,backColor.endColor)
            )
     */

}