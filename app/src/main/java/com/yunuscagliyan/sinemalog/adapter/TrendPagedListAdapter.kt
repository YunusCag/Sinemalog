package com.yunuscagliyan.sinemalog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.data.api.POSTER_BASE_URL
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.model.Trend
import com.yunuscagliyan.sinemalog.eventbus.TrendDetailMessage
import kotlinx.android.synthetic.main.network_state_item.view.*
import org.greenrobot.eventbus.EventBus

class TrendPagedListAdapter(
    private var context: Context,
    private var columnCount:Int
):PagedListAdapter<Trend,RecyclerView.ViewHolder>(TrendDiffCallback()) {
    val TREND_VIEW_TYPE=1
    val NETWORK_STATE_TYPE=2
    val ADVERTISE_VIEW_TYPE=3

    private var networkState:NetworkState?=null

    var advertiseShowed=false
    var perAdbyRow=columnCount*3-2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater=LayoutInflater.from(context)
        val view:View
        if(viewType==TREND_VIEW_TYPE){
            view=layoutInflater.inflate(R.layout.item_trend,parent,false)
            return TrendViewHolder(view)
        }else if(viewType==NETWORK_STATE_TYPE){
            view=layoutInflater.inflate(R.layout.network_state_item,parent,false)
            return TrendPagedListAdapter.NetworkStateViewHolder(view)
        }else{
            view=layoutInflater.inflate(R.layout.item_advertise,parent,false)
            return TrendPagedListAdapter.AdItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position)==TREND_VIEW_TYPE){
            if(advertiseShowed && position!=0 && perAdbyRow<position){
                (holder as TrendViewHolder).bindUI(getItem(position-1),context)
            }else{
                (holder as TrendViewHolder).bindUI(getItem(position),context)
            }
        }else if(getItemViewType(position)==NETWORK_STATE_TYPE){
            (holder as NetworkStateViewHolder).bindUI(networkState!!)

        }else{
            (holder as TrendPagedListAdapter.AdItemViewHolder).bindUI(context)
        }

    }
    override fun getItemViewType(position: Int): Int {

        return if(hasExtraRow()&&position==itemCount-1){
            NETWORK_STATE_TYPE
        }else if(position%perAdbyRow ==0 && position!=0){
            advertiseShowed=true
            perAdbyRow=columnCount*3+1
            ADVERTISE_VIEW_TYPE
        }else{
            TREND_VIEW_TYPE
        }
    }
    private fun hasExtraRow():Boolean{
        return networkState!=null&&networkState!=NetworkState.LOADED
    }
    fun setNetworkState(newNetworkState: NetworkState){
        val previousState=this.networkState
        val hadExtraRow=hasExtraRow()

        this.networkState=newNetworkState
        val hasExtraRow=hasExtraRow()

        if(hadExtraRow!=hasExtraRow){
            if(hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            }else{
                notifyItemInserted(super.getItemCount()-1)
            }

        }else if(hasExtraRow&&previousState!=newNetworkState){
            notifyItemChanged(super.getItemCount())
        }
    }

    class TrendDiffCallback: DiffUtil.ItemCallback<Trend>(){
        override fun areItemsTheSame(oldItem: Trend, newItem: Trend): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Trend, newItem: Trend): Boolean {
            return oldItem==newItem
        }

    }
    class TrendViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView){


        fun bindUI(trend:Trend?,context: Context){
            var imageView=itemView.findViewById<ImageView>(R.id.ImageView)

            val trendPosterURL= POSTER_BASE_URL+trend?.posterPath
            Glide.with(context)
                .load(trendPosterURL)
                .into(imageView)
            itemView.setOnClickListener {
                if(trend!=null){
                    EventBus.getDefault().post(TrendDetailMessage(trend))
                }

            }
        }
    }
    class NetworkStateViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindUI(networkState: NetworkState){
            if(networkState!=null&&networkState==NetworkState.LOADING){
                itemView.progressBarItem.visibility=View.VISIBLE
            }else{
                itemView.progressBarItem.visibility=View.VISIBLE
            }
            if(networkState!=null&&networkState==NetworkState.ERROR){
                itemView.error_msg.visibility=View.VISIBLE
                itemView.error_msg.text=networkState.msg
            }else{
                itemView.error_msg.visibility=View.GONE
            }
        }

    }
    class AdItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindUI(context: Context){
            val adView=itemView.findViewById<AdView>(R.id.rowAdItem)
            val adRequest = AdRequest.Builder()
                .build()
            adRequest.isTestDevice(context)
            adView.loadAd(adRequest)

        }

    }



}