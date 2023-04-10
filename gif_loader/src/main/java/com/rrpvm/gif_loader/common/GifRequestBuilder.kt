package com.rrpvm.gif_loader.common

import android.content.Context
import android.util.Log
import com.rrpvm.gif_loader.domain.entity.IGifDataSource
import com.rrpvm.gif_loader.domain.entity.IGifModelWriter
import com.rrpvm.gif_loader.domain.model.GifModel
import com.rrpvm.gif_loader.domain.model.GifParameters
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GifRequestBuilder(
    private val context: Context,
    private val videoGifSource: String,
    private val gifVideoSourceRetriever: IGifDataSource,
    private val gifWriter: IGifModelWriter = DefaultGifWriter(gifEncoder = NdkGifEncoder(context)),
    private val cacheRepository: IGifCacheRepository,
    private val workManager: GifRequestManager,
) {
    private var gifParameters: GifParameters = GifParameters()
    private var cacheStrategy: Boolean = true
    fun setParameters(params: GifParameters) {
        gifParameters = params
    }

    fun setResolution(res: GifParameters.GifResolution): GifRequestBuilder {
        return this.apply {
            gifParameters = gifParameters.copy(mResolution = res)
        }
    }

    fun setQuality(
        @androidx.annotation.IntRange(
            from = 1, to = 20
        ) quality: Int
    ): GifRequestBuilder {
        return this.apply {
            this.gifParameters = gifParameters.copy(mQuality = quality)
        }
    }

    fun setFrameRate(rate: Int): GifRequestBuilder {
        return this.apply {
            this.gifParameters = gifParameters.copy(mFrameRate = rate)
        }
    }

    fun setFrameCount(frames: Int): GifRequestBuilder {
        return this.apply {
            this.gifParameters = gifParameters.copy(mFrameCount = frames)
        }
    }

    fun setCaching(isCaching: Boolean): GifRequestBuilder {
        return this.apply {
            cacheStrategy = isCaching
        }
    }

    fun loadInto(vInto: (ByteArray) -> Unit) {
        val result = object : SharedResourceSubscriber<ByteArray>() {
            override fun onResourceSuccess(data: ByteArray) {
                vInto.invoke(data)
            }

            override fun onResourceFailed() {
                Log.e("FAIL", "FAIL")
            }
        }
        val jobId = "${videoGifSource}_${gifParameters.hashCode()}"
        val fetchJob = CoroutineScope(Dispatchers.IO).launch {
            val videoData = gifVideoSourceRetriever.getVideoSource() ?: return@launch
            val cache = cacheRepository.getCache(videoGifSource, gifParameters)
            if (cache == null) {
                //hard-work
                val gif = withContext(this.coroutineContext + Dispatchers.Default) {
                    gifWriter.writeVideoToGif(context, videoData, gifParameters)
                } ?: return@launch
                val model = GifModel(
                    mGifData = gif,
                    mOriginSource = videoGifSource,
                    gif.size.toLong(),
                    gifParameters.hashCode(),
                    mCreatedAt = System.currentTimeMillis()
                )
                this.launch {
                    cacheRepository.putCache(model)
                }
                workManager.getJobResource<ByteArray>(jobNameId = jobId)
                    .submit(SharedResourceState(gif))
            } else {
                workManager.getJobResource<ByteArray>(jobNameId = jobId)
                    .submit(SharedResourceState(cache.mGifData))
            }
            workManager.onJobFinal(videoGifSource)
        }
        workManager.onJobStart(jobId, fetchJob)
        workManager.getJobResource<ByteArray>(jobId).addObserver(result)
    }
    /*fun load(): ByteArray? {

    }*/
}