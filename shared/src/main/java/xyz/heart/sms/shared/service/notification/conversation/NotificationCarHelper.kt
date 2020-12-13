package xyz.heart.sms.shared.service.notification.conversation

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import xyz.heart.sms.shared.R
import xyz.heart.sms.shared.data.MimeType
import xyz.heart.sms.shared.data.pojo.NotificationConversation
import xyz.heart.sms.shared.receiver.notification_action.NotificationMarkReadReceiver
import xyz.heart.sms.shared.service.ReplyService

class NotificationCarHelper(private val service: Context) {

    fun buildExtender(conversation: NotificationConversation, remoteInput: RemoteInput): NotificationCompat.CarExtender {
        val carReply = Intent().addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction("xyz.heart.sms.CAR_REPLY")
                .putExtra(ReplyService.EXTRA_CONVERSATION_ID, conversation.id)
                .setPackage("xyz.heart.sms")
        val pendingCarReply = PendingIntent.getBroadcast(service, conversation.id.toInt(),
                carReply, PendingIntent.FLAG_UPDATE_CURRENT)

        val carRead = Intent().addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction("xyz.heart.sms.CAR_READ")
                .putExtra(NotificationMarkReadReceiver.EXTRA_CONVERSATION_ID, conversation.id)
                .setPackage("xyz.heart.sms")
        val pendingCarRead = PendingIntent.getBroadcast(service, conversation.id.toInt(),
                carRead, PendingIntent.FLAG_UPDATE_CURRENT)

        // Android Auto extender
        val car = NotificationCompat.CarExtender.UnreadConversation.Builder(if (conversation.privateNotification) service.getString(R.string.new_message) else conversation.title)
                .setReadPendingIntent(pendingCarRead)
                .setReplyAction(pendingCarReply, remoteInput)
                .setLatestTimestamp(conversation.timestamp)

        if (!conversation.privateNotification) {
            for ((_, data, mimeType) in conversation.messages) {
                if (mimeType == MimeType.TEXT_PLAIN) {
                    car.addMessage(data)
                } else {
                    car.addMessage(service.getString(R.string.new_mms_message))
                }
            }
        }

        return NotificationCompat.CarExtender().setUnreadConversation(car.build())
    }
}