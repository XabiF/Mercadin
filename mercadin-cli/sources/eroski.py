import requests
from typing import Union
from .common import perform_post, make_product, CORE_UNITS

SEARCH_URL = "https://supermercado.eroski.es/es/index.menulayout.searchbar.suggestform"

def raw_search_products(query: str) -> tuple[int, Union[dict,None]]:
    params = {}
    body = {
        "t%3Azoneid": "suggestListZone",
        "t%3Aformdata": "e5HR8oJqcUi36yfl7K15KzLV7uQ%3D%3AH4sIAAAAAAAAAC3OMW4CMRBAUQcpadIgUqZJQRfJVGlIkyoKEkJIdHSz3olxtJ6xZsZhOQGX4QBcijuwSFS%2F%2BM07Xdzj3rv3BbXYzzNS7eDA1bwiSNg1IF5rjKj2y5ITlWpOxX2xRA8Fwg69QRm2HD58YMEuNUNzYUIy9T%2BpbZGma%2BGAqpva5KSamLbHt5f%2B9fw0cg9L9xyYTLhbQUZzk%2BUf%2FMOsA4qzjUmi%2BNkXc%2BO743twLG6OK5z20Xq7AAAA",
        "suggestFormInput": query
    }
    extra_headers = {
        'x-requested-with': 'XMLHttpRequest',
        "content-type": "application/json"
    }
    return perform_post(SEARCH_URL, params, body, extra_headers)

# TODO: search_products
