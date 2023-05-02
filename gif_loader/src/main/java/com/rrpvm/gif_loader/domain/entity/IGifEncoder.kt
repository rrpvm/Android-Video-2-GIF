package com.rrpvm.gif_loader.domain.entity

import android.graphics.Bitmap
import com.rrpvm.gif_loader.domain.model.GifParameters
import java.io.File

interface IGifEncoder {
    suspend fun encodeGif(
        frames: ArrayList<Bitmap>,
        config: GifParameters,
        fileCreator: (String) -> File
    ): ByteArray?
}