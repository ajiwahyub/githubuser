package com.submisi2.consumerapp

import android.database.ContentObserver
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.submisi2.consumerapp.adapter.FavoriteAdapter
import com.submisi2.consumerapp.database.DatabaseContract
import com.submisi2.consumerapp.helper.MappingHelper
import com.submisi2.consumerapp.model.Favorite
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter
    companion object {
        private const val PUT_FAVORITE = "put_favorite"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(title = "ConsumerApp")

        setRecycleView()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadLocalFavoriteData()
            }
        }

        contentResolver.registerContentObserver(DatabaseContract.FavoriteColumns.CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadLocalFavoriteData()
        }
        else {
            savedInstanceState.getParcelableArrayList<Favorite>(PUT_FAVORITE)?.also {
                adapter.listFavorite = it
            }
        }
    }

    private fun setRecycleView() {
        tv_empty_favorite.visibility = View.GONE
        rv_github_user_favorite.layoutManager = LinearLayoutManager(this)
        rv_github_user_favorite.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        rv_github_user_favorite.adapter = adapter
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

    private fun loadLocalFavoriteData() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUserFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver.query(DatabaseContract.FavoriteColumns.CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val listFav = deferredUserFavorite.await()
            if (listFav.size > 0) {
                tv_empty_favorite.visibility = View.GONE
                adapter.listFavorite = listFav
            }
            else {
                tv_empty_favorite.visibility = View.VISIBLE
                adapter.listFavorite = ArrayList()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(PUT_FAVORITE, adapter.listFavorite)
    }


}