package com.xabif.mercadin.src

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.xabif.mercadin.util.Config
import com.xabif.mercadin.util.QueryFilter
import com.xabif.mercadin.util.QuerySorting
import kotlin.collections.List

object SourceManager {
    private val sources: MutableMap<ProductSource, SourceInstance?> = mutableMapOf()

    fun initialize() {
        for (source in ProductSource.entries) {
            toggleSource(source, Config.Instance.getSourceEnabled(source))
        }
    }

    fun toggleSource(source: ProductSource, enabled: Boolean) {
        if(enabled) {
            sources[source] = source.create()
        }
        else {
            sources[source] = null
        }
    }

    suspend fun queryProductById(source: ProductSource, id: String) : ProductInfo {
        return if(sources[source] != null) {
            sources[source]!!.queryProductById(id)
        } else {
            source.create().queryProductById(id)
        }
    }

    suspend fun queryProducts(query: String, filter: QueryFilter, sorting: QuerySorting): List<ProductInfo> {
        val products: MutableList<ProductInfo> = mutableListOf()
        for ((_, instance) in sources) {
            if(instance != null) {
                products.addAll(instance.queryProducts(query))
            }
        }

        // Primero un sorting por defecto
        val (key, nope) = products.partition { product -> product.name.contains(query, ignoreCase = true) }
        val keys = key.sortedBy {
                product -> product.name.indexOf(query, ignoreCase = true)
        }
        val init_sorted_products = keys + nope

        val sorted_products = when(sorting) {
            QuerySorting.Default -> { init_sorted_products }
            QuerySorting.PriceLowest -> {
                init_sorted_products.sortedBy { it.actualUnitPrice() }
            }
            QuerySorting.PriceHighest -> {
                init_sorted_products.sortedByDescending { it.actualUnitPrice() }
            }
        }

        val filtered_sorted_products = when(filter) {
            QueryFilter.Default -> { sorted_products }
            QueryFilter.SalesOnly -> { sorted_products.filter { it.isSale() } }
        }

        return filtered_sorted_products
    }

    fun open(context: Context, product: ProductInfo) {
        val intent = Intent(Intent.ACTION_VIEW, product.url.toUri())
        val chooser = Intent.createChooser(intent, "Abrir producto en la web con")
        context.startActivity(chooser)
    }
}
