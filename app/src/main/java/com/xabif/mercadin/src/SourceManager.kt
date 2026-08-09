package com.xabif.mercadin.src

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.xabif.mercadin.apis.aldi.Aldi
import com.xabif.mercadin.apis.bm.Bm
import com.xabif.mercadin.apis.carrefour.Carrefour
import com.xabif.mercadin.apis.dia.Dia
import com.xabif.mercadin.apis.mercadona.Mercadona
import com.xabif.mercadin.util.QueryFilter
import com.xabif.mercadin.util.QuerySorting
import kotlin.collections.List

object SourceManager {
    private var mercadonaEnabled = true;
    private val mercadona: Mercadona = Mercadona();
    private var bmEnabled = true;
    private val bm: Bm = Bm();
    private var diaEnabled = true;
    private val dia: Dia = Dia();
    private var carrefourEnabled = true;
    private val carrefour: Carrefour = Carrefour();
    private var aldiEnabled = true;
    private val aldi: Aldi = Aldi();

    fun toggleSource(source: ProductSource, enabled: Boolean) {
        when(source) {
            ProductSource.Bm -> bmEnabled = enabled;
            ProductSource.Mercadona -> mercadonaEnabled = enabled;
            ProductSource.Dia -> diaEnabled = enabled;
            ProductSource.Carrefour -> carrefourEnabled = enabled;
            ProductSource.Aldi -> aldiEnabled = enabled;
        }
    }

    suspend fun queryProductById(source: ProductSource, id: String) : ProductInfo {
        return when(source) {
            ProductSource.Bm -> {
                bm.queryProductById(id)
            }
            ProductSource.Mercadona -> {
                mercadona.queryProductById(id)
            }
            ProductSource.Dia -> {
                dia.queryProductById(id)
            }
            ProductSource.Carrefour -> {
                carrefour.queryProductById(id)
            }
            ProductSource.Aldi -> {
                aldi.queryProductById(id)
            }
        };
    }

    suspend fun queryProducts(query: String, filter: QueryFilter, sorting: QuerySorting): List<ProductInfo> {
        val products: MutableList<ProductInfo> = mutableListOf();
        if(bmEnabled) {
            products.addAll(bm.queryProducts(query));
        }
        if(mercadonaEnabled) {
            products.addAll(mercadona.queryProducts(query));
        }
        if(diaEnabled) {
            products.addAll(dia.queryProducts(query));
        }
        if(carrefourEnabled) {
            products.addAll(carrefour.queryProducts(query));
        }
        if(aldiEnabled) {
            products.addAll(aldi.queryProducts(query));
        }

        // Primero un sorting por defecto
        val (key, nope) = products.partition { product -> product.name.contains(query, ignoreCase = true) };
        val keys = key.sortedBy {
                product -> product.name.indexOf(query, ignoreCase = true)
        };
        val init_sorted_products = keys + nope;

        val sorted_products = when(sorting) {
            QuerySorting.Default -> { init_sorted_products }
            QuerySorting.PriceLowest -> {
                init_sorted_products.sortedBy { it.actualUnitPrice() };
            }
            QuerySorting.PriceHighest -> {
                init_sorted_products.sortedByDescending { it.actualUnitPrice() };
            }
        };

        val filtered_sorted_products = when(filter) {
            QueryFilter.Default -> { sorted_products }
            QueryFilter.SalesOnly -> { sorted_products.filter { it.isSale() } }
        }

        return filtered_sorted_products;
    }

    fun open(context: Context, product: ProductInfo) {
        val intent = Intent(Intent.ACTION_VIEW, product.url.toUri());
        val chooser = Intent.createChooser(intent, "Abrir producto en la web con");
        context.startActivity(chooser);
    }
}
