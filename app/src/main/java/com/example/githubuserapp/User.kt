package com.example.githubuserapp

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tbuserfavorite")
class User {
    @PrimaryKey
    lateinit var name: String

    @ColumnInfo(name = "username")
    var username: String? = null

    @ColumnInfo(name = "location")
    var location: String? = null

    @ColumnInfo(name = "repository")
    var repository: String? = null

    @ColumnInfo(name = "company")
    var company: String? = null

    @ColumnInfo(name = "followers")
    var followers: String? = null

    @ColumnInfo(name = "following")
    var following: String? = null

    @ColumnInfo(name = "avatar")
    var avatar: String? = null

    @ColumnInfo(name = "publicRepo")
    var publicRepo: String? = null

    companion object {
        fun fromContentValues(contentValues: ContentValues): User {
            val user = User()
            if (contentValues.containsKey("username")) {
                user.username = contentValues.getAsString("username")
            }
            if (contentValues.containsKey("name")) {
                user.name = contentValues.getAsString("name")
            }
            if (contentValues.containsKey("location")) {
                user.location = contentValues.getAsString("location")
            }
            if (contentValues.containsKey("repository")) {
                user.repository = contentValues.getAsString("repository")
            }
            if (contentValues.containsKey("company")) {
                user.company = contentValues.getAsString("company")
            }
            if (contentValues.containsKey("followers")) {
                user.followers = contentValues.getAsString("followers")
            }
            if (contentValues.containsKey("following")) {
                user.following = contentValues.getAsString("following")
            }
            if (contentValues.containsKey("avatar")) {
                user.avatar = contentValues.getAsString("avatar")
            }
            if (contentValues.containsKey("publicRepo")) {
                user.publicRepo = contentValues.getAsString("publicRepo")
            }
            return user
        }
    }
}