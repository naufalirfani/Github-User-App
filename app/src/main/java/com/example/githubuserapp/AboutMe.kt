package com.example.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class AboutMe : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)

        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "About Me"
        //set back button
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val iv: ImageView = findViewById(R.id.img_my_photo)
        iv.setImageResource(resources.getIdentifier("fotoku", "drawable", packageName))

        val myName: TextView = findViewById(R.id.tv_name)
        val email: TextView = findViewById(R.id.tv_email)

        myName.text = getString(R.string.data_pribadi)
        email.text = getString(R.string.email)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

