package iscot.events;
import iscot.enums.Areas;
import iscot.enums.AirConditioningMode;

public class AreaTemperatureOrderEvent extends HighLevelOrderEvent {
    private final AirConditioningMode mode;
    private final double temperature;

    public AreaTemperatureOrderEvent(Areas area, String deviceId, AirConditioningMode mode, double temperature) {
        super(area, deviceId);
        this.mode = mode;
        this.temperature = temperature;
    }

    public AirConditioningMode getMode() { return mode; }
    public double getTemperature() { return temperature; }

}
