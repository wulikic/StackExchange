package com.vesna.stackexchange.data

import com.vesna.stackexchange.data.retrofit.RetrofitApi
import com.vesna.stackexchange.domain.Badges
import com.vesna.stackexchange.domain.GetFirst20UsersSortedAlphabetically
import com.vesna.stackexchange.domain.User
import io.reactivex.Single
import java.util.*

class GetFirst20UsersSortedAlphabeticallyImpl(private val api: RetrofitApi) :
    GetFirst20UsersSortedAlphabetically {

    override fun execute(nameQuery: String): Single<List<User>> {
        return api.search(
            page = 1,
            pageSize = 20,
            order = "asc",
            sort = "name",
            nameQuery = nameQuery,
            site = "stackoverflow"
        ).map { response ->
            response.items.map {
                User(
                    id = it.userId,
                    username = it.displayName,
                    reputation = it.reputation,
                    avatar = it.profilePicture,
                    badges = Badges(
                        bronzeCount = it.badges.bronze,
                        silverCount = it.badges.silver,
                        goldCount = it.badges.gold
                    ),
                    creationDate = Date(it.creationDate * 1000),
                    location = it.location,
                    age = it.age
                )
            }
        }
    }
}