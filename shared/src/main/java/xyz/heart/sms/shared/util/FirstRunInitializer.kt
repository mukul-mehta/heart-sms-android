package xyz.heart.sms.shared.util

import android.annotation.SuppressLint
import android.content.Context
import xyz.heart.sms.shared.R
import xyz.heart.sms.shared.data.Settings

object FirstRunInitializer {

    @SuppressLint("ApplySharedPref")
    fun applyDefaultSettings(context: Context) {
        if (Settings.firstStart) {
            Settings.setValue(context, context.getString(R.string.pref_conversation_categories), false)
            Settings.setValue(context, context.getString(R.string.pref_apply_primary_color_toolbar), false)
            Settings.setValue(context, context.getString(R.string.pref_base_theme), if (AndroidVersionUtil.isAndroidQ) "day_night" else "dark")
        }
    }
}