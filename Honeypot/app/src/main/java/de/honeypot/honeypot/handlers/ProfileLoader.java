package de.honeypot.honeypot.handlers;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.zip.Inflater;

import de.honeypot.honeypot.R;

public class ProfileLoader extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_loader);
    }
}
