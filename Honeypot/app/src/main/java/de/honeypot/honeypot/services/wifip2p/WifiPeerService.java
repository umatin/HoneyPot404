package de.honeypot.honeypot.services.wifip2p;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class WifiPeerService {
    private static final Logger logger = Logger.getLogger("WifiPeerService");
    public static final int DISCOVERY_REQUEST_PERIOD = 5000;

    private static WifiPeerService instance;

    public static WifiPeerService getInstance(WifiPeerListener listener) {
        if (instance == null) instance = new WifiPeerService(listener);
        return instance;
    }

    private final IntentFilter intentFilter = new IntentFilter();

    private Activity host;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private WifiDirectBroadCastReceiver receiver;

    private boolean peerEnabled = false;
    private volatile String ownMacAddress = null;

    private WifiPeerListener wifiPeerListener;

    public WifiPeerService(WifiPeerListener wifiPeerListener) {
        logger.info("Initializing peer service");
        this.wifiPeerListener = wifiPeerListener;
        receiver = new WifiDirectBroadCastReceiver();
    }

    public void startUpdateThread() {
        new UpdateThread().start();
    }

    public void scan() {
        logger.info("Scanning for peers");
        peers.clear();

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int code) {
                logger.warning("Failed to discover peers");
            }
        });
    }

    public void start(Activity a) {
        if (host != null) {
            stop();
        }
        logger.info("Starting up peer service");
        host = a;

        // enable Wifi
        WifiManager wifiManager = (WifiManager) host.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) host.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(host, host.getMainLooper(), null);
        host.registerReceiver(receiver, intentFilter);
    }

    public void stop() {
        logger.info("Stopping peer service");
        //manager.stopPeerDiscovery(channel, null);
        host.unregisterReceiver(receiver);
        host = null;
    }


    private List<WifiP2pDevice> peers = new ArrayList<>();

    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            peers.clear();
            peers.addAll(wifiP2pDeviceList.getDeviceList());


            for (int i = 0; i < peers.size(); i++) {
                String hash = calculateHash(peers.get(i).deviceAddress);
                wifiPeerListener.deviceDiscovered(hash);
            }

        }
    };


    public class WifiDirectBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {


            String action = intent.getAction();
            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    peerEnabled = true;
                    if (wifiPeerListener != null)
                        wifiPeerListener.onWifiEnabled();
                } else {
                    peerEnabled = false;
                    if (wifiPeerListener != null)
                        wifiPeerListener.onWifiEnabled();
                }

                logger.info("WifiDirect enabled: " + peerEnabled);
            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                if (manager != null) {
                    manager.requestPeers(channel, peerListListener);
                }

                logger.info("new devices discovered");
            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                if (ownMacAddress == null) {
                    WifiP2pDevice ownDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                    ownMacAddress = ownDevice.deviceAddress;
                    logger.info("Received mac address " + ownMacAddress);
                }
            }
        }
    }


    public class UpdateThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(DISCOVERY_REQUEST_PERIOD);
                } catch (InterruptedException e) {
                    return;
                }
                scan();
            }
        }
    }


    public String getHashedMAC() {
        while (ownMacAddress == null) {
            try {
                Thread.sleep(200);
            } catch (Exception e) {

            }
        }

        return calculateHash(ownMacAddress);
    }

    public String calculateHash(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            byte[] hashed = digest.digest(string.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < hashed.length; i++) {
                stringBuffer.append(Integer.toString((hashed[i] & 0xff) + 0x100, 16).substring(1));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    public boolean isPeerEnabled() {
        return peerEnabled;
    }

}
