package Event;

public abstract class Event {
    private int eventCol, eventRow;
    private String reqDirection;

    public Event(int eventCol, int eventRow, String reqDirection) {
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

    public String getReqDirection() {
        return reqDirection;
    }

    public void setReqDirection(String reqDirection) {
        this.reqDirection = reqDirection;
    }
    
}
