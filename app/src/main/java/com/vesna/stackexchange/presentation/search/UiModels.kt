package com.vesna.stackexchange.presentation.search

import com.vesna.stackexchange.domain.User

data class SearchUiModel(
    val users: List<UserUiModel>,
    val showSearchInProgress: Boolean,
    val showError: Event<Any>?,
    val navigateToUserDetails: Event<User>?
)

data class UserUiModel(
    val username: String,
    val reputation: Int,
    val userId: Int
)