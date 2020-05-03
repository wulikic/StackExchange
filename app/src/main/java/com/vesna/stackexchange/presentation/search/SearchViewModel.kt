package com.vesna.stackexchange.presentation.search

import androidx.lifecycle.ViewModel
import com.vesna.stackexchange.domain.GetFirst20UsersSortedAlphabetically
import com.vesna.stackexchange.domain.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.lang.IllegalStateException

class SearchViewModel(
    private val searchUsers: GetFirst20UsersSortedAlphabetically
) : ViewModel() {

    private var disposable: Disposable? = null

    private val clearState = SearchState(
        query = "",
        users = emptyList(),
        searchInProgress = false
    )

    private val states = BehaviorSubject.createDefault(clearState)
    private val events = PublishSubject.create<SearchUiEvent>()

    val uiStates: Observable<SearchUiModel> = states.map { toUi(it) }
    val uiEvents: Observable<SearchUiEvent> = events

    fun onSearchClicked() {
        disposable?.dispose()
        val searchQuery = states.last().query
        if (searchQuery.isNotEmpty()) {
            disposable = performSearch(searchQuery)
        }
    }

    fun onSearchTextChanged(text: String) {
        updateState { it.copy(query = text) }
    }

    fun onUserClicked(userId: Int) {
        val user = states.last().users.find { it.id == userId }
        user?.let {
            events.onNext(NavigateToUserDetails(it))
        } ?: kotlin.run { throw IllegalStateException("Selected user is not in the list") }
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
                    updateState { it.copy(searchInProgress = false) }
                    events.onNext(ShowError)
                })
    }

    private fun updateState(reducer: (SearchState) -> SearchState) {
        val last = states.last()
        states.onNext(reducer(last))
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    private fun BehaviorSubject<SearchState>.last(): SearchState {
        return value ?: throw IllegalStateException("There should be at least the initial value")
    }

    private fun toUi(state: SearchState): SearchUiModel {
        return SearchUiModel(
            users = state.users.map {
                UserUiModel(
                    username = it.username,
                    reputation = it.reputation,
                    userId = it.id
                )
            },
            showSearchInProgress = state.searchInProgress
        )
    }
}

private data class SearchState(
    val query: String,
    val users: List<User>,
    val searchInProgress: Boolean
)
