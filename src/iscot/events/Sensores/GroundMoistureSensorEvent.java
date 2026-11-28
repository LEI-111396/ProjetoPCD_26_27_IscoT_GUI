package iscot.events.Sensores;
import iscot.enums.Areas;
import iscot.events.SensorEvent;

public class GroundMoistureSensorEvent extends SensorEvent {
    private final double moisture;

    public GroundMoistureSensorEvent(Areas area, String deviceId, double moisture) {
        super(area, deviceId);
        this.moisture = moisture;
    }

    public double getMoisture() { return moisture; }
    @Override

    public String toString() {
        return getClass().getSimpleName() + " [moisture=" + moisture + ", " + commonFields() + "]";
    }

}
