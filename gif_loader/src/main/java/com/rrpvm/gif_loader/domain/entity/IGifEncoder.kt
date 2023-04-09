package com.rrpvm.gif_loader.domain.entity

import android.graphics.Bitmap
import com.rrpvm.gif_loader.domain.model.GifParameters

interface IGifEncoder {
    suspend fun encodeGif(
        frames: ArrayList<Bitmap>,
        config: GifParameters,
    ): ByteArray?
}