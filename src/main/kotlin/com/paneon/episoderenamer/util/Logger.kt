package com.paneon.episoderenamer.util

enum class LoggerLevel {
    INFO,
    VERBOSE,
}

enum class LoggerAction(val icon: String, val description: String) {
    MOVE(LoggerIcon.MOVE.icon, "Move with new name"),
    COPY(LoggerIcon.COPY.icon, "Copy with new name"),
    SKIP(LoggerIcon.SKIP.icon, "Skip file"),
    SKIP_FILE_EXISTS(LoggerIcon.SKIP.icon, "Skip because file exists already."),
    SKIP_SHOW_NOT_FOUND(LoggerIcon.SKIP.icon, "Show now found."),
    SKIP_MATCHER_NOT_FOUND(LoggerIcon.SKIP.icon, "Matcher not found."),
}

enum class LoggerIcon(val icon: String) {
    DRY_RUN("🔍"),
    MOVE("➡\uFE0F"),
    COPY("➡\uFE0F"),
    SKIP("❌"),
}

class Logger(
    private val loggerLevel: LoggerLevel = LoggerLevel.INFO,
    private val dryRun: Boolean = false,
) {
    companion object {
        private const val CONSOLE_LEFT_COL_WIDTH = 18

        fun info(message: String) {
            printConsoleLine("Info", message)
        }

        fun printConsoleLine(
            label: String,
            content: String,
        ) {
            val paddedLabel = label.padEnd(CONSOLE_LEFT_COL_WIDTH, ' ')
            println("$paddedLabel | $content")
        }
    }

    private fun separator() {
        println("---------------------------")
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
        action: LoggerAction,
    ) {
        separator()
        printConsoleLine("File", originalName)
        printConsoleLine("New Name", newName)
        printConsoleLine("Target Directory", targetName)
        printConsoleLine("Action", "${getActionIcon(action)} ${action.description}")
    }

    private fun getActionIcon(action: LoggerAction): String {
        return if (dryRun) LoggerIcon.DRY_RUN.toString() else action.icon
    }

    fun skipBlock(
        originalName: String,
        action: LoggerAction = LoggerAction.SKIP,
    ) {
        separator()
        printConsoleLine("File", originalName)
        printConsoleLine("Action", "${getActionIcon(action)} ${action.description}")
    }

    fun error(message: String) {
        System.err.println("Error: $message")
    }
}
