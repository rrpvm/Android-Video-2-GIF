package com.rrpvm.gif_loader.domain.entity

enum class GifLoaderRequestCacheStrategy {
    CACHE_SOURCE_AND_GIF,
    CACHE_ONLY_GIF,
    CACHE_ONLY_SOURCE,
    NO_CACHE
}