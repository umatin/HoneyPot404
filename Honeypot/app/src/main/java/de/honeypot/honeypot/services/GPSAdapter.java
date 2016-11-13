package de.honeypot.honeypot.services;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import de.honeypot.honeypot.services.location.GPSProvider;
import de.honeypot.honeypot.services.location.testing.GPSListener;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class GPSAdapter {
    private static GPSAdapter instance;

    public static GPSAdapter getInstance() {
        if (instance == null) instance = new GPSAdapter();
        return instance;
    }

    private GPSProvider gpsProvider;

    public GPSAdapter()
    {

    }

    public class Listener implements GPSListener
    {
        @Override
        public void onUpdate(Location location) {

        }
    }

    public void start(Activity activity) {
        gpsProvider = new GPSProvider(new Listener());
        gpsProvider.start(activity);
    }

    public void stop() {
        gpsProvider.stop();

    }

}
