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

package xyz.heart.sms.fragment.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import xyz.heart.sms.R
import xyz.heart.sms.api.implementation.Account
import xyz.heart.sms.api.implementation.ApiUtils
import xyz.heart.sms.shared.data.Settings

/**
 * Fragment for modifying app settings_global.
 */
@Suppress("DEPRECATION")
class OpenSourceExperimentsFragment : MaterialPreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.settings_experiments)

        initOpenSourceLink()

        initSmartReplyTimeout()
    }

    private fun initOpenSourceLink() {
        findPreference(getString(R.string.pref_open_source_link))
                .setOnPreferenceClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://github.com/klinker-apps/pulse-sms-android")
                    startActivity(intent)
                    true
                }
    }

    private fun initSmartReplyTimeout() {
        val preference = findPreference(getString(R.string.pref_smart_reply_timeout))
        preference.setOnPreferenceChangeListener { _, o ->
            val useSmartReplyTimeout = o as Boolean
            ApiUtils.updateSmartReplyTimeout(Account.accountId, useSmartReplyTimeout)
            true
        }
    }

    private fun initBlacklistPhraseRegex() {
        val preference = findPreference(getString(R.string.pref_blacklist_phrase_regex))
        preference.setOnPreferenceChangeListener { _, o ->
            val useBlacklistPhraseRegex = o as Boolean;
            ApiUtils.updateBlacklistPhraseRegex(Account.accountId, useBlacklistPhraseRegex)
            true
        }
    }

    override fun onStop() {
        super.onStop()
        Settings.forceUpdate(activity)
    }
}
