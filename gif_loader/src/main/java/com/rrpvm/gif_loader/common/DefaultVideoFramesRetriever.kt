package com.rrpvm.gif_loader.common

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.Log
import com.rrpvm.gif_loader.domain.entity.IVideoFramesRetriever
import wseemann.media.FFmpegMediaMetadataRetriever
import java.io.File

class DefaultVideoFramesRetriever : IVideoFramesRetriever {
    override suspend fun getVideoFrames(
        source: File,
        frames: Int,
    ): ArrayList<Bitmap>? {
        return kotlin.runCatching {
            val start = System.currentTimeMillis()
            val mmRetriever = MediaMetadataRetriever().apply {
                setDataSource(source.path)
            }
            val numFrames: Int =
                mmRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT)
                    ?.toInt() ?: return null

            return ArrayList(
                mmRetriever.getFramesAtIndex(
                    0,
                    Integer.min(frames + 1, numFrames),
                    MediaMetadataRetriever.BitmapParams()
                        .apply { preferredConfig = Bitmap.Config.ARGB_8888 }
                )
            ).also {
                mmRetriever.close()
                Log.e(
                    "com.rrpvm",
                    "${System.currentTimeMillis() - start} <- getVideoFrames with FFmpegMediaMetadataRetriever"
                )
            }
        }.getOrNull()
    }

    override suspend fun getVideoFramesInTime(
        source: File,
        millisToTakeForVideo: Int
    ): ArrayList<Bitmap>? {
        return kotlin.runCatching {
            val start = System.currentTimeMillis()
            val ffmpeg = FFmpegMediaMetadataRetriever().apply {
                this.setDataSource(source.path)
            }
            val pStep = 30
            val uniqueFrames = mutableSetOf<Bitmap>()
            for (time in 0..millisToTakeForVideo * 1000 step pStep * 1000) {
                uniqueFrames.add(
                    ffmpeg.getFrameAtTime(
                        time.toLong(),
                        FFmpegMediaMetadataRetriever.OPTION_CLOSEST
                    )
                )
            }
            val end = System.currentTimeMillis()
            Log.e("com.rrpvm", "${end - start} <- getVideoFrames with FFmpegMediaMetadataRetriever")
            return@runCatching ArrayList(uniqueFrames).also {
                ffmpeg.release()
            }
        }.getOrNull()
    }
}
