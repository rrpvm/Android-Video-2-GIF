package com.rrpvm.gif_loader.domain.entity

import android.content.Context
import com.rrpvm.gif_loader.domain.model.GifParameters

interface IGifModelWriter {
    suspend fun writeVideoToGif(
        context: Context,
        pVideoData: ByteArray,
        params: GifParameters,
    ) : ByteArray?
}



