package com.xabif.mercadin.apis.dia

import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.ProductSource

data class SearchItem(
    val display_name: String,
    val image: String,
    val prices: ItemPrices,
    val sku_id: String,
    val url: String
) {
    fun toItem() : Item {
        return Item(display_name, image, prices, sku_id, "https://www.dia.es/$url");
    }
}
