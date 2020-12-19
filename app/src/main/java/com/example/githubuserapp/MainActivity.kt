package com.example.githubuserapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var rvHeroes: RecyclerView
    private lateinit var searchView: SearchView

    private lateinit var adapter: ListUserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar
        actionBar!!.title = "Github Users Search"


        rvHeroes = findViewById(R.id.rv_heroes)
        rvHeroes.setHasFixedSize(true)

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        rvHeroes.layoutManager = LinearLayoutManager(this)
        rvHeroes.adapter = adapter
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        tv_main_nothing.visibility = View.GONE
        main_progressbar.visibility = View.GONE
        searchView = findViewById(R.id.main_SearchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                main_progressbar.visibility = View.VISIBLE
                tv_main_nothing.visibility = View.GONE
                mainViewModel.setUser(query, applicationContext, tv_main_nothing, main_progressbar)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        mainViewModel.getUser().observe(this, Observer { DataUser ->
            if (DataUser != null) {
                adapter.setData(DataUser)
                main_progressbar.visibility = View.GONE
            }
            else{
                tv_main_nothing.visibility = View.VISIBLE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.about_item) {
            val intentAboutMe = Intent(this@MainActivity, AboutMeActivity::class.java)
            startActivity(intentAboutMe)
        }
        else if(id == R.id.setting_item){
            val intentSetting = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(intentSetting)
        }
        return super.onOptionsItemSelected(item)
    }
}
