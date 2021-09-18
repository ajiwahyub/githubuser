package com.submisi2.githubuser

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.submisi2.githubuser.database.DatabaseContract.AUTHORITY
import com.submisi2.githubuser.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.submisi2.githubuser.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.submisi2.githubuser.database.FavoriteHelper

class FavoriteProvider : ContentProvider() {
    companion object {
        private const val GITHUB_USER = 1
        private const val GITHUB_USER_ID = 2
        private const val GITHUB_USER_USERNAME = 3
        private lateinit var favoriteHelper: FavoriteHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, GITHUB_USER)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", GITHUB_USER_ID)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", GITHUB_USER_USERNAME)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (sUriMatcher.match(uri)) {
            //GITHUB_USER_ID -> favoriteHelper.deleteById(uri.lastPathSegment.toString())
            GITHUB_USER_USERNAME -> favoriteHelper.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (GITHUB_USER) {
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()

        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            GITHUB_USER -> favoriteHelper.queryAll()
            //GITHUB_USER_ID -> favoriteHelper.queryById(uri.lastPathSegment.toString())
            GITHUB_USER_USERNAME -> favoriteHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (GITHUB_USER_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return updated
    }
}