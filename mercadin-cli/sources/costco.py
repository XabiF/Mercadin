import requests
from typing import Union
from .common import perform_post, make_product

SEARCH_URL = "https://www.ubereats.com/_p/api/getInStoreSearchV1"
QUERY_URL = "https://www.ubereats.com/_p/api/getMenuItemV1"

def raw_search_products(query: str) -> tuple[int, Union[dict,None]]:
    params = {}
    body = {
        "diningMode": "DELIVERY",
        "sectionUUIDs": None,
        "storeUUIDs": [ "3fe1f9ef-60bd-559d-8db1-f06d9f4511b7" ],
        "userQuery": query,
        "isGrocery": True,
        "targetLocation": {},
        "entrypointContext": "IN_STORE_SEARCH"
    }
    extra_headers = {
        'x-uber-client-gitref': 'd4ff4bb7347f9a61196d2bb125afe894dd3853a3',
        'x-uber-request-id': '55337e94-fb57-426d-bf22-102bb5ccde06',
        'x-uber-device-location-latitude': '43.2970409',
        'x-uber-device-location-longitude': '-2.9850738999999997',
        'x-uber-target-location-latitude': '43.2970409',
        'x-uber-target-location-longitude': '-2.9850738999999997',
        'x-uber-ciid': '9212cb23-c156-4141-866b-7b07bc67e438',
        'x-uber-session-id': 'dd31d1ec-4a9d-4ed3-a244-d51a2e7a53d2',
        "x-csrf-token": "x",
    }
    return perform_post(SEARCH_URL, params, body, extra_headers)

def raw_query_product(id: str) -> tuple[int, Union[dict,None]]:
    uuid, section_uuid, subsection_uuid = id.split("@")

    params = {}
    body = {
        "itemRequestType": "ITEM",
        "storeUuid": "3fe1f9ef-60bd-559d-8db1-f06d9f4511b7",
        "sectionUuid": section_uuid,
        "subsectionUuid": subsection_uuid,
        "menuItemUuid": uuid,
        "isEditFlow": False,
        "cbType": "EATER_ENDORSED",
        "includeCheaperAlternatives": False,
        "contextReferences": [
            {
                "type": "GROUP_ITEMS",
                "payload": {
                    "type": "groupItemsContextReferencePayload",
                    "groupItemsContextReferencePayload": {}
                },
                "pageContext": "UNKNOWN"
            }
        ]
    }
    extra_headers = {
        'x-uber-client-gitref': 'd4ff4bb7347f9a61196d2bb125afe894dd3853a3',
        'x-uber-request-id': '55337e94-fb57-426d-bf22-102bb5ccde06',
        'x-uber-device-location-latitude': '43.2970409',
        'x-uber-device-location-longitude': '-2.9850738999999997',
        'x-uber-target-location-latitude': '43.2970409',
        'x-uber-target-location-longitude': '-2.9850738999999997',
        'x-uber-ciid': '9212cb23-c156-4141-866b-7b07bc67e438',
        'x-uber-session-id': 'dd31d1ec-4a9d-4ed3-a244-d51a2e7a53d2',
        "x-csrf-token": "x",
    }
    return perform_post(QUERY_URL, params, body, extra_headers)

def _data_to_product(data: dict) -> Union[dict,None]:
    if "priceBeforeDiscount" in data:
        base_price = data["priceBeforeDiscount"] / 100
        base_sale_price = data["price"] / 100
    elif "purchaseInfo" in data:
        base_price = data["purchaseInfo"]["purchaseOptions"][0]["purchasePriceV2"]["base"]["low"] / 100
        price_alt = data["price"] / 100
        base_sale_price = price_alt if (base_price != price_alt) else None
    else:
        base_price = data["price"] / 100
        base_sale_price = None

    return make_product(
        id=data["uuid"] + "@" + data["sectionUuid"] + "@" + data["subsectionUuid"],
        name=data["title"],
        brand="", # TODO ???
        available=data["isAvailable"] if "isAvailable" in data else True, # TODO: este campo no existe al hacer query?
        categories=[], # TODO ???
        page_url="", # TODO acaso existe tal concepto?
        image_url=data["imageUrl"],
        base_unit="ud",
        base_price=base_price,
        base_sale_price=base_sale_price,
        ref_unit="ud", #    TODO ???
        ref_price=base_price,
    )

def search_products(query: str) -> Union[list[dict],None]:
    code, data = raw_search_products(query)
    if code != 200:
        return None

    products = []
    for section, items in data["data"]["catalogSectionsMap"].items():
        for item in items:
            for product in item["payload"]["standardItemsPayload"]["catalogItems"]:
                products.append(_data_to_product(product))
    return products

def query_product(id: str) -> Union[dict,None]:
    code, data = raw_query_product(id)
    if code != 200:
        return None
    return _data_to_product(data["data"])
