 package com.submisi2.githubuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.submisi2.githubuser.adapter.*
import com.submisi2.githubuser.viewmodel.FollowingViewmodel
import kotlinx.android.synthetic.main.fragment_following.*

 class FollowingFragment : Fragment() {
     private lateinit var username: String
     private lateinit var adapter: FollowingAdapter
     private lateinit var followingViewModel: FollowingViewmodel

     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         return inflater.inflate(R.layout.fragment_following, container, false)
     }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         loadingStatusFollowing(true)
         adapter = FollowingAdapter()
         adapter.notifyDataSetChanged()
         recycleViewFollowing.layoutManager = LinearLayoutManager(activity)
         recycleViewFollowing.adapter = adapter
         getFollowinglist()
         loadingStatusFollowing(true)

     }
     private fun getFollowinglist() {
         if (arguments != null) {
             username = arguments?.getString(SectionsAdapter.PUT_EXTRA).toString()
         }
         followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewmodel::class.java)
         followingViewModel.setFollowinglist(username)
         followingViewModel.getFollowing().observe(viewLifecycleOwner,{ followingItems ->
             if (followingItems != null) {
                 adapter.setData(followingItems)
             }
         })
     }

     private fun loadingStatusFollowing(state: Boolean) {
         if (state) progressBarFollowing.visibility = View.VISIBLE
         else progressBarFollowing.visibility = View.GONE
         }

 }