package com.vesna.stackexchange.testutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class TestObserver<T> : Observer<T> {

    var value: T? = null
    private set

    override fun onChanged(t: T?) {
        value = t
    }
}

fun <T> LiveData<T>.testObserver(): TestObserver<T> {
    return TestObserver<T>().also { observeForever(it) }
}