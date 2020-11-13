package com.example.githubuserapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var rvHeroes: RecyclerView
    private var listItems: ArrayList<DataUser> = arrayListOf()
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar
        actionBar!!.title = "Github Users"

        rvHeroes = findViewById(R.id.rv_heroes)
        rvHeroes.setHasFixedSize(true)

        tv_main_nothing.visibility = View.GONE
        main_progressbar.visibility = View.GONE
        searchView = findViewById(R.id.main_SearchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                main_progressbar.visibility = View.VISIBLE
                getRandomQuote(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun showRecyclerList() {
        tv_main_nothing.visibility = View.GONE
        main_progressbar.visibility = View.GONE
        rvHeroes.layoutManager = LinearLayoutManager(this)
        val listHeroAdapter = ListUserAdapter(listItems)
        rvHeroes.adapter = listHeroAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.about_item) {
            val intentAboutMe = Intent(this@MainActivity, AboutMe::class.java)
            startActivity(intentAboutMe)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getRandomQuote(username: String?) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
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
                    val list = responseObject.getJSONArray("items")
                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        val userItems = DataUser()
                        userItems.username = user.getString("login")
                        userItems.name = ""
                        userItems.location = ""
                        userItems.repository = user.getString("repos_url")
                        userItems.company = ""
                        userItems.followers = user.getString("followers_url")
                        val following_url = user.getString("followers_url")
                        val following_url_fix = following_url.replace("{/other_user}", "")
                        userItems.following = following_url_fix
                        userItems.avatar = user.getString("avatar_url")
                        listItems.add(userItems)
                    }
                    showRecyclerList()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
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
                if(errorMessage == "$statusCode : Not Found"){
                    tv_main_nothing.visibility = View.VISIBLE
                }
                else{
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
