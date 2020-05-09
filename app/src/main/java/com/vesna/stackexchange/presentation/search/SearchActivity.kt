package com.vesna.stackexchange.presentation.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vesna.stackexchange.R
import com.vesna.stackexchange.presentation.App
import com.vesna.stackexchange.presentation.add
import com.vesna.stackexchange.presentation.toParcelable
import com.vesna.stackexchange.presentation.user.UserActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.view_holder.view.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var providerFactory: SearchProviderFactory
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: ListAdapter<UserUiModel, ViewHolder>

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)

        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this, providerFactory)[SearchViewModel::class.java]

        adapter = recyclerViewAdapter(recyclerView)
        button.setOnClickListener {
            viewModel.onSearchClicked()
        }
        searchView.addAfterTextChangedListener { viewModel.onSearchTextChanged(it) }
    }

    override fun onStart() {
        super.onStart()

        viewModel.uiStates
            .subscribe {
                adapter.submitList(it.users)
                if (it.showSearchInProgress) progressView.show() else progressView.hide()
            }
            .add(disposables)

        viewModel.uiEvents
            .subscribe { event ->
                when (event) {
                    ShowError -> Toast.makeText(
                        applicationContext,
                        R.string.search_error,
                        Toast.LENGTH_SHORT
                    ).show()
                    is NavigateToUserDetails -> startActivity(
                        Intent(
                            this,
                            UserActivity::class.java
                        ).apply {
                            putExtra(UserActivity.INTENT_DATA, event.user.toParcelable())
                        })
                }
            }
            .add(disposables)
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    private fun recyclerViewAdapter(recyclerView: RecyclerView): ListAdapter<UserUiModel, ViewHolder> {
        adapter = object : ListAdapter<UserUiModel, ViewHolder>(UserUiModelDiff()) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder, parent, false)
                return ViewHolder(view)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val item = getItem(position)
                holder.itemView.reputation.text = item.reputation.toString()
                holder.itemView.name.text = item.username
                holder.itemView.setOnClickListener { viewModel.onUserClicked(item.userId) }
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
        return adapter
    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}