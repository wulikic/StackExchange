package com.vesna.stackexchange.presentation.search

data class SearchUiModel(
    val users: List<UserUiModel>,
    val showSearchInProgress: Boolean,
    val showNoUsersFound: Boolean,
    val showError: Boolean
)

data class UserUiModel(
    val username: String,
    val reputation: Int,
    val userId: Int
)