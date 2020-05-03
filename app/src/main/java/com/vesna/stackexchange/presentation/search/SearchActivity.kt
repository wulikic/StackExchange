package com.vesna.stackexchange.presentation.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vesna.stackexchange.R
import com.vesna.stackexchange.presentation.App
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var providerFactory: SearchProviderFactory
    lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)

        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this, providerFactory)[SearchViewModel::class.java]

        button.setOnClickListener {
            viewModel.onSearchClicked()
        }


    }
}