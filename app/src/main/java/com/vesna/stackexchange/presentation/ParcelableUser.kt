package com.vesna.stackexchange.presentation

import android.os.Parcelable
import com.vesna.stackexchange.domain.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParcelableUser(
    val id: Int,
    val username: String,
    val reputation: Int,
    val avatar: String,
    val creationTime: Long,
    val bronzeBadges: Int,
    val silverBadges: Int,
    val goldBadges: Int,
    val location: String?,
    val age: Int?
): Parcelable

fun User.toParcelable(): ParcelableUser {
    return ParcelableUser(
        id = id,
        username = username,
        reputation = reputation,
        avatar = avatar,
        creationTime = creationDate.time,
        bronzeBadges = badges.bronzeCount,
        silverBadges = badges.silverCount,
        goldBadges = badges.goldCount,
        location = location,
        age = age
    )
}
