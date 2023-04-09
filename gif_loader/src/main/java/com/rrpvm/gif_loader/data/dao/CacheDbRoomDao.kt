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

    @Insert
    fun createGifCache(model: GifCacheModelRoom)

}