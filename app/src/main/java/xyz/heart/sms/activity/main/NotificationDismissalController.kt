package xyz.heart.sms.activity.main

import xyz.heart.sms.activity.MessengerActivity
import xyz.heart.sms.api.implementation.Account
import xyz.heart.sms.api.implementation.ApiUtils
import xyz.heart.sms.shared.MessengerActivityExtras

class NotificationDismissalController(private val activity: MessengerActivity) {

    private val intent
        get() = activity.intent
}