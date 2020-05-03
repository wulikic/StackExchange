package com.vesna.stackexchange.domain

import java.util.*

data class User(
    val id: Int,
    val username: String,
    val reputation: Int,
    val avatar: String,
    val creationDate: Date,
    val badges: Badges,
    val location: String?,
    val age: Int?
)

data class Badges(
    val bronzeCount: Int,
    val silverCount: Int,
    val goldCount: Int
)