package com.vesna.stackexchange.presentation

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.add(disposables: CompositeDisposable) {
    disposables.add(this)
}
