package com.mua.mobileattendance.helper


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionHelper(
    private val fragmentOrActivity: Any,
    private val permission: String,
    private val requestCode: Int = getNextRequestCode()
) {

    private companion object {
        private var _nextRequestCode: Int = 22576
        private fun getNextRequestCode() = _nextRequestCode++
    }

    private val targetActivity = when (fragmentOrActivity) {
        is Activity -> fragmentOrActivity
        is Fragment -> fragmentOrActivity.activity
        else -> throw IllegalArgumentException("You should provide Fragment or Activity type argument")
    }

    private fun shouldShowRequestPermissionRationale() =
        if (fragmentOrActivity is Fragment) {
            fragmentOrActivity.shouldShowRequestPermissionRationale(permission)
        } else {
            ActivityCompat.shouldShowRequestPermissionRationale(
                fragmentOrActivity as Activity,
                permission
            )
        }

    fun isGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            targetActivity!!,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    fun request() {
        if (fragmentOrActivity is Fragment) {
            fragmentOrActivity.requestPermissions(arrayOf(permission), requestCode)
        } else {
            ActivityCompat.requestPermissions(
                fragmentOrActivity as Activity,
                arrayOf(permission),
                requestCode
            )
        }
    }

    fun onRequestPermissionResult(
        requestCode: Int,
        grantResults: IntArray,
        permissionResult: PermissionResult.() -> Unit
    ): Boolean =
        if (requestCode == this.requestCode) {
            val pr = PermissionResult()
            pr.permissionResult()
            if (grantResults.isNotEmpty()) {
                when (grantResults[0]) {
                    PackageManager.PERMISSION_GRANTED -> pr.grantedCallback?.invoke()
                    PackageManager.PERMISSION_DENIED -> pr.deniedCallback?.invoke(!shouldShowRequestPermissionRationale())
                }
            }
            true
        } else {
            false
        }

    fun showApplicationDetailsSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.fromParts("package", targetActivity?.packageName, null)
        targetActivity?.startActivity(intent)
    }

    class PermissionResult {
        internal var grantedCallback: (() -> Unit)? = null
        internal var deniedCallback: ((isPermanent: Boolean) -> Unit)? = null

        fun onGranted(callback: () -> Unit) {
            grantedCallback = callback
        }

        fun onDenied(callback: (isPermanent: Boolean) -> Unit) {
            deniedCallback = callback
        }
    }

}