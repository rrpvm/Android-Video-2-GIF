package com.rrpvm.gif_loader.common.gif_writer

import android.content.Context
import com.rrpvm.gif_loader.common.DefaultFramesPostProcessor
import com.rrpvm.gif_loader.common.DefaultVideoFramesRetriever
import com.rrpvm.gif_loader.domain.entity.IGifEncoder
import com.rrpvm.gif_loader.domain.entity.IGifModelWriter
import com.rrpvm.gif_loader.domain.entity.IVideoFramesPostProcessor
import com.rrpvm.gif_loader.domain.entity.IVideoFramesRetriever
import com.rrpvm.gif_loader.domain.model.GifParameters
import java.io.DataOutputStream
import java.io.File
import java.util.*

class CircularReverseGifWriter(
    private val videoFramesRetriever: IVideoFramesRetriever = DefaultVideoFramesRetriever(),
    private val videoPostProcessor: IVideoFramesPostProcessor = DefaultFramesPostProcessor(),
    private val gifEncoder: IGifEncoder,
) : IGifModelWriter {
    override suspend fun writeVideoToGif(
        context: Context, pVideoData: ByteArray, params: GifParameters
    ): ByteArray? {
        val tmpFile = File(context.cacheDir, "${UUID.nameUUIDFromBytes(pVideoData)}_tmp")
        DataOutputStream(tmpFile.outputStream()).use { it.write(pVideoData) }
        val framesArray = params.mGifTime?.let {
            videoFramesRetriever.getVideoFramesInTime(tmpFile, it)
        } ?: videoFramesRetriever.getVideoFrames(tmpFile, params.mFrameCount) ?: return null

        val processedFrames = framesArray.map {
            videoPostProcessor.convert(it, params.mResolution)
        }
        val allFrames = processedFrames.plus(processedFrames.reversed())
        return gifEncoder.encodeGif(ArrayList(allFrames), params)
            .also {
                tmpFile.delete()
            }
    }
}