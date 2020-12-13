package xyz.heart.sms.shared.util

import android.app.Activity
import android.os.Handler
import com.sensortower.rating.RatingPrompt
import com.sensortower.rating.RatingPromptOptions
import com.sensortower.rating.util.RatingPromptSettings
import xyz.heart.sms.api.implementation.Account
import xyz.heart.sms.shared.data.Settings

class PromotionUtils(private val context: Activity) {

    fun checkPromotions(onTrialExpired: () -> Unit) {
        if (trialExpired()) {
            onTrialExpired()
        } else {
            askForRating()
        }
    }

    private fun trialExpired(): Boolean {
        return Account.exists() && Account.subscriptionType == Account.SubscriptionType.FREE_TRIAL && Account.getDaysLeftInTrial() <= 0
    }

    private fun askForRating() {
        if (Account.exists() && !Account.primary) {
            // only prompt for rating on the primary device
            return
        }

        Handler().postDelayed({
            RatingPrompt.show(context, RatingPromptOptions.Builder()
                    .useAlternateStyle(RatingPromptOptions.Popup.Builder("Pulse")
                            .accentColor(Settings.mainColorSet.color)
                            .darkTheme(Settings.isCurrentlyDarkTheme(context))
                    ).build())
        }, 500)
    }

}
