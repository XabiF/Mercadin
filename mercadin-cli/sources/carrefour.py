import requests
from typing import Union
from .common import perform_get, make_product, CORE_UNITS

SEARCH_URL = "https://www.carrefour.es/search-api/query/v1/search?internal={internal}&instance={instance}&env={env}&scope={scope}&lang={lang}&session={session}&citrusCatalog={citrusCatalog}&baseUrlCitrus={baseUrlCitrus}&enabled={enabled}&store={store}&shopperId={shopperId}&hasConsent={hasConsent}&siteKey={siteKey}&grid_def_search_sponsor_product={grid_def_search_sponsor_product}&grid_def_search_butterfly_banner={grid_def_search_butterfly_banner}&grid_def_search_sponsor_product_tablet={grid_def_search_sponsor_product_tablet}&grid_def_search_butterfly_banner_tablet={grid_def_search_butterfly_banner_tablet}&grid_def_search_sponsor_product_mobile={grid_def_search_sponsor_product_mobile}&grid_def_search_butterfly_banner_mobile={grid_def_search_butterfly_banner_mobile}&grid_def_search_luckycart_banner={grid_def_search_luckycart_banner}&empathypoc={empathypoc}&catalog={catalog}&query={query}&page={page}"

def raw_search_products(query: str) -> tuple[int, Union[dict,None]]:
    params = {
        "internal": True,
        "instance": "x-carrefour",
        "env": "https://www.carrefour.es",
        "scope": "desktop",
        "lang": "es",
        "session": "empathy",
        "citrusCatalog": "food",
        "baseUrlCitrus": "https://www.carrefour.es",
        "enabled": True,
        "store": "005290",
        "shopperId": "32EEFGvrFH9I3ZyxFITpQwpUbt0",
        "hasConsent": False,
        "siteKey": "wFOzqveg",
        "grid_def_search_sponsor_product": "3,5,11,13,19",
        "grid_def_search_butterfly_banner": "7-8,15-16",
        "grid_def_search_sponsor_product_tablet": "2,4,11,13,19",
        "grid_def_search_butterfly_banner_tablet": "6,12",
        "grid_def_search_sponsor_product_mobile": "2,4,11,13,19",
        "grid_def_search_butterfly_banner_mobile": "6,12",
        "grid_def_search_luckycart_banner": "22",
        "empathypoc": False,
        "catalog": "food",
        "query": query,
        "page": 1
    }
    return perform_get(SEARCH_URL, params)

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
