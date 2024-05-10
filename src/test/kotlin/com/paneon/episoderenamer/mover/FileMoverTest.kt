@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.paneon.episoderenamer.mover

import com.paneon.episoderenamer.episode.EpisodeFile
import com.paneon.episoderenamer.shows.Show
import com.paneon.episoderenamer.util.Logger
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class FileMoverTest {
    private lateinit var logger: Logger
    private val show = Show("showName", emptyList())
    private val episodeFile = EpisodeFile("src/test/resources/My Show S01E04.mp4", show, 1, 1)
    private val targetDirectory = "src/test/resources/target/"
    private val blankFile = "src/test/resources/blank.txt"
    private val dummyEpisodeFilePath = "src/test/resources/My Show S01E04.mp4"

    @BeforeEach
    fun setup() {
        logger = mockk(relaxed = true)
        File(targetDirectory).deleteRecursively()
        if (!File(dummyEpisodeFilePath).exists()) {
            File(blankFile).copyTo(File(dummyEpisodeFilePath))
        }
    }

    @Test
    fun `with dryRun, does not call moveFile or copyFile`() {
        val fileMover = spyk(FileMover(targetDirectory, dryRun = true, replaceFiles = false, logger = logger))
        fileMover.processFile(episodeFile, false)

        verify(exactly = 0) { fileMover.moveFile(any(), any()) }
        verify(exactly = 0) { fileMover.copyFile(any(), any()) }
    }

    @Test
    fun `with replaceFiles=false, skips existing file`() {
        // Arrange
        val fileMover = spyk(FileMover(targetDirectory, dryRun = false, replaceFiles = false, logger = logger))
        val existingFile = EpisodeFile("src/test/resources/ExistingFile.mp4", show, 1, 1)
        every { fileMover.checkFileExistence(any(), any()) } returns true

        // Act
        fileMover.processFile(existingFile, false)

        // Assert
        verify(exactly = 1) { fileMover.checkFileExistence(any(), any()) }
        verify(exactly = 0) { fileMover.moveFile(any(), any()) }
        verify(exactly = 0) { fileMover.copyFile(any(), any()) }
    }

    @Test
    fun `can copy files`() {
        val fileMover = spyk(FileMover(targetDirectory, dryRun = false, replaceFiles = false, logger = logger))

        fileMover.processFile(episodeFile, true)

        verify(exactly = 1) { fileMover.copyFile(any(), any()) }
        verify(exactly = 0) { fileMover.moveFile(any(), any()) }
    }

    @Test
    fun `can move files`() {
        val fileMover = spyk(FileMover(targetDirectory, false, false, logger))

        fileMover.processFile(episodeFile, false)
        verify(exactly = 1) { fileMover.moveFile(any(), any()) }
        verify(exactly = 0) { fileMover.copyFile(any(), any()) }
    }
}
