package com.xabif.mercadin.apis.carrefour

import com.xabif.mercadin.apis.mercadona.MercadonaAlgoliaApi
import com.xabif.mercadin.apis.mercadona.MercadonaApi
import com.xabif.mercadin.apis.mercadona.Product
import com.xabif.mercadin.apis.mercadona.QueryRequest
import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.Source
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Carrefour() : Source {
    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(CarrefourSearchApi.Companion.Url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    private val api: CarrefourSearchApi = this.retrofit.create(CarrefourSearchApi::class.java);

    override suspend fun queryProducts(query: String): List<ProductInfo> {
        val res = api.searchProducts(true, "x-carrefour", "https://www.carrefour.es", "desktop", "es", "empathy", "food", "https://www.carrefour.es", true, "005290", "32EEFGvrFH9I3ZyxFITpQwpUbt0", false, "wFOzqveg", "3,5,11,13,19", "7-8,15-16", "2,4,11,13,19", "6,12", "2,4,11,13,19", "6,12", "22", false, "food", query, 1);
        if(res.isSuccessful) {

            return res.body()!!.content.docs.map { doc: SearchDoc -> doc.toProductInfo() };
        }
        else {
            throw RuntimeException("Carrefour API queryProducts failed with code=${res.code()}: ${res.errorBody()!!.string()}");
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

        throw RuntimeException("Unable to find Carrefour product with id=$id...");
    }
}