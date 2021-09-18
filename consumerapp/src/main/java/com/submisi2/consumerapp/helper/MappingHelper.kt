package com.submisi2.consumerapp.helper

import android.database.Cursor
import com.submisi2.consumerapp.model.Favorite
import com.submisi2.consumerapp.database.DatabaseContract.FavoriteColumns

object MappingHelper {
    fun mapCursorToArrayList(userFavoriteCursor: Cursor?): ArrayList<Favorite> {
        val userFavoriteList = ArrayList<Favorite>()

        userFavoriteCursor?.apply {
            while(moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(FavoriteColumns._ID))
                val username = getString(getColumnIndexOrThrow(FavoriteColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(FavoriteColumns.NAME))
                val avatarUrl = getString(getColumnIndexOrThrow(FavoriteColumns.AVATAR_URL))
                val company = getString(getColumnIndexOrThrow(FavoriteColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(FavoriteColumns.LOCATION))
                val publicRepos = getInt(getColumnIndexOrThrow(FavoriteColumns.PUBLIC_REPOS))
                val following = getInt(getColumnIndexOrThrow(FavoriteColumns.FOLLOWING))
                val followers = getInt(getColumnIndexOrThrow(FavoriteColumns.FOLLOWERS))

                userFavoriteList.add(Favorite(id, username, name, avatarUrl, company, location, publicRepos, following, followers))
            }
        }

        return userFavoriteList
    }

}