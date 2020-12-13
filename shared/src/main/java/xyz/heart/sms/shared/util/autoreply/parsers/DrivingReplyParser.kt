package xyz.heart.sms.shared.util.autoreply.parsers

import android.content.Context
import xyz.heart.sms.shared.data.Settings
import xyz.heart.sms.shared.data.model.AutoReply
import xyz.heart.sms.shared.data.model.Conversation
import xyz.heart.sms.shared.data.model.Message
import xyz.heart.sms.shared.util.autoreply.AutoReplyParser

class DrivingReplyParser(context: Context?, reply: AutoReply) : AutoReplyParser(context, reply) {

    override fun canParse(conversation: Conversation, message: Message) = Settings.drivingMode

}
