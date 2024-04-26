package com.paneon.episoderenamer.parser.matcher

interface FileNameMatcher {
    fun matches(sourceFileName: String): EpisodeMatch?

    fun sanitize(filename: String): String
}
