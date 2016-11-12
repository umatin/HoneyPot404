package de.honeypot.honeypot.wifip2p;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public interface WifiP2PListener {

    public void deviceDiscovered();

    public void onWifiDiasbled();

    public void onWifiEnabled();
}
