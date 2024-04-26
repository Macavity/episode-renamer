package com.paneon.episoderenamer.parser

import com.paneon.episoderenamer.episode.createEpisodeFile
import com.paneon.episoderenamer.exception.MatcherNotFoundException
import com.paneon.episoderenamer.exception.ShowNotFoundException
import com.paneon.episoderenamer.exception.UnexpectedFileNameException
import com.paneon.episoderenamer.parser.formatter.Formatter
import com.paneon.episoderenamer.parser.matcher.FileNameMatcher
import com.paneon.episoderenamer.shows.ShowRepository
import com.paneon.episoderenamer.util.Logger
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.system.exitProcess

enum class Mode {
    MOVE,
    COPY,
}

class FileParser(
    private val dryRun: Boolean,
    private val matchers: List<FileNameMatcher>,
    private val formatter: Formatter,
    private val mode: Mode,
    private val showRepository: ShowRepository,
    private val replaceFiles: Boolean,
    private val logger: Logger,
) {
    fun renameFilesInDirectory(
        sourceDirectoryPath: String,
        targetDirectoryPath: String,
    ) {
        val sourceDirectory = File(sourceDirectoryPath)
        val targetDirectory = File(targetDirectoryPath)

        ensureDirectoriesExist(sourceDirectoryPath, targetDirectoryPath)

        sourceDirectory.listFiles { file -> file.isFile && file.extension == "mp4" }?.forEach { file ->
            try {
                processFile(file = file, targetDirectory = targetDirectory)
            } catch (exception: Exception) {
                logger.skipBlock(file.name, action = exception.message ?: "Unknown Error")
            }
        }
    }

    @Throws(UnexpectedFileNameException::class, ShowNotFoundException::class, MatcherNotFoundException::class)
    private fun processFile(
        file: File,
        targetDirectory: File,
    ) {
        val matcher = findFileNameMatcher(file)

        val episodeMatch = matcher.matches(file.name) ?: throw UnexpectedFileNameException(file.name)

        val show = showRepository.firstOrNull(episodeMatch.show) ?: throw ShowNotFoundException(episodeMatch.show)
        val episodeFile = createEpisodeFile(sourceFilePath = file.path, episodeMatch = episodeMatch, show = show)

        val showDirectory = File(targetDirectory, episodeFile.show.name.replace("/", "_")) // Safe directory name
        ensureDirectoryExists(showDirectory)

        val newName = formatter.format(episodeFile)
        val targetFile = File(showDirectory, newName)

        if (checkFileExistence(targetFile, file)) return

        val action =
            if (!dryRun) {
                "✅ ${mode.name} with new name"
            } else {
                "⏸\uFE0F ${mode.name} with new name (Dry Run)"
            }

        val performOperation: (File, File) -> Unit =
            when (mode) {
                Mode.COPY -> { src, dst ->
                    Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING)
                }

                Mode.MOVE -> { src, dst -> src.renameTo(dst) }
            }

        if (!dryRun) {
            performOperation(file, targetFile)
        }

        logger.infoBlock(
            originalName = file.name,
            newName = newName,
            targetName = targetFile.parent.toString(),
            action = action,
        )
    }

    private fun checkFileExistence(
        targetFile: File,
        file: File,
    ): Boolean {
        if (targetFile.exists() && !replaceFiles) {
            println("File ${targetFile.path} exists already. Do you want to replace it? [y/n]: ")
            val response = readlnOrNull()
            if (response?.lowercase() != "y" && response?.lowercase() != "yes") {
                logger.skipBlock(originalName = file.name, "Skipped because file exists on destination")
                return true
            }
        }
        return false
    }

    @Throws(MatcherNotFoundException::class)
    private fun findFileNameMatcher(sourceFile: File): FileNameMatcher {
        val fileName = sourceFile.name
        val matcher =
            matchers.firstOrNull { it.matches(fileName) != null } ?: throw MatcherNotFoundException(sourceFile.name)

        logger.debug("Matcher", matcher.javaClass.toString())

        return matcher
    }

    private fun ensureDirectoryExists(directory: File) {
        if (!directory.exists() && !dryRun) {
            directory.mkdirs()
            logger.info("Created directory ${directory.path}")
        }
    }

    private fun ensureDirectoriesExist(
        sourceDirectoryPath: String,
        targetDirectoryPath: String,
    ) {
        val sourceDirectory = File(sourceDirectoryPath)
        val targetDirectory = File(targetDirectoryPath)

        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory) {
            logger.error("Directory does not exist or is not a directory")
            exitProcess(0)
        }

        if (!targetDirectory.exists() && !dryRun) {
            ensureDirectoryExists(targetDirectory)
        }
    }
}
