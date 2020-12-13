package xyz.heart.sms.shared.util.vcard

import android.content.Context
import xyz.heart.sms.api.implementation.Account
import xyz.heart.sms.shared.data.model.Message

abstract class VcardParser(protected var context: Context?) {

    abstract fun canParse(message: Message): Boolean
    abstract fun getData(message: Message): String
    abstract fun getMimeType(message: Message): String

    fun parse(forMessage: Message): Message? {
        val message = Message()
        message.conversationId = forMessage.conversationId
        message.timestamp = forMessage.timestamp + 1
        message.type = if (forMessage.type == Message.TYPE_RECEIVED) Message.TYPE_RECEIVED else Message.TYPE_SENT
        message.read = forMessage.type != Message.TYPE_RECEIVED
        message.seen = forMessage.type != Message.TYPE_RECEIVED
        message.mimeType = getMimeType(forMessage)
        message.data = getData(forMessage)
        message.sentDeviceId =  if (Account.exists()) Account.deviceId!!.toLong() else -1L

        return if (message.data == null) null else message
    }
}