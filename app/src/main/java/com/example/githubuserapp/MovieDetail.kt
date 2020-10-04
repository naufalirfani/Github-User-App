package com.example.githubuserapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_movie_detail.*


class MovieDetail : AppCompatActivity() {

    private lateinit var rvDetail: RecyclerView
    private var list: ArrayList<DataUser> = arrayListOf()
    private var letak: Int = 0
    private lateinit var user: DataUser

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        user = intent.getParcelableExtra("userGithub")!!

        val tvDataReceived: TextView = findViewById(R.id.tv_data_received)
        tvDataReceived.text = user.name

        val actionbar = supportActionBar
        actionbar!!.title = "Detail User"
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val id: Int = resources.getIdentifier("com.example.githubuserapp:drawable/${user.avatar}", null, null)
        img_photo.setImageResource(id)
        tv_followers.text = user.followers + " Followers"
        tv_following.text = user.following + " Following"

        val detail = """
            Username    : ${user.username}
            Location       : ${user.location}
            Repository   : ${user.repository}
            Company     : ${user.company}
        """.trimIndent()

        tv_detail.text = detail
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

