package com.xabif.mercadin.util

import com.xabif.mercadin.R

enum class QuerySorting(val resId: Int) {
    Default(R.string.query_sorting_default),
    PriceLowest(R.string.query_sorting_price_lowest),
    PriceHighest(R.string.query_sorting_price_highest)
}