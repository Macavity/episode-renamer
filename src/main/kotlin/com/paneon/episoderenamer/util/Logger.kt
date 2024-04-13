package com.paneon.episoderenamer.util

class Logger(private val dryRun: Boolean) {

    private fun separator() {
        println("---------------------------")
    }

    fun log(message: String) {
        println("Error: $message")
    }

    fun infoBlock(originalName: String, newName: String) {
        separator()
        println("File     | $originalName")
        println("New Name | $newName")
        val action = if (!dryRun) "✅ Rename" else "⏸\uFE0F Dry Run"
        println("Action   | $action")
    }

    fun skipBlock(originalName: String) {
        separator()
        println("File     | $originalName")
        println("Action   | ❌ Skip")
    }


    fun error(message: String) {
        System.err.println("Error: $message")
    }
}