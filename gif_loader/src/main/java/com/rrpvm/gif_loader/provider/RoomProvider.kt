package com.rrpvm.gif_loader.provider

import android.content.Context
import androidx.room.Room
import com.rrpvm.gif_loader.data.dao.CacheDbRoomDao
import com.rrpvm.gif_loader.data.room.CacheDatabase

//TODO: change provider
object RoomProvider {
    private var cacheDatabase: CacheDatabase? = null
    private fun provideCacheRoomDb(applicationContext: Context): CacheDatabase {
        cacheDatabase =
            Room.databaseBuilder(applicationContext, CacheDatabase::class.java, CACHE_DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        return cacheDatabase ?: throw RuntimeException("can't create a Room db")
    }

    private var dbCacheDao: CacheDbRoomDao? = null

    fun provideCacheDao(applicationContext: Context): CacheDbRoomDao {
        return dbCacheDao ?: run {
            dbCacheDao = cacheDatabase?.getCacheDao()
                ?: provideCacheRoomDb(applicationContext).getCacheDao()
            return@run dbCacheDao
                ?: throw RuntimeException("Error in creating DAO")
        }
    }

    private const val CACHE_DB_NAME = "DB_CACHE_TEST"
}