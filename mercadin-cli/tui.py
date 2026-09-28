import sys
import json
from sources import SOURCES_MAP

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: tui.py <source> <mode+args>")
        exit()
    
    source = sys.argv[1]
    mode, args = sys.argv[2].split("=")
    source_mod = SOURCES_MAP[source]

    if mode == "search":
        print(f":: Running search...")
        ans = source_mod.search_products(args)
        if ans is not None:
            for product in ans:
                print(product)
        else:
            print("!! Search failed!")
    elif mode == "query":
        print(f":: Running search...")
        product = source_mod.query_product(args)
        if product is not None:
            print(product)
        else:
            print("!! Product query failed!")
    else:
        raise RuntimeError("Invalid mode!")

    print(":: Done!")
