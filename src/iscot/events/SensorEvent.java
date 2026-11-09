package iscot.events;

import iscot.enums.Areas;
import iscot.enums.Type;

public abstract class SensorEvent extends Event {
    protected SensorEvent(Areas area, String deviceId) {
        super(Type.SENSOR, area, deviceId);
    }
}