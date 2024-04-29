@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.paneon.episoderenamer.mover

import com.paneon.episoderenamer.episode.EpisodeFile
import com.paneon.episoderenamer.shows.Show
import com.paneon.episoderenamer.util.Logger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
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
        logger = mock()
        File(targetDirectory).deleteRecursively()
        if (!File(dummyEpisodeFilePath).exists()) {
            File(blankFile).copyTo(File(dummyEpisodeFilePath))
        }
    }

    @Test
    fun `with dryRun, does not call moveFile or copyFile but logs info block`() {
        val fileMover = spy(FileMover(targetDirectory, dryRun = true, replaceFiles = false, logger = logger))
        fileMover.processFile(episodeFile, false)
        verify(logger).infoBlock(any(), any(), any(), any())
        verify(fileMover, never()).moveFile(any(), any())
        verify(fileMover, never()).copyFile(any(), any())
    }

    @Test
    fun `with replaceFiles=false, skips existing file`() {
        val fileMover = spy(FileMover(targetDirectory, dryRun = false, replaceFiles = false, logger = logger))
        doReturn(true).`when`(fileMover).checkFileExistence(any(), any())
        fileMover.processFile(episodeFile, false)
        verify(logger).skipBlock(any(), any())
        verify(fileMover, never()).moveFile(any(), any())
        verify(fileMover, never()).copyFile(any(), any())
    }

    @Test
    fun `can copy files and logs info block`() {
        val fileMover = spy(FileMover(targetDirectory, dryRun = false, replaceFiles = false, logger = logger))
        fileMover.processFile(episodeFile, true)
        verify(logger).infoBlock(any(), any(), any(), any())
        verify(fileMover).copyFile(any(), any())
        verify(fileMover, never()).moveFile(any(), any())
    }

    @Test
    fun `can move files and logs info block`() {
        val fileMover = spy(FileMover(targetDirectory, false, false, logger))

        fileMover.processFile(episodeFile, false)
        verify(logger).infoBlock(any(), any(), any(), any())
        verify(fileMover).moveFile(any(), any())
        verify(fileMover, never()).copyFile(any(), any())
    }
}
