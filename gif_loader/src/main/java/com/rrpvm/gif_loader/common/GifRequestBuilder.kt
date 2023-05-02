package com.rrpvm.gif_loader.common

import android.content.Context
import android.util.Log
import com.rrpvm.gif_loader.common.gif_encoder.NdkGifEncoder
import com.rrpvm.gif_loader.common.gif_writer.DefaultGifWriter
import com.rrpvm.gif_loader.domain.entity.*
import com.rrpvm.gif_loader.domain.model.GifModel
import com.rrpvm.gif_loader.domain.model.GifParameters
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository
import kotlinx.coroutines.*

class GifRequestBuilder(
    private val context: Context,
    private val videoGifSource: String,
    private val gifVideoSourceRetriever: IGifDataSource,
    private val cacheRepository: IGifCacheRepository,
    private val workManager: GifRequestManager,
) {
    private var gifWriter: IGifModelWriter = DefaultGifWriter(gifEncoder = NdkGifEncoder(context))
    private var gifParameters: GifParameters = GifParameters()
    private var cacheStrategy: GifLoaderRequestCacheStrategy =
        GifLoaderRequestCacheStrategy.CACHE_ONLY_GIF
    private var onError: IResourceErrorEvent? = null
    fun setGifWriter(writer: IGifModelWriter): GifRequestBuilder {
        return this.apply {
            gifWriter = writer
        }
    }

    fun setOnErrorHandler(handler: IResourceErrorEvent): GifRequestBuilder {
        return this.apply {
            onError = handler
        }
    }

    fun setParameters(params: GifParameters): GifRequestBuilder {
        return this.apply {
            gifParameters = params
        }
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

    fun setVideoGifDuration(durationMs: Int): GifRequestBuilder {
        return this.apply {
            this.gifParameters = gifParameters.copy(mGifTime = durationMs)
        }
    }

    fun setCaching(isCaching: GifLoaderRequestCacheStrategy): GifRequestBuilder {
        return this.apply {
            cacheStrategy = isCaching
        }
    }

    fun loadInto(vInto: (ByteArray) -> Unit): Job {
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
            try {
                val videoData =
                    gifVideoSourceRetriever.getVideoSource(cacheStrategy) ?: return@launch
                val cache = cacheRepository.getCache(videoGifSource, gifParameters)
                if (cache == null || isNeedFullLoad()) {
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
            } catch (exception: Exception) {
                onError?.onResourceLoadError(exception)
                return@launch
            }
        }
        workManager.onJobStart(jobId, fetchJob)
        workManager.getJobResource<ByteArray>(jobId).addObserver(result)
        return fetchJob
    }

    private fun isNeedFullLoad(): Boolean {
        return cacheStrategy == GifLoaderRequestCacheStrategy.NO_CACHE || cacheStrategy == GifLoaderRequestCacheStrategy.CACHE_ONLY_SOURCE
    }
/*
    suspend fun load(): ByteArray? {
        val videoData = gifVideoSourceRetriever.getVideoSource() ?: return null
        val cache = cacheRepository.getCache(videoGifSource, gifParameters)
        if (cache == null) {
            val gif = gifWriter.writeVideoToGif(context, videoData, gifParameters)
                ?: return null
            val model = GifModel(
                mGifData = gif,
                mOriginSource = videoGifSource,
                gif.size.toLong(),
                gifParameters.hashCode(),
                mCreatedAt = System.currentTimeMillis()
            )
            coroutineScope {
                this.launch { cacheRepository.putCache(model) }
            }
            return gif
        } else {
            return cache.mGifData
        }
    }*/
}