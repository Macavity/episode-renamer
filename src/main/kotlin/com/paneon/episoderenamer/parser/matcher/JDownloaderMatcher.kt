package com.paneon.episoderenamer.parser.matcher

import com.paneon.episoderenamer.shows.ShowRepository

class JDownloaderMatcher(showRepository: ShowRepository) : BaseFileNameMatcher(showRepository) {
    override val regex = Regex("(?<show>.*?) S(?<season>\\d{2})E(?<episode>\\d{2})")

    override fun sanitize(filename: String): String {
        return filename.replace('.', ' ')
    }
}
