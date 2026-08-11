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
                Instance.load()
            }
        }

        fun save() {
            FileSystem.saveDataClass(Instance, Path)
        }

        fun defaultConfig() : Config {
            val config = Config(mutableMapOf())
            config.load()
            return config
        }
    }

    fun load() {
        for(source in ProductSource.entries) {
            if(!this.enabledSources.contains(source.ordinal)) {
                this.enabledSources[source.ordinal] = true
            }
        }
    }

    fun setSourceEnabled(source: ProductSource, enabled: Boolean) {
        this.enabledSources[source.ordinal] = enabled
        save()
    }

    fun getSourceEnabled(source: ProductSource) : Boolean {
        return this.enabledSources[source.ordinal]!!
    }
}