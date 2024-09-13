package com.paneon.episoderenamer.mover

import com.paneon.episoderenamer.episode.EpisodeFile
import com.paneon.episoderenamer.parser.formatter.EpisodeFormatter
import com.paneon.episoderenamer.util.Logger
import com.paneon.episoderenamer.util.LoggerAction
import com.paneon.episoderenamer.util.ensureDirectoryExists
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class FileMover(
    private val targetDirectoryPath: String,
    private val dryRun: Boolean,
    private val replaceFiles: Boolean,
    private val logger: Logger,
) {
    private fun prepareDirectory(showDirectory: File) {
        ensureDirectoryExists(showDirectory)
    }

    fun processFile(
        episodeFile: EpisodeFile,
        useCopy: Boolean,
    ) {
        val sourceFile = File(episodeFile.sourceFilePath)
        val targetDirectory = getTargetDirectory(episodeFile)

        val newName = EpisodeFormatter().format(episodeFile)
        val targetFile = File(targetDirectory, newName)

        prepareDirectory(targetDirectory)

        if (checkFileExistence(targetFile = targetFile, replaceFiles = replaceFiles)) {
            logger.skipBlock(episodeFile.sourceFilePath, LoggerAction.SKIP_FILE_EXISTS)
            return
        }

        val action = if (useCopy) LoggerAction.COPY else LoggerAction.MOVE

        if (!dryRun) {
            if (useCopy) {
                copyFile(sourceFile, targetFile)
            } else {
                moveFile(sourceFile, targetFile)
            }
        }

        logger.infoBlock(
            originalName = sourceFile.name,
            newName = newName,
            targetName = targetFile.parent.toString(),
            action = action,
        )
    }

    fun checkFileExistence(
        targetFile: File,
        replaceFiles: Boolean,
    ): Boolean {
        if (targetFile.exists() && !replaceFiles) {
            println("File ${targetFile.path} exists already. Do you want to replace it? [y/n]: ")
            val response = readlnOrNull()
            if (response?.lowercase() != "y" && response?.lowercase() != "yes") {
                return true
            }
        }
        return false
    }

    fun moveFile(
        sourceFile: File,
        targetFile: File,
    ) {
        sourceFile.renameTo(targetFile)
    }

    fun copyFile(
        sourceFile: File,
        targetFile: File,
    ) {
        Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }

    private fun getTargetDirectory(episodeFile: EpisodeFile): File {
        val showDirectory = File(targetDirectoryPath, episodeFile.show.name.replace("/", "_")) // Safe directory name

        return if (episodeFile.show.seasonDirectories)
            {
                File(showDirectory, "Season ${episodeFile.season.toString().padStart(2, '0')}")
            } else {
            showDirectory
        }
    }
}
