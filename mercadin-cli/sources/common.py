import requests
from typing import Union
from random_user_agent.user_agent import UserAgent
from random_user_agent.params import SoftwareName, OperatingSystem

software_names = [SoftwareName.CHROME.value]
operating_systems = [OperatingSystem.WINDOWS.value, OperatingSystem.LINUX.value]   
user_agent_rotator = UserAgent(software_names=software_names, operating_systems=operating_systems, limit=100)

def make_basic_headers():
    return {
        "User-Agent": 'Mozilla/5.0 (X11; Linux x86_64; rv:153.0) Gecko/20100101 Firefox/153.0',
        "Sec-GPC": "1",
        "Connection": "keep-alive",
        "Accept": "application/json"
    }

def perform_get(url: str, params: dict, extra_headers=None) -> tuple[int, Union[dict,None]]:
    actual_url = url.format(**params)
    print(f"@@ Performing GET on URL: '{actual_url}'")
    headers = make_basic_headers()
    if extra_headers is not None:
        headers |= extra_headers
    print(f"@@ With headers:")
    for header, value in headers.items():
        print(f"----- {header}: {value}")
    response = requests.get(actual_url, headers=headers)
    try:
        return (response.status_code, response.json())
    except:
        return (response.status_code, None)

def perform_post(url: str, params: dict, body: dict, extra_headers=None) -> tuple[int, Union[dict,None]]:
    actual_url = url.format(**params)
    print(f"@@ Performing POST on URL: '{actual_url}'")
    headers = make_basic_headers()
    if extra_headers is not None:
        headers |= extra_headers
    print(f"@@ With headers:")
    for header, value in headers.items():
        print(f"----- {header}: {value}")
    response = requests.post(actual_url, json=body, headers=headers)
    try:
        return (response.status_code, response.json())
    except:
        return (response.status_code, None)

CORE_UNITS = [
    "ud",
    "kg",
    "l",
]

def parse_unit(raw_unit: str):
    raw_unit = raw_unit.lower()

    if raw_unit in CORE_UNITS:
        return (1, raw_unit)

    if raw_unit == "kilo":
        return (1, "kg")
    elif raw_unit == "1 kg":
        return (1, "kg")
    elif raw_unit == "kg.":
        return (1, "kg")
    elif raw_unit == "fop.price.per.kg":
        return (1, "kg")
    elif raw_unit == "unidad":
        return (1, "ud")
    elif raw_unit == "u.":
        return (1, "ud")
    elif len(raw_unit) == 0:
        return (1, "ud")
    elif raw_unit == "litro":
        return (1, "L")

    raise RuntimeError(f"Unknown unit found: '{raw_unit}'")

def make_product(
    id: str,
    name: str,
    brand: str,
    available: bool,
    categories: list[str],
    page_url: str,
    image_url: str,
    base_unit: str,
    base_price: float, # Precio en las unidades en las que se vende
    base_sale_price: Union[float,None],
    ref_unit: str,
    ref_price: float,  # Precio en unidad de referencia relevante (kg, litro, etc.)
):
    return {
        "id": id,
        "name": name,
        "brand": brand,
        "available": available,
        "categories": categories,
        "page_url": page_url,
        "image_url": image_url,
        "base_unit": parse_unit(base_unit),
        "base_price": base_price,
        "base_sale_price": base_sale_price,
        "ref_unit": parse_unit(ref_unit),
        "ref_price": ref_price
    }
