package xyz.heart.sms.shared.service.jobs

import android.content.Context
import androidx.work.*
import xyz.heart.sms.shared.data.DataSource
import xyz.heart.sms.shared.data.Settings
import xyz.heart.sms.shared.util.TimeUtils
import java.util.concurrent.TimeUnit

class CleanupOldMessagesWork(private val context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val timeout = Settings.cleanupMessagesTimeout
        if (timeout > 0) {
            DataSource.cleanupOldMessages(context, TimeUtils.now - timeout)
        }

        scheduleNextRun(context)
        return Result.success()
    }

    companion object {

        fun scheduleNextRun(context: Context) {
            val time = TimeUtils.millisUntilHourInTheNextDay(3)
            val work = OneTimeWorkRequest.Builder(CleanupOldMessagesWork::class.java)
                    .setInitialDelay(time, TimeUnit.MILLISECONDS)
                    .build()

            WorkManager.getInstance().enqueueUniqueWork("cleanup-old-messages", ExistingWorkPolicy.REPLACE, work)
        }
    }
}
