package hu.bme.aut.eventhandler.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import hu.bme.aut.eventhandler.EventHandlerActivity;
import hu.bme.aut.eventhandler.R;
import hu.bme.aut.eventhandler.model.Currency;
import hu.bme.aut.eventhandler.network.CurrencyExchanger;
import hu.bme.aut.eventhandler.network.IGetRateCallback;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, IGetRateCallback {

    private String oldValue;
    private String newValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        ListPreference pref = (ListPreference) findPreference("currency");

        if (pref.getValue() == null) {
            pref.setValue("HUF");
        }

        oldValue = pref.getValue();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("currency")) {
            ListPreference pref = (ListPreference) findPreference(s);
            newValue = pref.getValue();

            CurrencyExchanger changer = new CurrencyExchanger(getContext(), oldValue);
            changer.change(this);

            oldValue = newValue;
        }
    }

    @Override
    public void onSuccess(Currency currency) {
        double value = currency.getRate(newValue);
        Intent i = new Intent();
        i.putExtra(EventHandlerActivity.RATE, value);
        getActivity().setResult(Activity.RESULT_OK, i);
        getActivity().finish();
    }
}
