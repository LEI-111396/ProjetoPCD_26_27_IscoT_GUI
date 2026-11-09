package iscot.events;
import iscot.enums.Areas;

public class HumiditySensorEvent extends SensorEvent {
    private final double humidity;

    public HumiditySensorEvent(Areas area, String deviceId, double humidity) {
        super(area, deviceId);
        this.humidity = humidity;
    }

    public double getHumidity() { return humidity; }
}
