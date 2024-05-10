package com.paneon.episoderenamer.exception

class MatcherNotFoundException(fileName: String) : Exception("No Matcher found for $fileName")
