package com.vesna.stackexchange.presentation.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vesna.stackexchange.R
import com.vesna.stackexchange.presentation.App
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.view_holder.*
import kotlinx.android.synthetic.main.view_holder.view.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var providerFactory: SearchProviderFactory
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: ListAdapter<UserUiModel, ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)

        setContentView(R.layout.activity_search)

        adapter = recyclerViewAdapter(recyclerView)
        button.setOnClickListener {
            viewModel.onSearchClicked()
        }
        searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.toString().orEmpty().let {
                    viewModel.onSearchTextChanged(it)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        viewModel = ViewModelProvider(this, providerFactory)[SearchViewModel::class.java]

        viewModel.uiStates.subscribe {
            adapter.submitList(it.users)
        }


    }

    private fun recyclerViewAdapter(recyclerView: RecyclerView): ListAdapter<UserUiModel, ViewHolder> {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = object : ListAdapter<UserUiModel, ViewHolder>(Diff()) {

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

    private class Diff : DiffUtil.ItemCallback<UserUiModel>() {

        override fun areItemsTheSame(oldItem: UserUiModel, newItem: UserUiModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserUiModel, newItem: UserUiModel): Boolean {
            return oldItem == newItem
        }
    }
}