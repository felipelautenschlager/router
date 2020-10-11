package br.com.felipelautenschlager.kn.router.model;

import java.util.Date;

public class Point {

    private String id;
    private int fromSeq;
    private int toSeq;
    private double latitude;
    private double longitude;
    private Date timestamp;
    private double knots;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFromSeq() {
        return fromSeq;
    }

    public void setFromSeq(int fromSeq) {
        this.fromSeq = fromSeq;
    }

    public int getToSeq() {
        return toSeq;
    }

    public void setToSeq(int toSeq) {
        this.toSeq = toSeq;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getKnots() {
        return knots;
    }

    public void setKnots(double knots) {
        this.knots = knots;
    }

    @Override
    public String toString() {
        return "Point{" +
                "id='" + id + '\'' +
                ", fromSeq=" + fromSeq +
                ", toSeq=" + toSeq +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", timestamp=" + timestamp +
                ", knots=" + knots +
                '}';
    }
}
