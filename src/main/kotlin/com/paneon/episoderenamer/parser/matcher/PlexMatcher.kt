package com.paneon.episoderenamer.parser.matcher

import com.paneon.episoderenamer.shows.ShowRepository

class PlexMatcher(showRepository: ShowRepository) : BaseFileNameMatcher(showRepository) {
    private val showNamesPatternPart = ShowRepository.findAll().joinToString("|") { Regex.escape(it.name) }
    override val regex: Regex by lazy {
        Regex("(?<show>$showNamesPatternPart) - S(?<season>\\d{2})E(?<episode>\\d{2}).mp4")
    }

    override fun sanitize(filename: String): String {
        return filename.replace('.', ' ')
    }
}
