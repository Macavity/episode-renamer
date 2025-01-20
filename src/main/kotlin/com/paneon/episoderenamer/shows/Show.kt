package com.paneon.episoderenamer.shows

data class Show(
    val name: String,
    val aliases: List<String> = listOf(),
    val seasonDirectories: Boolean = false,
)
