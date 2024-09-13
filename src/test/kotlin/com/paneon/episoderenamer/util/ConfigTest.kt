package com.paneon.episoderenamer.util

import com.paneon.episoderenamer.shows.Show
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConfigTest {
    @Test
    fun `loadConfig should return correct Config`() {
        // Arrange
        val yamlContent =
            """
            shows:
            - name: Test Show
              aliases:
                - Alias 1
                - Alias 2
            """.trimIndent()

        val configLoader = mockk<ConfigLoader>()
        every { configLoader.getConfigFileContent() } returns yamlContent

        // Define a Config object to be returned by loadConfig()
        val expectedConfig =
            Config(
                shows =
                    listOf(
                        Show(
                            name = "Test Show",
                            aliases = listOf("Alias 1", "Alias 2"),
                            seasonDirectories = false,
                        ),
                    ),
            )
        every { configLoader.loadConfig() } returns expectedConfig

        // Act
        val config = configLoader.loadConfig()

        // Assert
        assertEquals(expectedConfig, config)
    }

    @Test
    fun `loadConfig should return correct Config with seasonDirectories`() {
        // Arrange
        val yamlContent =
            """
            shows:
            - name: Test Show
              seasonDirectories: true
              aliases:
                - Alias 1
                - Alias 2
            """.trimIndent()

        val configLoader = mockk<ConfigLoader>()
        every { configLoader.getConfigFileContent() } returns yamlContent

        // Define a Config object to be returned by loadConfig()
        val expectedConfig =
            Config(
                shows =
                    listOf(
                        Show(
                            name = "Test Show",
                            seasonDirectories = true,
                            aliases = listOf("Alias 1", "Alias 2"),
                        ),
                    ),
            )
        every { configLoader.loadConfig() } returns expectedConfig

        // Act
        val config = configLoader.loadConfig()

        // Assert
        assertEquals(expectedConfig, config)
    }
}
