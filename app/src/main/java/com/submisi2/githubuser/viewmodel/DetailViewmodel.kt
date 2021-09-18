package com.submisi2.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.submisi2.githubuser.BuildConfig
import com.submisi2.githubuser.model.UserDetail
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailViewmodel :ViewModel(){
    private val usersDetail = MutableLiveData<UserDetail>()
    private val KEY = ""

    companion object {
        private val TAG = DetailViewmodel::class.java.simpleName
    }

    fun setDetailItems(users: String) {
        val detailUrl = "https://api.github.com/users/$users"
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token $KEY")
        client.get(detailUrl, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = responseBody?.let { String(it) }
                    Log.d(TAG, result!!)
                    val jsonObject = JSONObject(result)
                    val detailItems = UserDetail().apply {
                        username = jsonObject.getString("login")
                        name = jsonObject.getString("name")
                        avatar = jsonObject.getString("avatar_url")
                        company = jsonObject.getString("company")
                        location = jsonObject.getString("location")
                        repository = jsonObject.getString("public_repos")
                        followers = jsonObject.getString("followers")
                        following = jsonObject.getString("following")
                    }

                    usersDetail.postValue(detailItems)
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

    fun getDetailItems(): LiveData<UserDetail> = usersDetail
}