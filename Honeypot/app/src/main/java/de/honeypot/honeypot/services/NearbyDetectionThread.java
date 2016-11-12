package de.honeypot.honeypot.services;

import android.location.Location;

import org.json.JSONObject;

import de.honeypot.honeypot.data.NearbyObject;
import de.honeypot.honeypot.handlers.Network;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class NearbyDetectionThread extends Thread {

    public static final int DETECTION_PERIOD = 10000;

    public static NearbyObject[] nearbyObjects = new NearbyObject[0];//Access synchronized

    public NearbyDetectionThread()
    {

        this.start();
    }


    @Override
    public void run()
    {
        while(true)
        {
            try{Thread.sleep(DETECTION_PERIOD);}catch(InterruptedException e){}

            Location location = GPS.getLocation();
            double latitude = 0.0;
            double longitude = 0.0;

            if(location != null)
            {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            NearbyObject[] near = Network.nearby(latitude, longitude);

            synchronized (nearbyObjects)
            {
                nearbyObjects = near;
            }
        }
    }
}
