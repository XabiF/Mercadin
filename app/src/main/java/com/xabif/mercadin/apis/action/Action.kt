package com.xabif.mercadin.apis.action

import com.google.gson.Gson
import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.SourceInstance
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Action() : SourceInstance {
    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(ActionApi.Url)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(ActionInterceptor()).build())
        .build()
    private val base_api = this.retrofit.create(ActionApi::class.java)
    private val gson = Gson()

    override suspend fun queryProducts(query: String): List<ProductInfo> {
        val variables = GraphQLVariables(
            input = GraphQLVariablesInput(
                searchTerm = query
            )
        )
        val extensions = GraphQLExtensions(
            persistedQuery = GraphQLExtensionsPersistedQuery(
                sha256Hash = "fcd76d298b620d55a174c22401674d3c735c798d4d0fa4165c92a8dbaf5a9548",
                version = 1
            ),
            headers = GraphQLExtensionsHeaders(
                acceptLanguage = "es-ES"
            )
        )
        val res = this.base_api.graphQL("SearchProductsWithSuggestions", gson.toJson(variables), gson.toJson(extensions))
        if(res.isSuccessful) {
            return res.body()!!.data.searchWithSuggestions.productResults.searchProducts.map { searchProduct -> searchProduct.product.toProductInfo() }
        }
        else {
            throw RuntimeException("Action API queryProducts failed with code=${res.code()}: ${res.errorBody()!!.string()}")
        }
    }

    override suspend fun queryProductById(id: String): ProductInfo {
        throw RuntimeException("Action API queryProductById UNIMPLEMENTED")
    }
}
