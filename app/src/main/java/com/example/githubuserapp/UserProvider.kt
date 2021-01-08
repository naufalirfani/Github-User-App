package com.example.githubuserapp

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.annotation.Nullable


class UserProvider : ContentProvider() {

    companion object {
        private const val NOTE = 1
        private const val NOTE_ID = 2
        const val AUTHORITY = "com.example.githubuserapp"
        const val TABLE_NAME = "tbuserfavorite"
        val uriString = "content://$AUTHORITY/$TABLE_NAME"
        val CONTENT_URI = Uri.parse(uriString)
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private var userDao: UserDao? = null

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, NOTE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", NOTE_ID)
        }
    }

    override fun onCreate(): Boolean {
        userDao = AppDatabase.getInstance(context)?.userDao()
        return false
    }

    @Nullable
    override fun query(
        uri: Uri,
        @Nullable projection: Array<String?>?,
        @Nullable selection: String?,
        @Nullable selectionArgs: Array<String?>?,
        @Nullable sortOrder: String?
    ): Cursor? {
        val cursor: Cursor
        when (sUriMatcher.match(uri)) {
            NOTE -> {
                cursor = userDao?.getAllUser()!!
                if (context != null) {
                    cursor.setNotificationUri(context?.contentResolver, uri)
                    return cursor
                }
                throw IllegalArgumentException("Unknown URI: $uri")
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?
    ): Uri? {
        when (sUriMatcher.match(uri)) {
            NOTE -> {
                if (context != null) {
                    val id: Long? = values?.let { User.fromContentValues(it) }?.let {
                        userDao?.insertUser(
                            it
                        )
                    }
                    if (id != 0L) {
                        context!!.contentResolver.notifyChange(uri, null)
                        return id?.let { ContentUris.withAppendedId(uri, it) }
                    }
                }
                throw java.lang.IllegalArgumentException("Invalid URI: Insert failed$uri")
            }
            NOTE_ID -> throw java.lang.IllegalArgumentException("Invalid URI: Insert failed$uri")
            else -> throw java.lang.IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String?>?
    ): Int {
        val deleted: Int? = when (NOTE_ID) {
            sUriMatcher.match(uri) -> userDao?.deleteUser(selection.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted!!
    }
}
