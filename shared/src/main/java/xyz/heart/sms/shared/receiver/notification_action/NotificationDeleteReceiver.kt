package xyz.heart.sms.shared.receiver.notification_action

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import xyz.heart.sms.api.implementation.Account
import xyz.heart.sms.api.implementation.ApiUtils
import xyz.heart.sms.shared.data.DataSource
import xyz.heart.sms.shared.data.MimeType
import xyz.heart.sms.shared.data.model.Message
import xyz.heart.sms.shared.util.CursorUtil
import xyz.heart.sms.shared.util.NotificationUtils
import xyz.heart.sms.shared.util.UnreadBadger
import xyz.heart.sms.shared.widget.MessengerAppWidgetProvider

class NotificationDeleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) {
            return
        }

        Thread {
            val messageId = intent.getLongExtra(xyz.heart.sms.shared.receiver.notification_action.NotificationDeleteReceiver.Companion.EXTRA_MESSAGE_ID, -1)
            val conversationId = intent.getLongExtra(xyz.heart.sms.shared.receiver.notification_action.NotificationDeleteReceiver.Companion.EXTRA_CONVERSATION_ID, -1)

            DataSource.deleteMessage(context, messageId)

            val messages = DataSource.getMessages(context, conversationId, 1)
            var latest: Message? = null

            if (messages.size == 1) {
                latest = messages[0]
            }

            if (latest == null) {
                DataSource.deleteConversation(context, conversationId)
            } else if (latest.mimeType == MimeType.TEXT_PLAIN) {
                DataSource.updateConversation(context, conversationId, true, latest.timestamp, latest.data, latest.mimeType, false)
            }

            // cancel the notification we just replied to or
            // if there are no more notifications, cancel the summary as well
            val unseenMessages = DataSource.getUnseenMessages(context)
            if (unseenMessages.count <= 0) {
                NotificationUtils.cancelAll(context)
            } else {
                NotificationManagerCompat.from(context).cancel(conversationId.toInt())
            }

            CursorUtil.closeSilent(unseenMessages)

            ApiUtils.dismissNotification(Account.accountId,
                    Account.deviceId,
                    conversationId)

            xyz.heart.sms.shared.receiver.ConversationListUpdatedReceiver.Companion.sendBroadcast(context, conversationId,
                    if (latest != null && latest.mimeType == MimeType.TEXT_PLAIN) latest.data else MimeType.getTextDescription(context, latest?.mimeType),
                    true)

            UnreadBadger(context).clearCount()
            MessengerAppWidgetProvider.refreshWidget(context)
        }.start()
    }

    companion object {
        const val EXTRA_CONVERSATION_ID = "conversation_id"
        const val EXTRA_MESSAGE_ID = "message_id"
    }
}
