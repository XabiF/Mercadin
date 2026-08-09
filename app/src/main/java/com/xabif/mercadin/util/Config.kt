package com.xabif.mercadin.util

import com.xabif.mercadin.src.ProductSource

data class Config(
    val enabledSources: MutableMap<Int, Boolean>
) {
    companion object {
        lateinit var Instance: Config
        lateinit var Path: String

        fun initialize() {
            Path = FileSystem.getPath("config.json")

            val config = FileSystem.loadDataClass<Config>(Path)
            if(config == null) {
                Instance = defaultConfig()
                save()
            }
            else {
                Instance = config
            }
        }

        fun save() {
            FileSystem.saveDataClass(Instance, Path)
        }

        fun defaultConfig() : Config {
            val sources: MutableMap<Int, Boolean> = mutableMapOf()
            for (source in ProductSource.entries) {
                sources[source.ordinal] = true
            }
            return Config(sources)
        }
    }

    fun setSourceEnabled(source: ProductSource, enabled: Boolean) {
        enabledSources[source.ordinal] = enabled
        save()
    }

    fun getSourceEnabled(source: ProductSource) : Boolean {
        return enabledSources[source.ordinal]!!
    }
}