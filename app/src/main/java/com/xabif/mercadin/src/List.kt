package com.xabif.mercadin.src

import android.util.Log
import com.xabif.mercadin.util.FileSystem
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.List

object List {
    private val products: MutableMap<ProductInfo, ListEntry> = mutableMapOf();

    fun getProductCount(info: ProductInfo) : Int {
        return products.getOrDefault(info, ListEntry(0, false)).count;
    }

    const val BackupFile = "cart.json";

    private fun getBackupPath() : String {
        return FileSystem.getPath(BackupFile);
    }

    fun backup() {
        val list = JSONArray();
        for((product, entry) in products) {
            val base_obj = JSONObject();

            val product_obj = product.toJson();
            base_obj.put("product", product_obj);
            base_obj.put("count", entry.count);
            base_obj.put("checked", entry.checked);

            list.put(base_obj);
        }

        FileSystem.saveJSONArray(list, getBackupPath());
        Log.d("list", "saved to ${getBackupPath()}");
    }

    fun restore() {
        products.clear();
        val list = FileSystem.loadJSONArray(getBackupPath());
        if(list != null) {
            try {
                for (i in 0..<list.length()) {
                    val base_obj = list.getJSONObject(i);
                    val count = base_obj.getInt("count");
                    val checked = base_obj.getBoolean("checked");
                    val product_json = base_obj.getJSONObject("product");
                    val product = ProductInfo(product_json);
                    products[product] = ListEntry(count, checked);
                }
            }
            catch(e: Exception) {
                // Reset the backup if anything did not load fine
                Log.d("backup", "Found exception ($e), resetting...");
                backup();
            }
        }
    }

    fun addProduct(info: ProductInfo) {
        var checked = false;
        if(products[info] != null) {
            checked = products[info]!!.checked;
        }

        products[info] = ListEntry(getProductCount(info) + 1, checked);
        backup();
    }

    fun hasCheckedValue(info: ProductInfo): Boolean {
        return products.contains(info);
    }

    fun isChecked(info: ProductInfo): Boolean {
        return products[info]?.checked ?: false;
    }

    fun setChecked(info: ProductInfo, checked: Boolean) {
        val entry = products[info];
        if(entry != null) {
            entry.checked = checked;
        }
        else {
            products[info] = ListEntry(0, checked);
        }
    }

    fun removeProduct(info: ProductInfo) {
        val newCount = getProductCount(info) - 1;
        if(newCount == 0) {
            products.remove(info);
            backup();
        }
        else {
            val entry = products[info];
            if(entry != null) {
                entry.count -= 1;
                backup();
            }
        }
    }

    fun canRemoveProduct(info: ProductInfo) : Boolean {
        return getProductCount(info) > 0;
    }

    suspend fun listAvailable() : List<ProductInfo> {
        val available: MutableList<ProductInfo> = mutableListOf();

        for(entry in products) {
            if(entry.value.count > 0) {
                // Volver a buscar item!
                try {
                    val product = SourceManager.queryProductById(entry.key.source, entry.key.id);
                    available.add(product);

                    Log.d("list", "A: ${entry.key}");
                    Log.d("list", "B: ${product}");
                    Log.d("list", "eq: ${product.equals(entry.key)}");
                }
                catch(e: Exception) {
                    Log.e("list", "Unable to query product by source=${entry.key.source} ID=${entry.key.id}: $e");
                }
            }
        }

        return available;
    }

    fun count() : Int  {
        var count = 0;
        for(product in products) {
            count += product.value.count;
        }
        return count;
    }

    fun totalPrice() : Float {
        var price = 0.0f;
        for(product in products) {
            price += product.key.actualProductPrice() * product.value.count;
        }
        return price;
    }
}
