/*
 * Copyright © 2018 Syberia Project
 * Date: 22.08.2018
 * Time: 21:21
 * Author: @alexxxdev <alexxxdev@ya.ru>
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

package com.syberia.settings.fragments;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;

import com.syberia.settings.preference.CustomSeekBarPreference;

public class ButtonsSettings extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener{

	private static final String KEY_BUTTON_BRIGHTNESS = "button_brightness";
    private static final String KEY_BACKLIGHT_TIMEOUT = "backlight_timeout";

    private static final String CATEGORY_HWKEY = "hardware_keys";
    
    private ListPreference mBacklightTimeout;
    private CustomSeekBarPreference mButtonBrightness;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.buttons_settings);

		mBacklightTimeout = (ListPreference) findPreference(KEY_BACKLIGHT_TIMEOUT);
		mButtonBrightness = (CustomSeekBarPreference) findPreference(KEY_BUTTON_BRIGHTNESS);
		if (mBacklightTimeout != null) {
			mBacklightTimeout.setOnPreferenceChangeListener(this);
			int BacklightTimeout = Settings.System.getInt(getContentResolver(), Settings.System.BUTTON_BACKLIGHT_TIMEOUT, 5000);
			mBacklightTimeout.setValue(Integer.toString(BacklightTimeout));
			mBacklightTimeout.setSummary(mBacklightTimeout.getEntry());
		}
		if (mButtonBrightness != null) {
			int ButtonBrightness = Settings.System.getInt(getContentResolver(),	Settings.System.BUTTON_BRIGHTNESS, 255);
			mButtonBrightness.setValue(ButtonBrightness / 1);
			mButtonBrightness.setOnPreferenceChangeListener(this);
		}
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.SYBERIA;
    }

	@Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
		ContentResolver resolver = getActivity().getContentResolver();

		if (preference == mBacklightTimeout) {
            String BacklightTimeout = (String) newValue;
            int BacklightTimeoutValue = Integer.parseInt(BacklightTimeout);
            Settings.System.putInt(getActivity().getContentResolver(), Settings.System.BUTTON_BACKLIGHT_TIMEOUT, BacklightTimeoutValue);
            int BacklightTimeoutIndex = mBacklightTimeout.findIndexOfValue(BacklightTimeout);
            mBacklightTimeout.setSummary(mBacklightTimeout.getEntries()[BacklightTimeoutIndex]);
            return true;
        } else if (preference == mButtonBrightness) {
            int value = (Integer) newValue;
            Settings.System.putInt(getActivity().getContentResolver(), Settings.System.BUTTON_BRIGHTNESS, value * 1);
            return true;
        }
        return false;
    }
}