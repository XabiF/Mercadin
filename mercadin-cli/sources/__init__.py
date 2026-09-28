import importlib

SOURCES = [
    "bm",
    "mercadona",
    "lidl",
    "dia",
    "carrefour",
    "corteingles",
    "aldi",
    "action",
    "alcampo",
    "consum",
    "eroski",
    "costco",
]

###############################################

SOURCES_MAP = {
    source: importlib.import_module("." + source, "sources") for source in SOURCES
}
