package websocket.messages;

import com.google.gson.Gson;

public class Notification extends ServerMessage{

    private String message;

    public Notification(String message){
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }


    public String getNotificationMessage (){
        return message;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
