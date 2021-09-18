package com.submisi2.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.submisi2.githubuser.R
import com.submisi2.githubuser.model.UserList
import kotlinx.android.synthetic.main.item_user.view.*

class UserlistAdapter (private val listUsers: ArrayList<UserList>) :
    RecyclerView.Adapter<UserlistAdapter.RecyclerViewHolder>(){

    private lateinit var  onItemClickDetail: OnItemClickCallback


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickDetail = onItemClickCallback
    }
    fun setUserData(items: ArrayList<UserList>) {
        listUsers.clear()
        listUsers.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return RecyclerViewHolder(view)
    }
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) =
        holder.bind(listUsers[position])

    override fun getItemCount(): Int = listUsers.size

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(list: UserList) {
            with(itemView) {
                tv_username.text = list.username
                tv_type.text = context.getString(R.string.type_user,list.type)
                tv_url.text = context.getString(R.string.url_user,list.url)
                Glide.with(itemView.context)
                    .load(list.avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(iv_avatar)

                itemView.setOnClickListener { onItemClickDetail.onItemClicked(list) }
            }
        }
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: UserList)
    }
}