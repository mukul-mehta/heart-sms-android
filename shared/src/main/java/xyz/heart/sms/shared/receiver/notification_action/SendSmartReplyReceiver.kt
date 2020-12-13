package xyz.heart.sms.shared.receiver.notification_action

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import android.util.Log

import xyz.heart.sms.shared.R
import xyz.heart.sms.api.implementation.Account
import xyz.heart.sms.api.implementation.ApiUtils
import xyz.heart.sms.api.implementation.firebase.AnalyticsHelper
import xyz.heart.sms.shared.data.DataSource
import xyz.heart.sms.shared.data.MimeType
import xyz.heart.sms.shared.data.model.Message
import xyz.heart.sms.shared.receiver.ConversationListUpdatedReceiver
import xyz.heart.sms.shared.receiver.MessageListUpdatedReceiver
import xyz.heart.sms.shared.service.ReplyService
import xyz.heart.sms.shared.util.*
import xyz.heart.sms.shared.widget.MessengerAppWidgetProvider

class SendSmartReplyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reply = intent.getStringExtra(ReplyService.EXTRA_REPLY) ?: return
        val conversationId = intent.getLongExtra(ReplyService.EXTRA_CONVERSATION_ID, -1)

        if (conversationId == -1L) {
            Log.e(TAG, "could not find attached conversation id")
            return
        }

        val conversation = DataSource.getConversation(context, conversationId) ?: return

        val m = Message()
        m.conversationId = conversationId
        m.type = Message.TYPE_SENDING
        m.data = reply
        m.timestamp = TimeUtils.now
        m.mimeType = MimeType.TEXT_PLAIN
        m.read = true
        m.seen = true
        m.from = null
        m.color = null
        m.simPhoneNumber = if (conversation.simSubscriptionId != null)
            DualSimUtils
                    .getPhoneNumberFromSimSubscription(conversation.simSubscriptionId!!)
        else
            null
        m.sentDeviceId = if (Account.exists()) java.lang.Long.parseLong(Account.deviceId!!) else -1L

        DataSource.insertMessage(context, m, conversationId)
        DataSource.readConversation(context, conversationId)

        Log.v(TAG, "sending message $reply to ${conversation.phoneNumbers}")

        SendUtils(conversation.simSubscriptionId).send(context, reply, conversation.phoneNumbers!!)

        // cancel the notification we just replied to or
        // if there are no more notifications, cancel the summary as well
        val unseenMessages = DataSource.getUnseenMessages(context)
        if (unseenMessages.count <= 0) {
            try {
                NotificationUtils.cancelAll(context)
            } catch (e: SecurityException) {
            }
        } else {
            NotificationManagerCompat.from(context).cancel(conversationId.toInt())
        }

        ApiUtils.dismissNotification(Account.accountId, Account.deviceId, conversationId)

        unseenMessages.closeSilent()

        ConversationListUpdatedReceiver.sendBroadcast(context, conversationId, context.getString(R.string.you) + ": " + reply, true)
        MessageListUpdatedReceiver.sendBroadcast(context, conversationId)
        MessengerAppWidgetProvider.refreshWidget(context)

        AnalyticsHelper.sendSmartReply(context)
    }

    companion object {
        private const val TAG = "SmartReplySender"
    }
}
