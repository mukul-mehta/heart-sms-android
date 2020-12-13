package xyz.heart.sms.shared.service.notification

import xyz.heart.sms.shared.util.TimeUtils

interface ShortcutUpdater {

    fun refreshDynamicShortcuts(delay: Long = 10 * TimeUtils.SECOND)

}