package com.submisi2.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserList(
                     var username: String? ="" ,
                     var avatar: String? ="",
                     var type: String?="",
                     var url: String?=""
) : Parcelable

