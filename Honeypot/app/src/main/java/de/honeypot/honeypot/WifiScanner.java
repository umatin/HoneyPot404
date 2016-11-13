package de.honeypot.honeypot;

import android.content.Context;
import android.net.wifi.ScanResult;

import java.util.List;
import java.util.logging.Logger;

import de.honeypot.honeypot.handlers.WlanDetection;

/**
 * Created by Tobias on 13.11.2016.
 */

public class WifiScanner extends Thread { // Background thread which gives an event if a network could be a pi

    private boolean abbruch = false;
    private WlanDetection wifi;
    private Context mContext;


    private final static Logger logger = Logger.getLogger("WlanDetection");

    public WifiScanner(Context context) {
        mContext = context;
        wifi = new WlanDetection(mContext);
        start();

    }

    public void run() {

        while (!abbruch) {
            wifi.StartSearching();
            List<ScanResult> wifiresults = wifi.getResults();

            for (int i = 0; i < wifiresults.size(); i++) {
                if (wifiresults.get(i).SSID.substring(0, 7).equals("honeypot")) {
                    //wifi detected --> conquer
                    logger.info("Found Wifi Network " + wifiresults.get(i).SSID);

                }
            }

            try {
                Thread.sleep(1000 * 20); // alle 20 Sekunden nach dem pi scannen
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }


    public void stopService() {
        abbruch = true;
    }

    public void startService() {
        abbruch = false;
        start();
    }


}
