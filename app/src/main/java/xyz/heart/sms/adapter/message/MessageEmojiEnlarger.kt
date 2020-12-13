package xyz.heart.sms.adapter.message

import xyz.heart.sms.adapter.view_holder.MessageViewHolder
import xyz.heart.sms.shared.data.Settings
import xyz.heart.sms.shared.data.model.Message
import xyz.heart.sms.shared.util.Regex

class MessageEmojiEnlarger {

    fun enlarge(holder: MessageViewHolder, message: Message) {
        if (isNotEmpty(message) && onlyHasEmojis(message)) {
            holder.message!!.textSize = 35f
        } else {
            holder.message?.textSize = Settings.largeFont.toFloat()
        }
    }

    private fun isNotEmpty(message: Message) = message.data!!.isNotEmpty()
    private fun onlyHasEmojis(message: Message) = message.data!!.replace(Regex.EMOJI.toRegex(), "").isEmpty()
}