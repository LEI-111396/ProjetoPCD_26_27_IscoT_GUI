package iscot.events;
import iscot.enums.Areas;

public class GroundMoistureSensorEvent extends SensorEvent {
    private final double moisture;

    public GroundMoistureSensorEvent(Areas area, String deviceId, double moisture) {
        super(area, deviceId);
        this.moisture = moisture;
    }

    public double getMoisture() { return moisture; }
}
