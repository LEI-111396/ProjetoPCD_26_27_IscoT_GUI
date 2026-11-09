package iscot.events;
import iscot.enums.Areas;

public class TemperatureSensorEvent extends SensorEvent {
    private final double temperature;

    public TemperatureSensorEvent(Areas area, String deviceId, double temperature) {
        super(area, deviceId);
        this.temperature = temperature;
    }

    public double getTemperature() { return temperature; }

}
