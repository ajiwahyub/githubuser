package com.submisi2.githubuser.helper

import android.database.Cursor
import com.submisi2.githubuser.database.DatabaseContract
import com.submisi2.githubuser.model.Favorite
import com.submisi2.githubuser.database.DatabaseContract.FavoriteColumns

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
    fun mapCursorToObject(userFavoriteCursor: Cursor?): Favorite {
        var favorite = Favorite()

        userFavoriteCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(FavoriteColumns._ID))
            val username = getString(getColumnIndexOrThrow(FavoriteColumns.USERNAME))
            val name = getString(getColumnIndexOrThrow(FavoriteColumns.NAME))
            val avatarUrl = getString(getColumnIndexOrThrow(FavoriteColumns.AVATAR_URL))
            val company = getString(getColumnIndexOrThrow(FavoriteColumns.COMPANY))
            val location = getString(getColumnIndexOrThrow(FavoriteColumns.LOCATION))
            val publicRepos = getInt(getColumnIndexOrThrow(FavoriteColumns.PUBLIC_REPOS))
            val following = getInt(getColumnIndexOrThrow(FavoriteColumns.FOLLOWING))
            val followers = getInt(getColumnIndexOrThrow(FavoriteColumns.FOLLOWERS))

            favorite = Favorite(id, username, name, avatarUrl, company, location, publicRepos, following, followers)
        }

        return favorite
    }
}