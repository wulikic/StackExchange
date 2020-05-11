package com.vesna.stackexchange.presentation.search

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vesna.stackexchange.testutils.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

    private lateinit var context: Context

    @Rule
    @JvmField
    val mockServerRule = MockServerTestRule()

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun screen_shows_list_of_names_and_reputations() {
        with(SearchActivityRobot()) {
            @GIVEN I_open_search_screen()
            @WHEN I_enter_search_query("comm")
            mockServerRule.enqueueResponse(content("response.json"), 200)
            @AND I_click_search_button()
            @THEN I_should_see_a_list()
            @AND I_should_see_name_and_reputation_at_the_position(0, "1commongoal", "1")
            @AND I_should_see_name_and_reputation_at_the_position(19, "common", "26")
        }
    }

    private fun content(path: String): String {
        return context.assets.fileAsString(path)
    }
}