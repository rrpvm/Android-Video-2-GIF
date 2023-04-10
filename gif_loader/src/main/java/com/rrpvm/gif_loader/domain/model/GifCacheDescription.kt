package com.rrpvm.gif_loader.domain.model

data class GifCacheDescription(
    val mModelId : Long,
    val mLocalPath: String,//local file
    val mOrigin : String,//from downloaded
    val mCacheSize: Long,
    val mCreatedAt: Long,
    val mParamHashcode: Int,
)