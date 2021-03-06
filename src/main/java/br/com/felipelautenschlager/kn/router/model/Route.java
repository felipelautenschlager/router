package br.com.felipelautenschlager.kn.router.model;

import java.util.Objects;

public class Route {

    private String id;
    private int fromSeq;
    private int toSeq;
    private String fromPort;
    private String toPort;
    private long legDuration;
    private int count;
    private String points;

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

    public String getFromPort() {
        return fromPort;
    }

    public void setFromPort(String fromPort) {
        this.fromPort = fromPort;
    }

    public String getToPort() {
        return toPort;
    }

    public void setToPort(String toPort) {
        this.toPort = toPort;
    }

    public long getLegDuration() {
        return legDuration;
    }

    public void setLegDuration(long legDuration) {
        this.legDuration = legDuration;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return fromSeq == route.fromSeq &&
                toSeq == route.toSeq &&
                legDuration == route.legDuration &&
                count == route.count &&
                id.equals(route.id) &&
                fromPort.equals(route.fromPort) &&
                toPort.equals(route.toPort) &&
                points.equals(route.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromSeq, toSeq, fromPort, toPort, legDuration, count, points);
    }

    @Override
    public String toString() {
        return "Route{" +
                "id='" + id + '\'' +
                ", fromSeq=" + fromSeq +
                ", toSeq=" + toSeq +
                ", fromPort='" + fromPort + '\'' +
                ", toPort='" + toPort + '\'' +
                ", legDuration=" + legDuration +
                ", count=" + count +
                '}';
    }
}
