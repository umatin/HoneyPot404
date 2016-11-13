package de.honeypot.honeypot.services.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.logging.Logger;

import de.honeypot.honeypot.services.location.testing.GPSListener;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class GPSProvider implements LocationListener {
    private static final Logger logger = Logger.getLogger("GPSProvider");
    public static final boolean ALLOW_MOCK_LOCATION = true;

    private static GPSProvider instance;

    public static GPSProvider getInstance(GPSListener listener) {
        if (instance == null) instance = new GPSProvider(listener);
        return instance;
    }

    private GPSListener listener;

    private LocationManager locationManager;
    private Activity uiActivity;

    private Location currentLocationNetwork = null;
    private Location currentLocationGPS = null;

    public GPSProvider(GPSListener gpsListener) {
        this.listener = gpsListener;
    }

    public void start(Activity a) {
        if (this.uiActivity != null) stop();
        final Activity activity = a;
        this.uiActivity = activity;

        String provider = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) {
            Toast.makeText(activity, "Please enable GPS to use this app!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            activity.startActivityForResult(intent, 1);
        }

        if (!Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0") && !ALLOW_MOCK_LOCATION) {

            System.exit(1);
        }


        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, GPSProvider.this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, GPSProvider.this);
        }

    }

    public void stop() {
        logger.info("Stopping GPS Provider");
    }


    public Location getLastKnownLocation() {
        if (currentLocationGPS != null && ((System.currentTimeMillis() - currentLocationGPS.getTime()) < 10000 && currentLocationGPS.getAccuracy() < currentLocationNetwork.getAccuracy()))
            return getGPSLocation();
        else
            return getNetworkLocation();
    }

    public Location getNetworkLocation() {
        return currentLocationNetwork;
    }

    public Location getGPSLocation() {
        return currentLocationGPS;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            currentLocationGPS = location;
        } else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            currentLocationNetwork = location;
        }

        if (location != null) listener.onUpdate(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        uiActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(uiActivity, "Please enable GPS to use this app!", Toast.LENGTH_LONG).show();
            }
        });
    }
}