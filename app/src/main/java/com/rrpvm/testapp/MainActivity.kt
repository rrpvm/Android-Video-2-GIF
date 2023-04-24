package com.rrpvm.testapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.rrpvm.testapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val viewmodel: MainViewModel by viewModels()

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
        if (intent.data != null) {
            //check valid

            /* val intent = NavDeepLinkBuilder(this)
                 .setGraph(R.navigation.main_nav)
                 .setDestination(R.id.fragment_nav)
                 .setComponentName(this.componentName)
                 .createPendingIntent()
             intent.send()*/
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fcv) as NavHostFragment
            val navController = navHostFragment.navController
              navController.setGraph(R.navigation.fragment_nav)
            // navController.handleDeepLink(intent)
            // findNavController(R.id.main_nav).setGraph(R.navigation.main_nav)

        }

        /* lifecycleScope.launch {
             delay(2000L)
             val navController = findNavController(R.id.fcv)
             navController.navigate(FirstFragmentDirections.action1())
         }*/
    }

}