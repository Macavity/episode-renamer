package com.paneon.episoderenamer.exception

class ShowNotFoundException(show: String) : Exception("Show $show not found.")
