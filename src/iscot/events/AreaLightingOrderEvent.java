package iscot.events;
import iscot.enums.Areas;

public class AreaLightingOrderEvent extends HighLevelOrderEvent {
    private final boolean state;
    private final int intensity;

    public AreaLightingOrderEvent(Areas area, String deviceId, boolean state, int intensity) {
        super(area, deviceId);
        this.state = state;
        this.intensity = intensity;
    }

    public boolean getState() { return state; }
    public int getIntensity() { return intensity; }
}