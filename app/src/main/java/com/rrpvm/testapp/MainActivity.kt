package com.rrpvm.testapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.rrpvm.gif_loader.GifLoader
import com.rrpvm.gif_loader.domain.model.GifParameters
import com.rrpvm.testapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv) as NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.fragment_nav)
        navController.handleDeepLink(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GifLoader.withContext(binding.mainGif.context)
            .from("https://sw.trackercracker.ru/upload/task/63ff266c8f3bd79ee8bf899c/document_5201954330378249996.mp4")
            .setResolution(GifParameters.GifResolution.MEDIUM)
            .setFrameCount(40)
            .setFrameRate(30)
            .loadInto {
                Glide.with(binding.mainGif.context).load(it).fitCenter()
                    .into(binding.mainGif)
            }
    }

}