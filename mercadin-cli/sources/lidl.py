import requests
from typing import Union
from .common import perform_get, make_product

SEARCH_URL = "https://www.lidl.es/q/api/search?assortment={assortment}&locale={locale}&version={version}&q={q}"
QUERY_URL = "https://www.lidl.es/p/api/gridboxes/ES/es?erpNumbers={erpNumbers}"

def raw_search_products(query: str) -> tuple[int, Union[dict,None]]:
    params = {
        "assortment": "ES",
        "locale": "es_ES",
        "version": "v2.0.0",
        "q": query
    }
    extra_headers = {
        "Accept": "*/*"
    }
    return perform_get(SEARCH_URL, params, extra_headers)

def raw_query_product(id: str) -> tuple[int, Union[dict,None]]:
    params = {
        "erpNumbers": id
    }
    return perform_get(QUERY_URL, params)

def _data_to_product(data: dict) -> Union[dict,None]:
    # TODO: algunos no tienen "price", mirar "regionsPrices", "currentLidlPlusPrice"
    price = data["gridbox"]["data"]["price"]["price"]
    if "oldPrice" in data["gridbox"]["data"]["price"]:
        base_price = data["gridbox"]["data"]["price"]["oldPrice"]
        base_sale_price = price
    else:
        base_price = price
        base_sale_price = None

    return make_product(
        id=data["gridbox"]["data"]["erpNumber"],
        name=data["gridbox"]["data"]["fullTitle"],
        brand=data["gridbox"]["data"]["brand"]["name"] if "name" in data["gridbox"]["data"]["brand"] else "",
        available=data["gridbox"]["data"]["stockAvailability"]["availabilityIndicator"] == 3, # "AVAILABLE_ONLINE"
        categories=[data["gridbox"]["data"]["category"]],
        page_url="https://www.lidl.es" + data["gridbox"]["data"]["canonicalUrl"],
        image_url=data["gridbox"]["data"]["image"],
        base_unit="ud",
        base_price=base_price,
        base_sale_price=base_sale_price,
        ref_unit="ud",
        ref_price=data["gridbox"]["data"]["price"]["price"], # TODO: unidades en cantidades?
    )

def search_products(query: str) -> Union[list[dict],None]:
    code, data = raw_search_products(query)
    if code != 200:
        return None

    products = []
    for item in data["items"]:
        if item["type"] == "product":
            products.append(_data_to_product(item))
    return products

def query_product(id: str) -> Union[dict,None]:
    code, data = raw_query_product(id)
    if code != 200:
        return None
    return _data_to_product(data)
