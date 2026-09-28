import requests
from typing import Union
from .common import perform_get, make_product

SEARCH_URL = "https://www.compraonline.alcampo.es/api/webproductpagews/v6/product-pages/search/?includeAdditionalPageInfo={includeAdditionalPageInfo}&maxPageSize={maxPageSize}&maxProductsToDecorate={maxProductsToDecorate}&q={q}&tag={tag}"
QUERY_URL = "https://www.compraonline.alcampo.es/api/webproductpagews/v5/products/bop/?retailerProductId={retailerProductId}"

def raw_search_products(query: str) -> tuple[int, Union[dict,None]]:
    params = {
        "includeAdditionalPageInfo": "true",
        "maxPageSize": 300,
        "maxProductsToDecorate": 50,
        "q": query,
        "tag": "web"
    }
    extra_headers = {
        "ecom-request-source": "web",
        "client-route-id": "dda564e6-e048-497e-b89a-89a9d7666ec0",
        "ecom-request-source-version": "2.0.0-2026-08-21-08h23m46s-2e65d129",
        "page-view-id": "78a40bec-7ab6-42aa-930f-fef4a8f5883f",
        "Host": "www.compraonline.alcampo.es",
        "Connection": "keep-alive",
        "Sec-GPC": "1",
        "Alt-Used": "www.compraonline.alcampo.es",
        "Sec-Fetch-Dest": "empty",
        "Sec-Fetch-Mode": "cors",
        "Sec-Fetch-Site": "same-origin",
        "Accept": "application/json; charset=utf-8",
        "Priority": "u=0",
        "Accept-Language": "en-US,en;q=0.9",
        "Accept-Encoding": "gzip, deflate, br, zstd",
        "Referer": "https://www.compraonline.alcampo.es/products/auchan-pechuga-fileteada-corte-fino-8-12-uds-producto-alcampo/24056"
    }
    return perform_get(SEARCH_URL, params, extra_headers)

def raw_query_product(id: str) -> tuple[int, Union[dict,None]]:
    params = {
        "retailerProductId": id
    }
    return perform_get(QUERY_URL, params)

def _data_to_product(data: dict) -> Union[dict,None]:
    return make_product(
        id=data["retailerProductId"],
        name=data["name"],
        brand=data["brand"],
        available=data["available"],
        categories=data["categoryPath"],
        page_url=f"https://www.compraonline.alcampo.es/products/{data['retailerProductId']}",
        image_url=data["image"]["src"],
        base_unit="ud",
        base_price=float(data["price"]["amount"]),
        base_sale_price=None, # Solo parecen tener ofertas de descuentos en 2a unidad/etc, no bajadas de precios individuales...
        ref_unit=data["unitPrice"]["unit"],
        ref_price=float(data["unitPrice"]["price"]["amount"]),
    )

def search_products(query: str) -> Union[list[dict],None]:
    code, data = raw_search_products(query)
    if code != 200:
        return None

    return [_data_to_product(product_data) for group in data["productGroups"] for product_data in group["decoratedProducts"]]

def query_product(id: str) -> Union[dict,None]:
    code, data = raw_query_product(id)
    if code != 200:
        return None
    return _data_to_product(data)
