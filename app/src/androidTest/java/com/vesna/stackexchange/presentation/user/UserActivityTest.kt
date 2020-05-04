package com.vesna.stackexchange.presentation.user

import android.content.Context
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserActivityTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        init()
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @After
    fun tearDown() {
        release()
    }

    @Test
    fun should_show_username_sent_through_intent() {
        with(UserActivityRobot) {
            launch_activity_with_passed_username("Username007")
            username_should_be("Username007")
        }
    }

    @Test
    fun should_show_age_sent_through_intent() {
        with(UserActivityRobot) {
            launch_activity_with_passed_age(67)
            age_should_be("67")
        }
    }
}
