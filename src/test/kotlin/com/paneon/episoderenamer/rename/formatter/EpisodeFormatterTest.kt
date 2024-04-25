package com.paneon.episoderenamer.rename.formatter

import com.paneon.episoderenamer.rename.matcher.MatchedEpisode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EpisodeFormatterTest {
    private val formatter = EpisodeFormatter()

    @Test
    fun format() {
        val episode = MatchedEpisode("My Show", 1, 6)
        val expected = "My Show - S01E06.mp4"
        val actual = formatter.format(episode)

        assertEquals(expected, actual, "Format does not match expected")
    }
}
