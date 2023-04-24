package com.rrpvm.gif_loader.domain.entity

interface IResourceErrorEvent {
    fun onResourceLoadError(exception: Exception)
}