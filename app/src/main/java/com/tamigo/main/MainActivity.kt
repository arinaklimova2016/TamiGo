package com.tamigo.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.lifecycle.lifecycleScope
import com.tamigo.base.BaseActivity
import com.tamigo.base.BindingInflation
import com.tamigo.databinding.ActivityMainBinding
import com.tamigo.navigation.Router
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val bindingInflation: BindingInflation<ActivityMainBinding> =
        ActivityMainBinding::inflate
    private val router by inject<Router>()
    private val mainViewModel: MainViewModel by viewModel()

    private val permissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class)
    )

    private val requestPermissionActivityContract = PermissionController
        .createRequestPermissionResultContract()

    private val requestPermissions = registerForActivityResult(
        requestPermissionActivityContract
    ) { granted ->
        if (granted.containsAll(permissions)) {
            Toast.makeText(applicationContext, "Permission Granted launch", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Permission not Granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        router.attachTo(this)
        installSplashScreen().setKeepOnScreenCondition {
            mainViewModel.navigate()
            false
        }

        val availabilityStatus = HealthConnectClient.sdkStatus(applicationContext)
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE) {
            return // early return as there is no viable integration
        }
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            // Optionally redirect to package installer to find a provider, for example:
            val uriString = "market://details?url=healthconnect%3A%2F%2Fonboarding"
            applicationContext.startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    setPackage("com.android.vending")
                    data = Uri.parse(uriString)
                    putExtra("overlay", true)
                    putExtra("callerId", applicationContext.packageName)
                }
            )
            return
        }
        val healthConnectClient = HealthConnectClient.getOrCreate(applicationContext)
        lifecycleScope.launch{
            checkPermissionsAndRun(healthConnectClient)
        }
    }

    private suspend fun checkPermissionsAndRun(healthConnectClient: HealthConnectClient) {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        if (granted.containsAll(permissions)) {
            Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            requestPermissions.launch(permissions)
        }
        requestPermissions.launch(permissions)
    }
}
