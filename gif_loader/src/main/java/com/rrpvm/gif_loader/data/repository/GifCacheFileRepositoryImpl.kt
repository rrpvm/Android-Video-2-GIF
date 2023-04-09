package com.rrpvm.gif_loader.data.repository

/*
class GifDirectoryManager private constructor(appContext: Context) : IGifCacheToolManager {
    private var context: Context = appContext

    override fun onClearCache() {
        val cacheDir = getCacheDirectory()
        val cacheFiles = cacheDir.listFiles(FilenameFilter { dir, name ->
            return@FilenameFilter name.contains(CACHE_PREFIX)
        }) ?: return
        deleteOldVersionCache(cacheFiles)
        deleteOldCacheFiles(cacheFiles)
    }

    override fun onInit() {
        onClearCache()
    }

    fun getCacheDir(name: String): File {
        return File(getCacheDirectory(), name)
    }

    private fun deleteOldCacheFiles(allFiles: Array<File>, expression: File.() -> Boolean) {
        val cacheFilesToDelete = mutableListOf<File>()
        allFiles.forEach { file ->
            if (file.expression()) cacheFilesToDelete.add(file)
        }
        cacheFilesToDelete.forEach { it.delete() }
    }

    private fun deleteOldVersionCache(allFiles: Array<File>) {
        deleteOldCacheFiles(allFiles) {
            val gifModel = GifModelReader.getGifModelObject(this) ?: return@deleteOldCacheFiles true
            return@deleteOldCacheFiles gifModel.version != CACHE_VERSION
        }
    }

    private fun deleteOldCacheFiles(allFiles: Array<File>) {
        val time = System.currentTimeMillis()
        deleteOldCacheFiles(allFiles) {
            val created = this.lastModified()
            time - created > 1000 * 60 * 60 * 24 * 7
        }
    }

    private fun getCacheDirectory(): File {
        return context.cacheDir ?: throw RuntimeException("cache dir is null")
    }

    class Builder {
        fun init(applicationContext: Context): GifDirectoryManager {
            return GifDirectoryManager(applicationContext).apply {
                onInit()
            }
        }
    }

    companion object {
        private const val CACHE_VERSION: Int = 0
    }
}*/