package xyz.heart.sms.shared.util

import android.content.Context
import me.leolin.shortcutbadger.ShortcutBadger
import xyz.heart.sms.shared.data.DataSource

class UnreadBadger(private val context: Context?) {

    fun clearCount() {
        writeCount(0)
    }

    fun writeCount(newCount: Int) {
        Thread { shortcutBadger(newCount) }.start()
    }

    private fun shortcutBadger(count: Int) {
        if (context != null) {
            try {
                ShortcutBadger.applyCountOrThrow(context, count)
            } catch (e: Exception) {
            }
        }
    }

}
