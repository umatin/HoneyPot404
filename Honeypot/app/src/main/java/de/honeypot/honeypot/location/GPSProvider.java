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

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class GPSProvider implements LocationListener {

    public static final boolean ALLOW_MOCK_LOCATION = false;

    LocationManager locationManager;
    Activity activity;

    Location currentLocation = null;


    public GPSProvider(Activity activity) {
        this.activity = activity;
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
        //TODO: maybe remove network provider
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }



    public Location getLastKnownLocation()
    {
        return this.currentLocation;
    }




    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;

        //TODO: use
        location.getProvider();
        location.getAccuracy();
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