package com.submisi2.githubuser.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.submisi2.githubuser.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.submisi2.githubuser.database.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.submisi2.githubuser.database.DatabaseContract.FavoriteColumns.Companion._ID
import java.sql.SQLException
import kotlin.jvm.Throws

class FavoriteHelper(context: Context) {
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME

        private var INSTANCE: FavoriteHelper? = null
        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }
    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (database.isOpen)database.close()
    }

    fun queryAll(): Cursor {
        Log.d("helperMode", "Query All Database")

        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC",
            null
        )
    }


    fun queryByUsername(username: String): Cursor {
        Log.d("helperMode", "Query by username $username")

        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        Log.d("helperMode", "Insert with data $values")
        return database.insert(DATABASE_TABLE, null, values)

    }
    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }
    fun deleteByUsername(username: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = ?", arrayOf(username))
    }
}
