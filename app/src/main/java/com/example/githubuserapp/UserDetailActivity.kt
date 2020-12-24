package com.example.githubuserapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayout
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@Suppress("DEPRECATION")
class UserDetailActivity : AppCompatActivity() {

    private var user: DataUser? = null
    private var isFavorite: Boolean = false
    private lateinit var db: AppDatabase
    private lateinit var userFavorite: User
    private var listItems2: ArrayList<DataUser> = arrayListOf()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        user = intent.getParcelableExtra("userGithub")
        detail_progressbar.visibility = View.VISIBLE
        setUser(user?.username)

        val actionbar = supportActionBar
        actionbar?.hide()

        btn_detail_back.setOnClickListener { onBackPressed() }

        loadFragment()

        btn_detail_setting.setOnClickListener {
            val intent = Intent(this@UserDetailActivity, SettingActivity::class.java)
            startActivity(intent)
        }

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "favoritedb"
        ).build()

        userFavorite = User()
        userFavorite.name = user?.name.toString()
        userFavorite.username = user?.username
        userFavorite.location = user?.location
        userFavorite.repository = user?.repository
        userFavorite.company = user?.company
        userFavorite.followers = user?.followers
        userFavorite.following = user?.following
        userFavorite.avatar = user?.avatar
        userFavorite.publicRepo = user?.publicRepo

        loadIfFavorite()

        btn_detail_favorite.setOnClickListener {
            if (isFavorite){
                isFavorite = false
                val img: Drawable = this.resources.getDrawable(R.drawable.ic_favorite_border_black_24dp)
                btn_detail_favorite.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
                Toast.makeText(this, "Dihapus dari Favorite", Toast.LENGTH_SHORT).show()
                GlobalScope.launch {
                    val url = UserProvider.CONTENT_URI.toString() + "/${user?.name}"
                    contentResolver.delete(Uri.parse(url), user?.name, null)
                }
            }
            else{
                isFavorite = true
                val img: Drawable = this.resources.getDrawable(R.drawable.ic_favorite_white_24dp)
                btn_detail_favorite.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
                Toast.makeText(this, "Ditambahkan ke Favorite", Toast.LENGTH_SHORT).show()
                GlobalScope.launch {
                    val contentValue = ContentValues()
                    contentValue.put("username", user?.username)
                    contentValue.put("name", user?.name.toString())
                    contentValue.put("location", user?.location)
                    contentValue.put("repository", user?.repository)
                    contentValue.put("company", user?.company)
                    contentValue.put("followers", user?.followers)
                    contentValue.put("following", user?.following)
                    contentValue.put("avatar", user?.avatar)
                    contentValue.put("publicRepo", user?.publicRepo)

                    contentResolver.insert(UserProvider.CONTENT_URI, contentValue)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setUser(user?.username)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun loadIfFavorite(){
        detail_progressbar.visibility = View.VISIBLE
        val user = db.userDao().loadById(user?.name.toString())
        user.observe(this, Observer { User ->
            if (User != null) {
                isFavorite = true
                val img: Drawable = this.resources.getDrawable(R.drawable.ic_favorite_white_24dp)
                btn_detail_favorite.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
                detail_progressbar.visibility = View.GONE
            }
            else{
                detail_progressbar.visibility = View.GONE
            }
        })
    }

    fun setUser(username: String?) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
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
        val jumlahRepo = user.publicRepo + " " + resources.getString(R.string.repositories)
        tv_detail_repo.text = jumlahRepo

        detail_progressbar.visibility = View.GONE
    }

    private fun loadFragment(){
        try{
            val mFragmentManager = supportFragmentManager
            val mFollowersFragment = FollowersFragment(user?.followers, " Followers", applicationContext, tv_followers)
            val mFollowingfragment = FollowingFragment(user?.following, " Following", applicationContext, tv_following, 0)
            mFragmentManager
                .beginTransaction()
                .add(R.id.frame_container, mFollowingfragment, FollowingFragment::class.java.simpleName)
                .commit()

            mFragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, mFollowersFragment, FollowersFragment::class.java.simpleName)
                commit()
            }

            tabLayout1.addTab(tabLayout1.newTab().setText("Followers"))
            tabLayout1.addTab(tabLayout1.newTab().setText("Following"))
            tabLayout1.setTabTextColors(Color.parseColor("#BDBDBD"), Color.parseColor("#FFFFFF"))
            tabLayout1.setSelectedTabIndicatorHeight(4)
            tabLayout1.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
                override fun onTabSelected(p0: TabLayout.Tab?) {
                    if(p0?.position == 0){
                        mFragmentManager.beginTransaction().apply {
                            replace(R.id.frame_container, mFollowersFragment, FollowersFragment::class.java.simpleName)
                            commit()
                        }
                    }
                    else{
                        mFragmentManager.beginTransaction().apply {
                            val mFollowingfragment2 = FollowingFragment(user?.following, " Following", applicationContext, tv_following, p0?.position)
                            replace(R.id.frame_container, mFollowingfragment2, FollowingFragment::class.java.simpleName)
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
        catch (e: Exception) {
            Toast.makeText(this@UserDetailActivity, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun getFollowersOrFollowing(url:String?, penanda: String?, context: Context, textView: TextView){
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

                    if(penanda == " Followers"){
                        val followers = listItems2.size.toString() + penanda
                        textView.text = followers

                        detail_progressbar.visibility = View.GONE
                    }
                    else{
                        val following = listItems2.size.toString() + penanda
                        textView.text = following

                        detail_progressbar.visibility = View.GONE
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

