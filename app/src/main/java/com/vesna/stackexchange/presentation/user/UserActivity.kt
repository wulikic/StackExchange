package com.vesna.stackexchange.presentation.user

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.vesna.stackexchange.R
import com.vesna.stackexchange.presentation.ParcelableUser
import kotlinx.android.synthetic.main.activity_user.*
import java.text.SimpleDateFormat
import java.util.*

class UserActivity : AppCompatActivity() {

    private val dateFormatter = SimpleDateFormat.getDateInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user)
        supportActionBar?.apply {
            title = "User"
            setDisplayHomeAsUpEnabled(true)
        }

        val user = intent.extras?.getParcelable<ParcelableUser>("user")!!

        name.text = user.username
        Glide.with(this).load(user.avatar).into(avatar)
        reputation.text = user.reputation.toString()

        goldBadge.apply {
            text = user.goldBadges.toString()
            setBadgeColor(R.color.gold)
        }

        silverBadge.apply {
            text = user.silverBadges.toString()
            setBadgeColor(R.color.silver)
        }

        bronzeBadge.apply {
            text = user.bronzeBadges.toString()
            setBadgeColor(R.color.bronze)
        }

        location.text = user.location
        age.text = user.age.toString()
        created.text = dateFormatter.format(Date(user.creationTime))
    }

    private fun TextView.setBadgeColor(@ColorRes color: Int) {
        compoundDrawablesRelative[0].colorFilter =
            PorterDuffColorFilter(
                ContextCompat.getColor(this.context, color),
                PorterDuff.Mode.SRC_IN
            )
    }
}

