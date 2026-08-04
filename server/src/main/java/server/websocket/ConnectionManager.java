package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Session>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {

        ArrayList<Session> listOfSessions = connections.get(gameID);
        if (listOfSessions == null){
            listOfSessions = new ArrayList<>();
        }
        listOfSessions.add(session);
        connections.put(gameID, listOfSessions);
    }

    public void remove(int gameID, Session session) {
//        connections.remove(gameID);
        ArrayList<Session> listOfSessions = connections.get(gameID);
        listOfSessions.remove(session);


    }

    // 3 different types of broadcast: ROOT_ONLY, ALL, ALL_BUT_ROOT
    // null for exclude session = all

    public void broadcast(Session excludeSession, Notification notification) throws IOException {
        String msg = notification.toString();

            for (ArrayList<Session> list : connections.values()) {

                for (Session c : list) {
                    if (c.isOpen()) {
                        if (!c.equals(excludeSession)) {
                            c.getRemote().sendString(msg);
                        }
                    }
                }

            }
        }


    public boolean isEmpty() {
        return connections.isEmpty();
    }



}
