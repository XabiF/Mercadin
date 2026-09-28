import requests
from typing import Union
from .common import perform_get, perform_post, CORE_UNITS, make_product

SEARCH_URL = "https://7uzjkl1dj0-dsn.algolia.net/1/indexes/products_prod_{wh}_{lang}/query?lang={lang}&wh={wh}&x-algolia-agent{x_algolia_agent}&x-algolia-application-id={x_algolia_app_id}&x-algolia-api-key={x_algolia_api_key}"
QUERY_URL = "https://tienda.mercadona.es/api/products/{id}?lang={lang}&wh={wh}"

def raw_search_products(query: str) -> tuple[int, Union[dict,None]]:
    params = {
        "lang": "es",
        "wh": 4572,
        "x_algolia_agent": "Algolia for JavaScript (3.35.1); Browser",
        "x_algolia_app_id": "7UZJKL1DJ0",
        "x_algolia_api_key": "9d8f2e39e90df472b4f2e559a116fe17"
    }
    body = {
        "query": query,
        "clickAnalytics": True,
        "analyticsTags": ["web"],
        "getRankingInfo": True,
        "analytics": True
    }
    return perform_post(SEARCH_URL, params, body)

def raw_query_product(id: str) -> tuple[int, Union[dict,None]]:
    params = {
        "id": id,
        "lang": "es",
        "wh": 4572
    }
    return perform_get(QUERY_URL, params)

def _extract_categories(categories, cat_data):
    if "categories" in cat_data:
        for subcat_data in cat_data["categories"]:
            categories.append(subcat_data["name"])
            _extract_categories(categories, subcat_data)

def _data_to_product(data: dict) -> Union[dict,None]:
    categories = []
    _extract_categories(categories, data)

    if data["price_instructions"]["previous_unit_price"] is not None:
        base_price = float(data["price_instructions"]["previous_unit_price"])
        base_sale_price = float(data["price_instructions"]["unit_price"])
    else:
        base_price = float(data["price_instructions"]["unit_price"])
        base_sale_price = None

    return make_product(
        id=data["id"],
        name=data["display_name"],
        brand=data["brand"],
        available=data["limit"] > 0, # Buena forma de hacer esto?
        categories=categories,
        page_url=data["share_url"],
        image_url=data["thumbnail"],
        base_unit="ud",
        base_price=base_price,
        base_sale_price=base_sale_price,
        ref_unit=data["price_instructions"]["size_format"],
        ref_price=float(data["price_instructions"]["bulk_price"]),
    )

def search_products(query: str) -> Union[list[dict],None]:
    code, data = raw_search_products(query)
    if code != 200:
        return None
    return [_data_to_product(product_data) for product_data in data["hits"]]

def query_product(id: str) -> Union[dict,None]:
    code, data = raw_query_product(id)
    if code != 200:
        return None
    return _data_to_product(data)
