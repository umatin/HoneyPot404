package de.honeypot.honeypot.data;

/**
 * Created by Geosearchef on 12.11.2016.
 */

public class NearbyObject {
    private int id;
    private double distance;
    private String device;


    public NearbyObject(int id, double distance, String device) {
        this.id = id;
        this.distance = distance;
        this.device = device;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
