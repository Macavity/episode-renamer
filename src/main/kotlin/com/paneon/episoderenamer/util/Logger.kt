package com.paneon.episoderenamer.util

enum class LoggerLevel {
    INFO,
    VERBOSE,
}

class Logger(
    private val loggerLevel: LoggerLevel = LoggerLevel.INFO,
) {
    companion object {
        private const val CONSOLE_LEFT_COL_WIDTH = 18
    }

    private fun separator() {
        println("---------------------------")
    }

    fun info(message: String) {
        printConsoleLine("Info", message)
    }

    fun debug(
        label: String,
        message: String,
    ) {
        if (loggerLevel == LoggerLevel.VERBOSE) {
            printConsoleLine(label, message)
        }
    }

    fun infoBlock(
        originalName: String,
        newName: String,
        targetName: String,
        action: String,
    ) {
        separator()
        printConsoleLine("File", originalName)
        printConsoleLine("New Name", newName)
        printConsoleLine("Target Directory", targetName)
        printConsoleLine("Action", action)
    }

    fun skipBlock(
        originalName: String,
        action: String = "❌ Skip",
    ) {
        separator()
        printConsoleLine("File", originalName)
        printConsoleLine("Action", action)
    }

    private fun printConsoleLine(
        label: String,
        content: String,
    ) {
        val paddedLabel = label.padEnd(CONSOLE_LEFT_COL_WIDTH, ' ')
        println("$paddedLabel | $content")
    }

    fun error(message: String) {
        System.err.println("Error: $message")
    }
}
