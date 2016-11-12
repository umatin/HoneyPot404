package de.honeypot.honeypot;

import android.content.Intent;
import android.preference.PreferenceActivity;
import android.os.Bundle;

import de.honeypot.honeypot.R;
import de.honeypot.honeypot.Settings;

public class SettingsLoader extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent( this, Settings.class );
        intent.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT, Settings.class.getName() );
        intent.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
    }
}
