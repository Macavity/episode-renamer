package com.paneon.episoderenamer.util

import java.io.File

fun ensureDirectoryExists(directory: File) {
    if (!directory.exists()) {
        directory.mkdirs()
        Logger.info("Created directory ${directory.path}")
    }
}
