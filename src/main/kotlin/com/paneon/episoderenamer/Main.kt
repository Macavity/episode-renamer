package com.paneon.episoderenamer

import com.paneon.episoderenamer.parser.FileParser
import com.paneon.episoderenamer.parser.Mode
import com.paneon.episoderenamer.parser.formatter.EpisodeFormatter
import com.paneon.episoderenamer.parser.matcher.GermanVerboseMatcher
import com.paneon.episoderenamer.parser.matcher.JDownloaderMatcher
import com.paneon.episoderenamer.parser.matcher.PlexMatcher
import com.paneon.episoderenamer.shows.ShowRepository
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
    val showRepository = ShowRepository(ShowRepository.findAll())

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
