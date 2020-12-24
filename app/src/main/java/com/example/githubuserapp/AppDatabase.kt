package com.example.githubuserapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME = "favoritedb"
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context?): AppDatabase? {
            if (INSTANCE == null) {
                INSTANCE = context?.let {
                    Room
                        .databaseBuilder(
                            it,
                            AppDatabase::class.java,
                            DATABASE_NAME
                        ).build()
                }
            }
            return INSTANCE
        }
    }
}