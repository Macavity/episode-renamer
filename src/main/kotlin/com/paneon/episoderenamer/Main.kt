package com.paneon.episoderenamer

import com.paneon.episoderenamer.rename.FileRenamer
import com.paneon.episoderenamer.rename.Mode
import com.paneon.episoderenamer.rename.formatter.EpisodeFormatter
import com.paneon.episoderenamer.rename.matcher.GermanVerboseMatcher
import com.paneon.episoderenamer.rename.matcher.JDownloaderMatcher
import com.paneon.episoderenamer.rename.matcher.PlexMatcher
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
    val logger = Logger(
        loggerLevel = if("--verbose" in args) LoggerLevel.VERBOSE else LoggerLevel.INFO,
    )
    val shows = ShowRepository().findAll()

    println("Episode Renamer")
    println("Dry-Run: $dryRun")
    println("Copy: $useCopy")
    println("Source Directory: $sourceDirectory")
    println("Target Directory: $targetDirectory")

    val matchers = listOf(
        GermanVerboseMatcher(shows),
        PlexMatcher(shows),
        JDownloaderMatcher()
    )

    val fileRenamer = FileRenamer(
        dryRun = dryRun,
        matchers = matchers,
        formatter = EpisodeFormatter(),
        mode = if(useCopy) Mode.COPY else Mode.MOVE,
        replaceFiles = replaceFiles,
        logger = logger
    )
    fileRenamer.renameFilesInDirectory(sourceDirectoryPath = sourceDirectory, targetDirectoryPath = targetDirectory)

    //val fileMover = com.paneon.episoderenamer.move.FileMover(dryRun, logger)
}
