package com.paneon.episoderenamer

import com.paneon.episoderenamer.rename.FileRenamer
import com.paneon.episoderenamer.rename.formatter.EpisodeFormatter
import com.paneon.episoderenamer.rename.matcher.GermanVerboseMatcher
import com.paneon.episoderenamer.rename.matcher.JDownloaderMatcher
import com.paneon.episoderenamer.shows.ShowRepository
import com.paneon.episoderenamer.util.Logger
import io.github.cdimascio.dotenv.dotenv

fun main(args: Array<String>) {
    val dotenv = dotenv()
    val dryRun = "--dry-run" in args
    val logger = Logger(dryRun)
    val sourceDirectory = dotenv["SOURCE_DIRECTORY"]
    val targetDirectory = dotenv["TARGET_DIRECTORY"]
    val shows = ShowRepository().findAll()

    println("Episode Renamer")
    println("Dry-Run: $dryRun")
    println("Source Directory: $sourceDirectory")
    println("Target Directory: $targetDirectory")

    val matchers = listOf(GermanVerboseMatcher(shows), JDownloaderMatcher(shows))

    val fileRenamer = FileRenamer(dryRun = dryRun, matchers = matchers, formatter = EpisodeFormatter(), logger = logger)
    fileRenamer.renameFilesInDirectory(sourceDirectoryPath = sourceDirectory)

    //val fileMover = com.paneon.episoderenamer.move.FileMover(dryRun, logger)
}
