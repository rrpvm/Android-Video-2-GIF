package com.rrpvm.gif_loader.common

import android.content.Context
import android.util.Log
import com.rrpvm.gif_loader.domain.entity.ICacheToolManager
import com.rrpvm.gif_loader.domain.model.GifCacheDescription
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File

class DefaultCacheManager(
    private val context: Context,
    private val cacheSizeCapacity: Int
) : ICacheToolManager {
    private val file: File = File(context.cacheDir, DEFAULT_FILE_NAME)
    private val TAG = ":DefaultCacheManager"
    override fun onAddCache(repo: IGifCacheRepository, cacheSize: Int) {
        try {
            val length = DataInputStream(file.inputStream()).use { it.readInt() }
            Log.e(TAG, length.toString())
            if (length + cacheSize > cacheSizeCapacity) {

            } else {
                saveActualCacheSize(length + cacheSize)
            }
        } catch (e: Exception) {
            e.printStackTrace()

            DataOutputStream(file.outputStream()).writeInt(0)
        }
    }

    override fun onInit(repo: IGifCacheRepository) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var length = DataInputStream(file.inputStream()).use { it.readInt() }
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
                saveActualCacheSize(length)
            } catch (e: Exception) {
                e.printStackTrace()
                saveActualCacheSize(0)
            }
        }
    }

    override fun onReset() {

    }

    private fun saveActualCacheSize(size: Int) {
        DataOutputStream(file.outputStream()).writeInt(size)
    }
}

const val DEFAULT_CACHE_SIZE_CAPACITY = 1024 * 1024 * 1024 //1Gb, 1kb->1mb->1gb
private const val DEFAULT_FILE_NAME = "DEFAULT_CACHE_CONTROL.txt"