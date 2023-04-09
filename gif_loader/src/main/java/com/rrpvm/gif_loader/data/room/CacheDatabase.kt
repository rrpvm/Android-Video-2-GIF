package com.rrpvm.gif_loader.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rrpvm.gif_loader.data.dao.CacheDbRoomDao
import com.rrpvm.gif_loader.data.model.GifCacheModelRoom

@Database(version = CACHE_DB_VERSION, entities = [GifCacheModelRoom::class])
abstract class CacheDatabase : RoomDatabase() {
    abstract fun getCacheDao(): CacheDbRoomDao
}

const val CACHE_DB_VERSION = 5