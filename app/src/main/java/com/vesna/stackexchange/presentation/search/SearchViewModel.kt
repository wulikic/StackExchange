package com.vesna.stackexchange.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.vesna.stackexchange.domain.GetFirst20UsersSortedAlphabetically
import com.vesna.stackexchange.domain.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalStateException

class SearchViewModel(
    private val searchUsers: GetFirst20UsersSortedAlphabetically
) : ViewModel() {

    private var disposable: Disposable? = null

    private val _states = MutableLiveData<SearchState>()

    init {
        _states.value = SearchState(
            query = "",
            users = emptyList(),
            searchInProgress = false,
            searchFailed = null,
            userClicked = null
        )
    }

    val states: LiveData<SearchUiModel>
        get() = _states.toUi()

    fun onSearchClicked() {
        disposable?.dispose()
        val searchQuery = _states.last().query
        if (searchQuery.isNotEmpty()) {
            disposable = performSearch(searchQuery)
        }
    }

    fun onSearchTextChanged(text: String) {
        updateState { it.copy(query = text) }
    }

    fun onUserClicked(userId: Int) {
        updateState { state ->
            val user = state.users.find { it.id == userId }
            user?.let {
                state.copy(userClicked = Event(it))
            } ?: kotlin.run { throw IllegalStateException("Selected user is not in the list") }
        }
    }

    private fun performSearch(query: String): Disposable {
        return searchUsers.execute(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                updateState {
                    it.copy(
                        users = emptyList(),
                        searchInProgress = true
                    )
                }
            }
            .doAfterTerminate { disposable = null }
            .subscribe(
                { result ->
                    updateState {
                        it.copy(
                            users = result,
                            searchInProgress = false
                        )
                    }
                }, {
                    updateState {
                        it.copy(
                            searchInProgress = false,
                            searchFailed = Event(Any())
                        )
                    }
                })
    }

    private fun updateState(reducer: (SearchState) -> SearchState) {
        val last = _states.last()
        _states.value = reducer(last)
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}

private data class SearchState(
    val query: String,
    val users: List<User>,
    val searchInProgress: Boolean,
    val searchFailed: Event<Any>?,
    val userClicked: Event<User>?
)

private fun LiveData<SearchState>.toUi(): LiveData<SearchUiModel> {
    return map { state ->
        SearchUiModel(
            users = state.users.map {
                UserUiModel(
                    username = it.username,
                    reputation = it.reputation,
                    userId = it.id
                )
            },
            showSearchInProgress = state.searchInProgress,
            showError = state.searchFailed,
            navigateToUserDetails = state.userClicked
        )
    }
}

private fun LiveData<SearchState>.last(): SearchState {
    return value ?: throw IllegalStateException("There should be at least the initial value")
}
