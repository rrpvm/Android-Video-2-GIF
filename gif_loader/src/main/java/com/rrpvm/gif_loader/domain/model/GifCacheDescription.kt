package com.rrpvm.gif_loader.domain.model

data class GifCacheDescription(
    val mModelId : Long,
    val mOriginSource: String,
    val mCacheSize: Long,
    val mCreatedAt: Long,
    val mParamHashcode: Int,
)