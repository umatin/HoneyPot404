package de.honeypot.honeypot.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MainBackgroundService extends IntentService {

    public MainBackgroundService() {
        super("");
    }

    @Override
    public IBinder onBind(Intent intent) {

        Toast.makeText(this, "Service Test", Toast.LENGTH_SHORT).show();

        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
