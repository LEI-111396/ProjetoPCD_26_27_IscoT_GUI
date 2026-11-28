package iscot.events.Atuadores;
import iscot.enums.Areas;
import iscot.events.HighLevelOrderEvent;

public class AreaVentilationOrderEvent extends HighLevelOrderEvent {
    private final boolean state;

    public AreaVentilationOrderEvent(Areas area, String deviceId, boolean state) {
        super(area, deviceId);
        this.state = state;
    }

    public boolean getState() { return state; }
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [state=" + state + ", " + commonFields() + "]";
    }

}