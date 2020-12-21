package com.example.githubuserapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbuserfavorite")
data class User(
    @ColumnInfo(name = "username") val username: String?,
    @PrimaryKey val name: String?,
    @ColumnInfo(name = "location") val location: String?,
    @ColumnInfo(name = "repository") val repository: String?,
    @ColumnInfo(name = "company") val company: String?,
    @ColumnInfo(name = "followers") val followers: String?,
    @ColumnInfo(name = "following") val following: String?,
    @ColumnInfo(name = "avatar") val avatar: String?,
    @ColumnInfo(name = "publicRepo") val publicRepo: String?
)