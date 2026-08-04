package websocket.messages;

import com.google.gson.Gson;

public class Notification extends ServerMessage{

    private String message;

    public Notification(String message){
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

//
//    public enum Type {
//        ROOT_ONLY,
//        ALL_BUT_ROOT,
//        ALL
//    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
