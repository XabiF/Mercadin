package com.xabif.mercadin.util

import android.content.Context
import android.os.Environment
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException


class FileSystem {
    companion object {
        lateinit var ExternalFilesDir: File
        var Serializer = Gson()

        fun initialize(context: Context) {
            ExternalFilesDir = File(Environment.getExternalStorageDirectory(), "Android/media/${context.packageName}")
            if(!ExternalFilesDir.exists()) {
                ExternalFilesDir.mkdirs()
            }
        }

        fun getPath(file: String) : String {
            return File(ExternalFilesDir, file).toString()
        }

        fun saveJSONArray(arr: JSONArray, path: String) {
            val file = File(path)

            try {
                FileOutputStream(file).use { fos ->
                    fos.write(arr.toString(4).toByteArray())
                    fos.flush()
                }
            }
            catch(e: IOException) {
                e.printStackTrace()
            }
        }

        fun loadJSONArray(path: String): JSONArray? {
            val file = File(path)
            val sb = StringBuilder()

            try {
                BufferedReader(FileReader(file)).use { br ->
                    var line: String?
                    while((br.readLine().also { line = it }) != null) {
                        sb.append(line)
                    }
                    return JSONArray(sb.toString())
                }
            }
            catch(e: Exception) {
                e.printStackTrace()
                return null
            }
        }


        fun <T> saveDataClass(obj: T, path: String) {
            val file = File(path)

            val jsonStr = Serializer.toJson(obj)
            try {
                FileOutputStream(file).use { fos ->
                    fos.write(jsonStr.toByteArray())
                    fos.flush()
                }
            }
            catch(e: IOException) {
                e.printStackTrace()
            }
        }

        inline fun <reified T> loadDataClass(path: String): T? {
            val file = File(path)
            val sb = StringBuilder()

            try {
                BufferedReader(FileReader(file)).use { br ->
                    var line: String?
                    while((br.readLine().also { line = it }) != null) {
                        sb.append(line)
                    }
                    return Serializer.fromJson(sb.toString(), T::class.java)
                }
            }
            catch(e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }
}