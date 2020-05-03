package com.vesna.stackexchange.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vesna.stackexchange.R
import com.vesna.stackexchange.presentation.App
import com.vesna.stackexchange.presentation.add
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
        searchView.addTextChangedListener(object : AfterTextChangedWatcher() {
            override fun textChanged(text: String) {
                viewModel.onSearchTextChanged(text)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.uiStates
            .subscribe {
                adapter.submitList(it.users)
                if (it.showSearchInProgress) progressView.show() else progressView.hide()
            }
            .add(disposables)
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    private fun recyclerViewAdapter(recyclerView: RecyclerView): ListAdapter<UserUiModel, ViewHolder> {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
        recyclerView.adapter = adapter
        return adapter
    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}