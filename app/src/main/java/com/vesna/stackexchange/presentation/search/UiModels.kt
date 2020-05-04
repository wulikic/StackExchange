package com.vesna.stackexchange.presentation.search

import com.vesna.stackexchange.domain.User

data class SearchUiModel(
    val users: List<UserUiModel>,
    val showSearchInProgress: Boolean
)

data class UserUiModel(
    val username: String,
    val reputation: Int,
    val userId: Int
)



sealed class SearchUiEvent
object ShowError : SearchUiEvent()
data class NavigateToUserDetails(val user: User) : SearchUiEvent()

