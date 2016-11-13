package de.honeypot.honeypot;

import android.content.Context;
import android.net.wifi.ScanResult;

import java.util.List;
import java.util.logging.Logger;

import de.honeypot.honeypot.handlers.HotspotHandler;
import de.honeypot.honeypot.handlers.WlanDetection;

/**
 * Created by Tobias on 13.11.2016.
 */

public class WifiScanner extends Thread { // Background thread which gives an event if a network could be a pi
    private final static int SCAN_INTERVAL = 20000;
    private final static String WIFI_BUZZWORD = "honeypot";
    private final static Logger logger = Logger.getLogger("WifiScanner");

    private static WifiScanner instance;

    public static WifiScanner getInstance() {
        if (instance == null) instance = new WifiScanner();
        return instance;
    }

    public void run() {
        while (true) {
            WlanDetection.getInstance().startSearch();
            List<ScanResult> results = WlanDetection.getInstance().getResults();

            for (int i = 0; i < results.size(); i++) {
                String ssid = results.get(i).SSID;
                //for the show:
                if (ssid=="eduroam"){
                    new HotspotHandler().informUser(ssid);
                }

                if (ssid.length() == 32 && ssid.substring(0, 7).equals(WIFI_BUZZWORD)) {
                    new HotspotHandler().informUser(ssid);
                }
            }

            try {
                Thread.sleep(SCAN_INTERVAL);
            } catch (InterruptedException e) {
                return;
            }
        }
    }


    public void stopService() {
        WlanDetection.getInstance().stop();
    }

    public void startService(Context context) {
        WlanDetection.getInstance().start(context);
        if (!this.isAlive()) start();
    }
}
