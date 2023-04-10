package com.rrpvm.gif_loader.common

import android.content.Context

import com.rrpvm.gif_loader.domain.entity.IGifEncoder
import com.rrpvm.gif_loader.domain.entity.IGifModelWriter
import com.rrpvm.gif_loader.domain.entity.IVVideoFramesPostProcessor
import com.rrpvm.gif_loader.domain.entity.IVideoFramesRetriever
import com.rrpvm.gif_loader.domain.model.GifParameters
import java.io.DataOutputStream
import java.io.File
import java.util.*

class DefaultGifWriter(
    private val videoFramesRetriever: IVideoFramesRetriever = DefaultVideoFramesRetriever(),
    private val videoPostProcessor: IVVideoFramesPostProcessor = DefaultFramesPostProcessor(),
    private val gifEncoder: IGifEncoder,
) : IGifModelWriter {
    override suspend fun writeVideoToGif(
        context: Context, pVideoData: ByteArray, params: GifParameters
    ): ByteArray? {
        val tmpFile = File(context.cacheDir, "${UUID.nameUUIDFromBytes(pVideoData)}_tmp")
        DataOutputStream(tmpFile.outputStream()).use { it.write(pVideoData) }
        val framesArray = videoFramesRetriever.getVideoFrames(tmpFile, params.mFrameCount)
            ?: return null
        val processedFrames = framesArray.map {
            videoPostProcessor.convert(it, params.mResolution)
        }
        return gifEncoder.encodeGif(ArrayList(processedFrames), params)
            .also {
                tmpFile.delete()
            }
    }
}