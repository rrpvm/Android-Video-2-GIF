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

class DefaultGifWriter(
    private val videoFramesRetriever: IVideoFramesRetriever = DefaultVideoFramesRetriever(),
    private val videoPostProcessor: IVideoFramesPostProcessor = DefaultFramesPostProcessor(),
    private val gifEncoder: IGifEncoder,
) : IGifModelWriter {
    override suspend fun writeVideoToGif(
        context: Context, pVideoSource: String, params: GifParameters
    ): ByteArray? {
        val framesArray = params.mGifTime?.let {
            videoFramesRetriever.getVideoFramesInTime(pVideoSource, it)
        } ?: videoFramesRetriever.getVideoFrames(pVideoSource, params.mFrameCount) ?: return null

        val processedFrames = framesArray.map {
            videoPostProcessor.convert(it, params.mResolution)
        }
        return gifEncoder.encodeGif(ArrayList(processedFrames), params)
    }
}