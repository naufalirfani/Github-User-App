package com.example.githubuserapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayout
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_movie_detail.*
import org.json.JSONObject


class UserDetailActivity : AppCompatActivity() {

    private var user: DataUser? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        user = intent.getParcelableExtra("userGithub")
        setUser(user?.username)

        val actionbar = supportActionBar
        actionbar?.hide()

        btn_detail_back.setOnClickListener { onBackPressed() }

        val mFragmentManager = supportFragmentManager
        val mFollowersFragment = FollowersFragment(user?.followers, " Followers", this, tv_followers)
        mFragmentManager
            .beginTransaction()
            .add(R.id.frame_container, mFollowersFragment, FollowersFragment::class.java.simpleName)
            .commit()

        tabLayout1.addTab(tabLayout1.newTab().setText("Followers"))
        tabLayout1.addTab(tabLayout1.newTab().setText("Following"))
        tabLayout1.setTabTextColors(Color.parseColor("#BDBDBD"), Color.parseColor("#FFFFFF"))
        tabLayout1.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(p0: TabLayout.Tab?) {
                if(p0?.position == 0){
                    mFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_container, mFollowersFragment, FollowersFragment::class.java.simpleName)
                        addToBackStack(null)
                        commit()
                    }
                }
                else{
                    val mFollowingfragment = FollowingFragment()
                    mFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_container, mFollowingfragment, FollowingFragment::class.java.simpleName)
                        addToBackStack(null)
                        commit()
                    }
                }
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }
        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setUser(username: String?) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", "token 187eb466afb595a53a772e6cf6b007819ab293ef")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                // Jika koneksi berhasil
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val userItems = DataUser()
                    userItems.username = responseObject.getString("login")
                    userItems.name = responseObject.getString("name")
                    userItems.location = responseObject.getString("location")
                    userItems.repository = responseObject.getString("repos_url")
                    userItems.company = responseObject.getString("company")
                    userItems.followers = responseObject.getString("followers_url")
                    val following_url = responseObject.getString("followers_url")
                    val following_url_fix = following_url.replace("{/other_user}", "")
                    userItems.following = following_url_fix
                    userItems.avatar = responseObject.getString("avatar_url")
                    userItems.publicRepo = responseObject.getInt("public_repos").toString()
                    inisiasi(userItems)
                } catch (e: Exception) {
                    Toast.makeText(this@UserDetailActivity, e.message, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@UserDetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun inisiasi(user: DataUser){
        Glide.with(this)
            .load(user.avatar)
            .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
            .into(img_photo)
        tv_detail_name.text = user.name
        tv_detail_company.text = user.company
        tv_detail_location.text = user.location
        val following = user.following + " Following"
        tv_followers.text = ""
        tv_following.text = following
        val jumlahRepo = user.publicRepo + " " + resources.getString(R.string.repositories)
        tv_detail_repo.text = jumlahRepo
    }
}

