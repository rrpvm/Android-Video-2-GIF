package com.rrpvm.gif_loader.domain.entity

import android.graphics.Bitmap
import java.io.File

interface IVideoFramesRetriever {
    suspend fun getVideoFrames(
        source : File,
        frames: Int,
    ): ArrayList<Bitmap>?
}