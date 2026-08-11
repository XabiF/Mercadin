package com.xabif.mercadin.apis.corteingles

import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.SourceInstance
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CorteIngles : SourceInstance {
    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(CorteInglesApi.Companion.Url)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(CorteInglesInterceptor()).build())
        .build()
    private val api: CorteInglesApi = this.retrofit.create(CorteInglesApi::class.java)

    override suspend fun queryProducts(query: String): List<ProductInfo> {
        val res = api.queryProducts(query, "supermercado", "text_box")
        if(res.isSuccessful) {
            val products: MutableList<ProductInfo> = mutableListOf()
            for (product in res.body()!!.products) {
                if(product.isValid()) {
                    products.add(product.toProductInfo())
                }
            }
            return products
        }
        else {
            throw RuntimeException("Corte Inglés API queryProducts failed with code=${res.code()}: ${res.errorBody()!!.string()}")
        }
    }

    override suspend fun queryProductById(id: String): ProductInfo {
        // TODO: does this API have a query-by-id endpoint?

        val products = this.queryProducts(id)
        for(product in products) {
            if(product.id == id) {
                return product
            }
        }

        throw RuntimeException("Unable to find Corte Inglés product with id=$id...")
    }
}