package com.paneon.episoderenamer.util

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.paneon.episoderenamer.shows.Show
import kotlinx.serialization.Serializable
import java.io.File
import java.io.IOException

@Serializable
data class ConfigFile(
    val shows: List<ConfigShowEntry>? = emptyList(),
)

@Serializable
data class ConfigShowEntry(
    val name: String?,
    val aliases: List<String> = emptyList(),
)

data class Config(
    val shows: List<Show>,
)

class ConfigLoader {
    fun getConfigFileContent(): String {
        val configFile = File("config/shows.yml")
        val defaultConfigFile = File("config/shows.yml.dist")

        return try {
            if (configFile.exists()) {
                configFile.readText()
            } else {
                println("Config file not found, using default config")
                defaultConfigFile.readText()
            }
        } catch (e: IOException) {
            println("Error reading config file: ${e.message}")
            throw e
        }
    }

    fun loadConfig(): Config {
        val configFileContent = getConfigFileContent()
        val yaml = Yaml(configuration = YamlConfiguration(strictMode = false))
        val data: ConfigFile = yaml.decodeFromString(ConfigFile.serializer(), configFileContent)

        val config =
            data?.shows?.mapNotNull { show ->
                if (show.name == null) {
                    println("Show name is missing in config file")
                    null
                } else {
                    Show(name = show.name, aliases = show.aliases)
                }
            }?.let {
                Config(shows = it)
            }

        return config ?: Config(shows = emptyList())
    }
}
