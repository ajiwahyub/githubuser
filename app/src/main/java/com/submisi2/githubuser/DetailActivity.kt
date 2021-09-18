package com.submisi2.githubuser

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.ContentObserver
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.submisi2.githubuser.adapter.SectionsAdapter
import com.submisi2.githubuser.database.DatabaseContract.FavoriteColumns
import com.submisi2.githubuser.helper.MappingHelper
import com.submisi2.githubuser.model.Favorite
import com.submisi2.githubuser.model.UserDetail
import com.submisi2.githubuser.viewmodel.DetailViewmodel
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class DetailActivity : AppCompatActivity() {

    private lateinit var uriWithQuery: Uri
    private lateinit var detailUserViewModel: DetailViewmodel
    private lateinit var username: String
    private lateinit var avatarString: String
    private var favorite: Favorite? = null

    companion object {
        const val PUT_EXTRA = "extra_parcelable"
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        showLoadingUserDetails(true)
        getSelectedUser()
        setActionBar(username)
        pagerConfig()


        detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewmodel::class.java)
        detailUserViewModel.setDetailItems(username)
        detailUserViewModel.getDetailItems().observe(this,{ detailItems ->
            if (detailItems != null) {
                showUserDetails(detailItems)
                showLoadingUserDetails(false)
                setUriQuery()
            }
        })
    }

    private fun setUriQuery() {
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                GlobalScope.launch(Dispatchers.Main) {
                    val deferredUserFavorite = async(Dispatchers.IO) {
                        val cursor = contentResolver.query(FavoriteColumns.CONTENT_URI, null, null, null, null)
                        MappingHelper.mapCursorToArrayList(cursor)
                    }

                    deferredUserFavorite.await()
                }
            }
        }

        contentResolver.registerContentObserver(FavoriteColumns.CONTENT_URI, true, myObserver)
        setUriAndFavoriteStatus(username)
    }

    private fun setUriAndFavoriteStatus(uri: String) {
        uriWithQuery = Uri.parse("${FavoriteColumns.CONTENT_URI}/$uri")
        val cursor = contentResolver.query(uriWithQuery, null, null, null, null)

        if (cursor != null) {
            try {
                favorite = MappingHelper.mapCursorToObject(cursor)
                cursor.close()

                favoriteStatus(true)
            }
            catch (e: Exception) {
                Log.d("cursorStatus", "User not found on database")
                favorite = null

                favoriteStatus(false)
            }
        }
        else {
            favorite = null

            favoriteStatus(false)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun favoriteStatus(isFavorite: Boolean) {
        if (!isFavorite) {
            fab_fav.setImageDrawable(getDrawable(R.drawable.ic_outline_favorite_border_24))
            fab_fav.setOnClickListener {
                addUserFavorite()}
        }
        else {
            fab_fav.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite_24))
            fab_fav.setOnClickListener {
                deleteUserFavorite()}
        }
    }

    private fun deleteUserFavorite() {
        contentResolver.delete(uriWithQuery, null, null)
        setUriAndFavoriteStatus(username)
        Toast.makeText(this, getString(R.string.unvaforite_user, username), Toast.LENGTH_LONG).show()
    }

    private fun showUserDetails(detailUserItems: UserDetail) {
        avatarString = detailUserItems.avatar.toString()
        tv_detail_name.text = detailUserItems.name
        tv_detail_username.text = username
        tv_detail_company.text = detailUserItems.company
        tv_detail_location.text = detailUserItems.location
        tv_detail_repo.text = detailUserItems.repository.toString()
        tv_detail_follower.text = detailUserItems.followers.toString()
        tv_detail_following.text = detailUserItems.following.toString()
        Glide.with(this)
            .load(detailUserItems.avatar)
            .into(detail_avatar)
    }
    private fun pagerConfig() {
        val pager = SectionsAdapter(this, supportFragmentManager)
        pager.setUsername(username)
        view_pager.adapter = pager
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }
    private fun getSelectedUser() {
        val user = intent.getStringExtra(PUT_EXTRA)
        username = user.toString()
    }
    private fun showLoadingUserDetails(state: Boolean) {
        if (state) detail_toolbarprogress.visibility = View.VISIBLE
        else detail_toolbarprogress.visibility = View.GONE
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


    private fun addUserFavorite( ) {

        val itemUsername = tv_detail_username.text.toString()
        val itemName = tv_detail_name.text.toString()
        val itemAvatar = avatarString
        val itemCompany = tv_detail_company.text.toString()
        val itemLocation = tv_detail_location.text.toString()
        val itemRepo = tv_detail_repo.text.toString()
        val itemFollowing = tv_detail_following.text.toString()
        val itemFollower = tv_detail_follower.text.toString()


        val values = ContentValues()
            values.put(FavoriteColumns.USERNAME, itemUsername)
            values.put(FavoriteColumns.NAME, itemUsername)
            values.put(FavoriteColumns.AVATAR_URL, itemAvatar)
            values.put(FavoriteColumns.COMPANY, itemCompany)
            values.put(FavoriteColumns.LOCATION, itemLocation)
            values.put(FavoriteColumns.PUBLIC_REPOS, itemRepo)
            values.put(FavoriteColumns.FOLLOWING, itemFollowing)
            values.put(FavoriteColumns.FOLLOWERS, itemFollower)

        contentResolver.insert(FavoriteColumns.CONTENT_URI, values)
        Toast.makeText(this, getString(R.string.favorite_user, itemUsername), Toast.LENGTH_LONG).show()
        setUriAndFavoriteStatus(itemUsername)
    }

}