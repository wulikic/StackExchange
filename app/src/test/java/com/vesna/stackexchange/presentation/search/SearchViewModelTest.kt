package com.vesna.stackexchange.presentation.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vesna.stackexchange.domain.GetFirst20UsersSortedAlphabetically
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
    fun `when text is entered and search button clicked then should show search in progress`() {
        val sut = SearchViewModel(searchUsersMock)
        val testObserver = sut.states.testObserver()
        sut.onSearchTextChanged("fake query")
        sut.onSearchClicked()
        assertTrue(testObserver.value!!.showSearchInProgress)
    }

    @Test
    fun `when text is not entered and search button clicked then should not show search in progress`() {
        val sut = SearchViewModel(searchUsersMock)
        val testObserver = sut.states.testObserver()
        sut.onSearchClicked()
        assertFalse(testObserver.value!!.showSearchInProgress)
    }

}