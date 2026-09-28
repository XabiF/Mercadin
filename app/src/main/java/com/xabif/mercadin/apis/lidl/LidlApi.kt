package com.xabif.mercadin.apis.lidl

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LidlApi {
    companion object {
        const val Url = "https://www.lidl.es/"
    }

    @GET("/q/api/search")
    suspend fun queryProducts(
        @Query("assortment") assortment: String,
        @Query("locale") locale: String,
        @Query("version") version: String,
        @Query("q") q: String,
    ): Response<QueryResult>

    @GET("/p/api/gridboxes/ES/es")
    suspend fun queryProduct(
        @Query("erpNumbers") erpNumbers: String,
    ): Response<QueryItemGridboxData>
}
