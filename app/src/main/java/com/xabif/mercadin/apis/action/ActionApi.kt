package com.xabif.mercadin.apis.action

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ActionApi {
    companion object {
        const val Url = "https://www.action.com/api/"
    }

    @GET("graphql/")
    suspend fun graphQL(
        @Query("operationName") operationName: String,
        @Query("variables") variablesJson: String,
        @Query("extensions") extensionsJson: String,
    ): Response<QueryResponse>
}
