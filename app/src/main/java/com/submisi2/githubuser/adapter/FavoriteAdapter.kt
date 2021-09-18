package com.submisi2.githubuser.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.submisi2.githubuser.DetailActivity
import com.submisi2.githubuser.R
import com.submisi2.githubuser.model.Favorite
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
                btn_to_detail.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.PUT_EXTRA, list.username)
                    context.startActivity(intent)
                }

            }
        }
    }
}