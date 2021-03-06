package com.example.githubuserapp


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray

class FollowingFragment(
    private val url: String?,
    private val penanda: String?,
    private val context2: Context,
    private val textView: TextView,
    private val lokasi: Int?
) : Fragment() {

    private var listItems2: ArrayList<DataUser> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFollowersOrFollowing(url, penanda, context2, textView)
    }

    private fun getFollowersOrFollowing(
        url: String?,
        penanda: String?,
        context: Context,
        textView: TextView
    ) {
        if (lokasi == 1) {
            following_progressbar.visibility = View.VISIBLE
        }
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                listItems2.clear()
                val result = String(responseBody)
                try {
                    val responseObject = JSONArray(result)
                    for (i in 0 until responseObject.length()) {
                        val user = responseObject.getJSONObject(i)
                        val userItems = DataUser()
                        userItems.username = user.getString("login")
                        userItems.name = user.getInt("id").toString()
                        userItems.location = ""
                        userItems.repository = user.getString("repos_url")
                        userItems.company = ""
                        userItems.followers = user.getString("followers_url")
                        val followingUrl = user.getString("following_url")
                        val followingUrlFix = followingUrl.replace("{/other_user}", "")
                        userItems.following = followingUrlFix
                        userItems.avatar = user.getString("avatar_url")
                        listItems2.add(userItems)
                    }
                    if (penanda == " Following") {
                        val followings = listItems2.size.toString() + penanda
                        textView.text = followings
                    }

                    if (lokasi == 1) {
                        rv_following.layoutManager = LinearLayoutManager(context)
                        val adapter = ListUserAdapter()
                        adapter.setData(listItems2)
                        adapter.notifyDataSetChanged()
                        rv_following.adapter = adapter

                        following_progressbar.visibility = View.GONE
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
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }


}
