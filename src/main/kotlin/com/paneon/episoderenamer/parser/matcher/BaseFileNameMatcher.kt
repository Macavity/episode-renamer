package com.paneon.episoderenamer.parser.matcher

import com.paneon.episoderenamer.shows.ShowRepository

abstract class BaseFileNameMatcher(private val showRepository: ShowRepository) : FileNameMatcher {
    abstract val regex: Regex

    override fun matches(sourceFileName: String): EpisodeMatch? {
        val sanitizedFileName = sanitize(sourceFileName)
        val matchResult = regex.find(sanitizedFileName) ?: return null

        val episode = matchResult.groups["episode"]?.value ?: return null
        val season = matchResult.groups["season"]?.value ?: return null
        val showName = matchResult.groups["show"]?.value ?: return null
        val show = showRepository.firstOrNull(showName) ?: return null

        return EpisodeMatch(show.name, season, episode)
    }
}
