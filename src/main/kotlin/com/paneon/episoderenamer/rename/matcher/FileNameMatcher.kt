package com.paneon.episoderenamer.rename.matcher

interface FileNameMatcher {
    fun matches(filename: String): Boolean

    fun extract(filename: String): MatchedEpisode?
}
