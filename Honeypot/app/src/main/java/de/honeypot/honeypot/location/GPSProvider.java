package de.honeypot.honeypot.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import de.honeypot.honeypot.location.testing.GPSListener;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class GPSProvider implements LocationListener {

    public static final boolean ALLOW_MOCK_LOCATION = false;

    private GPSListener gpsListener;

    private LocationManager locationManager;
    private Activity activity;

    private Location currentLocationNetwork = null;
    private Location currentLocationGPS = null;

    public GPSProvider(Activity activity)
    {
        this.activity = activity;
        init();
    }

    public GPSProvider(Activity activity, GPSListener gpsListener) {
        this.activity = activity;
        this.gpsListener = gpsListener;
        init();
    }

    private void init() {

        //TODO: turn gps on if necessary
        if(Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Please turn of GPS mocking to use this app!", Toast.LENGTH_LONG).show();
                }
            });

            System.exit(-1);//TODO: overkill
        }



        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //Request updates for both providers
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }



    public Location getLastKnownLocation()
    {
        //return this.currentLocation;
        return null;//TODO:
    }

    public Location getNetworkLocation()
    {
        return currentLocationNetwork;
    }

    public Location getGPSLocation()
    {
        return currentLocationGPS;
    }


    @Override
    public void onLocationChanged(Location location) {

        //TODO: use
        if(location.getProvider().equals(LocationManager.GPS_PROVIDER))
        {
            currentLocationGPS = location;
        }
        else if(location.getProvider().equals(LocationManager.NETWORK_PROVIDER))
        {
            currentLocationNetwork = location;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(gpsListener != null)
                    gpsListener.onUpdate(getLastKnownLocation());
            }
        }).start();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Please enable GPS to use this app!", Toast.LENGTH_LONG).show();
            }
        });
    }
}