package com.rrpvm.gif_loader.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rrpvm.gif_loader.data.model.GifCacheModelRoom

@Dao
interface CacheDbRoomDao {
    /**
    params:
    name -> URI/Path of gif
     **/
    @Query("SELECT * FROM gif_models_db WHERE cache_origin_source LIKE :source AND cache_gif_parameter_hashcode LIKE :gifParametersHashCode")
    fun getGifCache(source: String, gifParametersHashCode: Int): GifCacheModelRoom?


    @Query("SELECT * FROM gif_models_db ORDER BY cache_date")
    fun getAllGifCache(): List<GifCacheModelRoom>

    @Insert
    fun createGifCache(model: GifCacheModelRoom)

    @Query("SELECT * FROM gif_models_db ORDER BY cache_date LIMIT :limit OFFSET :page")
    fun getGifCacheList(page: Int, limit: Int): List<GifCacheModelRoom>

    @Query("DELETE FROM gif_models_db WHERE cache_id IN (:mIds)")
    fun deleteCacheList(mIds: List<Long>)

    @Query("DELETE FROM sqlite_sequence")
    fun resetStatement()
}