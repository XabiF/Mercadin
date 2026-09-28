import requests
from typing import Union
from .common import perform_get, make_product

SEARCH_URL = "https://www.dia.es/api/v1/search-back/search/reduced?q={q}&page={page}"
QUERY_URL = "https://www.dia.es/api/v1/pdp-back/reduced/{id}/"

def raw_search_products(query: str) -> tuple[int, Union[dict,None]]:
    params = {
        "page": 1,
        "q": query
    }
    return perform_get(SEARCH_URL, params)

def raw_query_product(id: str) -> tuple[int, Union[dict,None]]:
    params = {
        "id": id
    }
    return perform_get(QUERY_URL, params)

def _data_to_product(data: dict) -> Union[dict,None]:
    price_2 = data["prices"]["price"]
    base_price = data["prices"]["strikethrough_price"]
    base_sale_price = None if (base_price == price_2) else price_2
    
    return make_product(
        id=data["object_id"],
        name=data["display_name"],
        brand=data["brand"],
        available=data["units_in_stock"] > 0,
        categories=[data["l1_category_description"], data["l2_category_description"]],
        page_url="https://www.dia.es" + data["url"],
        image_url="https://www.dia.es" + data["image"],
        base_unit="ud",
        base_price=data["prices"]["strikethrough_price"],
        base_sale_price=base_sale_price,
        ref_unit=data["prices"]["measure_unit"],
        ref_price=data["prices"]["price_per_unit"],
    )

def search_products(query: str) -> Union[list[dict],None]:
    code, data = raw_search_products(query)
    if code != 200:
        return None

    products = []
    for product_data in data["search_items"]:
        price_2 = product_data["prices"]["price"]
        base_price = product_data["prices"]["strikethrough_price"]
        base_sale_price = None if (base_price == price_2) else price_2
        
        product = make_product(
            id=product_data["object_id"],
            name=product_data["display_name"],
            brand=product_data["brand"],
            available=product_data["units_in_stock"] > 0,
            categories=[product_data["l1_category_description"], product_data["l2_category_description"]],
            page_url="https://www.dia.es" + product_data["url"],
            image_url="https://www.dia.es" + product_data["image"],
            base_unit="ud",
            base_price=product_data["prices"]["strikethrough_price"],
            base_sale_price=base_sale_price,
            ref_unit=product_data["prices"]["measure_unit"],
            ref_price=product_data["prices"]["price_per_unit"],
        )
        products.append(product)

    return products

def query_product(id: str) -> Union[dict,None]:
    code, data = raw_query_product(id)
    if code != 200:
        return None

    product_data = data["product"]

    price_2 = product_data["prices"]["price"]
    base_price = product_data["prices"]["strikethrough_price"]
    base_sale_price = None if (base_price == price_2) else price_2

    # Heurística: calculamos la URL usando el breadcrumb de URL más larga
    # (así parecen estar siempre formadas las URLs aquí)
    longest_bc_link = ""
    for bc in product_data["breadcrumb"]:
        if len(bc["link"]) > len(longest_bc_link):
            longest_bc_link = bc["link"]
    bc_link_start, _ = longest_bc_link.split("/c/")
    sku_id = product_data["sku_id"]
    url_base = f"{bc_link_start}/p/{sku_id}"

    return make_product(
        id=sku_id,
        name=product_data["primary_info"]["title"],
        brand=product_data["manufacturer_contact"]["manufacturer_contact_name"], # No es del todo lo mismo...
        available=product_data["units_in_stock"] > 0,
        categories=[breadcrumb["title"] for breadcrumb in product_data["breadcrumb"]],
        page_url="https://www.dia.es" + url_base,
        image_url="https://www.dia.es" + product_data["images"][0],
        base_unit="ud",
        base_price=product_data["prices"]["strikethrough_price"],
        base_sale_price=base_sale_price,
        ref_unit=product_data["prices"]["measure_unit"],
        ref_price=product_data["prices"]["price_per_unit"],
    )

    return _data_to_product(data)
