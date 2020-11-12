package com.example.githubuserapp

import android.os.Parcel
import android.os.Parcelable

data class DataUser(
    var username: String = "",
    var name: String = "",
    var location: String = "",
    var repository: String = "",
    var company: String = "",
    var followers: String = "",
    var following: String = "",
    val avatar: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(name)
        parcel.writeString(location)
        parcel.writeString(repository)
        parcel.writeString(company)
        parcel.writeString(followers)
        parcel.writeString(following)
        parcel.writeString(avatar)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataUser> {
        override fun createFromParcel(parcel: Parcel): DataUser {
            return DataUser(parcel)
        }

        override fun newArray(size: Int): Array<DataUser?> {
            return arrayOfNulls(size)
        }
    }
}