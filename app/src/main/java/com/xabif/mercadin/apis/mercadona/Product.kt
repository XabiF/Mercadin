package com.xabif.mercadin.apis.mercadona

import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.ProductSource

data class Product(
    val id: String,
    val ean: String,
    val display_name: String,
    var thumbnail: String,
    val photos: List<ProductPhoto>,
    val price_instructions: ProductPriceInstructions
) {
    fun toProductInfo() : ProductInfo {
        val price_val: Float;
        val ref_price: Float;
        val sale_price_val: Float?;
        val sale_ref_price: Float?;
        if(this.price_instructions.previous_unit_price != null) {
            sale_price_val = this.price_instructions.unit_price.toFloat();
            sale_ref_price = this.price_instructions.reference_price.toFloat();
            price_val = this.price_instructions.previous_unit_price.toFloat();
            ref_price = sale_ref_price * (price_val / sale_price_val);
        }
        else {
            sale_price_val = null;
            sale_ref_price = null;
            price_val = this.price_instructions.unit_price.toFloat();
            ref_price = this.price_instructions.reference_price.toFloat();
        }

        val url = "https://tienda.mercadona.es/product/${this.id}";

        try {
            return ProductInfo(ProductSource.Mercadona, this.id, this.display_name, price_val, sale_price_val, this.price_instructions.reference_format, ref_price, sale_ref_price, this.thumbnail, url);
        }
        catch (e: Exception) {
            throw RuntimeException("Exception parsing Mercadona product ID=$id, display_name=$display_name:\n${e.message}");
        }
    }
}
