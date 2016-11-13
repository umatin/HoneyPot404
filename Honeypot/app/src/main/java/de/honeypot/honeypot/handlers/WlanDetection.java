package de.honeypot.honeypot.handlers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by marti on 13/11/2016.
 */

public class WlanDetection {
    private final static Logger LOGGER = Logger.getLogger("WlanDetection");
    private WifiManager wifi;
    private List<ScanResult> results;
    private Context context;

    private static WlanDetection instance;

    private BroadcastReceiver receiver;

    public static WlanDetection getInstance() {
        if (instance == null) instance = new WlanDetection();
        return instance;
    }

    public void start(Context context) {
        if (this.context != null) stop();

        results = new ArrayList<>();
        wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                results = wifi.getScanResults();
            }
        };

        context.registerReceiver(receiver, filter);
    }

    public void startSearch() {
        wifi.startScan();
    }

    public void stop() {
        if (context != null) {
            context.unregisterReceiver(receiver);
            context = null;
        }
    }

    public List<ScanResult> getResults() {
        return results;
    }
}
