package com.rrpvm.gif_loader.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.rrpvm.gif_loader.domain.entity.IVVideoFramesPostProcessor
import com.rrpvm.gif_loader.domain.model.GifParameters
import java.io.ByteArrayOutputStream

private const val DEFAULT_QUALITY_BOS = 50

class DefaultFramesPostProcessor : IVVideoFramesPostProcessor {
    private val TAG = ":DefaultFramesPostProcessor"
    override suspend fun convert(frame: Bitmap, resolution: GifParameters.GifResolution): Bitmap {
        val start = System.currentTimeMillis()
        val result = decodeSampledBitmapWithConfig(
            frame, resolution.maxSize,
            resolution.maxSize
        )
        Log.e(
            TAG,
            "frame(${frame.width}/${frame.height}) processed by (${System.currentTimeMillis() - start}) ms"
        )
        return result
    }
}

private fun decodeSampledBitmapWithConfig(
    bitmap: Bitmap,
    reqWidth: Int, reqHeight: Int
): Bitmap {
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, DEFAULT_QUALITY_BOS, baos)
    val bArray = baos.toByteArray()
    val inSampleSize = calculateInSampleSize(bitmap.width, bitmap.height, reqWidth, reqHeight)
    val opt = BitmapFactory.Options().apply {
        this.inSampleSize = inSampleSize
        this.outWidth = bitmap.width
        this.outHeight = bitmap.height
        this.inJustDecodeBounds = false
        this.outConfig = bitmap.config
    }
    return BitmapFactory.decodeByteArray(bArray, 0, bArray.size, opt)
}

private fun calculateInSampleSize(
    w: Int, h: Int, reqWidth: Int, reqHeight: Int
): Int {
    var inSampleSize = 1
    if (h > reqHeight || w > reqWidth) {
        val halfHeight = h / 2
        val halfWidth = w / 2
        while (halfHeight / inSampleSize >= reqHeight
            && halfWidth / inSampleSize >= reqWidth
        ) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}