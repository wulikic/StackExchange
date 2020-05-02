package com.vesna.stackexchange.domain

import java.util.*

data class User(
    val username: String,
    val reputation: Int,
    val avatar: String,
    val location: String,
    val creationDate: Date,
    val badges: Badges
)

data class Badges(
    val bronzeCount: Int,
    val silverCount: Int,
    val goldCount: Int
)