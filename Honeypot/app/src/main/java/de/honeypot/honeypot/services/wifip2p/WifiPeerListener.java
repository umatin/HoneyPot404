package de.honeypot.honeypot.services.wifip2p;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public interface WifiPeerListener {

    public void deviceDiscovered(String address);

    public void onWifiDisabled();

    public void onWifiEnabled();
}
