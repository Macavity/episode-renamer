package com.paneon.episoderenamer.episode

import com.paneon.episoderenamer.exception.ShowNotFoundException
import com.paneon.episoderenamer.parser.matcher.EpisodeMatch
import com.paneon.episoderenamer.shows.Show

@Throws(ShowNotFoundException::class)
fun createEpisodeFile(
    sourceFilePath: String,
    episodeMatch: EpisodeMatch,
    show: Show,
): EpisodeFile {
    return EpisodeFile(
        sourceFilePath = sourceFilePath,
        show = show,
        season = episodeMatch.season.toInt(),
        episode = episodeMatch.episode.toInt(),
    )
}
