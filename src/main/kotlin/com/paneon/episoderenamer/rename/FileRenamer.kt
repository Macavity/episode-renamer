package com.paneon.episoderenamer.rename

import com.paneon.episoderenamer.rename.formatter.Formatter
import com.paneon.episoderenamer.rename.matcher.FileNameMatcher
import com.paneon.episoderenamer.rename.matcher.MatchedEpisode
import com.paneon.episoderenamer.util.Logger
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.system.exitProcess

enum class Mode {
    MOVE,
    COPY,
}

class FileRenamer(
    private val dryRun: Boolean,
    private val matchers: List<FileNameMatcher>,
    private val formatter: Formatter,
    private val mode: Mode,
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
            processFile(file = file, targetDirectory = targetDirectory)
        }
    }

    private fun processFile(
        file: File,
        targetDirectory: File,
    ) {
        val matchedEpisode = matchEpisode(file.name)

        if (matchedEpisode == null) {
            logger.skipBlock(file.name)
            return
        }

        val showDirectory = File(targetDirectory, matchedEpisode.show.replace("/", "_")) // Safe directory name
        ensureDirectoryExists(showDirectory)

        val newName = formatter.format(matchedEpisode)
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

    private fun matchEpisode(fileName: String): MatchedEpisode? {
        val matcher = matchers.firstOrNull { it.matches(fileName) }
        if (matcher != null) {
            logger.debug("Matcher", matcher.javaClass.toString())
        }

        return matcher?.extract(fileName)
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
