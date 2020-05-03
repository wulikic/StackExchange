package com.vesna.stackexchange.presentation.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vesna.stackexchange.domain.Badges
import com.vesna.stackexchange.domain.GetFirst20UsersSortedAlphabetically
import com.vesna.stackexchange.domain.User
import com.vesna.stackexchange.testutils.RxJavaTestRule
import com.vesna.stackexchange.testutils.testObserver
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class SearchViewModelTest {

    @Rule @JvmField
    val rxRule = RxJavaTestRule()

    @Rule @JvmField
    val liveDataRule = InstantTaskExecutorRule()

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
    fun `when text is entered and search button clicked then should show emit search in progress`() {
        val sut = SearchViewModel(searchUsersMock)
        val testObserver = sut.states.testObserver()
        sut.onSearchTextChanged("fake query")
        sut.onSearchClicked()
        assertTrue(testObserver.value!!.showSearchInProgress)
    }

    @Test
    fun `when text is not entered and search button clicked then should not emit show search in progress`() {
        val sut = SearchViewModel(searchUsersMock)
        val testObserver = sut.states.testObserver()
        sut.onSearchClicked()
        assertFalse(testObserver.value!!.showSearchInProgress)
    }

    @Test
    fun `when user from the list is clicked then should emit navigate to user details`() {
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
        )))
        val sut = SearchViewModel(searchUsersMock)
        val testObserver = sut.states.testObserver()
        sut.onSearchTextChanged("fake query")
        sut.onSearchClicked()
        sut.onUserClicked(123)

        val actual = testObserver.value!!.navigateToUserDetails
        assertNotNull(actual)
        val actualUser = actual!!.peek()
        assertEquals(123, actualUser.id)
        assertEquals("fakeUsername", actualUser.username)
        assertEquals(10, actualUser.reputation)
        assertEquals("Fake location", actualUser.location)
        assertEquals("fake avatar path", actualUser.avatar)
        assertEquals(12, actualUser.badges.bronzeCount)
        assertEquals(3, actualUser.badges.silverCount)
        assertEquals(1, actualUser.badges.goldCount)
        assertEquals(Date(123456789), actualUser.creationDate)
        assertEquals(56, actualUser.age)
    }

}