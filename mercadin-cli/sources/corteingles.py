import requests
from typing import Union
from .common import perform_get, make_product

SEARCH_URL = "https://www.elcorteingles.es/supermercado/buscar/?question={question}&catalog={catalog}&stype={stype}"

# TODO: no funciona la API...?

def raw_search_products(query: str) -> tuple[int, Union[dict,None]]:
    params = {
        "question": query,
        "catalog": "supermercado",
        "stype": "text_box"
    }
    extra_headers = {
        "response_type": "json"
    }
    return perform_get(SEARCH_URL, params, extra_headers)

def _extract_categories(categories, cat_data):
    if "name" in cat_data:
        categories.append(cat_data["name"])

    if "parent_category" in cat_data:
        parent_cat = cat_data["parent_category"]
        _extract_categories(categories, parent_cat)

def _data_to_product(data: dict) -> Union[dict,None]:
    categories = []
    if "categories" in data:
        for cat in data["categories"]:
            _extract_categories(categories, cat)

    base_price = float(data["priceSpecification"]["price"].replace(",", "."))
    price_2 =float(data["priceSpecification"]["salePrice"].replace(",", "."))
    if price_2 != base_price:
        base_sale_price = price_2
    else:
        base_sale_price = None

    return make_product(
        id=data["id"],
        name=data["description"],
        brand=data["brand"]["name"],
        available=True, # ???
        categories=categories,
        page_url="https://www.elcorteingles.es" + data["url"],
        image_url=data["image"],
        base_unit="ud",
        base_price=base_price,
        base_sale_price=base_sale_price,
        ref_unit=data["pum"],
        ref_price=float(data["priceSpecification"]["measurementUnitPrice"].replace(",", ".")),
    )

def search_products(query: str) -> Union[list[dict],None]:
    code, data = raw_search_products(query)
    if code != 200:
        return None

    products = []
    for product_data in data["products"]:
        if product_data["type"] == "item":
            products.append(_data_to_product(product_data))
    return products

def query_product(id: str) -> Union[dict,None]:
    return None
