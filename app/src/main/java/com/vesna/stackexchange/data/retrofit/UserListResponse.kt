package com.vesna.stackexchange.data.retrofit

import com.google.gson.annotations.SerializedName

data class UserListResponse(
    @SerializedName("items") val items: List<UserResponse>
)

data class UserResponse(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("reputation") val reputation: Int,
    @SerializedName("creation_date") val creationDate: Long,
    @SerializedName("profile_image") val profilePicture: String,
    @SerializedName("badge_counts") val badges: BadgesResponse,
    @SerializedName("location") val location: String?,
    @SerializedName("age") val age: Int?
)

data class BadgesResponse(
    @SerializedName("bronze") val bronze: Int,
    @SerializedName("silver") val silver: Int,
    @SerializedName("gold") val gold: Int
)