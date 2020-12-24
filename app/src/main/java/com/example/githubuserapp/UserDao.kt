package com.example.githubuserapp

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM tbuserfavorite")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM tbuserfavorite")
    fun getAllUser(): Cursor
    
    @Query("SELECT * FROM tbuserfavorite WHERE name == :userId")
    fun loadById(userId: String): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Long

    @Query("DELETE FROM tbuserfavorite")
    fun deleteAll()

    @Query("DELETE FROM tbuserfavorite WHERE name = :id ")
    fun deleteUser(id: String): Int
}