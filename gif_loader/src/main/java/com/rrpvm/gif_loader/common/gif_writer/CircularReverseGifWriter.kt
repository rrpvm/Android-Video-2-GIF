package com.rrpvm.gif_loader.common.gif_writer

import android.content.Context
import com.rrpvm.gif_loader.common.DefaultFramesPostProcessor
import com.rrpvm.gif_loader.common.DefaultVideoFramesRetriever
import com.rrpvm.gif_loader.domain.entity.IGifEncoder
import com.rrpvm.gif_loader.domain.entity.IGifModelWriter
import com.rrpvm.gif_loader.domain.entity.IVideoFramesPostProcessor
import com.rrpvm.gif_loader.domain.entity.IVideoFramesRetriever
import com.rrpvm.gif_loader.domain.model.GifParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class CircularReverseGifWriter(
    private val videoFramesRetriever: IVideoFramesRetriever = DefaultVideoFramesRetriever(),
    private val videoPostProcessor: IVideoFramesPostProcessor = DefaultFramesPostProcessor(),
    private val gifEncoder: IGifEncoder,
) : IGifModelWriter {
    override suspend fun writeVideoToGif(
        context: Context, pVideoSource: String, params: GifParameters
    ): ByteArray? {
        return withContext(Dispatchers.Default) {
            val framesArray = params.mGifTime?.let {
                videoFramesRetriever.getVideoFramesInTime(pVideoSource, it)
            } ?: videoFramesRetriever.getVideoFrames(pVideoSource, params.mFrameCount)
            ?: return@withContext null

            val processedFrames = framesArray.map {
                videoPostProcessor.convert(it, params.mResolution)
            }
            framesArray.clear()
            val allFrames = processedFrames.plus(processedFrames.reversed())
            return@withContext gifEncoder.encodeGif(ArrayList(allFrames), params) {
                val file = File(context.cacheDir, it)
                file.createNewFile()
                return@encodeGif file
            }
        }
    }
}