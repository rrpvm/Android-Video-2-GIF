package com.rrpvm.gif_loader.domain.entity

import android.graphics.Bitmap
import java.io.File

interface IVideoFramesRetriever {
    suspend fun getVideoFrames(
        source : String,
        frames: Int,
    ): ArrayList<Bitmap>?
    suspend fun getVideoFramesInTime(
        source : String,
        millisToTakeForVideo: Int,
    ): ArrayList<Bitmap>?
}
