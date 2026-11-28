package iscot.events.Sensores;
import iscot.enums.Areas;
import iscot.events.SensorEvent;

public class LightSensorEvent extends SensorEvent {
    private final double light;

    public LightSensorEvent(Areas area, String deviceId, double light) {
        super(area, deviceId);
        this.light = light;
    }

    public double getLight() { return light; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [light=" + light + ", " + commonFields() + "]";
    }
}