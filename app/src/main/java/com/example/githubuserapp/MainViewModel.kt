package com.example.githubuserapp

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private val listUsers = MutableLiveData<ArrayList<DataUser>>()

    fun setUser(username: String?, context: Context, tv: TextView, loading: ProgressBar) {
        val listItems = ArrayList<DataUser>()

        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        client.addHeader("Authorization", "token 187eb466afb595a53a772e6cf6b007819ab293ef")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                // Jika koneksi berhasil
                listItems.clear()
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")
                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        val userItems = DataUser()
                        userItems.username = user.getString("login")
                        userItems.name = user.getInt("id").toString()
                        userItems.location = ""
                        userItems.repository = user.getString("repos_url")
                        userItems.company = ""
                        userItems.followers = user.getString("followers_url")
                        val following_url = user.getString("followers_url")
                        val following_url_fix = following_url.replace("{/other_user}", "")
                        userItems.following = following_url_fix
                        userItems.avatar = user.getString("avatar_url")
                        listItems.add(userItems)
                    }
                    if(listItems.isNotEmpty()){
                        listUsers.postValue(listItems)
                    }
                    else{
                        loading.visibility = View.GONE
                        tv.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                if(errorMessage == "$statusCode : Not Found"){
                    tv.visibility = View.VISIBLE
                }
                else{
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun getUser(): LiveData<ArrayList<DataUser>> {
        return listUsers
    }
}