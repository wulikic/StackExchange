package com.vesna.stackexchange.presentation.search

import androidx.recyclerview.widget.DiffUtil

class UserUiModelDiff : DiffUtil.ItemCallback<UserUiModel>() {

    override fun areItemsTheSame(oldItem: UserUiModel, newItem: UserUiModel): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: UserUiModel, newItem: UserUiModel): Boolean {
        return oldItem == newItem
    }
}