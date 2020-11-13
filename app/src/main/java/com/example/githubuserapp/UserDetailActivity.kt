package com.example.githubuserapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_movie_detail.*


class UserDetailActivity : AppCompatActivity() {

    private lateinit var rvDetail: RecyclerView
    private var list: ArrayList<DataUser> = arrayListOf()
    private var letak: Int = 0
    private var user: DataUser? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        user = intent.getParcelableExtra("userGithub")

        val tvDataReceived: TextView = findViewById(R.id.tv_data_received)
        tvDataReceived.text = user?.name

        val actionbar = supportActionBar
        actionbar!!.title = "Detail User"
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val id: Int = resources.getIdentifier("com.example.githubuserapp:drawable/${user?.avatar}", null, null)
        Glide.with(this)
            .load(id)
            .apply(RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
            .into(img_photo)
        val followers = user?.followers + " Followers"
        val following = user?.following + " Following"
        tv_followers.text = followers
        tv_following.text = following

        val detail = """
            Username    : ${user?.username}
            Location       : ${user?.location}
            Repository   : ${user?.repository}
            Company     : ${user?.company}
        """.trimIndent()

        tv_detail.text = detail
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

