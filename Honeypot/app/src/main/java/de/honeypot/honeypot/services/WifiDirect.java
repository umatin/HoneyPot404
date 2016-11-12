package de.honeypot.honeypot.services;

import android.app.Activity;

import de.honeypot.honeypot.handlers.WifiP2PHandler;
import de.honeypot.honeypot.services.wifip2p.WifiP2PComponent;
import de.honeypot.honeypot.services.wifip2p.WifiP2PListener;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class WifiDirect {

    private static volatile boolean alreadyInitialized = false;

    public static void init(Activity activity)
    {
        WifiP2PComponent.init(activity, new WifiDirectListener());
        WifiP2PComponent.startUpdateThread();
        WifiDirect.alreadyInitialized = true;
    }

    public static String getOwnDeviceAddress()
    {
        return WifiP2PComponent.getOwnHashedDeviceAddress();
    }




    public static class WifiDirectListener implements WifiP2PListener
    {
        @Override
        public void deviceDiscovered(String address) {
            WifiP2PHandler.compareToNearby(address);
        }

        @Override
        public void onWifiEnabled() {

        }

        @Override
        public void onWifiDiasbled() {

        }
    }
}
