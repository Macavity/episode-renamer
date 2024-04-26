package com.paneon.episoderenamer.parser.matcher

import com.paneon.episoderenamer.shows.Show
import com.paneon.episoderenamer.shows.ShowRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class JDownloaderMatcherTest {
    private val shows = listOf(Show("My Show"), Show("Another Show Here"), Show("Dr. Episode", listOf("Dr Episode")))
    private val matcher = JDownloaderMatcher(ShowRepository(shows))

    @Test
    fun matches() {
        val filenames =
            listOf(
                "My.Show.S01E07.German.123a.AAC.worD.x264.mp4",
                "Another.Show.Here.S01E07.German.4050c.AAC.Some.thing.mp4",
            )

        filenames.forEach { filename ->
            assertTrue(matcher.matches(filename) != null, "Filename should match: $filename")
        }
    }

    @Test
    fun extractEpisodeData() {
        val filename = "My.Show.S01E07.German.123a.AAC.worD.x264.mp4"
        val actual = matcher.matches(filename) ?: fail()

        assertEquals("My Show", actual.show, "Show does not match expected")
        assertEquals("07", actual.episode, "Episode does not match expected")
        assertEquals("01", actual.season, "Season does not match expected")
    }

    @Test
    fun aliasesEpisode() {
        val filename = "Dr.Episode.S01E45.123a.AAC.worD.x264.mp4"
        val matches = matcher.matches(filename)
        val actual = matcher.matches(filename) ?: fail()

        assertNotNull(matches)
        assertEquals("Dr. Episode", actual.show)
    }
}
