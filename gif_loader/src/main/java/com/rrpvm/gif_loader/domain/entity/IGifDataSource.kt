package com.rrpvm.gif_loader.domain.entity

interface IGifDataSource {
    fun getVideoSource(): ByteArray?
}