package de.honeypot.honeypot.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    public static final boolean ALLOW_MOCK_LOCATION = true;

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

    private void init()
    {
        String provider = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps"))
        {
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Please enable GPS to use this app!", Toast.LENGTH_LONG).show();
                }
            });

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            activity.startActivityForResult(intent, 1);
        }

        if(!Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0") && !ALLOW_MOCK_LOCATION)
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Please turn of GPS mocking to use this app!", Toast.LENGTH_LONG).show();
                }
            });

            try{Thread.sleep(1000);}catch(InterruptedException e){}

            System.exit(-1);
        }



        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        //Request updates for both providers
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    System.exit(5);
                }
                else
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, GPSProvider.this);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, GPSProvider.this);
                }
            }
        });
    }



    public Location getLastKnownLocation()
    {
        if(currentLocationGPS != null && ((System.currentTimeMillis() - currentLocationGPS.getTime()) < 10000 && currentLocationGPS.getAccuracy() < currentLocationNetwork.getAccuracy()))
            return getGPSLocation();
        else
            return getNetworkLocation();
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