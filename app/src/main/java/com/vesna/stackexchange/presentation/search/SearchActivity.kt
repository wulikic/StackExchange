package com.vesna.stackexchange.presentation.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vesna.stackexchange.presentation.App
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var providerFactory: SearchProviderFactory
    lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)

        viewModel = ViewModelProvider(this, providerFactory)[SearchViewModel::class.java]

    }
}