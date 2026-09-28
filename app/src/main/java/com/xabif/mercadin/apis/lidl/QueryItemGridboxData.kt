package com.xabif.mercadin.apis.lidl

import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.ProductSource

data class QueryItemGridboxData(
    val canonicalUrl: String,
    val fullTitle: String,
    val image: String,
    val erpNumber: String,
    val price: QueryItemGridboxDataPrice,
) {
    fun toProductInfo() : ProductInfo {
        var priceVal = this.price.price
        var salePriceVal: Float? = null
        if(this.price.oldPrice != null) {
            priceVal = this.price.oldPrice
            salePriceVal = this.price.price
        }

        val unit = "unidad" // TODO!

        val url = "https://www.lidl.es${this.canonicalUrl}"

        try {
            return ProductInfo(ProductSource.Lidl, this.erpNumber, this.fullTitle, priceVal, salePriceVal, unit, priceVal, salePriceVal, this.image, url)
        }
        catch (e: Exception) {
            throw RuntimeException("Exception parsing LIDL product ID=${this.erpNumber}, name=${this.fullTitle}:\n${e.stackTraceToString()}")
        }
    }
}
