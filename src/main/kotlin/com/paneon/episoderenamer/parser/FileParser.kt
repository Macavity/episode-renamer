package com.paneon.episoderenamer.parser

import com.paneon.episoderenamer.episode.EpisodeFile
import com.paneon.episoderenamer.episode.createEpisodeFile
import com.paneon.episoderenamer.parser.matcher.FileNameMatcher
import com.paneon.episoderenamer.shows.ShowRepository
import com.paneon.episoderenamer.util.Logger
import com.paneon.episoderenamer.util.LoggerAction
import com.paneon.episoderenamer.util.ensureDirectoryExists
import java.io.File

enum class Mode {
    MOVE,
    COPY,
}

class FileParser(
    private val matchers: List<FileNameMatcher>,
    private val showRepository: ShowRepository,
    private val logger: Logger,
) {
    fun renameFilesInDirectory(sourceDirectoryPath: String): List<EpisodeFile> {
        val sourceDirectory = File(sourceDirectoryPath)
        ensureDirectoryExists(sourceDirectory)

        return sourceDirectory.listFiles { file -> file.isFile && file.extension == "mp4" }?.mapNotNull { file ->
            processFile(file = file)
        } ?: emptyList()
    }

    private fun processFile(file: File): EpisodeFile? {
        return run {
            val matcher = findFileNameMatcher(file)
            if (matcher == null) {
                logger.skipBlock(file.name, LoggerAction.SKIP_MATCHER_NOT_FOUND)
                return@run null
            }

            val episodeMatch = matcher.matches(file.name)
            if (episodeMatch == null) {
                logger.skipBlock(file.name, LoggerAction.SKIP)
                return@run null
            }

            val show = showRepository.firstOrNull(episodeMatch.show)
            if (show == null) {
                logger.skipBlock(file.name, LoggerAction.SKIP_SHOW_NOT_FOUND)
                return@run null
            }

            createEpisodeFile(sourceFilePath = file.path, episodeMatch = episodeMatch, show = show)
        }
    }

    private fun findFileNameMatcher(sourceFile: File): FileNameMatcher? {
        val fileName = sourceFile.name
        val matcher = matchers.firstOrNull { it.matches(fileName) != null }

        if (matcher != null) {
            logger.debug("Matcher", matcher.javaClass.toString())
        }

        return matcher
    }
}
