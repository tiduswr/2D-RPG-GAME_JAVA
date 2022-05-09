package event;

import entity.Direction;

public abstract class Event {
    private int eventCol, eventRow;
    private Direction reqDirection;
    
    public Event(int eventCol, int eventRow, Direction reqDirection) {
        this.eventCol = eventCol;
        this.eventRow = eventRow;
        this.reqDirection = reqDirection;
    }

    public void execute(){};

    public int getEventCol() {
        return eventCol;
    }

    public void setEventCol(int eventCol) {
        this.eventCol = eventCol;
    }

    public int getEventRow() {
        return eventRow;
    }

    public void setEventRow(int eventRow) {
        this.eventRow = eventRow;
    }

    public Direction getReqDirection() {
        return reqDirection;
    }

    public void setReqDirection(Direction reqDirection) {
        this.reqDirection = reqDirection;
    }
}
