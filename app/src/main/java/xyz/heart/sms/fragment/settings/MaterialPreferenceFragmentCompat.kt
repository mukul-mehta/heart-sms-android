package xyz.heart.sms.fragment.settings

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import xyz.heart.sms.R

@Suppress("DEPRECATION")
abstract class MaterialPreferenceFragmentCompat : PreferenceFragmentCompat() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.setBackgroundColor(resources.getColor(R.color.drawerBackground))
    }

    fun findPreference(key: String) = findPreference<Preference>(key as CharSequence)!!
}
