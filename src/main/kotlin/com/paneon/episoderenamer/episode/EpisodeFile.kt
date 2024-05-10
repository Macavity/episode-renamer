package com.paneon.episoderenamer.episode

import com.paneon.episoderenamer.shows.Show

data class EpisodeFile(
    val sourceFilePath: String,
    val show: Show,
    val season: Int,
    val episode: Int,
)
