package com.paneon.episoderenamer.shows

class ShowRepository(private val shows: List<Show>) {
    fun allShowNamesWithAliases(): List<String> {
        return shows.flatMap { show -> listOf(show.name) + show.aliases }
    }

    fun firstOrNull(name: String): Show? {
        val inputName = name.trim()

        for (show in shows) {
            if (show.name.equals(inputName, ignoreCase = true) ||
                show.aliases.any {
                        alias ->
                    alias.equals(inputName, ignoreCase = true)
                }
            ) {
                return show
            }
        }

        return null
    }
}
