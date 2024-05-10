package com.paneon.episoderenamer.exception

class SkipFileException(fileName: String) : Exception("Skipping file $fileName")
