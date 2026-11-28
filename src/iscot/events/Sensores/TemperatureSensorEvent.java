package iscot.events.Sensores;
import iscot.enums.Areas;
import iscot.events.SensorEvent;

public class TemperatureSensorEvent extends SensorEvent {
    private final double temperature;

    public TemperatureSensorEvent(Areas area, String deviceId, double temperature) {
        super(area, deviceId);
        this.temperature = temperature;
    }

    public double getTemperature() { return temperature; }
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [temperature=" + temperature + ", " + commonFields() + "]";
    }
}
