package xyz.heart.sms.shared.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CarReadReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        xyz.heart.sms.shared.receiver.notification_action.NotificationMarkReadReceiver.handle(intent, context)
    }
}
