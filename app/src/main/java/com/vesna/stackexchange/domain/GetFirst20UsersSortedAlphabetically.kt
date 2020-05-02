package com.vesna.stackexchange.domain

import io.reactivex.Single

interface GetFirst20UsersSortedAlphabetically {

    fun execute(nameMatcher: String): Single<List<User>>
}