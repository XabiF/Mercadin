package com.xabif.mercadin.apis.dia

import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.ProductSource

data class Item(
    val display_name: String,
    val image: String,
    val prices: ItemPrices,
    val id: String,
    val url: String
) {
    fun toProductInfo() : ProductInfo {
        val price_val: Float;
        val ref_price: Float;
        val sale_price_val: Float?;
        val sale_ref_price: Float?;
        if(this.prices.price != this.prices.strikethrough_price) {
            sale_price_val = this.prices.price;
            sale_ref_price = this.prices.price_per_unit;
            price_val = this.prices.strikethrough_price;
            ref_price = sale_ref_price * (price_val / sale_price_val);
        }
        else {
            sale_price_val = null;
            sale_ref_price = null;
            price_val = this.prices.price;
            ref_price = this.prices.price_per_unit;
        }

        try {
            return ProductInfo(ProductSource.Dia, id, display_name, price_val, sale_price_val, prices.measure_unit, ref_price, sale_ref_price, "https://www.dia.es/$image", url);
        }
        catch (e: Exception) {
            throw RuntimeException("Exception parsing Dia product display_name=$display_name:\n${e.message}");
        }
    }
}