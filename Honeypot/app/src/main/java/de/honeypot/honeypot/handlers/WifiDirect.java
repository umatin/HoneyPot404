package de.honeypot.honeypot.handlers;

import android.app.Activity;

import de.honeypot.honeypot.wifip2p.WifiP2PComponent;
import de.honeypot.honeypot.wifip2p.WifiP2PListener;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class WifiDirect {

    private static boolean alreadyInitialized = false;

    public static void init(Activity activity)
    {
        if(alreadyInitialized)
            return;

        WifiP2PComponent.init(activity, new WifiDirectListener());
        WifiP2PComponent.startUpdateThread();

        alreadyInitialized = true;
    }

    public static String getOwnDeviceAddress()
    {
        return WifiP2PComponent.getOwnHashedDeviceAddress();
    }




    public static class WifiDirectListener implements WifiP2PListener
    {
        @Override
        public void deviceDiscovered(String address) {
            //TODO:
        }

        @Override
        public void onWifiEnabled() {

        }

        @Override
        public void onWifiDiasbled() {

        }
    }
}
