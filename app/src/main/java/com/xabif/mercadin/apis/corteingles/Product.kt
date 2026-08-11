package com.xabif.mercadin.apis.corteingles

import com.xabif.mercadin.src.ProductInfo
import com.xabif.mercadin.src.ProductSource

data class Product(
    val id: String,
    val description: String?,
    val image: String?,
    val url: String?,
    val pum: String?,
    val priceSpecification: PriceSpecification?
) {
    fun isValid() : Boolean {
        // Algunos productos no contienen muchos de estos campos necesarios, por lo que sea
        return this.description != null
    }

    fun toProductInfo() : ProductInfo {
        val url = "https://www.elcorteingles.es${this.url}"
        val price = this.priceSpecification!!.price.replace(',', '.').toFloat()
        val refPrice = if(this.priceSpecification.measurementUnitPrice.isEmpty()) {
            price
        }
        else {
            this.priceSpecification.measurementUnitPrice.replace(',', '.').toFloat()
        }
        val salePrice = if(this.priceSpecification.has_discount ?: false) {
            this.priceSpecification.salePrice?.replace(',', '.')?.toFloat()
        }
        else {
            null
        }
        val saleRefPrice = if(salePrice == null) {
            null
        }
        else {
            refPrice * (salePrice / price)
        }
        val image = this.image!!
        val name = this.description!!
        val unit_ref = this.priceSpecification.pum_description

        try {
            return ProductInfo(ProductSource.CorteIngles, this.id, name, price, salePrice, unit_ref, refPrice, saleRefPrice, image, url)
        }
        catch (e: Exception) {
            throw RuntimeException("Exception parsing Corte Inglés product ID=${this.id}, display_name=$name: ${e.message}\n\n${this}")
        }
    }
}