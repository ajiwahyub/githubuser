package com.submisi2.consumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite (
    var id: Int? = 0,
    var username: String? = null,
    var name: String? = null,
    var avatarUrl: String? = null,
    var company: String? = null,
    var location: String? = null,
    var publicRepos: Int? = null,
    var following: Int? = null,
    var followers: Int? = null
) : Parcelable
