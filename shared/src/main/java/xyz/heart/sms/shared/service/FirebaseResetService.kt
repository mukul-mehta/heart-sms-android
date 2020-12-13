package xyz.heart.sms.shared.service

import android.app.IntentService
import android.content.Intent
import androidx.core.app.NotificationCompat

import xyz.heart.sms.api.implementation.Account
import xyz.heart.sms.shared.R
import xyz.heart.sms.shared.data.ColorSet
import xyz.heart.sms.shared.data.DataSource
import xyz.heart.sms.shared.util.AndroidVersionUtil
import xyz.heart.sms.shared.util.NotificationUtils

class FirebaseResetService : IntentService("FirebaseResetService") {

    override fun onHandleIntent(intent: Intent?) {
        if (Account.primary) {
            return
        }

        if (AndroidVersionUtil.isAndroidO) {
            val notification = NotificationCompat.Builder(this,
                    NotificationUtils.SILENT_BACKGROUND_CHANNEL_ID)
                    .setContentTitle(getString(R.string.media_parse_text))
                    .setSmallIcon(R.drawable.ic_stat_notify_group)
                    .setProgress(0, 0, true)
                    .setLocalOnly(true)
                    .setColor(ColorSet.DEFAULT(this).color)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .build()
            startForeground(FOREGROUND_ID, notification)
        }

        // going to re-download everything I guess..
        DataSource.clearTables(this)
        ApiDownloadService.start(this)

        if (AndroidVersionUtil.isAndroidO) {
            stopForeground(true)
        }
    }

    companion object {
        private val FOREGROUND_ID = 1223
    }
}
