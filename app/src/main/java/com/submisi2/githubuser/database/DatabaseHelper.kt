package com.submisi2.githubuser.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.submisi2.githubuser.database.DatabaseContract.FavoriteColumns

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbfavorite"

        private const val DATABASE_VERSION = 4

        private const val SQL_CREATE_TABLE_GITHUB_USER = "" +
                "CREATE TABLE ${FavoriteColumns.TABLE_NAME}" +
                "(${FavoriteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${FavoriteColumns.USERNAME} TEXT NOT NULL," +
                "${FavoriteColumns.NAME} TEXT NOT NULL," +
                "${FavoriteColumns.AVATAR_URL} TEXT NOT NULL," +
                "${FavoriteColumns.COMPANY} TEXT NOT NULL," +
                "${FavoriteColumns.LOCATION} TEXT NOT NULL," +
                "${FavoriteColumns.PUBLIC_REPOS} TEXT NOT NULL," +
                "${FavoriteColumns.FOLLOWING} TEXT NOT NULL," +
                "${FavoriteColumns.FOLLOWERS} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_GITHUB_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${FavoriteColumns.TABLE_NAME}")
        onCreate(db)
    }
}
