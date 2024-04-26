package com.paneon.episoderenamer.parser.formatter

import com.paneon.episoderenamer.episode.EpisodeFile

class EpisodeFormatter : Formatter {
    override fun format(matchedEpisode: EpisodeFile): String {
        val season = matchedEpisode.season.toString().padStart(2, '0')
        val episode = matchedEpisode.episode.toString().padStart(2, '0')

        return "${matchedEpisode.show.name} - S${season}E$episode.mp4"
    }
}
