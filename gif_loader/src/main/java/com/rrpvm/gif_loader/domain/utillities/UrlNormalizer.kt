package com.rrpvm.gif_loader.domain.utillities

object UrlNormalizer {
    fun String.urlToNormalPath(): String {
        return this.replace("/", "").replace("\\", "").replace(":", "")
    }
}