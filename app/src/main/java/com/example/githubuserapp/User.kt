package com.example.githubuserapp

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbuserfavorite")
data class User(
    @ColumnInfo(name = "username") var username: String?,
    @PrimaryKey var name: String,
    @ColumnInfo(name = "location") var location: String?,
    @ColumnInfo(name = "repository") var repository: String?,
    @ColumnInfo(name = "company") var company: String?,
    @ColumnInfo(name = "followers") var followers: String?,
    @ColumnInfo(name = "following") var following: String?,
    @ColumnInfo(name = "avatar") var avatar: String?,
    @ColumnInfo(name = "publicRepo") var publicRepo: String?
)