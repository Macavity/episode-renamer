package com.paneon.episoderenamer.rename.formatter

import com.paneon.episoderenamer.rename.matcher.MatchedEpisode

class EpisodeFormatter : Formatter {
    override fun format(matchedEpisode: MatchedEpisode): String {
        val season = matchedEpisode.season.toString().padStart(2, '0')
        val episode = matchedEpisode.episode.toString().padStart(2, '0')

        return "${matchedEpisode.show} - S${season}E${episode}.mp4"
    }
}