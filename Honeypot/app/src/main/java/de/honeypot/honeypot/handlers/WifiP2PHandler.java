package de.honeypot.honeypot.handlers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import de.honeypot.honeypot.MainActivity;
import de.honeypot.honeypot.data.NearbyObject;
import de.honeypot.honeypot.services.NearbyDetectionThread;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class WifiP2PHandler {

    public static void informUser(NearbyObject detectedUser)
    {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.instance);

        notificationBuilder.setSmallIcon(android.R.drawable.btn_minus);
        notificationBuilder.setContentTitle("Honeypot - User nearby!");
        notificationBuilder.setContentText("A user has been found nearby, click hear, to see him and say hello.");
        notificationBuilder.build();

        //Add intent
        Intent resultIntent = new Intent(MainActivity.instance, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.instance);
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);

        ((NotificationManager)MainActivity.instance.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notificationBuilder.build());
    }

    public static void handleDeviceDetected(String deviceAddress)
    {
        NearbyObject detectedUser = compareToNearby(deviceAddress);

        if(detectedUser == null)
        {
            System.out.println("detected user is not nearby");
            return;
        }
        else
        {
            System.out.println("found user nearby");
            informUser(detectedUser);
        }
    }

    public static NearbyObject compareToNearby(String deviceAddress)
    {
        NearbyObject[] nearbyObjects = NearbyDetectionThread.nearbyObjects;

        synchronized (nearbyObjects)
        {
            for(NearbyObject nearbyObject : nearbyObjects)
            {
                if(nearbyObject.getDevice().equals(deviceAddress))
                    return nearbyObject;
            }
        }
        return null;
    }


}
