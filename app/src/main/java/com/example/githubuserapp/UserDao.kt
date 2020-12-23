package com.example.githubuserapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM tbuserfavorite")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM tbuserfavorite WHERE name == :userId")
    fun loadById(userId: String): LiveData<User>

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM tbuserfavorite")
    fun deleteAll()
}