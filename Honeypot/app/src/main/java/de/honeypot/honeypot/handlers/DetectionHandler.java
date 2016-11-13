package de.honeypot.honeypot.handlers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import java.util.logging.Logger;

import de.honeypot.honeypot.MainActivity;
import de.honeypot.honeypot.data.NearbyObject;
import de.honeypot.honeypot.services.DeviceDetection;
import de.honeypot.honeypot.services.wifip2p.WifiPeerListener;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class DetectionHandler  implements WifiPeerListener {
    private final static Logger logger = Logger.getLogger("DetectionHandler");
    
    public void informUser(NetworkAdapter.NearbyDevice detectedUser) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.instance);

        notificationBuilder.setSmallIcon(android.R.drawable.btn_minus);
        notificationBuilder.setContentTitle("A honeybee is around 'ya!");
        notificationBuilder.setContentText("A user has been found nearby, click hear, to see him and say hello.");
        notificationBuilder.build();

        //Add intent
        Intent resultIntent = new Intent(MainActivity.instance, MainActivity.class);
        resultIntent.putExtra("profile", detectedUser.getID());
        resultIntent.putExtra("discover", true);
        resultIntent.putExtra("device", detectedUser.getDeviceID());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.instance);
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);

        ((NotificationManager) MainActivity.instance.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notificationBuilder.build());
    }

    public NetworkAdapter.NearbyDevice compareDevice(String deviceID) {
        NetworkAdapter.NearbyDevice[] devices = DeviceDetection.getNearby();

        synchronized (devices) {
            for (NetworkAdapter.NearbyDevice nearbyDevice : devices) {
                if (nearbyDevice.getDeviceID().equals(deviceID)) {
                    return nearbyDevice;
                }
            }
        }

        return null;
    }


    @Override
    public void deviceDiscovered(String address) {
        NetworkAdapter.NearbyDevice detectedUser = compareDevice(address);

        if (detectedUser == null) {
            logger.info("Detected device is not your honey");
        } else {
            logger.info("Detected device is nearly your honey");
            informUser(detectedUser);
        }
    }

    @Override
    public void onWifiDisabled() {

    }

    @Override
    public void onWifiEnabled() {

    }
}
