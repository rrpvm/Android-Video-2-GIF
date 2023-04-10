package com.rrpvm.gif_loader.data.model

import androidx.room.*
import com.rrpvm.gif_loader.domain.model.GifCacheDescription
import com.rrpvm.gif_loader.domain.model.GifModel
import java.io.File

@Entity(tableName = "gif_models_db")
data class GifCacheModelRoom(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cache_id") val mCacheId: Long,
    @ColumnInfo(name = "cache_origin_source") val mName: String,
    @ColumnInfo(name = "cache_data_path") val mSourcePath: String,
    @ColumnInfo(name = "cache_size") val mSize: Long,
    @ColumnInfo(name = "cache_version") val mVersion: Int,
    @ColumnInfo(name = "cache_gif_parameter_hashcode") val mParamHashcode: Int,
    @ColumnInfo(name = "cache_date") val mCreatedAt: Long,
) {
    constructor(
        mName: String,
        mSourcePath: String,
        mSize: Long,
        mVersion: Int,
        mParamHashcode: Int,
        mCreatedAt: Long
    ) : this(
        mCacheId = 0,
        mName = mName,
        mSourcePath = mSourcePath,
        mSize = mSize,
        mVersion = mVersion,
        mParamHashcode = mParamHashcode,
        mCreatedAt = mCreatedAt
    )

    fun toDomain(): GifModel? {
        try {
            return GifModel(
                mGifData = File(mSourcePath).inputStream().readBytes(),
                mOriginSource = mName,
                mCacheSize = mSize,
                mParamHashcode = mParamHashcode,
                mCreatedAt = mCreatedAt
            )
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return null
    }

    fun toDomainDescription(): GifCacheDescription {
        return GifCacheDescription(
            mModelId = mCacheId,
            mOriginSource = mSourcePath,
            mCacheSize = mSize,
            mCreatedAt = mCreatedAt,
            mParamHashcode = mParamHashcode
        )
    }
}