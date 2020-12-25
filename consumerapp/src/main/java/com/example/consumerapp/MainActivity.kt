package com.example.consumerapp

import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var CONTENT_URI: Uri
    private lateinit var adapter: ListUserAdapter

    companion object {
        const val AUTHORITY = "com.example.githubuserapp"
        const val SCHEME = "content"
        const val TABLE_NAME = "tbuserfavorite"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Consumer App"

        tv_main_nothing.visibility = View.GONE

        rv_main.setHasFixedSize(true)
        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()
        rv_main.layoutManager = LinearLayoutManager(this)
        rv_main.adapter = adapter

        CONTENT_URI = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        loadNotesAsync()
    }

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<DataUser> {
        val userList = ArrayList<DataUser>()

        notesCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow("username"))
                val name = getString(getColumnIndexOrThrow("name"))
                val location = getString(getColumnIndexOrThrow("location"))
                val repository = getString(getColumnIndexOrThrow("repository"))
                val company = getString(getColumnIndexOrThrow("company"))
                val followers = getString(getColumnIndexOrThrow("followers"))
                val following = getString(getColumnIndexOrThrow("following"))
                val avatar = getString(getColumnIndexOrThrow("avatar"))
                val publicRepo = getString(getColumnIndexOrThrow("publicRepo"))
                userList.add(
                    DataUser(
                        username,
                        name,
                        location,
                        repository,
                        company,
                        followers,
                        following,
                        avatar,
                        publicRepo
                    )
                )
            }
        }
        return userList
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            main_progressbar.visibility = View.VISIBLE
            val deferredUsers = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                mapCursorToArrayList(cursor)
            }
            val users = deferredUsers.await()
            main_progressbar.visibility = View.INVISIBLE
            if (users.size > 0) {
                adapter.setData(users)
            } else {
                adapter.setData(arrayListOf())
                tv_main_nothing.visibility = View.VISIBLE
            }
        }
    }
}
