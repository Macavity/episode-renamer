package com.paneon.episoderenamer.rename.formatter

import com.paneon.episoderenamer.rename.matcher.MatchedEpisode
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class EpisodeFormatterTest {
    private val formatter = EpisodeFormatter()
    @Test
    fun format() {
        val episode = MatchedEpisode("Air Master", 1, 6)
        val expected = "Air Master - S01E06"
        val actual = formatter.format(episode)

        assertEquals(expected, actual, "Format does not match expected")
    }
}