package com.paneon.episoderenamer.parser.matcher

import com.paneon.episoderenamer.shows.Show
import com.paneon.episoderenamer.shows.ShowRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class GermanVerboseMatcherTest {
    private val shows = listOf(Show("My Show"), Show("My other Show"))
    private val matcher = GermanVerboseMatcher(ShowRepository(shows))

    @Test
    fun testMatches() {
        val filenames =
            listOf(
                "Episode 6 Staffel 1 von My Show | SomePage.mp4",
                "Episode 6 Staffel 1 von My other Show &#124; SomePage.mp4",
                "Episode_21_Staffel_1_von_My_Show_|_SomePage.mp4",
                "Episode_6_Staffel_1_von_My_Show_something-something.encoding.mp4",
            )

        filenames.forEach { filename ->
            assertTrue(matcher.matches(filename) != null, "Filename should match: $filename")
        }
    }

    @Test
    fun testExtractWithSpaces() {
        val filename = "Episode 6 Staffel 1 von My Show | SomePage.mp4"
        val actual = matcher.matches(filename)

        assertNotNull(actual)

        assertEquals("My Show", actual.show, "Show does not match expected")
        assertEquals("6", actual.episode, "Episode does not match expected")
        assertEquals("1", actual.season, "Season does not match expected")
    }

    @Test
    fun testExtractWithUnderscores() {
        val filename = "Episode_6_Staffel_1_von_My_Show_something-something.encoding.mp4"
        val actual = matcher.matches(filename)

        assertNotNull(actual)
        assertEquals("My Show", actual.show, "Show does not match expected")
        assertEquals("6", actual.episode, "Episode does not match expected")
        assertEquals("1", actual.season, "Season does not match expected")
    }
}
