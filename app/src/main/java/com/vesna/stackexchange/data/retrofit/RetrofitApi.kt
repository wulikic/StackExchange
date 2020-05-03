package com.vesna.stackexchange.data.retrofit

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET("2.2/users")
    fun search(
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int,
        @Query("order") order: String,
        @Query("sort") sort: String,
        @Query("inname") nameQuery: String,
        @Query("site") site: String
    ): Single<UserListResponse>
}