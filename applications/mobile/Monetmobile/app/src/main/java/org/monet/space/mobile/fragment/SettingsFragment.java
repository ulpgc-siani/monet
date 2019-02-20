package org.monet.space.mobile.fragment;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.preference.PreferenceFragmentCompat;

import org.monet.space.mobile.R;

public class SettingsFragment extends PreferenceFragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    addPreferencesFromResource(R.xml.preferences);
    
    final ListPreference prefList = (ListPreference) findPreference("pref_key_sync_interval");
    prefList.setDefaultValue(prefList.getEntryValues()[0]);
    String ss = prefList.getValue();
    if (ss == null) {
        prefList.setValue((String)prefList.getEntryValues()[0]);
        ss = prefList.getValue();
    }
    prefList.setSummary(prefList.getEntries()[prefList.findIndexOfValue(ss)]);


    prefList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            prefList.setSummary(prefList.getEntries()[prefList.findIndexOfValue(newValue.toString())]);
            return true;
        }
    }); 
  }

}
