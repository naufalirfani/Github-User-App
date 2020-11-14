package com.example.githubuserapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private var listItems2: ArrayList<DataUser> = arrayListOf()

    private val mData = ArrayList<DataUser>()
    fun setData(items: ArrayList<DataUser>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_user, viewGroup, false)
        return ListViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = mData[position]
//        val id: Int = holder.itemView.resources.getIdentifier("com.example.githubuserapp:drawable/${user.avatar}", null, null)
        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .apply(
                RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(
                    Target.SIZE_ORIGINAL))
            .into(holder.imgPhoto)
        holder.tvUsername.text = user.username
        holder.tvId.text = user.name
        getFollowersOrFollowing(user.followers, " Followers", holder.itemView.context, holder.tvFollowers)
        getFollowersOrFollowing(user.following, " Following", holder.itemView.context, holder.tvFollowing)

//        holder.tvFollowers.text = user.followers + " Followers"
//        holder.tvFollowing.text = user.following + " Following"

        val userGithub = DataUser(
            user.username,
            user.name,
            user.location,
            user.repository,
            user.company,
            user.followers,
            user.following,
            user.avatar
        )

        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, UserDetailActivity::class.java)
            intent.putExtra("userGithub", userGithub)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        var tvId: TextView = itemView.findViewById(R.id.tv_id)
        var tvFollowers: TextView = itemView.findViewById(R.id.tv_followers)
        var tvFollowing: TextView = itemView.findViewById(R.id.tv_following)
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
    }

    private fun getFollowersOrFollowing(url:String, penanda: String, context: Context, textView: TextView){
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
                        userItems.name = ""
                        userItems.location = ""
                        userItems.repository = user.getString("repos_url")
                        userItems.company = ""
                        userItems.followers = ""
                        userItems.following = ""
                        userItems.avatar = user.getString("avatar_url")
                        listItems2.add(userItems)
                    }
                    if(penanda == " Followers"){
                        val followers = listItems2.size.toString() + penanda
                        textView.text = followers
                    }
                    else{
                        val followings = listItems2.size.toString() + penanda
                        textView.text = followings
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