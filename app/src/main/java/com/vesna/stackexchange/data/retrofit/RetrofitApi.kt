package com.vesna.stackexchange.data.retrofit

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

//https://api.stackexchange.com/2.2/users?page=1&pagesize=20&order=asc&sort=name&inname=john&site=stackoverflow
interface RetrofitApi {

    @GET("2.2/users?page={page}&pagesize={pagesize}&order={order}&sort={sort}&inname={inname}&site={site}")
    fun search(
        @Path("page") page: Int,
        @Path("pagesize") pageSize: Int,
        @Path("order") order: String,
        @Path("sort") sort: String,
        @Path("inname") nameQuery: String,
        @Path("site") site: String
    ): Single<UserListResponse>
}