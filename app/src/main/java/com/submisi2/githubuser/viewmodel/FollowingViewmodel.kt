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
import org.json.JSONArray
import java.lang.Exception

class FollowingViewmodel : ViewModel() {
    private val listFollowing = MutableLiveData(ArrayList<UserList>())
    private val KEY = ""

    companion object {
        private val TAG = FollowingViewmodel::class.java.simpleName
    }

    fun setFollowinglist(id: String) {
        val followingList = ArrayList<UserList>()
        val usersUrl = "https://api.github.com/users/$id/following"
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
                    val respondArray = JSONArray(result)


                    for (i in 0 until respondArray.length()) {
                        val item = respondArray.getJSONObject(i)
                        val dataUsername = item.getString("login")
                        val dataAvatar = item.getString("avatar_url")
                        val following =
                            UserList(dataUsername, dataAvatar)
                        followingList.add(following)
                    }
                    listFollowing.postValue(followingList)
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

    fun getFollowing(): LiveData<ArrayList<UserList>> {
        return listFollowing
    }
}
