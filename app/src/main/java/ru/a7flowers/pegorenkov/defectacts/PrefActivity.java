package ru.a7flowers.pegorenkov.defectacts;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class PrefActivity extends PreferenceActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFragment()).commit();
    }

    public static class PrefFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(final Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_server)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_user)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_password)));

            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_prefix)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sufix)));
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);

            onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if(preference.getKey().equals(getString(R.string.pref_password))){
                stringValue = "***********";
            }

            preference.setSummary(stringValue);

            return true;
        }
    }
}
