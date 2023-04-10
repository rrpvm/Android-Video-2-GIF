package com.rrpvm.gif_loader.data.room

import androidx.annotation.Keep
import androidx.room.Database
import androidx.room.RoomDatabase
import com.rrpvm.gif_loader.data.dao.CacheDbRoomDao
import com.rrpvm.gif_loader.data.model.GifCacheModelRoom

@Database(version = CACHE_DB_VERSION, entities = [GifCacheModelRoom::class])
@Keep
abstract class CacheDatabase : RoomDatabase() {
    abstract fun getCacheDao(): CacheDbRoomDao
}
@Keep
const val CACHE_DB_VERSION = 5