package iscot.events;

import iscot.enums.Areas;
import iscot.enums.Type;

public class RegistrationEvent extends Event {
    public RegistrationEvent(Type type, Areas area, String deviceId) {
        super(type, area, deviceId);
    }
}
