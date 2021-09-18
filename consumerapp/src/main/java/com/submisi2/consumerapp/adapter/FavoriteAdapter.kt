package com.submisi2.consumerapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.submisi2.consumerapp.R
import com.submisi2.consumerapp.model.Favorite
import kotlinx.android.synthetic.main.item_favorit.view.*


class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.RecyclerViewHolder>() {

    var listFavorite = ArrayList<Favorite>()
        set(listFavorite){
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)

            notifyDataSetChanged()

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_favorit, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int = listFavorite.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) =
        holder.bind(listFavorite[position])

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(list: Favorite) {
            with(itemView) {
                tv_favusername.text = list.username
                tv_favcompany.text = list.company
                tv_favlocation.text = list.location
                tv_favname.text = list.name
                Glide.with(itemView.context)
                    .load(list.avatarUrl)
                    .apply { RequestOptions().override(100, 100) }
                    .into(ivfav_avatar)

            }
        }
    }
}