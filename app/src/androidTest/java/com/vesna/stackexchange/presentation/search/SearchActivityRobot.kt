package com.vesna.stackexchange.presentation.search

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.vesna.stackexchange.R
import com.vesna.stackexchange.testutils.RecyclerViewMatcher
import org.junit.Rule

class SearchActivityRobot {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(SearchActivity::class.java, true, false)

    fun I_open_search_screen() {
        activityRule.launchActivity(Intent())
    }

    fun I_enter_search_query(query: String) {
        onView(withId(R.id.searchView)).perform(typeText(query))
    }

    fun I_click_search_button() {
        onView(withId(R.id.button)).perform(click())
    }

    fun I_should_see_a_list() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }

    fun I_should_see_name_and_reputation_at_the_position(
        position: Int,
        name: String,
        reputation: String
    ) {
        val scrollToPosition =
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position)
        onView(withId(R.id.recyclerView)).perform(scrollToPosition)
        onView(RecyclerViewMatcher(R.id.recyclerView).atPositionOnView(position, R.id.name))
            .check(matches(withText(name)))
        onView(RecyclerViewMatcher(R.id.recyclerView).atPositionOnView(position, R.id.reputation))
            .check(matches(withText(reputation)))
    }
}