package com.paneon.episoderenamer

import com.paneon.episoderenamer.mover.FileMover
import com.paneon.episoderenamer.parser.FileParser
import com.paneon.episoderenamer.parser.matcher.GermanVerboseMatcher
import com.paneon.episoderenamer.parser.matcher.JDownloaderMatcher
import com.paneon.episoderenamer.parser.matcher.PlexMatcher
import com.paneon.episoderenamer.shows.ShowRepository
import com.paneon.episoderenamer.util.ConfigLoader
import com.paneon.episoderenamer.util.Logger
import com.paneon.episoderenamer.util.LoggerLevel
import io.github.cdimascio.dotenv.dotenv

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
    val config = ConfigLoader().loadConfig()
    val showRepository = ShowRepository(config.shows)

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
            matchers = matchers,
            logger = logger,
            showRepository = showRepository,
        )
    val fileMover =
        FileMover(
            targetDirectoryPath = targetDirectory,
            dryRun = dryRun,
            logger = logger,
            replaceFiles = replaceFiles,
        )
    val episodeFiles =
        fileParser.renameFilesInDirectory(
            sourceDirectoryPath = sourceDirectory,
        )

    episodeFiles.forEach { episodeFile ->
        fileMover.processFile(episodeFile = episodeFile, useCopy = useCopy)
    }
}

// fun loadConfig(): List<Show> {
//    val yaml = Yaml()
//    val showConfigFile = File("config/shows.yml")
//    val config: Map<String, Any> = yaml.load(showConfigFile.inputStream())
//
//    val showsList = config["shows"]
//    if (showsList !is List<*>) {
//        throw InvalidConfigException("Invalid config: 'shows' is not a list")
//    }
//
//    val shows =
//        showsList.map { showMap ->
//            if (showMap !is Map<*, *>) {
//                throw InvalidConfigException("Invalid config: show is not a map")
//            }
//
//            val name = showMap["name"]
//            if (name !is String) {
//                throw InvalidConfigException("Invalid config: 'name' is not a string")
//            }
//
//            val aliases = showMap["aliases"]
//            val aliasStrings =
//                if (aliases is List<*>) {
//                    val aliasStrings = aliases.filterIsInstance<String>()
//                    if (aliasStrings.size != aliases.size) {
//                        throw InvalidConfigException("Invalid config: 'aliases' contains non-string values")
//                    }
//                    aliasStrings
//                } else {
//                    listOf<String>()
//                }
//
//            Show(name, aliasStrings)
//        }
//
//    return shows
// }
