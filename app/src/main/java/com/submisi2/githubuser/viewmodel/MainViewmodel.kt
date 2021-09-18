package com.submisi2.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.submisi2.githubuser.BuildConfig
import com.submisi2.githubuser.model.UserList
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewmodel :ViewModel() {

    private val userUsers = MutableLiveData(ArrayList<UserList>())
    private val KEY = ""

    companion object {
        private val TAG = MainViewmodel::class.java.simpleName
    }

    fun setUserlist(id: String) {
        val ItemList = ArrayList<UserList>()
        val usersUrl = "https://api.github.com/search/users?q=$id"
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $KEY")
        client.get(usersUrl, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    Log.d(TAG, result)
                    val respondObject = JSONObject(result)
                    val items = respondObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val itemUsername = item.getString("login")
                        val itemAvatar = item.getString("avatar_url")
                        val itemType = item.getString("type")
                        val itemUrl  =item.getString("html_url")
                        val userItems =
                            UserList(itemUsername, itemAvatar, itemType,itemUrl)
                        ItemList.add(userItems)
                    }
                    userUsers.postValue(ItemList)

                } catch (e: Exception) {
                    Log.d("Exception: ", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d("onFailure: ", error?.message.toString())
            }

        })
    }

    fun getUsernames(): LiveData<ArrayList<UserList>> = userUsers
}
