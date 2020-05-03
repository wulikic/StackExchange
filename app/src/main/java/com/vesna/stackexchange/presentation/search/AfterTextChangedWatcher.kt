package com.vesna.stackexchange.presentation.search

import android.text.Editable
import android.text.TextWatcher

abstract class AfterTextChangedWatcher : TextWatcher {

    abstract fun textChanged(text: String)

    override fun afterTextChanged(s: Editable?) {
        textChanged(s?.toString().orEmpty())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}