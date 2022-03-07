package com.kiylx.downloader.ui.fragments.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.kiylx.downloader.R
import com.kiylx.downloader.ui.fragments.active.ActiveFragment

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
    companion object {
        @JvmStatic
        fun newInstance() = ActiveFragment()
    }
}