package com.paneon.episoderenamer.rename.matcher

import com.paneon.episoderenamer.shows.Show

class JDownloaderMatcher(private val shows: List<Show>): FileNameMatcher {
    private val regex = Regex("(.*?) S(\\d{2})E(\\d{2})")

    override fun matches(filename: String): Boolean {
        val sanitizedFilename = filename.replace('.', ' ')
        return regex.containsMatchIn(sanitizedFilename) // Use containsMatchIn for partial matches
    }

    override fun extract(filename: String): MatchedEpisode {
        val sanitizedFilename = filename.replace('.', ' ')
        val matchResult = regex.find(sanitizedFilename) ?: throw IllegalArgumentException("Filename does not match: $filename")
        val (showName, season, episode) = matchResult.destructured
        // Transform the show name back if needed, or use the sanitized version directly
        return MatchedEpisode(show = showName.trim(), season = season.toInt(), episode = episode.toInt())
    }
}
