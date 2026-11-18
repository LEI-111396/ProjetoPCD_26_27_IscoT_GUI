package iscot.events;
import iscot.enums.Areas;

public class AreaIrrigationOrderEvent extends HighLevelOrderEvent {
    private final boolean active;
    private final int duration;

    public AreaIrrigationOrderEvent(Areas area, String deviceId, boolean active, int duration) {
        super(area, deviceId);
        this.active = active;
        this.duration = duration;
    }

    public boolean isActive() { return active; }
    public int getDuration() { return duration; }

}
