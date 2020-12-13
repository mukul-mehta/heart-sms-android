/*
 * Copyright (C) 2020 Luke Klinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.heart.sms.shared.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import xyz.heart.sms.api.implementation.firebase.ScheduledTokenRefreshService
import xyz.heart.sms.shared.data.Settings
import xyz.heart.sms.shared.service.QuickComposeNotificationService
import xyz.heart.sms.shared.service.jobs.*
import xyz.heart.sms.shared.util.UpdateUtils

/**
 * Receiver for when boot has completed. This will be responsible for starting up the content
 * observer service and scheduling any messages that are to be sent in the future.
 */
class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
                UpdateUtils.rescheduleWork(context)

                if (Settings.quickCompose) QuickComposeNotificationService.start(context)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

}
