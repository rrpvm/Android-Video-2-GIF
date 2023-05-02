package com.rrpvm.testapp

import android.app.Activity
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.rrpvm.gif_loader.GifLoader
import com.rrpvm.gif_loader.domain.entity.GifLoaderRequestCacheStrategy
import com.rrpvm.gif_loader.domain.entity.IResourceErrorEvent

class MainActivity : Activity() {
    override fun onStart() {
        super.onStart()
        setContentView(com.rrpvm.gif_loader.R.layout.main_activity)
        GifLoader.withContext(applicationContext)
            .fromLocal("content://media/external/video/media/55514")
            .setCaching(GifLoaderRequestCacheStrategy.NO_CACHE)
            //.from("https://sw.trackercracker.ru/upload/task/63ff266c8f3bd79ee8bf899c/document_5201954330378249996.mp4")
            .setOnErrorHandler(object : IResourceErrorEvent {
                override fun onResourceLoadError(exception: Exception) {
                    exception.printStackTrace()
                }
            })

            .loadInto {
                Log.e("t", it.size.toString())
                val img = findViewById<ImageView>(com.rrpvm.gif_loader.R.id.iv_1) ?: return@loadInto
                Glide.with(applicationContext).load(it).fitCenter().into(img)
            }
        GifLoader.withContext(applicationContext)
            .fromLocal("content://media/external/video/media/55504")
            .setCaching(GifLoaderRequestCacheStrategy.NO_CACHE)
            //.from("https://sw.trackercracker.ru/upload/task/63ff266c8f3bd79ee8bf899c/document_5201954330378249996.mp4")
            .setOnErrorHandler(object : IResourceErrorEvent {
                override fun onResourceLoadError(exception: Exception) {
                    exception.printStackTrace()
                }
            })

            .loadInto {
                Log.e("t", it.size.toString())
                val img = findViewById<ImageView>(com.rrpvm.gif_loader.R.id.iv_2) ?: return@loadInto
                Glide.with(applicationContext).load(it).fitCenter().into(img)
            }
    }
}