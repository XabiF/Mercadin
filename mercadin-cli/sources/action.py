import requests
from typing import Union
from .common import perform_get, perform_post
import json

SEARCH_URL = "https://www.action.com/api/graphql?operationName={operationName}&variables={variables}&extensions={extensions}"

def raw_search_products(query: str) -> tuple[int, Union[dict,None]]:
    params = {
        "operationName": "SearchProductsWithSuggestions",
        "variables": json.dumps({
            "input": {
                "searchTerm": query
            }
        }),
        "extensions": json.dumps({
            "persistedQuery": {
                "sha256Hash": "fcd76d298b620d55a174c22401674d3c735c798d4d0fa4165c92a8dbaf5a9548",
                "version": 1,
            },
            "headers": {
                "Accept-Language": "es-ES",
            }
        })
    }
    extra_headers = {
        "Accept-Language": "es-ES",
        "apollographql-client-name": "web",
        "x-client-version": "1.319",
        "cache-control": "no-cache"
    }
    return perform_get(SEARCH_URL, params, extra_headers)

def _data_to_product(data: dict) -> Union[dict,None]:
    # El precio viene sólo formateado, lo sacamos a mano
    ref_price = float(data["price_per_unit_text"].split(" ")[0].replace(",", "."))

    if "strikethrough_price" in data:
        base_price = data["strikethrough_price"]
        base_sale_price = data["active_price"]
    else:
        base_price = data["active_price"]
        base_sale_price = None

    return make_product(
        id=data["product_id"],
        name=data["display_name"],
        brand=data["brand"],
        available=data["stock"] > 0,
        categories=[], # TODO
        page_url="https://carrefour.es" + data["url"],
        image_url=data["image_path"],
        base_unit="ud",
        base_price=base_price,
        base_sale_price=base_sale_price,
        ref_unit=data["measure_unit"],
        ref_price=ref_price,
    )

def search_products(query: str) -> Union[list[dict],None]:
    code, data = raw_search_products(query)
    if code != 200:
        return None
    return [_data_to_product(product_data) for product_data in data["content"]["docs"]]

def query_product(id: str) -> Union[dict,None]:
    # TODO
    return None
