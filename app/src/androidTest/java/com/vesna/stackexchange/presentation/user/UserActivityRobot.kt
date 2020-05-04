package com.vesna.stackexchange.presentation.user

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.vesna.stackexchange.R
import com.vesna.stackexchange.presentation.ParcelableUser
import org.junit.Rule

object UserActivityRobot {

    @Rule
    @JvmField
    val rule = ActivityTestRule(UserActivity::class.java, true, false)

    fun launch_activity_with_passed_username(username: String) {
        rule.launchActivity(Intent().apply {
            putExtra("user", parcelableUser.copy(username = username))
        })
    }

    fun username_should_be(username: String) {
        onView(withId(R.id.name)).check(matches(withText(username)))
    }

    fun launch_activity_with_passed_age(age: Int?) {
        rule.launchActivity(Intent().apply {
            putExtra("user", parcelableUser.copy(age = age))
        })
    }

    fun age_should_be(age: String) {
        onView(withId(R.id.age)).check(matches(withText(age)))
    }

    private val parcelableUser = ParcelableUser(
        id = 0,
        username = "",
        reputation = 0,
        avatar = "",
        creationTime = 0,
        bronzeBadges = 0,
        silverBadges = 0,
        goldBadges = 0,
        location = null,
        age = null
    )
}