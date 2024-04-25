package com.paneon.episoderenamer.rename.matcher

import com.paneon.episoderenamer.shows.Show
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GermanVerboseMatcherTest {
    private val shows = listOf(Show("My Show"), Show("My other Show"))
    private val matcher = GermanVerboseMatcher(shows)

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
            assertTrue(matcher.matches(filename), "Filename should match: $filename")
        }
    }

    @Test
    fun testExtractWithSpaces() {
        val filename = "Episode 6 Staffel 1 von My Show | SomePage.mp4"
        val actual = matcher.extract(filename)

        assertEquals("My Show", actual.show, "Show does not match expected")
        assertEquals(6, actual.episode, "Episode does not match expected")
        assertEquals(1, actual.season, "Season does not match expected")
    }

    @Test
    fun testExtractWithUnderscores() {
        val filename = "Episode_6_Staffel_1_von_My_Show_something-something.encoding.mp4"
        val actual = matcher.extract(filename)

        assertEquals("My Show", actual.show, "Show does not match expected")
        assertEquals(6, actual.episode, "Episode does not match expected")
        assertEquals(1, actual.season, "Season does not match expected")
    }
}
