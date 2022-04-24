package ui;

public class Message {
    private String message;
    private int messageCounter;

    public Message(String message) {
        this.message = message;
        this.messageCounter = 0;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageCounter() {
        return messageCounter;
    }

    public void setMessageCounter(int messageCounter) {
        this.messageCounter = messageCounter;
    }
    
    public void messageCounterPlusPlus(){
        messageCounter++;
    }
    
}
