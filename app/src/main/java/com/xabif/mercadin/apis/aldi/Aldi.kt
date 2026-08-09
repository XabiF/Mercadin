package com.xabif.mercadin.apis.aldi

import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.SourceInstance
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Aldi : SourceInstance {
    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(AldiSearchApi.Companion.Url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    private val api: AldiSearchApi = this.retrofit.create(AldiSearchApi::class.java);

    override suspend fun queryProducts(query: String): List<ProductInfo> {
        val res = api.queryProducts(AldiSearchApi.Agent, AldiSearchApi.ApplicationId, AldiSearchApi.ApiKey, QueryRequest(query))
        if(res.isSuccessful) {
            val products: MutableList<ProductInfo> = mutableListOf();
            for (product in res.body()!!.hits) {
                if(product.isValid()) {
                    products.add(product.toProductInfo());
                }
            }
            return products;
        }
        else {
            throw RuntimeException("ALDI API queryProducts failed with code=${res.code()}: ${res.errorBody()!!.string()}");
        }
    }

    override suspend fun queryProductById(id: String): ProductInfo {
        // Ugly trick due to only having a search API, but works fine (surprisingly, searching by the EAN13 does find the product!)

        val products = this.queryProducts(id);
        for(product in products) {
            if(product.id == id) {
                return product;
            }
        }

        throw RuntimeException("Unable to find ALDI product with id=$id...");
    }
}