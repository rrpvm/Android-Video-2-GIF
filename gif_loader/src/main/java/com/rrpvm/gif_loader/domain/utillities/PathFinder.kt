package com.rrpvm.gif_loader.domain.utillities

import android.content.Context
import java.io.File

object PathFinder {
    fun Context.getMyCacheDir(cacheFileName: String): File {
        val file = File(this.cacheDir, "rrpvm_gif").also {
            if (!it.exists()) it.mkdir()
        }
        return File(file, cacheFileName).also {
            it.createNewFile()
        }
    }
}