package com.rrpvm.gif_loader.common.data_source

import android.content.Context
import androidx.core.net.toUri
import com.rrpvm.gif_loader.domain.entity.GifLoaderRequestCacheStrategy
import com.rrpvm.gif_loader.domain.entity.IGifDataSource
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class LocalFileDataSource(
    private val path: String,
    private val context: Context
) : IGifDataSource {
    /* @kotlin.jvm.Throws(IOException::class, NullPointerException::class)
     override fun getVideoSource(cacheStrategy: GifLoaderRequestCacheStrategy): ByteArray {
         return File(path).inputStream().use { input ->
             return@use input.readBytes()
         }
     }*/
    @kotlin.jvm.Throws(IOException::class, NullPointerException::class)
    override fun getVideoSource(cacheStrategy: GifLoaderRequestCacheStrategy): ByteArray {
        return context.contentResolver.openInputStream(path.toUri()).use { input ->
            return@use input?.readBytes() ?: throw FileNotFoundException()
        }
    }
}
