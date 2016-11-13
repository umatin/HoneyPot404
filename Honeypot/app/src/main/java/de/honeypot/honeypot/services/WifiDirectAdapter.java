package de.honeypot.honeypot.services;

import android.app.Activity;

import java.util.logging.Logger;

import de.honeypot.honeypot.services.wifip2p.WifiPeerService;
import de.honeypot.honeypot.services.wifip2p.WifiPeerListener;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class WifiDirectAdapter {

    private final Logger directAdapterLogger = Logger.getLogger("WifiDirectAdapter");

    private static WifiDirectAdapter instance;

    public static WifiDirectAdapter getInstance() {
        if (instance == null) instance = new WifiDirectAdapter();
        return instance;
    }

    private WifiDirectListener listener;

    public void start(Activity activity) {
        directAdapterLogger.info("Starting up Wi-Fi Direct in activity " + activity.toString());
        WifiPeerService.getInstance(listener).start(activity);
    }

    public void stop() {
        directAdapterLogger.info("Stopping Wi-Fi Direct");
        WifiPeerService.getInstance(listener).stop();
    }

    public WifiDirectAdapter()
    {
        listener = new WifiDirectListener();
        WifiPeerService.getInstance(listener).startUpdateThread();
    }

    public String getDeviceHash()
    {
        return WifiPeerService.getInstance(listener).getHashedMAC();
    }

    public class WifiDirectListener implements WifiPeerListener
    {
        @Override
        public void deviceDiscovered(String address) {

        }

        @Override
        public void onWifiEnabled() {

        }

        @Override
        public void onWifiDisabled() {

        }
    }
}
