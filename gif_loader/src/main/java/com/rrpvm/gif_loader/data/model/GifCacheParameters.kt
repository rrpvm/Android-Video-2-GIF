package com.rrpvm.gif_loader.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rrpvm.gif_loader.domain.model.GifParameters
/*
@Entity(
    "db_gif_cache_params", /*foreignKeys = arrayOf(
        ForeignKey(
            entity = GifCacheModelRoom::class,
            parentColumns = arrayOf("gif_cache_param_id"),
            childColumns = arrayOf(
                "cache_path_uri_id"
            )
        )
    )*/
)
data class GifCacheParameters(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("gif_cache_param_id") val id: Long,
    @ColumnInfo("gif_frame_count") val mFrameCount: Int,
    @ColumnInfo("gif_frame_rate") val mFrameRate: Int,
    @ColumnInfo("gif_quality") val mQuality: Int,
    @ColumnInfo("git_resolution") val mResolution: GifParameters.GifResolution,
    @ColumnInfo("gif_parameter_hashcode") val mGifHashCode: Int,
) {
    fun toDomain(): GifParameters {
        return GifParameters(mFrameCount, mFrameRate, mQuality, mResolution)
    }

    companion object {
        fun GifParameters.fromDomain(): GifCacheParameters {
            return GifCacheParameters(
                0,
                mFrameCount,
                mFrameRate,
                mQuality,
                mResolution,
                this.hashCode()
            )
        }
    }
}*/