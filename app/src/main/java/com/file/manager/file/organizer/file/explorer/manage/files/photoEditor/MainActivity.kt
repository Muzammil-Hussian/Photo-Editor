package com.file.manager.file.organizer.file.explorer.manage.files.photoEditor

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.file.manager.file.organizer.file.explorer.manage.files.photoEditor.databinding.ActivityMainBinding
import com.yalantis.ucrop.UCropFragment
import com.yalantis.ucrop.UCropFragmentCallback

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), UCropFragmentCallback {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.i(TAG, "onCreate: controller: $controller")
            Log.i(TAG, "onCreate: destination: ${destination.label}")
            Log.i(TAG, "onCreate: argument: $arguments")
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun loadingProgress(p0: Boolean) {
        Log.i(TAG, "loadingProgress: $p0")
    }

    override fun onCropFinish(p0: UCropFragment.UCropResult?) {
        Log.i(TAG, "onCropFinish: $p0")
    }

}