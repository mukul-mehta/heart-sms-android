package xyz.heart.sms.shared.util

import android.content.Context
import xyz.heart.sms.api.implementation.Account
import xyz.heart.sms.api.implementation.ApiUtils
import xyz.heart.sms.shared.R
import xyz.heart.sms.shared.data.FeatureFlags
import xyz.heart.sms.shared.data.MmsSettings
import xyz.heart.sms.shared.data.Settings

object KotlinObjectInitializers {

    fun initializeObjects(context: Context) {
        try {
            ApiUtils.environment = context.getString(R.string.environment)
        } catch (e: Exception) {
            ApiUtils.environment = "release"
        }

        Account.init(context)
        FeatureFlags.init(context)
        Settings.init(context)
        MmsSettings.init(context)
        DualSimUtils.init(context)
        EmojiInitializer.initializeEmojiCompat(context)
    }
}