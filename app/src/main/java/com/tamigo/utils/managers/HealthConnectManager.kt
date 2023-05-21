package com.tamigo.utils.managers

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.tamigo.utils.preferences.Preferences
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class HealthConnectManager(
    private val context: Context,
    private val preferences: Preferences
) {
    private var activity: ComponentActivity? = null
    private var permissionRequest: SuspendActivityResultLauncher? = null

    fun attach(activity: ComponentActivity) {
        this.activity = activity
        permissionRequest = SuspendActivityResultLauncher(
            activity = activity
        )
    }

    fun checkHealthConnect() {
        val availabilityStatus = HealthConnectClient.sdkStatus(context)
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE) {
            return
        }
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            context.startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    setPackage(MARKET_PACKAGE)
                    data = Uri.parse(HEALTH_CONNECT_URI)
                    putExtra(OVERLAY, true)
                    putExtra(CALLER_ID, context.packageName)
                }
            )
            return
        }
        val healthConnectClient = HealthConnectClient.getOrCreate(context)
        checkPermissions(healthConnectClient)
    }

    suspend fun getSteps(): Long {
        val now = ZonedDateTime.now()
        val startTargetTime: ZonedDateTime = try {
            ZonedDateTime.parse(preferences.getStartTargetTime())
        } catch (e: java.lang.Exception) {
            now.truncatedTo(ChronoUnit.DAYS)
        }
        val timeRangeFilter = TimeRangeFilter.between(
            startTargetTime.toLocalDateTime(),
            now.toLocalDateTime()
        )

        val stepsRecordRequest = ReadRecordsRequest(StepsRecord::class, timeRangeFilter)
        val healthConnectClient = HealthConnectClient.getOrCreate(context)
        return healthConnectClient.readRecords(stepsRecordRequest)
            .records.sumOf { it.count }
    }

    private fun checkPermissions(healthConnectClient: HealthConnectClient?) {
        CoroutineScope(Dispatchers.IO).launch {
            val granted = healthConnectClient?.permissionController?.getGrantedPermissions()
            if (granted?.containsAll(permissions) == false) {
                permissionRequest?.await(permissions)
            }
        }
    }

    private class SuspendActivityResultLauncher(
        activity: ComponentActivity
    ) {
        private var result: CompletableDeferred<Set<String>>? = null

        private val launcher = activity.registerForActivityResult(
            PermissionController
                .createRequestPermissionResultContract()
        ) {
            result?.complete(it)
        }

        suspend fun await(input: Set<String>): Set<String> {
            result = CompletableDeferred()
            launcher.launch(input)
            return result?.await() ?: throw IllegalStateException(EXCEPTION_MESSAGE)
        }
    }

    companion object {
        private const val HEALTH_CONNECT_URI =
            "market://details?url=healthconnect%3A%2F%2Fonboarding"
        private const val MARKET_PACKAGE = "com.android.vending"

        private const val OVERLAY = "overlay"
        private const val CALLER_ID = "callerId"

        private const val EXCEPTION_MESSAGE = "Inconsistent result state"

        private val permissions = setOf(
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getWritePermission(StepsRecord::class)
        )
    }

}