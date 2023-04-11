package com.rrpvm.gif_loader.common

import android.content.Context
import android.util.Log
import com.rrpvm.gif_loader.data.model.CacheData
import com.rrpvm.gif_loader.domain.entity.ICacheSelector
import com.rrpvm.gif_loader.domain.entity.ICacheToolManager
import com.rrpvm.gif_loader.domain.model.GifCacheDescription
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository
import kotlinx.coroutines.*
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


//avoid context store
class DefaultCacheManager(
    context: Context,
    private val cacheSizeCapacity: Int,
    private val cacheSelector: ICacheSelector,
) : ICacheToolManager {
    private val file: File = File(context.cacheDir, DEFAULT_FILE_NAME)
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    override fun onAddCache(repo: IGifCacheRepository, cacheSize: Int) {
        try {
            val length = ObjectInputStream(file.inputStream()).use { it.readObject() } as CacheData
            var extraSize = length.cacheSize + cacheSize - cacheSizeCapacity
            while (extraSize > 0) {
                cacheSelector.getCacheForDelete(repo)?.let {
                    repo.removeCache(listOf(it))
                    extraSize -= it.mCacheSize.toInt()
                }
            }
            val newLength = cacheSizeCapacity + extraSize
            cacheData.cacheSize = newLength
            saveActualCacheSize()
            Log.e(TAG, "new size : ${newLength / 1024.0 / 1024.0}")
        } catch (e: Exception) {
            e.printStackTrace()
            saveActualCacheSize()
        }
    }

    override fun onInit(repo: IGifCacheRepository) {
        scope.launch {
            try {
                var length = (withContext(Dispatchers.IO) {
                    ObjectInputStream(file.inputStream()).use { it.readObject() }
                } as CacheData).cacheSize
                Log.e(TAG, length.toString())
                while (length > cacheSizeCapacity) {
                    val cacheList = repo.getCacheList(1, 10)
                    val cacheToDelete = ArrayList<GifCacheDescription>(10)
                    for (cache in cacheList) {
                        length -= cache.mCacheSize.toInt()
                        cacheToDelete.add(cache)
                        if (length <= cacheSizeCapacity) break
                    }
                    repo.removeCache(cacheToDelete.toList())
                }
                cacheData.cacheSize = length
                saveActualCacheSize()
            } catch (e: Exception) {
                e.printStackTrace()
                saveActualCacheSize()
            }
        }
    }

    override fun onReset(repo: IGifCacheRepository) {
        scope.launch {
            val cache = repo.getAllCache()
            repo.removeCache(cache)
            repo.clearRoomSqlStatement()
        }
    }

    private fun saveActualCacheSize() {
        ObjectOutputStream(file.outputStream()).writeObject(cacheData)
    }

    companion object {
        private val cacheData: CacheData = CacheData(0)
    }
}

const val DEFAULT_CACHE_SIZE_CAPACITY = 1024 * 1024 * 1024 //1Gb, 1kb->1mb->1gb
private const val DEFAULT_FILE_NAME = "DEFAULT_CACHE_CONTROL.txt"
private const val TAG = ":DefaultCacheManager"