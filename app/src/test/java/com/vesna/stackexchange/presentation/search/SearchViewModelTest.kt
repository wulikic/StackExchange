package com.vesna.stackexchange.presentation.search

import com.vesna.stackexchange.domain.Badges
import com.vesna.stackexchange.domain.GetFirst20UsersSortedAlphabetically
import com.vesna.stackexchange.domain.User
import com.vesna.stackexchange.testutils.RxJavaTestRule
import com.vesna.stackexchange.testutils.assertLastValue
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.functions.Predicate
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class SearchViewModelTest {

    @Rule
    @JvmField
    val rxRule = RxJavaTestRule()

    @MockK
    lateinit var searchUsersMock: GetFirst20UsersSortedAlphabetically

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { searchUsersMock.execute(any()) } returns Single.never()
    }

    @Test
    fun `when text is entered and search button clicked then should execute GetFirst20UsersSortedAlphabetically`() {
        val sut = SearchViewModel(searchUsersMock)
        sut.onSearchTextChanged("fake query")
        sut.onSearchClicked()
        verify(exactly = 1) { searchUsersMock.execute("fake query") }
    }

    @Test
    fun `when GetFirst20UsersSortedAlphabetically fails then search should not be in progress`() {
        every { searchUsersMock.execute(any()) } returns Single.error(Throwable())
        val sut = SearchViewModel(searchUsersMock)
        sut.onSearchTextChanged("fake query")
        sut.onSearchClicked()
        sut.uiStates.test().assertLastValue(Predicate { !it.showSearchInProgress })
    }

    @Test
    fun `when GetFirst20UsersSortedAlphabetically succeeds then search should not be in progress`() {
        every { searchUsersMock.execute(any()) } returns Single.just(emptyList())
        val sut = SearchViewModel(searchUsersMock)
        sut.onSearchTextChanged("fake query")
        sut.onSearchClicked()
        sut.uiStates.test().assertLastValue(Predicate { !it.showSearchInProgress })
    }

    @Test
    fun `when text is entered and search button not clicked then should not execute GetFirst20UsersSortedAlphabetically`() {
        val sut = SearchViewModel(searchUsersMock)
        sut.onSearchTextChanged("fake query")
        verify(exactly = 0) { searchUsersMock.execute(any()) }
    }

    @Test
    fun `when text is not entered and search button is clicked then should not execute GetFirst20UsersSortedAlphabetically`() {
        val sut = SearchViewModel(searchUsersMock)
        sut.onSearchClicked()
        verify(exactly = 0) { searchUsersMock.execute(any()) }
    }

    @Test
    fun `when text is empty and search button is clicked then should not execute GetFirst20UsersSortedAlphabetically`() {
        val sut = SearchViewModel(searchUsersMock)
        sut.onSearchTextChanged("")
        sut.onSearchClicked()
        verify(exactly = 0) { searchUsersMock.execute(any()) }
    }

    @Test
    fun `when text is entered and search button clicked then search should be in progress`() {
        val sut = SearchViewModel(searchUsersMock)
        val testObserver = sut.uiStates.test()
        sut.onSearchTextChanged("fake query")
        sut.onSearchClicked()
        testObserver.assertLastValue(Predicate { it.showSearchInProgress })
    }

    @Test
    fun `when text is not entered and search button clicked then search should not be in progress`() {
        val sut = SearchViewModel(searchUsersMock)
        val testObserver = sut.uiStates.test()
        sut.onSearchClicked()
        testObserver.assertLastValue(Predicate { !it.showSearchInProgress })
    }

    @Test
    fun `when user from the list is clicked then should navigate to user details`() {
        every { searchUsersMock.execute("fake query") } returns Single.just(
            listOf(
                User(
                    id = 123,
                    username = "fakeUsername",
                    reputation = 10,
                    location = "Fake location",
                    avatar = "fake avatar path",
                    badges = Badges(bronzeCount = 12, silverCount = 3, goldCount = 1),
                    creationDate = Date(123456789),
                    age = 56
                )
            )
        )
        val sut = SearchViewModel(searchUsersMock)
        val testObserver = sut.uiEvents.test()
        sut.onSearchTextChanged("fake query")
        sut.onSearchClicked()
        sut.onUserClicked(123)

        testObserver.assertLastValue(Predicate {
            it is NavigateToUserDetails
                    && it.user.id == 123
                    && it.user.username == "fakeUsername"
                    && it.user.avatar == "fake avatar path"
                    && it.user.reputation == 10
                    && it.user.location == "Fake location"
                    && it.user.badges.bronzeCount == 12
                    && it.user.badges.silverCount == 3
                    && it.user.badges.goldCount == 1
                    && it.user.creationDate == Date(123456789)
                    && it.user.age == 56
        })
    }

}