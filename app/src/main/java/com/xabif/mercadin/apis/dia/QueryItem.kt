package com.xabif.mercadin.apis.dia

import android.util.Log

data class QueryItem(
    val breadcrumb: List<Breadcrumb>,
    val primary_info: ItemPrimaryInfo,
    val prices: ItemPrices,
    val images: List<String>,
    val sku_id: String
) {
    fun toItem() : Item {
        // Trick: compute URL based on largest (most-detailed) breadcrumb
        var cur_link = ""
        for(bc in this.breadcrumb) {
            if(bc.link.length > cur_link.length) {
                cur_link = bc.link
            }
        }
        val tokens = cur_link.split("/c/")
        assert(tokens.size == 2)
        val url = "https://www.dia.es/${tokens.first()}/p/$sku_id"

        return Item(primary_info.title, images.first(), prices, sku_id, url)
    }
}
