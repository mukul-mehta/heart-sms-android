package xyz.heart.sms.shared.receiver.notification_action

import android.content.Context
import android.content.Intent
import xyz.heart.sms.shared.MessengerActivityExtras
import xyz.heart.sms.shared.data.DataSource
import xyz.heart.sms.shared.data.Settings

open class NotificationArchiveReceiver : xyz.heart.sms.shared.receiver.notification_action.NotificationMarkReadReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        // mark as read functionality

        if (context == null || intent == null) {
            return
        }

        Thread {
            val conversationId = intent.getLongExtra(xyz.heart.sms.shared.receiver.notification_action.NotificationMarkReadReceiver.EXTRA_CONVERSATION_ID, -1L)
            if (conversationId != -1L) {
                DataSource.archiveConversation(context, conversationId)
                Settings.setValue(context, MessengerActivityExtras.EXTRA_SHOULD_REFRESH_LIST, true)
            }
        }.start()

    }

}
