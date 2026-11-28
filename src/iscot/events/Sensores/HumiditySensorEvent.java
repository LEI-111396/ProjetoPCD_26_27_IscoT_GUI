package iscot.events.Sensores;
import iscot.enums.Areas;
import iscot.events.SensorEvent;

public class HumiditySensorEvent extends SensorEvent {
    private final double humidity;

    public HumiditySensorEvent(Areas area, String deviceId, double humidity) {
        super(area, deviceId);
        this.humidity = humidity;
    }

    public double getHumidity() { return humidity; }
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [humidity=" + humidity + ", " + commonFields() + "]";
    }
}
