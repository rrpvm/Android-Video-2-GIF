package com.rrpvm.gif_loader

import android.content.Context
import com.rrpvm.gif_loader.common.DefaultGifWriter
import com.rrpvm.gif_loader.common.NdkGifEncoder
import com.rrpvm.gif_loader.domain.entity.IGifModelWriter
import com.rrpvm.gif_loader.domain.model.GifModel
import com.rrpvm.gif_loader.domain.model.GifParameters
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository
import kotlinx.coroutines.*

class GifRequestParameters(
    private val context: Context,
    private val gifName: String,
    private val request: Deferred<ByteArray?>,
    private val cacheRepository: IGifCacheRepository,
    private val gifWriter: IGifModelWriter = DefaultGifWriter(gifEncoder = NdkGifEncoder(context)),
) {
    private var gifParameters: GifParameters = GifParameters()
    private var cacheStrategy: Boolean = true
    fun setParameters(params: GifParameters) {
        gifParameters = params
    }

    fun setResolution(res: GifParameters.GifResolution): GifRequestParameters {
        return this.apply {
            gifParameters = gifParameters.copy(mResolution = res)
        }
    }

    fun setQuality(
        @androidx.annotation.IntRange(
            from = 1, to = 20
        ) quality: Int
    ): GifRequestParameters {
        return this.apply {
            this.gifParameters = gifParameters.copy(mQuality = quality)
        }
    }

    fun setFrameRate(rate: Int): GifRequestParameters {
        return this.apply {
            this.gifParameters = gifParameters.copy(mFrameRate = rate)
        }
    }

    fun setFrameCount(frames: Int): GifRequestParameters {
        return this.apply {
            this.gifParameters = gifParameters.copy(mFrameCount = frames)
        }
    }

    fun setCaching(isCaching: Boolean): GifRequestParameters {
        return this.apply {
            cacheStrategy = isCaching
        }
    }

    fun loadDataInto(into: (ByteArray) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val setDataToView: suspend (d: ByteArray) -> Unit = { data: ByteArray ->
                withContext(Dispatchers.Main) {
                    into.invoke(data)
                }
            }
            val videoData = request.await() ?: return@launch
            val cache = cacheRepository.getCache(gifName, gifParameters)
            if (cache == null) {
                //hard-work
                val gif =
                    gifWriter.writeVideoToGif(context, videoData, gifParameters) ?: return@launch
                val model = GifModel(
                    mGifData = gif,
                    mOriginSource = gifName,
                    gif.size.toLong(),
                    gifParameters.hashCode(),
                    mCreatedAt = System.currentTimeMillis()
                )
                cacheRepository.putCache(model)
                setDataToView(gif)
            } else {
                setDataToView(cache.mGifData)
            }
        }
    }
}