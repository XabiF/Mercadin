package com.xabif.mercadin.apis.action

import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.ProductSource

data class Product(
    val id: String,
    val href: String,
    val title: String,
    val image: String,
    val price: ProductPrice,
) {
    fun toProductInfo() : ProductInfo {
        try {
            return ProductInfo(ProductSource.Action, this.id, this.title, this.price.current.amount, null, "unidad", this.price.current.amount, null, this.image, this.href)
        }
        catch (e: Exception) {
            throw RuntimeException("Exception parsing Action product ID=${this.id}, display_name=${this.title}:\n${e.stackTraceToString()}")
        }
    }
}
