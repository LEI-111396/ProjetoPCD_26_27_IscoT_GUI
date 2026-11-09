package iscot.events;
import iscot.enums.Areas;

public class AreaIrrigationOrderEvent extends HighLevelOrderEvent {
    private final boolean active;
    private final int zone;
    private final int duration;

    public AreaIrrigationOrderEvent(Areas area, String deviceId, boolean active, int zone, int duration) {
        super(area, deviceId);
        this.active = active;
        this.zone = zone;
        this.duration = duration;
    }

    public boolean isActive() { return active; }
    public int getZone() { return zone; }
    public int getDuration() { return duration; }

}
