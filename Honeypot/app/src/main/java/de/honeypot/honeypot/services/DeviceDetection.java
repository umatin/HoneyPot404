package de.honeypot.honeypot.services;

import android.location.Location;

import de.honeypot.honeypot.data.NearbyObject;
import de.honeypot.honeypot.handlers.NetworkAdapter;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class DeviceDetection extends Thread {

    public static final int DETECTION_PERIOD = 100000;

    public static NetworkAdapter.NearbyDevice[] nearbyDevices = new NetworkAdapter.NearbyDevice[0];

    public static synchronized NetworkAdapter.NearbyDevice[] getNearby() {
        return nearbyDevices;
    }

    @Override
    public void run()
    {
        while (true)
        {
            try {
                Thread.sleep(DETECTION_PERIOD);
            } catch (InterruptedException e){
                return;
            }

            Location location = null;//GPS.getLocation();
            double latitude = 0.0;
            double longitude = 0.0;

            if (location != null)
            {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            nearbyDevices = NetworkAdapter.getInstance().getNearbyDevices(latitude, longitude);
        }
    }
}
