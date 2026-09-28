import sys
import json
from sources import SOURCES_MAP

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: api.py <source> <mode+args> [<out-cli-or-json>]")
        exit()
    
    source = sys.argv[1]
    mode, args = sys.argv[2].split("=")
    out = sys.argv[3] if len(sys.argv) > 3 else "cli"

    print(f":: Running operation...")

    source_mod = SOURCES_MAP[source]
    source_fn = source_mod.raw_search_products if mode == "search" else source_mod.raw_query_product
    code, res_json = source_fn(args)

    print(f":: Operation returned status code {code}")
    if res_json is not None:
        res_str = json.dumps(res_json, indent=4)
        if out == "cli":
            print(100*"=")
            print(f"=== {source}, {mode}({args})")
            print(100*"=")
            print(res_str)
            print(100*"=")
        else:
            with open(out, "w") as f:
                f.write(res_str)
    else:
        print("No hubo respuesta JSON!")

    print(":: Done!")
