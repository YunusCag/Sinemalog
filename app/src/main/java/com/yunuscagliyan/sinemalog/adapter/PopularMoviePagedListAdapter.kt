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
import com.yunuscagliyan.sinemalog.data.model.SingleMovie
import kotlinx.android.synthetic.main.network_state_item.view.*
import com.yunuscagliyan.sinemalog.eventbus.MovieDetailMessage
import org.greenrobot.eventbus.EventBus

class PopularMoviePagedListAdapter(
    private var context: Context,
    private var columnCount:Int
): PagedListAdapter<SingleMovie,RecyclerView.ViewHolder>(MovieDiffCallback()) {


    val MOVIE_VIEW_TYPE=1
    val NETWORK_STATE_TYPE=2
    val ADVERTISE_VIEW_TYPE=3
    private var networkState:NetworkState?=null
    var advertiseShowed=false
    var perAdbyRow=columnCount*3-2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater=LayoutInflater.from(parent.context)
        val view:View
        if(viewType==MOVIE_VIEW_TYPE){
            view=inflater.inflate(R.layout.item_popular,parent,false)
            return PopularItemViewHolder(view)
        }else if(viewType==NETWORK_STATE_TYPE){
            view=inflater.inflate(R.layout.network_state_item,parent,false)
            return NetworkStateViewHolder(view)
        }else{
            view=inflater.inflate(R.layout.item_advertise,parent,false)
            return AdItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(getItemViewType(position)==MOVIE_VIEW_TYPE){
            if(advertiseShowed && position!=0 && perAdbyRow<position){
                (holder as PopularItemViewHolder).bindUI(getItem(position-1),context)
            }else{
                (holder as PopularItemViewHolder).bindUI(getItem(position),context)
            }

        }else if(getItemViewType(position)==NETWORK_STATE_TYPE){
            (holder as NetworkStateViewHolder).bindUI(networkState!!)
        }else{
            (holder as AdItemViewHolder).bindUI(context)
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
            MOVIE_VIEW_TYPE
        }
    }
    override fun getItemCount(): Int {
        return super.getItemCount()+if(hasExtraRow())1 else 0
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
    class MovieDiffCallback: DiffUtil.ItemCallback<SingleMovie>(){
        override fun areItemsTheSame(oldItem: SingleMovie, newItem: SingleMovie): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: SingleMovie, newItem: SingleMovie): Boolean {
            return oldItem==newItem
        }

    }

    class PopularItemViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindUI(singleMovie: SingleMovie?,context: Context){
            val imageView=itemView.findViewById<ImageView>(R.id.popularIV)
            val moviePosterURL= POSTER_BASE_URL+singleMovie!!.posterPath
            Glide.with(context)
                .load(moviePosterURL)
                .into(imageView)
            itemView.setOnClickListener {
                if(singleMovie!=null){
                    EventBus.getDefault().post(MovieDetailMessage(singleMovie.id))
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