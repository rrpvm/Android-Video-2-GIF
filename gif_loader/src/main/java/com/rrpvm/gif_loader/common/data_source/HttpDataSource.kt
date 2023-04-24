package com.rrpvm.gif_loader.common.data_source

import com.rrpvm.gif_loader.domain.entity.IGifDataSource
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class HttpDataSource(private val url: String) : IGifDataSource {
    @kotlin.jvm.Throws(IOException::class, MalformedURLException::class, Exception::class)
    override fun getVideoSource(): ByteArray {
        return URL(url).openStream().use { input ->
            return@use input.readBytes()
        }
    }
}
