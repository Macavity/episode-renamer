package com.paneon.episoderenamer.rename

import com.paneon.episoderenamer.rename.formatter.Formatter
import com.paneon.episoderenamer.rename.matcher.FileNameMatcher
import com.paneon.episoderenamer.rename.matcher.MatchedEpisode
import com.paneon.episoderenamer.util.Logger
import java.io.File

enum class Mode {
    MOVE, COPY
}


class FileRenamer(
    private val dryRun: Boolean,
    private val matchers: List<FileNameMatcher>,
    private val formatter: Formatter,
    mode: Mode,
    replaceFiles: Boolean,
    private val logger: Logger,
) {
    fun renameFilesInDirectory(sourceDirectoryPath: String, targetDirectoryPath: String) {
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

        sourceDirectory.listFiles { file ->
            file.isFile && file.extension == "mp4"
        }?.forEach { file ->
            val matchedEpisode = matchEpisode(file.name)

            if (matchedEpisode == null) {
                logger.skipBlock(file.name)
                return@forEach
            }

            val newName = formatter.format(matchedEpisode)

            val showDirectory = File(targetDirectory, matchedEpisode.show.replace("/", "_")) // Safe directory name
            if (!showDirectory.exists() && !dryRun) {
                showDirectory.mkdirs()
                logger.info("Created directory for show: $showDirectory")
            }

            val targetFile = File(showDirectory, newName)
            val action = if (!dryRun) "✅ Renamed and moved" else "⏸\uFE0F Dry Run"

            if (!dryRun) {
                file.renameTo(targetFile, )
            }

            logger.infoBlock(originalName = file.name, newName = newName, targetName = targetFile.parent.toString(), action=action)
        }
    }

    private fun matchEpisode(fileName: String): MatchedEpisode? {
        val matcher = matchers.firstOrNull { it.matches(fileName) }
        if(matcher != null){
            logger.debug("Matcher", matcher.javaClass.toString())
        }

        return matcher?.extract(fileName)
    }
}

