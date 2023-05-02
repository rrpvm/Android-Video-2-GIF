package com.rrpvm.gif_loader.common.data_source

import android.content.Context
import androidx.core.net.toUri
import com.rrpvm.gif_loader.domain.entity.GifLoaderRequestCacheStrategy
import com.rrpvm.gif_loader.domain.entity.IGifDataSource
import com.rrpvm.gif_loader.domain.utillities.PathFinder.getMyCacheDir
import java.io.File
import java.io.IOException
import java.util.UUID

class LocalFileDataSource(
    private val path: String, private val context: Context
) : IGifDataSource {
    /* @kotlin.jvm.Throws(IOException::class, NullPointerException::class)
     override fun getVideoSource(cacheStrategy: GifLoaderRequestCacheStrategy): ByteArray {
         return File(path).inputStream().use { input ->
             return@use input.readBytes()
         }
     }*/
    @kotlin.jvm.Throws(IOException::class, NullPointerException::class)
    override fun getVideoSource(cacheStrategy: GifLoaderRequestCacheStrategy): String {
        return context.contentResolver.openInputStream(path.toUri()).use { input ->
            if (input == null) throw NullPointerException()
            val tmpOutput = context.getMyCacheDir(UUID.randomUUID().toString())
            tmpOutput.outputStream().use {
                input.copyTo(it)
            }.also {
                tmpOutput.deleteOnExit()
            }
            return@use tmpOutput.path
        }
        /* context.contentResolver.openInputStream(path.toUri()).use { input ->
                val MAX_VIDEO_SIZE_PART = 1024 * 1024 * 24
                val array = ByteArray(
                    minOf(
                        input?.available() ?: throw FileNotFoundException(),
                        MAX_VIDEO_SIZE_PART
                    )
                )
                val read = input.read(array, 0, array.size)
                return@use array
            }*/
    }
}
