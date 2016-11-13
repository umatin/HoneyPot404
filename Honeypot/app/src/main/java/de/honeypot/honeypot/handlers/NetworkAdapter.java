package de.honeypot.honeypot.handlers;

import android.util.JsonReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.honeypot.honeypot.services.WifiDirectAdapter;

/**
 * Created by user on 12.11.16.
 */

public class NetworkAdapter {
    public static class NearbyDevice {
        private NetworkAdapter source;
        private String deviceId;
        private double distance;
        private String date;
        private int id;

        public NearbyDevice(NetworkAdapter source) {
            this.source = source;
        }

        public String getDeviceID() {
            return deviceId;
        }

        public double getDistance() {
            return distance;
        }

        public String getDate() {
            return date;
        }

        public int getID() {
            return id;
        }
    }

    public static class Profile {
        private NetworkAdapter source;

        protected String name;
        protected int id;
        protected int points;
        protected List<Integer> friends;
        protected String picture;

        public String getName() {
            return name;
        }

        public int getID() {
            return id;
        }

        public int getPoints() {
            return points;
        }

        public Profile[] getFriends() {
            Profile[] profiles = new Profile[friends.size()];

            for (int i = 0; i < profiles.length; i++) {
                profiles[i] = source.getProfile(friends.get(i));
            }

            return null;
        }

        public byte[] getPicture() {
            return null;
        }

        public Profile(NetworkAdapter source) {
            this.source = source;
        }
    }

    private static final Logger networkLogger = Logger.getLogger("NetworkAdapter");

    private static NetworkAdapter instance;

    public static NetworkAdapter getInstance() {
        if (instance == null) instance = new NetworkAdapter();
        return instance;
    }

    public NetworkAdapter() {
        client = new DefaultHttpClient();
    }

    private final String ownProfileRequest = "/profile?token=%s";
    private final String profileRequest = "/profile/%d?token=%s";
    private final String registerRequest = "/register?name=%s&device=%s";
    private final String nearbyRequest = "/nearby/%f/%f?token=%s";
    private final String defaultEndpoint = "http://honeypot4431.cloudapp.net";

    private HttpClient client;

    private String token;
    private String device;

    public void authenticate(String name) throws IOException {
        String deviceID = WifiDirectAdapter.getInstance().getDeviceHash();
        String req = String.format(registerRequest, name, deviceID);

        try {
            JsonReader reader = retrieveJSON(req);

            reader.beginObject();
            while (reader.hasNext()) {
                String itemName = reader.nextName();
                switch (itemName) {
                    case "token":
                        token = reader.nextString();
                        break;
                    case "device":
                        device = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                }
            }
            reader.endObject();

            networkLogger.info("Authenticated as " + token);

            reader.close();
        } catch (IOException e) {
            networkLogger.warning(e.getMessage());
        }
    }

    public String getToken() {
        return token;
    }

    public String getDevice() {
        return device;
    }

    public Profile getOwnProfile() {
        String req = String.format(ownProfileRequest, token);
        return parseProfileRequest(req);
    }

    public Profile getProfile(int id) {
        String req = String.format(profileRequest, id, token);
        return parseProfileRequest(req);
    }

    private Profile parseProfileRequest(String req)  {
        Profile profile = new Profile(this);

        try {
            JsonReader reader = retrieveJSON(req);

            profile = new Profile(this);

            reader.beginObject();
            while (reader.hasNext()) {
                String nextEntry = reader.nextName();

                switch (nextEntry) {
                    case "id":
                        profile.id = reader.nextInt();
                        break;
                    case "name":
                        profile.name = reader.nextString();
                        break;
                    case "points":
                        profile.points = reader.nextInt();
                        break;
                    case "friends":
                        ArrayList<Integer> friends = new ArrayList<>();
                        reader.beginArray();
                        while (reader.hasNext()) {
                            friends.add(reader.nextInt());
                        }
                        reader.endArray();
                        profile.friends = friends;
                        break;
                    case "picture":
                        profile.picture = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                }
            }
            reader.endObject();

            networkLogger.info("Fetched profile " + profile.toString());

            reader.close();
        } catch (IOException e) {
            networkLogger.warning(e.getMessage());
        }

        return profile;
    }

    public NearbyDevice[] getNearbyDevices(double latitude, double longitude) {
        List<NearbyDevice> devices = new ArrayList<>();
        String req = String.format(nearbyRequest, latitude, longitude, token);

        try {
            JsonReader reader = retrieveJSON(req);

            reader.beginObject();
            while (reader.hasNext()) {
                String nextName = reader.nextName();
                if (nextName.equals("nearby")) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        NearbyDevice device = new NearbyDevice(this);
                        nextName = reader.nextName();
                        switch (nextName) {
                            case "id":
                                device.id = reader.nextInt();
                                break;
                            case "device":
                                device.deviceId = reader.nextString();
                                break;
                            case "distance":
                                device.distance = reader.nextDouble();
                                break;
                            case "date":
                                device.date = reader.nextString();
                                break;
                            default:
                                reader.skipValue();
                        }
                        devices.add(device);
                    }
                    reader.endArray();
                }
            }
            reader.endObject();

            networkLogger.info("Fetched " + devices.size() + " devices");

            reader.close();
        } catch (IOException e) {
            networkLogger.warning(e.getMessage());
        }

        return devices.toArray(new NearbyDevice[0]);
    }

    private JsonReader retrieveJSON(String request) throws IOException {
        request = defaultEndpoint + request;
        networkLogger.info("Sending network request: " + request);

        HttpGet httpGet = new HttpGet(request);
        HttpResponse httpResponse = client.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();

        networkLogger.info("Reecived " + httpEntity.getContentLength() + " bytes");

        return new JsonReader(new InputStreamReader(httpEntity.getContent(), "UTF-8"));
    }
}



























