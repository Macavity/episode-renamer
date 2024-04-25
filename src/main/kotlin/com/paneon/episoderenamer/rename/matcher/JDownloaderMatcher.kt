package com.paneon.episoderenamer.rename.matcher

import com.paneon.episoderenamer.shows.Show

class JDownloaderMatcher(val shows: List<Show>): FileNameMatcher {
    private val regex = Regex("(.*?) S(\\d{2})E(\\d{2})")

    override fun matches(filename: String): Boolean {
        val sanitizedFilename = sanitize(filename)
        val match = regex.find(sanitizedFilename) ?: return false
        val (extractedName) = match.destructured

        val showName = resolveShowName(extractedName)

        return showName != null
    }

    override fun extract(filename: String): MatchedEpisode? {
        val sanitizedFilename = sanitize(filename)
        val matchResult = regex.find(sanitizedFilename) ?: throw IllegalArgumentException("Filename does not match: $filename")
        val (extractedName, season, episode) = matchResult.destructured

        val showName = resolveShowName(extractedName) ?: return null

        return MatchedEpisode(show = showName, season = season.toInt(), episode = episode.toInt())
    }

    private fun resolveShowName(input: String): String? {
        val inputName = input.trim()

        for(show in shows){
            if(show.name.equals(inputName, ignoreCase = true) || show.aliases.any { alias -> alias.equals(inputName, ignoreCase = true) }){
                return show.name
            }
        }

        return null
    }

    private fun sanitize(filename: String): String {
        return filename.replace('.', ' ')
    }
}
