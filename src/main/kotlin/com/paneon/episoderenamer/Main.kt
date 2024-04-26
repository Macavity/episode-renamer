package com.paneon.episoderenamer

import com.paneon.episoderenamer.config.ConfigShows
import com.paneon.episoderenamer.parser.FileParser
import com.paneon.episoderenamer.parser.Mode
import com.paneon.episoderenamer.parser.formatter.EpisodeFormatter
import com.paneon.episoderenamer.parser.matcher.GermanVerboseMatcher
import com.paneon.episoderenamer.parser.matcher.JDownloaderMatcher
import com.paneon.episoderenamer.parser.matcher.PlexMatcher
import com.paneon.episoderenamer.shows.Show
import com.paneon.episoderenamer.shows.ShowRepository
import com.paneon.episoderenamer.util.Logger
import com.paneon.episoderenamer.util.LoggerLevel
import io.github.cdimascio.dotenv.dotenv
import org.yaml.snakeyaml.Yaml
import java.io.File

fun main(args: Array<String>) {
    val dotenv = dotenv()
    val dryRun = "--dry-run" in args
    val useCopy = "--copy" in args
    val replaceFiles = "--replace" in args
    val sourceDirectory = dotenv["SOURCE_DIRECTORY"]
    val targetDirectory = dotenv["TARGET_DIRECTORY"]
    val logger =
        Logger(
            loggerLevel = if ("--verbose" in args) LoggerLevel.VERBOSE else LoggerLevel.INFO,
        )
    val showConfig = loadConfig()
    val showRepository = ShowRepository(showConfig.shows)

    println("Episode Renamer")
    println("Dry-Run: $dryRun")
    println("Copy: $useCopy")
    println("Source Directory: $sourceDirectory")
    println("Target Directory: $targetDirectory")

    val matchers =
        listOf(
            GermanVerboseMatcher(showRepository),
            PlexMatcher(showRepository),
            JDownloaderMatcher(showRepository),
        )

    val fileParser =
        FileParser(
            dryRun = dryRun,
            matchers = matchers,
            formatter = EpisodeFormatter(),
            mode = if (useCopy) Mode.COPY else Mode.MOVE,
            replaceFiles = replaceFiles,
            logger = logger,
            showRepository = showRepository,
        )
    fileParser.renameFilesInDirectory(
        sourceDirectoryPath = sourceDirectory,
        targetDirectoryPath = targetDirectory,
    )
}

fun loadConfig(): ConfigShows {
//    val constructor = Constructor(ConfigShows::class.java, LoaderOptions())
//    val showDescription = TypeDescription(ConfigShows::class.java)
//    constructor.addTypeDescription(showDescription)
    val yaml = Yaml()
    val showConfigFile = File("config/shows.yml")
    val config: Map<String, Any> = yaml.load(showConfigFile.inputStream())

    val showsList = config["shows"]
    if (showsList !is List<*>) {
        throw IllegalArgumentException("Invalid config: 'shows' is not a list")
    }

    val shows =
        showsList.map { showMap ->
            if (showMap !is Map<*, *>) {
                throw IllegalArgumentException("Invalid config: show is not a map")
            }

            val name = showMap["name"]
            if (name !is String) {
                throw IllegalArgumentException("Invalid config: 'name' is not a string")
            }

            val aliases = showMap["aliases"]
            val aliasStrings =
                if (aliases is List<*>) {
                    val aliasStrings = aliases.filterIsInstance<String>()
                    if (aliasStrings.size != aliases.size) {
                        throw IllegalArgumentException("Invalid config: 'aliases' contains non-string values")
                    }
                    aliasStrings
                } else {
                    listOf<String>()
                }

            Show(name, aliasStrings)
        }

    return ConfigShows(shows)
}
