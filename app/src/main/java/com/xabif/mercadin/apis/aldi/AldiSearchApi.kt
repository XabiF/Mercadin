package com.xabif.mercadin.apis.aldi

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AldiSearchApi {
    companion object {
        const val Url = "https://l9knu74io7-dsn.algolia.net/1/"

        const val Agent = "Algolia for JavaScript (4.26.0); Browser"
        const val ApplicationId = "L9KNU74IO7"
        const val ApiKey = "83df5acd172c42ab174afa4583232b5d"
    }

    @POST("indexes/an_prd_es_es_pen_products2/query/")
    suspend fun queryProducts(
        @Query("x-algolia-agent") agent: String,
        @Query("x-algolia-application-id") app_id: String,
        @Query("x-algolia-api-key") api_key: String,
        @Body request: QueryRequest
    ): Response<QueryResult>
}
