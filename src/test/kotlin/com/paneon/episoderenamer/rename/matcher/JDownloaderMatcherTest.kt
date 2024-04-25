package com.paneon.episoderenamer.rename.matcher

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class JDownloaderMatcherTest {
    //private val shows = listOf(Show("My Show"), Show("Another Show Here"))
    private val matcher = JDownloaderMatcher()

    @Test
    fun matches() {
        val filenames = listOf(
            "My.Show.S01E07.German.123a.AAC.worD.x264.mp4",
            "Another.Show.Here.S01E07.German.4050c.AAC.Some.thing.mp4",
        )

        filenames.forEach { filename ->
            assertTrue(matcher.matches(filename), "Filename should match: $filename")
        }
    }

    @Test
    fun extractEpisodeData() {
        val filename = "My.Show.S01E07.German.123a.AAC.worD.x264.mp4"
        val actual = matcher.extract(filename)

        assertEquals("My Show", actual.show, "Show does not match expected")
        assertEquals(7, actual.episode, "Episode does not match expected")
        assertEquals(1, actual.season, "Season does not match expected")
    }
}