package com.xabif.mercadin.apis.bm

import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.ProductSource

data class Product(
    val code: String,
    val productData: ProductData,
    val priceData: ProductPriceData
) {
    fun toProductInfo() : ProductInfo {
        var price_val = 0.0f
        var ref_price = 0.0f
        var sale_price_val: Float? = null
        var sale_ref_price: Float? = null
        for(price in this.priceData.prices) {
            if(price.id == "PRICE") {
                price_val = price.value.centAmount
                ref_price = price.value.centUnitAmount
            }
            else if(price.id == "OFFER_PRICE") {
                sale_price_val = price.value.centAmount
                sale_ref_price = price.value.centUnitAmount
            }
        }

        var name = this.productData.name
        if(this.productData.brand.name.isNotBlank()) {
            name += " (" + this.productData.brand.name + ")"
        }

        val url = "https://www.online.bmsupermercados.es/es/p/${this.code}"

        try {
            return ProductInfo(ProductSource.Bm, this.code, name, price_val, sale_price_val, this.priceData.unitPriceUnitType, ref_price, sale_ref_price, this.productData.imageURL, url)
        }
        catch (e: Exception) {
            throw RuntimeException("Exception parsing BM product ID=$code, name=$name:\n${e.message}")
        }
    }
}
