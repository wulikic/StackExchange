package com.vesna.stackexchange.domain

import io.reactivex.Single

interface GetFirst20UsersSortedAlphabetically {

    fun execute(nameQuery: String): Single<List<User>>
}