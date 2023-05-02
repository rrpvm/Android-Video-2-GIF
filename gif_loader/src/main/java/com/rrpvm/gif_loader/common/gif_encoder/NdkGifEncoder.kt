package com.rrpvm.gif_loader.common.gif_encoder


import android.graphics.Bitmap
import android.util.Log
import com.rrpvm.gif_loader.domain.entity.IGifEncoder
import com.rrpvm.gif_loader.domain.model.GifParameters
import com.waynejo.androidndkgif.GifEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

private const val TAG = ":NdkGifEncoder"

class NdkGifEncoder : IGifEncoder {
    companion object {
        @Volatile
        private var WORKER_ID = 0L
    }

    override suspend fun encodeGif(
        frames: ArrayList<Bitmap>,
        config: GifParameters,
        fileCreator: (String) -> File
    ): ByteArray? {
        synchronized(NdkGifEncoder) {
            WORKER_ID++
        }
        val gifEncoder = GifEncoder()
        val width = frames.getOrNull(0)?.width ?: return null
        val height = frames.getOrNull(0)?.height ?: return null
        val file = withContext(Dispatchers.IO){
            fileCreator.invoke("TMP_${Thread.currentThread().name}_$WORKER_ID.tgif")
        }
        Log.e(TAG, file.path)
        val convertJobTime = System.currentTimeMillis()
        gifEncoder.init(
            width,
            height,
            file.absolutePath,
            GifEncoder.EncodingType.ENCODING_TYPE_FAST
        )
        frames.forEachIndexed { i, it ->
            gifEncoder.encodeFrame(it, 1000 / config.mFrameRate)
        }
        Log.e(TAG, (System.currentTimeMillis() - convertJobTime).toString())
        gifEncoder.close()
        return kotlin.runCatching {
            file.inputStream().readBytes().also {
                file.delete()
            }
        }.getOrNull()
    }
}