package com.rrpvm.gif_loader.common.gif_encoder

import android.graphics.Bitmap
import android.util.Log
import com.rrpvm.gif_loader.domain.entity.IGifEncoder
import com.rrpvm.gif_loader.domain.model.GifParameters
import com.rrpvm.gif_loader.domain.utillities.AnimatedGifEncoder
import java.io.ByteArrayOutputStream

class DefaultGifEncoder : IGifEncoder {
    private val TAG = ":DefaultGifEncoder"
    override suspend fun encodeGif(
        frames: ArrayList<Bitmap>,
        config: GifParameters
    ): ByteArray? {
        val bos = ByteArrayOutputStream()
        val gifEncoder = AnimatedGifEncoder()
        gifEncoder.start(bos)
        gifEncoder.setFrameRate(config.mFrameRate.toFloat())
        gifEncoder.setQuality(config.mQuality)
        val convertJobTime = System.currentTimeMillis()
        var prevJob = System.currentTimeMillis()
        frames.forEachIndexed { i, it ->
            Log.e(
                "$TAG, frame:($i/${frames.size})", (System.currentTimeMillis() - prevJob).toString()
            )
            prevJob = System.currentTimeMillis()
            gifEncoder.addFrame(it)
        }
        Log.e(TAG, (System.currentTimeMillis() - convertJobTime).toString())
        gifEncoder.finish()
        return bos.toByteArray()
    }
}