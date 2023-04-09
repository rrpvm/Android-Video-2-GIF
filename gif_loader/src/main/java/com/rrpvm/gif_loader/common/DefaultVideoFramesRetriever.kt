package com.rrpvm.gif_loader.common

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import com.rrpvm.gif_loader.domain.entity.IVideoFramesRetriever
import java.io.File

class DefaultVideoFramesRetriever : IVideoFramesRetriever {
    override suspend fun getVideoFrames(
        source: File,
        frames: Int,
    ): ArrayList<Bitmap>? {
        val mmRetriever = MediaMetadataRetriever().apply {
            setDataSource(source.path)
        }
        val numFrames: Int =
            mmRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT)
                ?.toInt() ?: return null

        return ArrayList(
            mmRetriever.getFramesAtIndex(
                0,
                Integer.min(frames, numFrames),
                MediaMetadataRetriever.BitmapParams()
                    .apply { preferredConfig = Bitmap.Config.ARGB_8888 }
            )
        ).also {
            mmRetriever.close()
        }
    }
}