package com.paneon.episoderenamer.rename

import com.paneon.episoderenamer.rename.formatter.Formatter
import com.paneon.episoderenamer.rename.matcher.FileNameMatcher
import com.paneon.episoderenamer.rename.matcher.MatchedEpisode
import com.paneon.episoderenamer.util.Logger
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

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

        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory) {
            logger.error("Directory does not exist or is not a directory")
            return
        }

        if (!targetDirectory.exists()) {
            if (!dryRun) {
                targetDirectory.mkdirs() // Ensure the target directory exists
            }
            logger.info("Created target directory at $targetDirectoryPath")
        }

        sourceDirectory.listFiles { file -> file.isFile && file.extension == "mp4" }?.forEach { file ->
            val matchedEpisode = matchEpisode(file.name)

            if (matchedEpisode == null) {
                logger.skipBlock(file.name)
                return@forEach
            }

            val showDirectory = File(targetDirectory, matchedEpisode.show.replace("/", "_")) // Safe directory name
            if (!showDirectory.exists() && !dryRun) {
                showDirectory.mkdirs()
                logger.info("Created directory for show: $showDirectory")
            }

            val newName = formatter.format(matchedEpisode)
            val targetFile = File(showDirectory, newName)

            if (targetFile.exists() && !replaceFiles) {
                println("File ${targetFile.path} exists already. Do you want to replace it? [y/n]: ")
                val response = readlnOrNull()
                if (response?.lowercase() != "y" && response?.lowercase() != "yes") {
                    logger.skipBlock(originalName = file.name, "Skipped because file exists on destination")
                    return@forEach
                }
            }

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
    }

    private fun matchEpisode(fileName: String): MatchedEpisode? {
        val matcher = matchers.firstOrNull { it.matches(fileName) }
        if (matcher != null) {
            logger.debug("Matcher", matcher.javaClass.toString())
        }

        return matcher?.extract(fileName)
    }
}
