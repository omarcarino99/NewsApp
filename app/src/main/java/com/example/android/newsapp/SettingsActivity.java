package com.example.android.newsapp;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_display);

            Preference datePref = findPreference(getString(R.string.settings_order_by_date_key));
            bindPrefSummary(datePref);

            Preference topicPref = findPreference(getString(R.string.settings_order_by_topic_key));
            bindPrefSummary(topicPref);
        }

        private void bindPrefSummary(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String stringPref = sharedPreferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, stringPref);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringPref = newValue.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int indexOfPref = listPreference.findIndexOfValue(stringPref);
                if (indexOfPref >= 0) {
                    CharSequence[] charSequence = listPreference.getEntries();
                    preference.setSummary(charSequence[indexOfPref]);
                }
            } else {
                preference.setSummary(stringPref);
            }
            return true;
        }
    }
}
