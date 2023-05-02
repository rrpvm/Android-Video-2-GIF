package com.rrpvm.gif_loader.common.data_source

import android.content.Context
import com.rrpvm.gif_loader.SV_MAX_FILE_SIZE
import com.rrpvm.gif_loader.domain.entity.GifLoaderRequestCacheStrategy
import com.rrpvm.gif_loader.domain.entity.IGifDataSource
import com.rrpvm.gif_loader.domain.exceptions.TooLargeVideoFileException
import com.rrpvm.gif_loader.domain.utillities.PathFinder.getMyCacheDir
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.UUID


class HttpDataSource(
    private val url: String,
    private val context: Context,
) : IGifDataSource {
    @kotlin.jvm.Throws(
        IllegalArgumentException::class,
        IOException::class,
        MalformedURLException::class,
        Exception::class,
    )
    override fun getVideoSource(cacheStrategy: GifLoaderRequestCacheStrategy): String? {
        return URL(url).openStream().use { input ->
            if (input.available() > SV_MAX_FILE_SIZE) throw TooLargeVideoFileException()
            val tempFile =
                context.getMyCacheDir("${UUID.randomUUID()}_${Thread.currentThread().name}")
            tempFile.outputStream().use {
                it.write(input.readBytes())
            }.also {
                tempFile.deleteOnExit()
            }
            return@use tempFile.path
        }
    }
}
