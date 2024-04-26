package com.paneon.episoderenamer.shows

class ShowRepository(private val shows: List<Show>) {
    companion object {
        fun findAll(): List<Show> {
            return listOf(
                Show("Air Gear"),
                // Re-Add when configuration allows to target different directories
                // Show("Air Master"),
                Show("Arifureta From Commonplace to World's Strongest"),
                Show("A Certain Scientific Railgun"),
                Show("A Certain Magical Index"),
                Show("A Returner's Magic Should Be Special"),
                Show("Beyblade"),
                Show("Berserk of Gluttony"),
                Show("Bofuri I Don't Want to Get Hurt, so I'll Max Out My Defense"),
                Show("Charlotte"),
                Show("Chained Soldier"),
                Show("Danmachi Is It Wrong to Try to Pick Up Girls in a Dungeon"),
                Show("Detektiv Conan"),
                Show("Digimon Tamers"),
                Show("Digimon Frontier"),
                Show("Dr. Stone"),
                Show("Dr. Slump", listOf("Dr Slump")),
                Show("Elfen Lied"),
                Show("Frieren Beyond Journey's End"),
                Show("Fullmetal Alchemist Brotherhood"),
                Show("hack SIGN"),
                Show("Highschool DxD"),
                Show("Infinite Dendrogram"),
                Show("Immoral Guild"),
                Show("Log Horizon"),
                Show("Kickers"),
                Show("Mobile Suit Gundam Wing"),
                Show("Mushoku Tensei Jobless Reincarnation"),
                Show("My Unique Skill Makes Me OP even at Level 1"),
                Show("My Hero Academia"),
                Show("My Instant Death Ability Is So Overpowered, No One in This Other World Stands a Chance Against Me!"),
                Show("Neon Genesis Evangelion"),
                Show("Noir"),
                Show("Shangri-La Frontier"),
                Show("Solo Leveling"),
                Show("Steins;Gate"),
                Show("Sword Art Online"),
                Show("Tokyo Revengers"),
                Show("The Detective Is Already Dead"),
                Show("The Eminence in Shadow"),
                Show("The Hidden Dungeon Only I Can Enter"),
                Show("The King's Avatar"),
                Show("The Rising of the Shield Hero"),
                Show("The Strongest Tank's Labyrinth"),
                Show("The Wrong Way to Use Healing"),
                Show("Undefeated Bahamut Chronicle"),
                Show("Villainess Level 99 I May Be the Hidden Boss but I'm Not the Demon Lord"),
            )
        }
    }

    fun findAll(): List<Show> {
        return shows
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
