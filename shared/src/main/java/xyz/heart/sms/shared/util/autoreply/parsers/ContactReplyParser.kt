package xyz.heart.sms.shared.util.autoreply.parsers

import android.content.Context
import xyz.heart.sms.shared.data.model.AutoReply
import xyz.heart.sms.shared.data.model.Conversation
import xyz.heart.sms.shared.data.model.Message
import xyz.heart.sms.shared.util.SmsMmsUtils
import xyz.heart.sms.shared.util.autoreply.AutoReplyParser

class ContactReplyParser(context: Context?, reply: AutoReply) : AutoReplyParser(context, reply) {

    override fun canParse(conversation: Conversation, message: Message): Boolean {
        if (reply.pattern == null) {
            return false
        }

        val autoReplyIdMatcher = SmsMmsUtils.createIdMatcher(reply.pattern!!)
        val receivedMessageMatcher = SmsMmsUtils.createIdMatcher(conversation.phoneNumbers!!)

        return autoReplyIdMatcher.default == receivedMessageMatcher.default
    }

}
