package xyz.heart.sms.shared.service

import android.app.IntentService
import android.content.Intent
import android.os.Build
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import xyz.heart.sms.api.implementation.Account
import xyz.heart.sms.api.implementation.ApiUtils
import xyz.heart.sms.api.implementation.firebase.AnalyticsHelper
import xyz.heart.sms.api.implementation.firebase.FirebaseApplication
import xyz.heart.sms.shared.data.Settings

class FirebaseTokenUpdateCheckService : IntentService("FirebaseTokenRefresh") {

    companion object {
        private const val TOKEN_PREF_KEY = "stored-firebase-token"
    }

    override fun onHandleIntent(intent: Intent?) {
        if (!Account.exists()) {
           return
        }

        val sharedPrefs = Settings.getSharedPrefs(this)
        val storedToken = sharedPrefs.getString(TOKEN_PREF_KEY, null)

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val currentToken = task.result?.token

                    // upload to server
                    if (currentToken != null && currentToken != storedToken) {
                        AnalyticsHelper.updatingFcmToken(this)
                        sharedPrefs.edit().putString(TOKEN_PREF_KEY, currentToken).apply()

                        Thread {
                            ApiUtils.updateDevice(Account.accountId, Integer.parseInt(Account.deviceId!!).toLong(), Build.MODEL,
                                    currentToken)
                        }.start()
                    }
                })

    }
}