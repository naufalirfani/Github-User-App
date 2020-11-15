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
import kotlinx.android.synthetic.main.fragment_followers.*
import org.json.JSONArray

/**
 * A simple [Fragment] subclass.
 */
class FollowersFragment(private val url: String?,
                        private val penanda: String?,
                        private val context2: Context,
                        private val textView: TextView) : Fragment() {

    private var listItems2: ArrayList<DataUser> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followers_progressbar.visibility = View.VISIBLE
        getFollowersOrFollowing(url, penanda, context2, textView)
    }

    private fun getFollowersOrFollowing(url:String?, penanda: String?, context: Context, textView: TextView){
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token c5c9d1d8cef9ce23b234ecfd6a7f5b105bc92b77")
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
                    rv_followers.layoutManager = LinearLayoutManager(context)
                    val adapter = ListUserAdapter()
                    adapter.setData(listItems2)
                    adapter.notifyDataSetChanged()
                    rv_followers.adapter = adapter

                    if(penanda == " Followers"){
                        val followers = listItems2.size.toString() + penanda
                        textView.text = followers

                        followers_progressbar.visibility = View.GONE
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
