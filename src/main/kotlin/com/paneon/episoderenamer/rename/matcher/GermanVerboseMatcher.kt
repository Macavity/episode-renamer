package com.paneon.episoderenamer.rename.matcher

import com.paneon.episoderenamer.shows.Show

class GermanVerboseMatcher(shows: List<Show>) : FileNameMatcher {
    private val showNamesPatternPart = shows.joinToString("|") { Regex.escape(it.name) }

    // private val regex = Regex("Episode[ _]+(\\d+)[ _]+Staffel[ _]+(\\d+)[ _]+von[ _]+(.*?)[ _]+\\|")
    // Construct the regex pattern dynamically
    private val regex: Regex by lazy {
        Regex("Episode[ _]+(\\d+)[ _]+Staffel[ _]+(\\d+)[ _]+von[ _]+($showNamesPatternPart)[ _]+.*")
    }

    override fun matches(filename: String): Boolean {
        val sanitizedFilename = sanitize(filename)
        return regex.containsMatchIn(sanitizedFilename)
    }

    override fun extract(filename: String): MatchedEpisode {
        val sanitizedFilename = sanitize(filename)
        val matchResult = regex.find(sanitizedFilename)
            ?: throw IllegalArgumentException("Filename does not match: $filename")
        val (episode, season, showName) = matchResult.destructured
        return MatchedEpisode(show = showName, season = season.toInt(), episode = episode.toInt())
    }

    private fun sanitize(filename: String): String {
        return filename.replace('_', ' ')
    }
}