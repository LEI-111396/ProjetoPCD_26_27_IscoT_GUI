package iscot.events;

import iscot.enums.Areas;
import iscot.enums.Type;

import java.io.Serializable;

public abstract class Event implements Serializable {
    private final Type type;
    private final Areas area;
    private final String deviceId;

    protected Event(Type type, Areas area, String deviceId) {
        this.type = type;
        this.area = area;
        this.deviceId = deviceId;
    }

    public Type getType() {
        return type;
    }

    public Areas getArea() {
        return area;
    }

    public String getDeviceId() {
        return deviceId;
    }
    protected String commonFields() {
        return "area=" + area + ", deviceId=" + deviceId + ", type=" + type;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + commonFields() + "]";
    }
}