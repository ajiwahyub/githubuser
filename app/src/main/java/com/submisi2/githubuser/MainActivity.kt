package com.submisi2.githubuser

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.submisi2.githubuser.adapter.UserlistAdapter
import com.submisi2.githubuser.model.UserList
import com.submisi2.githubuser.viewmodel.MainViewmodel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var users: ArrayList<UserList> = ArrayList()
    private lateinit var userlistAdapter: UserlistAdapter
    private lateinit var mainViewModel: MainViewmodel

    private var title = "Github User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(title)
        showRecyclerView()
        searchUser()
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewmodel::class.java)
        mainViewModel.getUsernames().observe(this, { usernameItems ->
            if (usernameItems != null) {
                userlistAdapter.setUserData(usernameItems)
                hideKeyboard()
                showLoading(false)
            }
        })

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        selectOptionsMenu(item.itemId)
        return super.onOptionsItemSelected(item)
    }
    private fun selectOptionsMenu(selectedMenu: Int) {
        when (selectedMenu) {
            R.id.action_favorite -> {
                val mIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(mIntent)
            }
            R.id.action_set_notification -> {
                val mIntent = Intent(this@MainActivity, NotificationActivity::class.java)
                startActivity(mIntent)
            }
        }
    }

    private fun showRecyclerView() {
        imageRecyclerView.layoutManager = LinearLayoutManager(this)
        imageRecyclerView.setHasFixedSize(true)
        userlistAdapter = UserlistAdapter(users)
        imageRecyclerView.adapter = userlistAdapter
        userlistAdapter.notifyDataSetChanged()


        userlistAdapter.setOnItemClickCallback(object : UserlistAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UserList) = setSelectedUser(data)

        })
    }

    private fun searchUser() {
        sv_username.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    users.clear()
                    showLoading(true)
                    mainViewModel.setUserlist(query)
                }
                 return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun setSelectedUser(user: UserList) {
        val move = Intent(this, DetailActivity::class.java)
        move.putExtra(DetailActivity.PUT_EXTRA, user.username)
        startActivity(move)
    }

    private fun setActionBar(title: String) {
        val icon = ResourcesCompat.getDrawable(resources, R.drawable.logo, null)?.toBitmap()
        val fixedIcon = BitmapDrawable(resources,
            icon.let { Bitmap.createScaledBitmap(it!!, 60, 60, true) })
        supportActionBar?.setHomeAsUpIndicator(fixedIcon)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = title
        }
    }
    private fun hideKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    private fun showLoading(state: Boolean) {
        if (state) toolbarprogress.visibility = View.VISIBLE
        else toolbarprogress.visibility = View.GONE
    }
    }

