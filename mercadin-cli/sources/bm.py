import requests
from typing import Union
from .common import perform_get, make_product

SEARCH_URL = "https://www.online.bmsupermercados.es/api/rest/V1.0/catalog/product?page={page}&limit={limit}&offset={offset}&orderById={orderById}&showRecommendations={showRecommendations}&q={q}"
QUERY_URL = "https://www.online.bmsupermercados.es/api/rest/V1.0/catalog/product/code/{id}"

def raw_search_products(query: str) -> tuple[int, Union[dict,None]]:
    params = {
        "page": 1,
        "limit": 100,
        "offset": 0,
        "orderById": 7,
        "showRecommendations": False,
        "q": query
    }
    return perform_get(SEARCH_URL, params)

def raw_query_product(id: str) -> tuple[int, Union[dict,None]]:
    params = {
        "id": id
    }
    return perform_get(QUERY_URL, params)

def _data_to_product(data: dict) -> Union[dict,None]:
    base_price = None
    base_sale_price = None
    ref_price = None
    for price_data in data["priceData"]["prices"]:
        if price_data["id"] == "PRICE":
            base_price = price_data["value"]["centAmount"]
            ref_price = price_data["value"]["centUnitAmount"]
        elif price_data["id"] == "OFFER_PRICE":
            base_sale_price = price_data["value"]["centAmount"]

    return make_product(
        id=data["code"],
        name=data["productData"]["name"],
        brand=data["productData"]["brand"]["name"].strip(),
        available=bool(int(data["productData"]["availability"])),
        categories=[cat["name"] for cat in data["categories"]],
        page_url=data["productData"]["url"],
        image_url=data["productData"]["imageURL"],
        base_unit=data["priceData"]["priceUnitType"],
        base_price=base_price,
        base_sale_price=base_sale_price,
        ref_unit=data["priceData"]["unitPriceUnitType"],
        ref_price=ref_price,
    )

def search_products(query: str) -> Union[list[dict],None]:
    code, data = raw_search_products(query)
    if code != 200:
        return None
    return [_data_to_product(product_data) for product_data in data["products"]]

def query_product(id: str) -> Union[dict,None]:
    code, data = raw_query_product(id)
    if code != 200:
        return None
    return _data_to_product(data)
