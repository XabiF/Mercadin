package com.xabif.mercadin.apis.carrefour

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CarrefourSearchApi {
    companion object {
        const val Url = "https://www.carrefour.es/search-api/query/v1/"
    }

    @GET("search/")
    suspend fun searchProducts(
        @Query("internal") internal: Boolean,
        @Query("instance") instance: String,
        @Query("env") env: String,
        @Query("scope") scope: String,
        @Query("lang") lang: String,
        @Query("session") session: String,
        @Query("citrusCatalog") citrusCatalog: String,
        @Query("baseUrlCitrus") baseUrlCitrus: String,
        @Query("enabled") enabled: Boolean,
        @Query("store") store: String,
        @Query("shopperId") shopperId: String,
        @Query("hasConsent") hasConsent: Boolean,
        @Query("siteKey") siteKey: String,
        @Query("grid_def_search_sponsor_product") grid_def_search_sponsor_product: String,
        @Query("grid_def_search_butterfly_banner") grid_def_search_butterfly_banner: String,
        @Query("grid_def_search_sponsor_product_tablet") grid_def_search_sponsor_product_tablet: String,
        @Query("grid_def_search_butterfly_banner_tablet") grid_def_search_butterfly_banner_tablet: String,
        @Query("grid_def_search_sponsor_product_mobile") grid_def_search_sponsor_product_mobile: String,
        @Query("grid_def_search_butterfly_banner_mobile") grid_def_search_butterfly_banner_mobile: String,
        @Query("grid_def_search_luckycart_banner") grid_def_search_luckycart_banner: String,
        @Query("empathypoc") empathypoc: Boolean,
        @Query("catalog") catalog: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<SearchResult>
}
