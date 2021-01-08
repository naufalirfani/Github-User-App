package com.example.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {

    lateinit var db: AppDatabase
    private lateinit var rvFavorite: RecyclerView
    private lateinit var adapter: ListFavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.title = "Favorite User"
            actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "favoritedb"
        ).build()

        rvFavorite = findViewById(R.id.rv_favorite)
        rvFavorite.setHasFixedSize(true)

        adapter = ListFavoriteAdapter(this, db)
        adapter.notifyDataSetChanged()

        rvFavorite.layoutManager = LinearLayoutManager(this)
        rvFavorite.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        db.userDao().getAll().observe(this, Observer { User ->
            if (User.isNotEmpty()) {
                tv_favorite_nothing.visibility = View.GONE
                adapter.setData(User)
                favorite_progressbar.visibility = View.GONE
            }
            else{
                favorite_progressbar.visibility = View.GONE
                tv_favorite_nothing.visibility = View.VISIBLE
            }
        })
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
            val intentSetting = Intent(this@FavoriteActivity, SettingActivity::class.java)
            startActivity(intentSetting)
        }
        return super.onOptionsItemSelected(item)
    }
}
