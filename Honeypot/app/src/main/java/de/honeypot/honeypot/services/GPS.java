package de.honeypot.honeypot.services;

import android.app.Activity;
import android.location.Location;

import de.honeypot.honeypot.services.location.GPSProvider;
import de.honeypot.honeypot.services.location.testing.GPSListener;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class GPS {

    //General object for accessing GPS functionality
    private static GPSProvider gpsProvider;
    private static NearbyDetectionThread nearbyDetectionThread;

    public static void init(Activity activity)
    {
        gpsProvider = new GPSProvider(activity, new Listener());
        nearbyDetectionThread = new NearbyDetectionThread();
    }

    public static Location getLocation()
    {
        return gpsProvider.getLastKnownLocation();
    }

    public static class Listener implements GPSListener
    {
        @Override
        public void onUpdate(Location location) {

        }
    }

}
