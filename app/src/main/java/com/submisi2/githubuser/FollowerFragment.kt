package com.submisi2.githubuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.submisi2.githubuser.adapter.*
import com.submisi2.githubuser.viewmodel.FollowersViewmodel
import kotlinx.android.synthetic.main.fragment_follower.*


class FollowerFragment : Fragment() {
    private lateinit var username: String
    private lateinit var adapter: FollowerAdapter
    private lateinit var followerViewModel: FollowersViewmodel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        loadingStatusFollower(true)
        getFollowerlist()
    }

    private fun setRecyclerView() {
        adapter = FollowerAdapter()
        adapter.notifyDataSetChanged()
        recycleViewFollowers.layoutManager = LinearLayoutManager(activity)
        recycleViewFollowers.adapter = adapter
    }

    private fun getFollowerlist() {
        if (arguments != null) {
            username = arguments?.getString(SectionsAdapter.PUT_EXTRA).toString()
        }
        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowersViewmodel::class.java)
        followerViewModel.setFollowerlist(username)
        followerViewModel.getFollowers().observe(viewLifecycleOwner, { followerItems ->
            if (followerItems != null) {
                adapter.setData(followerItems)
                loadingStatusFollower(false)
            }
        })
    }

    private fun loadingStatusFollower(state: Boolean) {
        if (state) progressBarFollowers.visibility = View.VISIBLE
        else progressBarFollowers.visibility = View.GONE
    }

}