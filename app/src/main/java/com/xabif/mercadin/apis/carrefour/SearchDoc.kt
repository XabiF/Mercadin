package com.xabif.mercadin.apis.carrefour

import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.ProductSource

data class SearchDoc(
    val active_price: Float,
    val display_name: String,
    val image_path: String,
    val measure_unit: String,
    val ean13: String,
    val url: String,
    val price_per_unit_text: String
) {
    fun toProductInfo() : ProductInfo {
        val price_val: Float
        val ref_price: Float
        val sale_price_val: Float?
        val sale_ref_price: Float?

        sale_price_val = null
        sale_ref_price = null
        price_val = this.active_price
        ref_price = this.price_per_unit_text.split(" ")[0].replace(',', '.').toFloat()

        val url = "https://carrefour.es${this.url}"

        try {
            return ProductInfo(ProductSource.Carrefour, this.ean13, this.display_name, price_val, sale_price_val, this.measure_unit, ref_price, sale_ref_price, this.image_path, url)
        }
        catch (e: Exception) {
            throw RuntimeException("Exception parsing Carrefour product ID=$ean13, display_name=$display_name:\n${e.message}")
        }
    }
}
