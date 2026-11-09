package iscot.events;
import iscot.enums.Areas;

public class AreaVentilationOrderEvent extends HighLevelOrderEvent {
    private final boolean state;

    public AreaVentilationOrderEvent(Areas area, String deviceId, boolean state) {
        super(area, deviceId);
        this.state = state;
    }

    public boolean getState() { return state; }

}