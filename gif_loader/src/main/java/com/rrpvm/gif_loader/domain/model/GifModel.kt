package com.rrpvm.gif_loader.domain.model

data class GifModel(
    val mGifData: ByteArray,
    val mOriginSource: String,
    val mCacheSize: Long,
    val mParamHashcode: Int,
    val mCreatedAt: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GifModel

        if (!mGifData.contentEquals(other.mGifData)) return false
        if (mOriginSource != other.mOriginSource) return false
        if (mCacheSize != other.mCacheSize) return false
        if (mParamHashcode != other.mParamHashcode) return false
        if (mCreatedAt != other.mCreatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mGifData.contentHashCode()
        result = 31 * result + mOriginSource.hashCode()
        result = 31 * result + (mCacheSize % Integer.MAX_VALUE).toInt()
        result = 31 * result + mParamHashcode
        result = 31 * result + mCreatedAt.hashCode()
        return result
    }

}