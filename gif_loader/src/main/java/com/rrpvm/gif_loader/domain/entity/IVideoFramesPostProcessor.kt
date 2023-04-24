package com.rrpvm.gif_loader.domain.entity

import android.graphics.Bitmap
import com.rrpvm.gif_loader.domain.model.GifParameters

interface IVideoFramesPostProcessor {
    suspend fun convert(frame: Bitmap, resolution: GifParameters.GifResolution): Bitmap
}