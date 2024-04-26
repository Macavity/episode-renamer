package com.paneon.episoderenamer.parser.matcher

import com.paneon.episoderenamer.shows.ShowRepository

class GermanVerboseMatcher(showRepository: ShowRepository) : BaseFileNameMatcher(showRepository) {
    private val showNamesPatternPart = showRepository.findAll().joinToString("|") { Regex.escape(it.name) }

    override val regex: Regex by lazy {
        Regex("Episode[ _]+(?<episode>\\d+)[ _]+Staffel[ _]+(?<season>\\d+)[ _]+von[ _]+(?<show>$showNamesPatternPart)[ _]+.*")
    }

    override fun sanitize(filename: String): String {
        return filename.replace('_', ' ')
    }
}
