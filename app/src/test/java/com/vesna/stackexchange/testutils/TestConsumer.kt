package com.vesna.stackexchange.testutils

import io.reactivex.functions.Predicate
import io.reactivex.observers.BaseTestConsumer

fun <T, U: BaseTestConsumer<T, U>> BaseTestConsumer<T, U>.assertLastValue(predicate: Predicate<T>): U {
    return assertValueAt(valueCount() - 1, predicate)
}

fun <T, U: BaseTestConsumer<T, U>> BaseTestConsumer<T, U>.assertLastValue(value: T): U {
    return assertValueAt(valueCount() - 1, value)
}