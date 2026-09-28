import requests
from typing import Union
from .common import perform_post, make_product

SEARCH_URL = "https://l9knu74io7-dsn.algolia.net/1/indexes/an_prd_es_es_pen_products2/query?x-algolia-agent{x_algolia_agent}&x-algolia-application-id={x_algolia_app_id}&x-algolia-api-key={x_algolia_api_key}"

def raw_search_products(query: str) -> tuple[int, Union[dict,None]]:
    params = {
        "x_algolia_agent": "Algolia for JavaScript (4.26.0); Browser",
        "x_algolia_app_id": "L9KNU74IO7",
        "x_algolia_api_key": "83df5acd172c42ab174afa4583232b5d"
    }
    body = {
        "query": query
    }
    return perform_post(SEARCH_URL, params, body)

def _data_to_product(data: dict) -> Union[dict,None]:
    primary_asset_url = None
    for asset in data["assets"]:
        if asset["type"] == "primary":
            primary_asset_url = asset["url"]
    assert primary_asset_url is not None

    categories = []
    if "hierarchicalCategories" in data: # Puede no existir, por lo visto
        for cat_x, values in data["hierarchicalCategories"].items():
            categories.append(values[0])

    obj_id = data["objectID"]

    if "strikePrice" in data["currentPrice"]:
        base_price = data["currentPrice"]["strikePrice"]["strikePriceValue"]
        base_sale_price = data["currentPrice"]["priceValue"]
    else:
        base_price = data["currentPrice"]["priceValue"]
        base_sale_price = None

    if "basePrice" in data["currentPrice"]:
        ref_unit = data["currentPrice"]["basePrice"][0]["basePriceScale"]
        ref_price = data["currentPrice"]["basePrice"][0]["basePriceValue"]
    else:
        ref_unit = "ud"
        ref_price = data["currentPrice"]["priceValue"]

    return make_product(
        id=obj_id,
        name=data["name"],
        brand=data["brandName"] if "brandName" in data else "",
        available=data["isAvailable"],
        categories=categories,
        page_url=f"https://www.aldi.es/producto/{obj_id}.html",
        image_url=primary_asset_url,
        base_unit="ud",
        base_price=base_price,
        base_sale_price=base_sale_price,
        ref_unit=ref_unit,
        ref_price=ref_price,
    )

def search_products(query: str) -> Union[list[dict],None]:
    code, data = raw_search_products(query)
    if code != 200:
        return None

    products = []
    for product_data in data["hits"]:
        if "currentPrice" in product_data: # Sólo nos quedamos con los que tengan info de precios
            products.append(_data_to_product(product_data))
    return products

def query_product(id: str) -> Union[dict,None]:
    # TODO
    return None
