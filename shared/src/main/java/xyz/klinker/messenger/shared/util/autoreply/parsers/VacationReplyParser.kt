package xyz.klinker.messenger.shared.util.autoreply.parsers

import android.content.Context
import xyz.klinker.messenger.shared.data.model.AutoReply
import xyz.klinker.messenger.shared.util.autoreply.AutoReplyParser
import java.util.regex.Pattern

class VacationReplyParser(context: Context?, reply: AutoReply) : AutoReplyParser(context, reply) {

    override fun canParse(phoneNumber: String, text: String) = true

}