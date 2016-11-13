package de.honeypot.honeypot.handlers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import de.honeypot.honeypot.MainActivity;

/**
 * Created by user on 13.11.16.
 */

public class HotspotHandler {
    public void informUser(String ssid) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.instance);

        notificationBuilder.setSmallIcon(android.R.drawable.btn_minus);
        notificationBuilder.setContentTitle("A hive to capture!");
        notificationBuilder.setContentText("Tap this notification to capture the hotspot.");
        notificationBuilder.build();

        //Add intent
        Intent resultIntent = new Intent(MainActivity.instance, MainActivity.class);
        resultIntent.putExtra("ssid", ssid);
        resultIntent.putExtra("capture", true);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.instance);
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);

        ((NotificationManager) MainActivity.instance.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notificationBuilder.build());
    }
}
