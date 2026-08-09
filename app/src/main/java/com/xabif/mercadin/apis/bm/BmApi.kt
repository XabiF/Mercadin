package com.xabif.mercadin.apis.bm

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BmApi {
    companion object {
        const val Url = "https://www.online.bmsupermercados.es/api/rest/V1.0/catalog/"
    }

    @GET("product/code/{id}/")
    suspend fun queryProduct(
        @Path("id") id: Int
    ): Response<Product>

    @GET("product/")
    suspend fun queryProductsByCategories(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("orderById") order_by_id: Int,
        @Query("showRecommendations") show_recommendations: Boolean,
        @Query("categories") categories: Int
    ): Response<QueryResult>

    @GET("product/")
    suspend fun queryProducts(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("orderById") order_by_id: Int,
        @Query("showRecommendations") show_recommendations: Boolean,
        @Query("q") query: String
    ): Response<QueryResult>
}
