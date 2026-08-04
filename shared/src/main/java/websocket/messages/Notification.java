package websocket.messages;

import com.google.gson.Gson;

public record Notification(Type type, String message) {
    public enum Type {
        ROOT_ONLY,
        ALL_BUT_ROOT,
        ALL
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
