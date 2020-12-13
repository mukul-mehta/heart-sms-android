package xyz.heart.sms.activity.main

import xyz.heart.sms.activity.MessengerActivity
import xyz.heart.sms.api.implementation.Account
import xyz.heart.sms.shared.MessengerActivityExtras
import xyz.heart.sms.shared.util.PermissionsUtils

class MainPermissionHelper(private val activity: MessengerActivity) {

    fun requestPermissions() {
        if (PermissionsUtils.checkRequestMainPermissions(activity)) {
            PermissionsUtils.startMainPermissionRequest(activity)
        }
    }

    fun requestDefaultSmsApp() {
        if (Account.primary && !PermissionsUtils.isDefaultSmsApp(activity)) {
            PermissionsUtils.setDefaultSmsApp(activity)
        }
    }

    fun handlePermissionResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        try {
            PermissionsUtils.processPermissionRequest(activity, requestCode, permissions, grantResults)
            if (requestCode == MessengerActivityExtras.REQUEST_CALL_PERMISSION) {
                activity.navController.messageActionDelegate.callContact()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}