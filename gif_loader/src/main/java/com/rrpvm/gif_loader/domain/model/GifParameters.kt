package com.rrpvm.gif_loader.domain.model

/**
 * parameters:
 * mFrameCount - the num of frames that will be retrivied,
 * mFrameRate -  num of frames per seconds, 1000/frameRate = delay of switching before frames
 * mQuality - quality of colors in generated gif, range : 1-20, where 20 the best
 * mWidth - the width of the output
 * mHeight - the height of the output
 * **/
data class GifParameters(
    val mFrameCount: Int = 48,//2s
    val mFrameRate: Int = 24,//24 fps
    @androidx.annotation.IntRange(from = 1, to = 20) val mQuality: Int = 5,
    val mResolution: GifResolution = GifResolution.LOW
) {
    enum class GifResolution(val maxSize: Int) {
        LOW(240), MEDIUM(320), HIGH(480)
    }
}


