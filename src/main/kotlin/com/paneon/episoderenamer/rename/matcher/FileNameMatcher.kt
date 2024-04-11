package com.paneon.episoderenamer.rename.matcher

import com.paneon.episoderenamer.shows.Show

interface FileNameMatcher {
    fun matches(filename: String): Boolean
    fun extract(filename: String): MatchedEpisode
}