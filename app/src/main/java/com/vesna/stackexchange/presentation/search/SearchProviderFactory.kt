package com.vesna.stackexchange.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vesna.stackexchange.domain.GetFirst20UsersSortedAlphabetically

class SearchProviderFactory(
    private val searchUsers: GetFirst20UsersSortedAlphabetically
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(searchUsers) as T
        } else {
            throw IllegalStateException("Class not supported")
        }
    }
}