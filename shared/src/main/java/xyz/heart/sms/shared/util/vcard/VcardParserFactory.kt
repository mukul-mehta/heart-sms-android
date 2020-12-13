package xyz.heart.sms.shared.util.vcard

import android.content.Context
import xyz.heart.sms.shared.data.model.Message
import xyz.heart.sms.shared.util.vcard.parsers.MapLocationVcardParser
import xyz.heart.sms.shared.util.vcard.parsers.TextAttributeVcardParser

class VcardParserFactory {

    fun getInstances(context: Context, message: Message): List<VcardParser> {
        message.data = VcardReader.readCotactCard(context, message.data!!)
        return buildParsers(context).filter { it.canParse(message) }
    }

    private fun buildParsers(context: Context): List<VcardParser> {
        return listOf(MapLocationVcardParser(context), TextAttributeVcardParser(context))
    }

}