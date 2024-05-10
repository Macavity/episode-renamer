package com.paneon.episoderenamer.parser.formatter

import com.paneon.episoderenamer.episode.EpisodeFile

interface Formatter {
    fun format(matchedEpisode: EpisodeFile): String
}
