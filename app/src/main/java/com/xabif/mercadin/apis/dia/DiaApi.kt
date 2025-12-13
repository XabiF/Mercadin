package com.xabif.mercadin.apis.dia

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DiaApi {
    companion object {
        const val Url = "https://www.dia.es/api/v1/";
    }

    @GET("pdp-back/reduced/{id}/")
    suspend fun queryItem(
        @Path("id") id: Int
    ): Response<QueryResult>

    @GET("search-back/search/reduced/")
    suspend fun searchItems(
        @Query("q") query: String,
        @Query("page") page: Int,
    ): Response<SearchResult>
}
