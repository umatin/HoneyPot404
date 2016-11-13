package de.honeypot.honeypot.handlers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by marti on 13/11/2016.
 */

public class WlanDetection {


    private final static Logger LOGGER = Logger.getLogger("WlanDetection");
    WifiManager myWifi;
    List<ScanResult> results;

    public WlanDetection(Context context)
    {
        myWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        myWifi.setWifiEnabled(true);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                results = myWifi.getScanResults();

                ///For DEBUG purposes remove in the end
                testSelf();

            }
        }, filter);

    }
    public void StartSearching()
    {
        myWifi.startScan();
    }
    public List<ScanResult> getResults()
    {
        return results;
    }

    public void testSelf()
    {
        int size = results.size();
        for(int i=0; i < size; ++i) {
            LOGGER.log(Level.INFO, "  BSSID       =" + results.get(i).BSSID+ "\n");
            LOGGER.log(Level.INFO, "  SSID        =" + results.get(i).SSID+ "\n");
            LOGGER.log(Level.INFO, "  Capabilities=" + results.get(i).capabilities+ "\n");
            LOGGER.log(Level.INFO, "  Frequency   =" + results.get(i).frequency+ "\n");
            LOGGER.log(Level.INFO, "  Level       =" + results.get(i).level+ "\n");
            LOGGER.log(Level.INFO, "---------------" + "\n");
        }
    }

}
