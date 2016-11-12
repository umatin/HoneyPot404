package de.honeypot.honeypot.handlers;

import android.app.Activity;

import de.honeypot.honeypot.wifip2p.WifiP2PComponent;
import de.honeypot.honeypot.wifip2p.WifiP2PListener;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class WifiDirect {

    public static void init(Activity activity)
    {
        WifiP2PComponent.init(activity, new WifiDirectListener());
        WifiP2PComponent.startUpdateThread();
    }

    public String getOwnDeviceAddress()
    {
        return WifiP2PComponent.getOwnHashedDeviceAddress();
    }




    public static class WifiDirectListener implements WifiP2PListener
    {
        @Override
        public void deviceDiscovered() {
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
