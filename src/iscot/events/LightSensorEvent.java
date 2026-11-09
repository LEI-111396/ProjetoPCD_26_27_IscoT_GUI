package iscot.events;
import iscot.enums.Areas;

public class LightSensorEvent extends SensorEvent {
    private final double light;

    public LightSensorEvent(Areas area, String deviceId, double light) {
        super(area, deviceId);
        this.light = light;
    }

    public double getLight() { return light; }

}