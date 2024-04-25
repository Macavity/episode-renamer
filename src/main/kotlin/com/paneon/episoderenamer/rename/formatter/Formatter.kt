package com.paneon.episoderenamer.rename.formatter

import com.paneon.episoderenamer.rename.matcher.MatchedEpisode

interface Formatter {
    fun format(matchedEpisode: MatchedEpisode): String
}