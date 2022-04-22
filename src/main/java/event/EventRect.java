package event;

import java.awt.Rectangle;

public class EventRect extends Rectangle{
    private int defaultEventX, defaultEventY;
    private boolean eventDone;
    
    public int getDefaultEventX() {
        return defaultEventX;
    }

    public void setDefaultEventX(int defaultEventX) {
        this.defaultEventX = defaultEventX;
    }

    public int getDefaultEventY() {
        return defaultEventY;
    }

    public void setDefaultEventY(int defaultEventY) {
        this.defaultEventY = defaultEventY;
    }

    public boolean isEventDone() {
        return eventDone;
    }

    public void setEventDone(boolean eventDone) {
        this.eventDone = eventDone;
    }
    
}
