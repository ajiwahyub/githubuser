package com.submisi2.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserDetail(var username: String? = "",
                 var name: String? = "",
                 var avatar: String? = "",
                 var company: String? = "",
                 var location: String? = "",
                 var repository: String? = "",
                 var followers: String? = "",
                 var following: String? = ""
) : Parcelable
