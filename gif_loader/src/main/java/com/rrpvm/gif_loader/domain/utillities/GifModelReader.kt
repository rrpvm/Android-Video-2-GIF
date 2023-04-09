package com.rrpvm.gif_loader.domain.utillities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.util.Log
import com.rrpvm.gif_loader.domain.model.GifParameters
import kotlinx.coroutines.*
import java.io.*
import java.util.*


object GifModelReader {
    suspend fun convertVideoToGif(
        context: Context,
        pVideoData: ByteArray,
        params: GifParameters,
    ): ByteArray? {
        return withContext(Dispatchers.Default) {
            val tmpFile = File(context.cacheDir, "${UUID.nameUUIDFromBytes(pVideoData)}_tmp.gif")
            DataOutputStream(tmpFile.outputStream()).use {
                it.write(pVideoData)
            }
            val mmRetriever = MediaMetadataRetriever().apply {
                setDataSource(tmpFile.path)
            }
            val numFrames: Int =
                mmRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT)
                    ?.toInt() ?: return@withContext null
            val bitmaps: ArrayList<Bitmap> = ArrayList(
                mmRetriever.getFramesAtIndex(
                    0,
                    Integer.min(params.mFrameCount, numFrames),
                    MediaMetadataRetriever.BitmapParams()
                        .apply { preferredConfig = Bitmap.Config.ARGB_8888 }
                )
            )
            val bitmapJobTime = System.currentTimeMillis()
            val nBitmap = ArrayList<Bitmap>(bitmaps.size)
            bitmaps.forEachIndexed { index, bitmap ->
                val scaledBitmap = decodeSampledBitmapWithConfig(
                    bitmap,
                    params.mResolution.maxSize,
                    params.mResolution.maxSize
                )
                if (scaledBitmap != null) {
                    nBitmap.add(scaledBitmap)
                }
                bitmap.recycle()
            }
            Log.e(
                "TAG,size-${nBitmap.size}",
                (System.currentTimeMillis() - bitmapJobTime).toString()
            )
            val bos = ByteArrayOutputStream()
            val gifEncoder = AnimatedGifEncoder()
            gifEncoder.start(bos)
            gifEncoder.setFrameRate(params.mFrameRate.toFloat())
            gifEncoder.setQuality(params.mQuality)
            val convertJobTime = System.currentTimeMillis()
            var prevJob = System.currentTimeMillis()
            nBitmap.forEachIndexed { i, it ->
                Log.e(
                    "TAG, frame:($i/${nBitmap.size})",
                    (System.currentTimeMillis() - prevJob).toString()
                )
                prevJob = System.currentTimeMillis()
                gifEncoder.addFrame(it)
            }
            Log.e("TAG", (System.currentTimeMillis() - convertJobTime).toString())
            gifEncoder.finish()
            mmRetriever.close()
            tmpFile.delete()
            return@withContext bos.toByteArray()
        }
    }
}

fun decodeSampledBitmapWithConfig(
    bitmap: Bitmap,
    reqWidth: Int, reqHeight: Int
): Bitmap? {
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
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

fun calculateInSampleSize(
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