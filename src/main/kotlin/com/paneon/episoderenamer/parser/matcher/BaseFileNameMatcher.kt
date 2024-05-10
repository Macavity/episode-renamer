package com.paneon.episoderenamer.parser.matcher

import com.paneon.episoderenamer.shows.ShowRepository

abstract class BaseFileNameMatcher(private val showRepository: ShowRepository) : FileNameMatcher {
    abstract val regex: Regex

    override fun matches(sourceFileName: String): EpisodeMatch? {
        return run {
            val sanitizedFileName = sanitize(sourceFileName)
            val matchResult = regex.find(sanitizedFileName) ?: return@run null

            val episode = matchResult.groups["episode"]?.value ?: return@run null
            val season = matchResult.groups["season"]?.value ?: return@run null
            val showName = matchResult.groups["show"]?.value ?: return@run null
            val show = showRepository.firstOrNull(showName) ?: return@run null

            EpisodeMatch(show.name, season, episode)
        }
    }
}
