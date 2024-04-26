package com.paneon.episoderenamer.parser.formatter

import com.paneon.episoderenamer.episode.EpisodeFile
import com.paneon.episoderenamer.shows.Show
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShowEpisodeFileFormatterTest {
    private val formatter = EpisodeFormatter()

    @Test
    fun format() {
        val episode = EpisodeFile("./dummypath.mp4", Show("My Show"), 1, 6)
        val expected = "My Show - S01E06.mp4"
        val actual = formatter.format(episode)

        assertEquals(expected, actual, "Format does not match expected")
    }
}
