package de.honeypot.honeypot.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class MainBackgroundService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Toast.makeText(this, "Service Test", Toast.LENGTH_SHORT).show();

        return null;
    }
}
