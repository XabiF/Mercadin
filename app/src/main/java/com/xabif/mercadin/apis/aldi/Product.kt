package com.xabif.mercadin.apis.aldi

import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.ProductSource

data class Product(
    val name: String,
    val objectID: String,
    val productSlug: String,
    val assets: List<ProductAsset>,
    val currentPrice: ProductPrice?
) {
    fun isValid() : Boolean {
        return this.currentPrice != null;
    }

    fun toProductInfo() : ProductInfo {
        var image_url = "";
        for (asset in this.assets) {
            if(asset.type == "primary") {
                image_url = asset.url;
            }
        }
        if(image_url.isEmpty() && this.assets.isNotEmpty()) {
            image_url = this.assets.first().url;
        }

        val url = "https://www.aldi.es/producto/${this.productSlug}.html"

        var price_unit = "unidad";
        var ref_price_value = this.currentPrice!!.priceValue;
        if(!this.currentPrice.basePrice.isNullOrEmpty()) {
            price_unit = this.currentPrice.basePrice.first().basePriceScale;
            ref_price_value = this.currentPrice.basePrice.first().basePriceValue;
        }

        try {
            return ProductInfo(ProductSource.Aldi, this.objectID, this.name, this.currentPrice.priceValue, null, price_unit, ref_price_value, null, image_url, url);
        }
        catch (e: Exception) {
            throw RuntimeException("Exception parsing ALDI product ID=${this.objectID}, display_name=${this.name}:\n${e.message}");
        }
    }
}
