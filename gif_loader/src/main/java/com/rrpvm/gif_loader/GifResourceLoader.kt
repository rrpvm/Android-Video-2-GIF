package com.rrpvm.gif_loader
import android.content.Context
import com.rrpvm.gif_loader.domain.repository.IGifCacheRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.net.URL

class GifResourceLoader(private val context: Context, private val repository: IGifCacheRepository) {
    /*fun load(path: Uri): GifRequestParameters {
        val result = CoroutineScope(Dispatchers.IO).async {
            return@async kotlin.runCatching {
                return@runCatching context.contentResolver.openInputStream(path).use {
                    return@use it?.readBytes() ?: throw RuntimeException("file doesnt exist")
                }
            }
        }
        return GifRequestParameters(result)
    }*/

    fun load(url: String): GifRequestParameters {
        return GifRequestParameters(context, url, CoroutineScope(Dispatchers.IO).async {
            // val cache = repository.getCache(url)
            val cache = null
            val gifModel: ByteArray? = if (cache == null) {
                val data = kotlin.runCatching {
                    return@runCatching URL(url).openStream().use { input ->
                        return@use input.readBytes()
                    }
                }
                if (data.isSuccess) {
                    val bArray = data.getOrThrow()
                    bArray
                    //  val cacheModel = GifModel(1, bArray.size, bArray, url)
                    //  repository.putCache(cacheModel)
                    //  cacheModel
                } else null
            } else {
                //  cache
                null
            }
            return@async gifModel
        },repository)
    }
}