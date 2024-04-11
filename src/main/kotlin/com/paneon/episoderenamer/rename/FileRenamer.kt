package com.paneon.episoderenamer.rename

import com.paneon.episoderenamer.rename.formatter.Formatter
import com.paneon.episoderenamer.rename.matcher.FileNameMatcher
import com.paneon.episoderenamer.util.Logger
import java.io.File

class FileRenamer(
    private val dryRun: Boolean,
    private val matchers: List<FileNameMatcher>,
    private val formatter: Formatter,
    private val logger: Logger
) {
    fun renameFilesInDirectory(sourceDirectoryPath: String) {
        val sourceDirectory = File(sourceDirectoryPath)

        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory) {
            logger.error("Directory does not exist or is not a directory")
            return
        }

        sourceDirectory.listFiles { file ->
            file.isFile && file.extension == "mp4"
        }?.forEach { file ->
            val newName = reformattedFileName(file.name)

            if (file.name != newName) {
                if (!dryRun) {
                    file.renameTo(File(sourceDirectory, newName))
                }
                logger.infoBlock(originalName = file.name, newName = newName)
            } else {
                logger.skipBlock(file.name)
            }
        }
    }

    private fun reformattedFileName(fileName: String): String {
        val matcher = matchers.firstOrNull { it.matches(fileName) } ?: return fileName
        val component = matcher.extract(fileName)

        return formatter.format(component)
    }
}

