package xyz.heart.sms.shared.util.autoreply.parsers

import android.content.Context
import xyz.heart.sms.shared.data.MimeType
import xyz.heart.sms.shared.data.model.AutoReply
import xyz.heart.sms.shared.data.model.Conversation
import xyz.heart.sms.shared.data.model.Message
import xyz.heart.sms.shared.util.autoreply.AutoReplyParser

class KeywordReplyParser(context: Context?, reply: AutoReply) : AutoReplyParser(context, reply) {

    override fun canParse(conversation: Conversation, message: Message): Boolean {
        if (reply.pattern == null || message.mimeType != MimeType.TEXT_PLAIN) {
            return false
        }

        return message.data!!.toLowerCase().contains(reply.pattern!!.toLowerCase())
    }

}