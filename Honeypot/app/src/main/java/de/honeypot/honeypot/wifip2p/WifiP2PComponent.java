package de.honeypot.honeypot.wifip2p;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import de.honeypot.honeypot.wifip2p.WifiP2PListener;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class WifiP2PComponent {

    public static final int DISCOVERY_REQUEST_PERIOD = 5000;

    private static Activity activity;

    private static final IntentFilter intentFilter = new IntentFilter();

    private static WifiP2pManager manager;
    private static WifiP2pManager.Channel channel;
    private static WifiDirectBroadCastReceiver receiver;

    private static boolean isWifiP2PEnabled = false;
    private static String ownMacAddress = null;

    private static WifiP2PListener wifiP2PListener;



    public static void init(Activity activity, WifiP2PListener wifiP2PListener)
    {
        WifiP2PComponent.activity = activity;
        WifiP2PComponent.wifiP2PListener = wifiP2PListener;

        //Enable Wifi
        WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(activity, activity.getMainLooper(), null);

        receiver = new WifiDirectBroadCastReceiver();
        activity.registerReceiver(receiver, intentFilter);
    }

    public static void startUpdateThread()
    {
        new UpdateThread().start();
    }

    public static void scan()
    {
        peers.clear();

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {
                print("WifiDirect discovery failed!");
            }
        });
    }

    public static void clean()
    {
        //TODO: may not be work
        //manager.stopPeerDiscovery(channel, null);
        activity.unregisterReceiver(receiver);
    }


    private static List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    private static WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            peers.clear();
            peers.addAll(wifiP2pDeviceList.getDeviceList());


            for(int i = 0;i < peers.size();i++)
            {
                String hash = sha1(((WifiP2pDevice)peers.get(i)).deviceAddress);
                wifiP2PListener.deviceDiscovered();
            }

        }
    };


    public static class WifiDirectBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(ownMacAddress == null) {
                WifiP2pDevice ownDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                ownMacAddress = ownDevice.deviceAddress;
            }

            String action = intent.getAction();
            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))
            {
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    isWifiP2PEnabled = true;
                    if(wifiP2PListener != null)
                        wifiP2PListener.onWifiEnabled();
                }
                else {
                    isWifiP2PEnabled = false;
                    if (wifiP2PListener != null)
                        wifiP2PListener.onWifiEnabled();
                }

                print("WifiDirect enabled: " + isWifiP2PEnabled);
            }
            else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
            {
                if (manager != null) {
                    manager.requestPeers(channel, peerListListener);
                }

                print("new devices discovered");
            }
            else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
            {

            }
            else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
            {

            }
        }
    }


    public static class UpdateThread extends Thread {
        @Override
        public void run()
        {
            while(true)
            {
                try{Thread.sleep(DISCOVERY_REQUEST_PERIOD);}catch(InterruptedException e){}
                scan();
            }
        }
    }


    public static String getOwnHashedDeviceAddress()
    {
        return sha1(ownMacAddress);
    }

    public static String sha1(String string)
    {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            byte[] hashed = digest.digest(string.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for(int i = 0;i < hashed.length;i++)
            {
                stringBuffer.append(Integer.toString((hashed[i] & 0xff) + 0x100, 16).substring(1));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            print("NoSuchAlgorithm (SHA1)");
            System.exit(-1);
        }
        return null;
    }

    public static boolean isWifiP2PEnabled()
    {
        return isWifiP2PEnabled;
    }

    public static void print(final String text)
    {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
