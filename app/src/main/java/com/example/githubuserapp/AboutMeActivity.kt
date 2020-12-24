package com.example.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView

class AboutMeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)

        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.title = "About Me"
            actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu2, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.setting_item){
            val intentSetting = Intent(this@AboutMeActivity, SettingActivity::class.java)
            startActivity(intentSetting)
        }
        return super.onOptionsItemSelected(item)
    }
}

