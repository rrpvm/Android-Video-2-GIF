package com.rrpvm.testapp


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rrpvm.gif_loader.GifLoader
import com.rrpvm.gif_loader.domain.model.GifParameters
import com.rrpvm.testapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GifLoader.withContext(binding.mainGif.context)
            .fromLocal("https://sw.trackercracker.ru/upload/task/63ff266c8f3bd79ee8bf899c/document_5201954330378249996.mp4")
            .setResolution(GifParameters.GifResolution.MEDIUM)
            .setFrameCount(40)
            .setFrameRate(30)
            .loadInto {
                Glide.with(binding.mainGif.context).load(it).fitCenter()
                    .into(binding.mainGif)
            }
    }

}