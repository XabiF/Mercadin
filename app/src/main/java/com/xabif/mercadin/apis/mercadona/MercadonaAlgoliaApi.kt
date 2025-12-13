package com.xabif.mercadin.apis.mercadona

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MercadonaAlgoliaApi {
    companion object {
        const val Url = "https://7uzjkl1dj0-dsn.algolia.net/1/";

        const val Agent = "Algolia for JavaScript (3.35.1); Browser";
        const val ApplicationId = "7UZJKL1DJ0";
        const val ApiKey = "9d8f2e39e90df472b4f2e559a116fe17";
    }

    @POST("indexes/products_prod_{wh}_{lang}/query/")
    suspend fun queryProducts(
        @Path("lang") lang: String,
        @Path("wh") warehouse: Int,
        @Query("x-algolia-agent") agent: String,
        @Query("x-algolia-application-id") app_id: String,
        @Query("x-algolia-api-key") api_key: String,
        @Body request: QueryRequest
    ): Response<QueryResult>
}
